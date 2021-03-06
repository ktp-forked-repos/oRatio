/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.oratio;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.InconsistencyException;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.core.Atom;
import it.cnr.istc.core.Core;
import it.cnr.istc.core.Disjunction;
import it.cnr.istc.core.IEnumItem;
import it.cnr.istc.core.IEnv;
import it.cnr.istc.core.IItem;
import it.cnr.istc.core.Type;
import it.cnr.istc.oratio.types.Agent;
import it.cnr.istc.oratio.types.ImpulsiveAgent;
import it.cnr.istc.oratio.types.PropositionalAgent;
import it.cnr.istc.oratio.types.PropositionalImpulsiveAgent;
import it.cnr.istc.oratio.types.PropositionalState;
import it.cnr.istc.oratio.types.ReusableResource;
import it.cnr.istc.oratio.types.StateVariable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());
    final Map<Atom, Flaw> reasons = new IdentityHashMap<>();
    private Map<Flaw, Double> costs = new IdentityHashMap<>();
    private Set<Flaw> flaws = new HashSet<>();
    private Set<Flaw> inconsistencies = new HashSet<>();
    private final LinkedList<Flaw> flaw_q = new LinkedList<>();
    final LinkedList<Resolver> resolvers = new LinkedList<>();
    private final LinkedList<Layer> layers = new LinkedList<>();
    private final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        try {
            boolean read = read(new File(Solver.class.getResource("time.rddl").getPath()));
            assert read;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        types.put(StateVariable.NAME, new StateVariable(this));
        types.put(ReusableResource.NAME, new ReusableResource(this));
        types.put(Agent.NAME, new Agent(this));
        types.put(ImpulsiveAgent.NAME, new ImpulsiveAgent(this));
        types.put(PropositionalAgent.NAME, new PropositionalAgent(this));
        types.put(PropositionalImpulsiveAgent.NAME, new PropositionalImpulsiveAgent(this));
        types.put(PropositionalState.NAME, new PropositionalState(this));
    }

    @Override
    public IEnumItem newEnumItem(Type type, IItem... values) {
        assert rootLevel();
        IEnumItem c_enum = super.newEnumItem(type, values);
        EnumFlaw flaw = new EnumFlaw(this, c_enum);
        fireFlawUpdate(flaw);
        if (resolvers.isEmpty()) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return c_enum;
    }

    @Override
    protected boolean newFact(Atom atom) {
        assert rootLevel();
        if (!super.newFact(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, true);
        fireFlawUpdate(flaw);
        reasons.put(atom, flaw);
        if (resolvers.isEmpty()) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return true;
    }

    @Override
    protected boolean activateFact(Atom atom) {
        return super.activateFact(atom);
    }

    @Override
    protected boolean unifyFact(Atom unifying, Atom with) {
        return super.unifyFact(unifying, with);
    }

    @Override
    protected boolean newGoal(Atom atom) {
        assert rootLevel();
        if (!super.newGoal(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, false);
        reasons.put(atom, flaw);
        fireFlawUpdate(flaw);
        if (resolvers.isEmpty()) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return true;
    }

    @Override
    protected boolean activateGoal(Atom atom) {
        return super.activateGoal(atom);
    }

    @Override
    protected boolean unifyGoal(Atom unifying, Atom with) {
        return super.unifyGoal(unifying, with);
    }

    @Override
    public boolean newDisjunction(IEnv env, Disjunction d) {
        assert rootLevel();
        if (!super.newDisjunction(env, d)) {
            return false;
        }
        DisjunctionFlaw flaw = new DisjunctionFlaw(this, env, d);
        fireFlawUpdate(flaw);
        if (resolvers.isEmpty()) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return true;
    }

    @Override
    public boolean solve() {
        LOG.info("solving the problem..");

        while (true) {
            // we update the planning graph..
            if (!update_planning_graph()) {
                // the problem is unsolvable..
                return false;
            }

            if (!clear_inconsistencies()) {
                // the problem is unsolvable..
                return false;
            }

            if (flaws.isEmpty()) {
                // Hurray!! We have found a solution..
                return true;
            }

            while (!flaws.isEmpty()) {
                // we clean up trivial flaws..
                Optional<Flaw> trivial_flaw = flaws.stream().filter(f -> f.isExpanded() && f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
                while (trivial_flaw.isPresent()) {
                    assert trivial_flaw.get().isExpanded();
                    assert trivial_flaw.get().in_plan.evaluate() == LBool.L_TRUE;
                    assert costs.getOrDefault(trivial_flaw.get(), Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY;
                    fireCurrentFlaw(trivial_flaw.get());
                    Resolver unique_resolver = trivial_flaw.get().getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).findAny().get();
                    assert unique_resolver.in_plan.evaluate() == LBool.L_TRUE;
                    assert unique_resolver.getPreconditions().stream().allMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_TRUE);
                    fireCurrentResolver(unique_resolver);
                    flaws.remove(trivial_flaw.get());
                    flaws.addAll(unique_resolver.getPreconditions());
                    trivial_flaw = flaws.stream().filter(f -> f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
                }

                assert flaws.stream().allMatch(f -> f.isExpanded());
                assert flaws.stream().allMatch(f -> f.in_plan.evaluate() == LBool.L_TRUE);
                assert flaws.stream().allMatch(f -> costs.getOrDefault(f, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY);

                if (flaws.isEmpty()) {
                    // Hurray!! We have found a solution..
                    return true;
                } else {
                    // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                    Flaw most_expensive_flaw = flaws.stream().max((Flaw f0, Flaw f1) -> Double.compare(costs.getOrDefault(f0, Double.POSITIVE_INFINITY), costs.getOrDefault(f1, Double.POSITIVE_INFINITY))).get();
                    assert most_expensive_flaw.isExpanded();
                    assert most_expensive_flaw.in_plan.evaluate() == LBool.L_TRUE;
                    assert costs.getOrDefault(most_expensive_flaw, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY;
                    fireCurrentFlaw(most_expensive_flaw);

                    // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                    Resolver least_expensive_resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + evaluate(r0.cost), r1.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + evaluate(r1.cost))).get();
                    assert least_expensive_resolver.in_plan.evaluate() == LBool.L_UNKNOWN;
                    fireCurrentResolver(least_expensive_resolver);
                    resolvers.add(least_expensive_resolver);

                    // we create a new layer..
                    push(least_expensive_resolver);
                    // and remove the flaw..
                    flaws.remove(most_expensive_flaw);

                    try {
                        // we try to enforce the resolver..
                        if (assign(least_expensive_resolver.in_plan)) {
                            // we add sub-goals..
                            flaws.addAll(least_expensive_resolver.getPreconditions());
                        }
                    } catch (InconsistencyException ex) {
                        // the problem is unsolvable..
                        return false;
                    }
                }
            }
        }
    }

    private boolean clear_inconsistencies() {
        while (!inconsistencies.isEmpty()) {
            // we clean up trivial inconsistencies..
            Optional<Flaw> trivial_inconsistency = inconsistencies.stream().filter(f -> f.isExpanded() && f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
            while (trivial_inconsistency.isPresent()) {
                assert trivial_inconsistency.get().isExpanded();
                assert trivial_inconsistency.get().in_plan.evaluate() == LBool.L_TRUE;
                assert costs.getOrDefault(trivial_inconsistency.get(), Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY;
                fireCurrentFlaw(trivial_inconsistency.get());
                Resolver unique_resolver = trivial_inconsistency.get().getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).findAny().get();
                assert unique_resolver.in_plan.evaluate() == LBool.L_TRUE;
                assert unique_resolver.getPreconditions().stream().allMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_TRUE);
                fireCurrentResolver(unique_resolver);
                inconsistencies.remove(trivial_inconsistency.get());
                inconsistencies.addAll(unique_resolver.getPreconditions());
                trivial_inconsistency = inconsistencies.stream().filter(f -> f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
            }

            assert inconsistencies.stream().allMatch(f -> f.isExpanded());
            assert inconsistencies.stream().allMatch(f -> f.in_plan.evaluate() == LBool.L_TRUE);
            assert inconsistencies.stream().allMatch(f -> costs.getOrDefault(f, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY);

            if (inconsistencies.isEmpty()) {
                // we collect the inconsistencies..
                LOG.info("extracting inconsistencies from smart types..");
                inconsistencies.addAll(get_inconsistencies());
                assert inconsistencies.stream().allMatch(flaw -> flaw.in_plan.evaluate() != LBool.L_FALSE);
                if (!inconsistencies.isEmpty()) {
                    for (Flaw flaw : inconsistencies) {
                        fireFlawUpdate(flaw);
                        flaw_q.add(flaw);
                    }
                    for (Flaw inconsistency : inconsistencies) {
                        if (!inconsistency.expand()) {
                            return false;
                        }
                        for (Resolver r : inconsistency.getResolvers()) {
                            resolvers.addFirst(r);
                            ctr_var = r.in_plan;
                            if (!r.apply()) {
                                return false;
                            }
                            if (r.getPreconditions().isEmpty()) {
                                // there are no requirements for this resolver..
                                setCost(inconsistency, evaluate(r.cost));
                            }
                            fireResolverUpdate(r);
                            resolvers.removeFirst();
                        }

                        fireFlawUpdate(inconsistency);
                    }
                }
            } else {
                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = inconsistencies.stream().max((Flaw f0, Flaw f1) -> Double.compare(costs.getOrDefault(f0, Double.POSITIVE_INFINITY), costs.getOrDefault(f1, Double.POSITIVE_INFINITY))).get();
                assert most_expensive_flaw.isExpanded();
                assert most_expensive_flaw.in_plan.evaluate() == LBool.L_TRUE;
                assert costs.getOrDefault(most_expensive_flaw, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY;
                fireCurrentFlaw(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                Resolver least_expensive_resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + evaluate(r0.cost), r1.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + evaluate(r1.cost))).get();
                assert least_expensive_resolver.in_plan.evaluate() == LBool.L_UNKNOWN;
                fireCurrentResolver(least_expensive_resolver);

                // we create a new layer..
                push(least_expensive_resolver);
                // and remove the flaw..
                inconsistencies.remove(most_expensive_flaw);

                try {
                    // we try to enforce the resolver..
                    if (assign(least_expensive_resolver.in_plan)) {
                        // we add sub-goals..
                        inconsistencies.addAll(least_expensive_resolver.getPreconditions());
                    }
                } catch (InconsistencyException ex) {
                    // the problem is unsolvable..
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Updates the planning graph returning {@code true} if the building process
     * succedes and {@code false} if the problem is detected as unsolvable.
     *
     * @return {@code true} if the building process succedes or {@code false} if
     * the problem is detected as unsolvable.
     */
    private boolean update_planning_graph() {
        if (flaw_q.isEmpty()) {
            // there is nothing to reason on..
            return true;
        }

        BoolExpr tmp_expr = ctr_var;
        while (Stream.concat(flaws.stream(), inconsistencies.stream()).anyMatch(flaw -> getCost(flaw) == Double.POSITIVE_INFINITY) && !flaw_q.isEmpty()) {
            Flaw flaw = flaw_q.pollFirst();
            assert !flaw.isExpanded();
            fireCurrentFlaw(flaw);
            if (!isDeferrable(flaw)) {
                LOG.log(Level.FINE, "expanding {0}", flaw.toSimpleString());
                if (!flaw.expand()) {
                    return false;
                }
                for (Resolver r : flaw.getResolvers()) {
                    resolvers.addFirst(r);
                    ctr_var = r.in_plan;
                    if (!r.apply()) {
                        return false;
                    }
                    if (r.getPreconditions().isEmpty()) {
                        // there are no requirements for this resolver..
                        setCost(flaw, evaluate(r.cost));
                    }
                    fireResolverUpdate(r);
                    resolvers.removeFirst();
                }

                fireFlawUpdate(flaw);
            } else {
                // we postpone the expansion..
                LOG.log(Level.FINE, "deferring {0}", flaw.toSimpleString());
                flaw_q.add(flaw);
            }
        }

        assert !flaw_q.isEmpty() || !Stream.concat(flaws.stream(), inconsistencies.stream()).anyMatch(flaw -> getCost(flaw) == Double.POSITIVE_INFINITY);

        ctr_var = tmp_expr;
        return true;
    }

    void setCost(Flaw flaw, double cost) {
        if (costs.getOrDefault(flaw, Double.POSITIVE_INFINITY) != cost) {
            costs.put(flaw, cost);
            fireFlawUpdate(flaw);

            Set<Flaw> updated = new HashSet<>();
            updated.add(flaw);
            LinkedList<Flaw> queue = new LinkedList<>();
            queue.addAll(flaw.getSupports().stream().map(supp -> supp.effect).collect(Collectors.toList()));
            while (!queue.isEmpty()) {
                Flaw c_flaw = queue.pollFirst();
                if (!updated.contains(c_flaw)) {
                    double c_cost = c_flaw.getResolvers().stream().mapToDouble(r -> getCost(r)).min().orElse(Double.POSITIVE_INFINITY);
                    if (costs.getOrDefault(c_flaw, Double.POSITIVE_INFINITY) != c_cost) {
                        updated.add(c_flaw);
                        costs.put(c_flaw, c_cost);
                        fireFlawUpdate(c_flaw);
                        queue.addAll(c_flaw.getSupports().stream().map(supp -> supp.effect).collect(Collectors.toList()));
                    }
                }
            }
        }
    }

    public double getCost(Flaw flaw) {
        return costs.getOrDefault(flaw, Double.POSITIVE_INFINITY);
    }

    public double getCost(Resolver resolver) {
        if (resolver.in_plan.evaluate() == LBool.L_FALSE) {
            return Double.POSITIVE_INFINITY;
        } else {
            return resolver.getPreconditions().stream().mapToDouble(pre -> pre.getSolver().getCost(pre)).max().orElse(0) + evaluate(resolver.getCost());
        }
    }

    public boolean isDeferrable(Flaw flaw) {
        LinkedList<Flaw> queue = new LinkedList<>();
        Set<Flaw> visited = new HashSet<>();
        queue.add(flaw);
        while (!queue.isEmpty()) {
            Flaw c_flaw = queue.pollFirst();
            if (!visited.contains(c_flaw)) {
                if (!c_flaw.isDisjunctive()) {
                    // we cannot defer this flaw..
                    return false;
                } else if (c_flaw.in_plan.evaluate() == LBool.L_FALSE) {
                    // it is not possible to solve this flaw with current assignments.. thus we defer..
                    return true;
                } else if (costs.getOrDefault(c_flaw, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY) {
                    // we already have a solution for this flaw.. thus we defer..
                    return true;
                }
                visited.add(c_flaw);
                // this flaw is deferrable if any of its causes is..
                queue.addAll(c_flaw.getCauses().stream().map(resolver -> resolver.effect).collect(Collectors.toList()));
            }
        }
        // we cannot defer this flaw..
        return false;
    }

    /**
     * Collects all the inconsistencies of the {@link SmartType} instances.
     *
     * @return a collection of {@link Flaw}s representing all the
     * inconsistencies.
     */
    private Collection<Flaw> get_inconsistencies() {
        Set<Type> c_types = new HashSet<>();
        LinkedList<Type> queue = new LinkedList<>();
        queue.addAll(types.values());
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (!c_types.contains(c_type)) {
                c_types.add(c_type);
                queue.addAll(c_type.getTypes());
            }
        }
        Collection<Flaw> c_flaws = new ArrayList<>();
        for (Type type : c_types) {
            if (type instanceof SmartType) {
                c_flaws.addAll(((SmartType) type).getInconsistencies());
            }
        }
        return c_flaws;
    }

    private void push(Resolver r) {
        // we create a new layer..
        Layer l = new Layer(r, flaws, inconsistencies);
        flaws = new HashSet<>(flaws);
        inconsistencies = new HashSet<>(inconsistencies);
        layers.add(l);

        // we also create a new layer in the constraint network..
        super.push();
    }

    @Override
    public void push() {
        push(null);
    }

    @Override
    public void pop() {
        Layer l_l = layers.getLast();
        if (!resolvers.isEmpty() && resolvers.getLast() == l_l.resolver) {
            resolvers.removeLast();
        }
        flaws = l_l.flaws;
        inconsistencies = l_l.inconsistencies;

        layers.pollLast();

        // we restore the constraint network state..
        super.pop();
    }

    void fireNewFlaw(Flaw f) {
        listeners.parallelStream().forEach(l -> l.newFlaw(f));
    }

    void fireNewResolver(Resolver r) {
        listeners.parallelStream().forEach(l -> l.newResolver(r));
    }

    private void fireFlawUpdate(Flaw f) {
        listeners.parallelStream().forEach(l -> {
            l.updateFlaw(f);
            for (Resolver supp : f.getSupports()) {
                fireResolverUpdate(supp);
            }
        });
    }

    void fireResolverUpdate(Resolver r) {
        listeners.parallelStream().forEach(l -> l.updateResolver(r));
    }

    void fireNewCausalLink(Flaw f, Resolver r) {
        listeners.parallelStream().forEach(l -> l.newCausalLink(f, r));
    }

    private void fireCurrentFlaw(Flaw f) {
        listeners.parallelStream().forEach(l -> l.currentFlaw(f));
    }

    private void fireCurrentResolver(Resolver r) {
        listeners.parallelStream().forEach(l -> l.currentResolver(r));
    }

    public void addSolverListener(SolverListener listener) {
        listeners.add(listener);
        for (Resolver r : resolvers) {
            listener.newResolver(r);
        }
    }

    public void removeSolverListener(SolverListener listener) {
        listeners.remove(listener);
    }

    private static class Layer {

        private final Resolver resolver;
        private final Set<Flaw> flaws;
        private final Set<Flaw> inconsistencies;

        Layer(Resolver resolver, Set<Flaw> flaws, Set<Flaw> inconsistencies) {
            this.resolver = resolver;
            this.flaws = flaws;
            this.inconsistencies = inconsistencies;
        }
    }
}
