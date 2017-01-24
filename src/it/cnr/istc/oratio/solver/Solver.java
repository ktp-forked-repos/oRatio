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
package it.cnr.istc.oratio.solver;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.BoolVar;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Network;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.Disjunction;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Type;
import it.cnr.istc.oratio.solver.types.Agent;
import it.cnr.istc.oratio.solver.types.ImpulsiveAgent;
import it.cnr.istc.oratio.solver.types.PropositionalAgent;
import it.cnr.istc.oratio.solver.types.PropositionalImpulsiveAgent;
import it.cnr.istc.oratio.solver.types.PropositionalState;
import it.cnr.istc.oratio.solver.types.ReusableResource;
import it.cnr.istc.oratio.solver.types.StateVariable;
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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());
    final Map<Atom, Flaw> reasons = new IdentityHashMap<>();
    Map<Flaw, Double> costs = new IdentityHashMap<>();
    Map<Flaw, Double> flaw_costs;
    Set<Flaw> flaws = new HashSet<>();
    Set<Flaw> inconsistencies = new HashSet<>();
    Resolver resolver;
    private final LinkedList<Flaw> flaw_q = new LinkedList<>();
    final LinkedList<Resolver> resolvers = new LinkedList<>();
    private final LinkedList<Layer> layers = new LinkedList<>();
    private final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        resolver = new FindSolution(this);
        ctr_var = resolver.in_plan;
        boolean propagate = network.add(ctr_var);
        assert propagate;

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
    public IEnumItem newEnum(Type type, IItem... values) {
        IEnumItem c_enum = super.newEnum(type, values);
        EnumFlaw flaw = new EnumFlaw(this, c_enum);
        fireFlawUpdate(flaw);
        if (!resolver.addPrecondition(flaw)) {
            LOG.log(Level.INFO, "cannot create enum {0}: inconsistent problem..", flaw.toSimpleString());
            return null;
        }
        fireResolverUpdate(resolver);
        if (resolver.effect == null) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return c_enum;
    }

    @Override
    protected boolean newFact(Atom atom) {
        if (!super.newFact(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, true);
        fireFlawUpdate(flaw);
        reasons.put(atom, flaw);
        if (!resolver.addPrecondition(flaw)) {
            LOG.log(Level.INFO, "cannot create fact {0}: inconsistent problem..", flaw.toSimpleString());
            return false;
        }
        fireResolverUpdate(resolver);
        if (resolver.effect == null) {
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
        if (!super.newGoal(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, false);
        fireFlawUpdate(flaw);
        reasons.put(atom, flaw);
        if (!resolver.addPrecondition(flaw)) {
            LOG.log(Level.INFO, "cannot create goal {0}: inconsistent problem..", flaw.toSimpleString());
            return false;
        }
        fireResolverUpdate(resolver);
        if (resolver.effect == null) {
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
        if (!super.newDisjunction(env, d)) {
            return false;
        }
        DisjunctionFlaw flaw = new DisjunctionFlaw(this, env, d);
        fireFlawUpdate(flaw);
        if (!resolver.addPrecondition(flaw)) {
            LOG.log(Level.INFO, "cannot create goal {0}: inconsistent problem..", flaw.toSimpleString());
            return false;
        }
        fireResolverUpdate(resolver);
        if (resolver.effect == null) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return true;
    }

    /**
     * Checks if the given boolean expression can be made {@code true} in the
     * current constraint network. This method saves the current state by
     * calling {@link Network#push()}. If the expression can be made
     * {@code true} the state of the network is restored, otherwise a no-good is
     * added to the network and {@link #backjump()} is called.
     *
     * @param expr the boolean expression to be checked.
     * @return {@code true} if the given boolean expression can be made true.
     */
    public boolean check(BoolExpr expr) {
        switch (expr.evaluate()) {
            case L_TRUE:
                return true;
            case L_FALSE:
                return false;
            case L_UNKNOWN:
                push();
                if (network.assign(expr)) {
                    pop();
                    return true;
                } else {
                    boolean backjump = backjump();
                    assert backjump;
                    return false;
                }
            default:
                throw new AssertionError(expr.evaluate().name());
        }
    }

    /**
     * Solves the current problem returning {@code true} if a solution has been
     * found and {@code false} if the problem is unsolvable.
     *
     * @return {@code true} if a solution has been found or {@code false} if the
     * problem is unsolvable.
     */
    public boolean solve() {
        LOG.info("solving the problem..");

        // we build the planning graph..
        if (!build_planning_graph()) {
            // the problem is unsolvable..
            return false;
        }

        while (true) {
            while (flaws.stream().anyMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_FALSE)) {
                if (rootLevel()) {
                    return false;
                }
                pop();
            }
            while (inconsistencies.stream().anyMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_FALSE)) {
                if (rootLevel()) {
                    return false;
                }
                pop();
            }

            // we clean up trivial flaws..
            clear_flaws(flaws);
            clear_flaws(inconsistencies);

            // we remove the inconsistencies..
            if (!inconsistencies.isEmpty()) {
                // we create a new layer..
                push();

                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = inconsistencies.stream().max((Flaw f0, Flaw f1) -> Double.compare(costs.getOrDefault(f0, Double.POSITIVE_INFINITY), costs.getOrDefault(f1, Double.POSITIVE_INFINITY))).get();
                assert most_expensive_flaw.in_plan.evaluate() == LBool.L_TRUE;
                fireCurrentFlaw(most_expensive_flaw);
                inconsistencies.remove(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(r0.cost), r1.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(r1.cost))).get();
                assert resolver.in_plan.evaluate() == LBool.L_UNKNOWN;
                fireCurrentResolver(resolver);

                // we try to enforce the resolver..
                if (network.assign(resolver.in_plan)) {
                    // we add sub-goals..
                    inconsistencies.addAll(resolver.getPreconditions());
                } else {
                    // we need to backjump..
                    if (!backjump()) {
                        // the problem is unsolvable..
                        return false;
                    }
                }
                continue;
            }

            // we collect the inconsistencies..
            inconsistencies.addAll(get_inconsistencies());
            if (inconsistencies.stream().anyMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_FALSE)) {
                if (rootLevel()) {
                    return false;
                }
                pop();
                continue;
            }
            if (!inconsistencies.isEmpty()) {
                for (Flaw flaw : inconsistencies) {
                    fireFlawUpdate(flaw);
                    if (!resolver.addPrecondition(flaw)) {
                        // the problem is unsolvable..
                        LOG.log(Level.INFO, "cannot create flaw {0}: inconsistent problem..", flaw.toSimpleString());
                        return false;
                    }
                    fireResolverUpdate(resolver);
                    flaw_q.add(flaw);
                }

                // we update the planning graph..
                if (!build_planning_graph()) {
                    // the problem is unsolvable..
                    return false;
                }
                continue;
            }

            assert inconsistencies.isEmpty();
            if (!flaws.isEmpty()) {
                // we create a new layer..
                push();

                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = flaws.stream().max((Flaw f0, Flaw f1) -> Double.compare(costs.getOrDefault(f0, Double.POSITIVE_INFINITY), costs.getOrDefault(f1, Double.POSITIVE_INFINITY))).get();
                assert most_expensive_flaw.in_plan.evaluate() == LBool.L_TRUE;
                fireCurrentFlaw(most_expensive_flaw);
                flaws.remove(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(r0.cost), r1.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(r1.cost))).get();
                assert resolver.in_plan.evaluate() == LBool.L_UNKNOWN;
                resolvers.add(resolver);
                fireCurrentResolver(resolver);

                // we try to enforce the resolver..
                if (network.assign(resolver.in_plan)) {
                    // we add sub-goals..
                    flaws.addAll(resolver.getPreconditions());
                } else {
                    // we need to back-jump..
                    if (!backjump()) {
                        // the problem is unsolvable..
                        return false;
                    }
                }
            } else {
                // Hurray!! We have found a solution..
                return true;
            }
        }
    }

    /**
     * Builds (or updates) the planning graph returning {@code true} if the
     * building process succedes and {@code false} if the problem is detected as
     * unsolvable.
     *
     * @return {@code true} if the building process succedes or {@code false} if
     * the problem is detected as unsolvable.
     */
    private boolean build_planning_graph() {
        LOG.info("building the planning graph..");

        if (flaw_q.isEmpty()) {
            // there is nothing to reason on..
            return true;
        }

        Resolver tmp_r = resolver;
        while (tmp_r.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(tmp_r.cost) == Double.POSITIVE_INFINITY && !flaw_q.isEmpty()) {
            Flaw flaw = flaw_q.pollFirst();
            fireFlawUpdate(flaw);
            if (!isDeferrable(flaw)) {
                LOG.log(Level.INFO, "expanding {0}", flaw.toSimpleString());
                if (!flaw.expand()) {
                    return false;
                }
                for (Resolver r : flaw.getResolvers()) {
                    resolver = r;
                    ctr_var = resolver.in_plan;
                    if (!resolver.apply()) {
                        return false;
                    }
                    fireResolverUpdate(resolver);
                    if (resolver.getPreconditions().isEmpty()) {
                        // there are no requirements for this resolver..
                        setCost(flaw, 0);
                    }
                }
            } else {
                // we postpone the expansion..
                LOG.log(Level.INFO, "deferring {0}", flaw.toSimpleString());
                flaw_q.add(flaw);
            }
        }

        assert tmp_r.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(tmp_r.cost) < Double.POSITIVE_INFINITY;

        resolver = tmp_r;
        ctr_var = resolver.in_plan;
        return true;
    }

    void setCost(Flaw flaw, double cost) {
        if (costs.getOrDefault(flaw, Double.POSITIVE_INFINITY) != cost) {
            if (!rootLevel() && !flaw_costs.containsKey(flaw) && costs.containsKey(flaw)) {
                flaw_costs.put(flaw, costs.get(flaw));
            }
            costs.put(flaw, cost);
            fireFlawUpdate(flaw);
            for (Resolver cause : flaw.getCauses()) {
                fireResolverUpdate(cause);
            }

            Set<Flaw> visited = new HashSet<>();
            visited.add(flaw);
            LinkedList<Flaw> queue = new LinkedList<>();
            queue.addAll(flaw.getCauses().stream().filter(cause -> cause.effect != null).map(cause -> cause.effect).collect(Collectors.toList()));
            while (!queue.isEmpty()) {
                Flaw c_flaw = queue.pollFirst();
                if (!visited.contains(c_flaw)) {
                    visited.add(c_flaw);
                    double c_cost = c_flaw.getResolvers().stream().mapToDouble(r -> r.getPreconditions().stream().mapToDouble(pre -> costs.getOrDefault(pre, Double.POSITIVE_INFINITY)).max().orElse(0) + network.evaluate(r.cost)).min().orElse(Double.POSITIVE_INFINITY);
                    if (costs.getOrDefault(c_flaw, Double.POSITIVE_INFINITY) != c_cost) {
                        if (!rootLevel() && !flaw_costs.containsKey(c_flaw) && costs.containsKey(c_flaw)) {
                            flaw_costs.put(c_flaw, costs.get(c_flaw));
                        }
                        costs.put(c_flaw, c_cost);
                        fireFlawUpdate(c_flaw);
                        for (Resolver cause : c_flaw.getCauses()) {
                            fireResolverUpdate(cause);
                        }
                        queue.addAll(c_flaw.getCauses().stream().filter(cause -> cause.effect != null).map(cause -> cause.effect).collect(Collectors.toList()));
                    }
                }
            }
        }
    }

    public double getCost(Flaw flaw) {
        return costs.getOrDefault(flaw, Double.POSITIVE_INFINITY);
    }

    public boolean isDeferrable(Flaw flaw) {
        if (!flaw.isDisjunctive()) {
            return false;
        } else if (costs.getOrDefault(flaw, Double.POSITIVE_INFINITY) < Double.POSITIVE_INFINITY) {
            return true;
        } else if (flaw.getCauses().size() == 1) {
            Flaw effect = flaw.getCauses().iterator().next().effect;
            return effect != null && isDeferrable(effect);
        } else {
            return flaw.getCauses().stream().map(cause -> cause.effect).filter(effect -> effect != null).anyMatch(effect -> isDeferrable(effect));
        }
    }

    /**
     * Collects all the inconsistencies of the {@link SmartType} instances.
     *
     * @return a collection of {@link Flaw}s representing all the
     * inconsistencies.
     */
    private Set<Flaw> get_inconsistencies() {
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
        Set<Flaw> c_flaws = new HashSet<>();
        for (Type type : c_types) {
            if (type instanceof SmartType) {
                c_flaws.addAll(((SmartType) type).getInconsistencies());
            }
        }
        return c_flaws;
    }

    /**
     * Cleans up trivial flaws from the set of given flaws. Flaws are considered
     * trivial if they have a single admissible resolver.
     *
     * @param c_flaws the set of flaws to be cleaned.
     */
    private void clear_flaws(Set<Flaw> c_flaws) {
        Optional<Flaw> trivial_flaw = c_flaws.stream().filter(f -> f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
        while (trivial_flaw.isPresent()) {
            assert trivial_flaw.get().in_plan.evaluate() == LBool.L_TRUE;
            Resolver unique_resolver = trivial_flaw.get().getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).findAny().get();
            assert unique_resolver.in_plan.evaluate() == LBool.L_TRUE;
            assert unique_resolver.getPreconditions().stream().allMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_TRUE);
            c_flaws.remove(trivial_flaw.get());
            c_flaws.addAll(unique_resolver.getPreconditions());
            trivial_flaw = c_flaws.stream().filter(f -> f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
        }
    }

    public boolean rootLevel() {
        return layers.isEmpty();
    }

    private void push() {
        // we create a new layer..
        Layer l = new Layer(resolver, flaw_costs, flaws, inconsistencies);
        flaw_costs = new IdentityHashMap<>();
        flaws = new HashSet<>(flaws);
        inconsistencies = new HashSet<>(inconsistencies);
        layers.add(l);

        // we also create a new layer in the constraint network..
        network.push();
    }

    void pop() {
        // we restore the constraint network state..
        network.pop();

        // we also restore updated flaws and resolvers costs..
        for (Map.Entry<Flaw, Double> entry : flaw_costs.entrySet()) {
            costs.put(entry.getKey(), entry.getValue());
        }

        Layer l_l = layers.getLast();
        resolver = l_l.resolver;
        flaw_costs = l_l.flaw_costs;
        flaws = l_l.flaws;
        inconsistencies = l_l.inconsistencies;
        if (resolvers.peekLast() == resolver) {
            resolvers.pollLast();
        }
        layers.pollLast();
    }

    /**
     * Generates a no-good and uses it to backjump until the no-good is
     * admissible returning {@code true} if the no-good can be added to the
     * constraint network or {@code false} if the problem is inconsistent.
     *
     * @return {@code true} if the no-good can be added to the constraint
     * network and {@code false} if the problem is inconsistent.
     */
    private boolean backjump() {
        LOG.info("backjumping..");
        // we compute the unsat-core..
        Collection<BoolVar> unsat_core = network.getUnsatCore();

        // we build a no-good..
        Collection<BoolVar> ng_vars = new ArrayList<>(unsat_core.size());
        for (BoolVar v : unsat_core) {
            ng_vars.add((BoolVar) network.not(v).to_var(network));
        }
        BoolExpr no_good = ng_vars.size() == 1 ? ng_vars.iterator().next() : network.or(ng_vars.toArray(new BoolVar[ng_vars.size()]));

        // we backtrack till we can enforce the no-good.. 
        while (!network.add(no_good)) {
            if (rootLevel()) {
                // the problem is inconsistent..
                return false;
            }

            // we restore flaws and resolvers state..
            pop();
        }

        return true;
    }

    void fireNewFlaw(Flaw f) {
        listeners.parallelStream().forEach(l -> l.newFlaw(f));
    }

    void fireNewResolver(Resolver r) {
        listeners.parallelStream().forEach(l -> l.newResolver(r));
    }

    private void fireFlawUpdate(Flaw f) {
        listeners.parallelStream().forEach(l -> l.updateFlaw(f));
    }

    private void fireResolverUpdate(Resolver r) {
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
        listener.newResolver(resolver);
        for (Resolver r : resolvers) {
            listener.newResolver(r);
        }
    }

    public void removeSolverListener(SolverListener listener) {
        listeners.remove(listener);
    }

    static class Layer {

        private final Resolver resolver;
        private final Map<Flaw, Double> flaw_costs;
        private final Set<Flaw> flaws;
        private final Set<Flaw> inconsistencies;

        Layer(Resolver resolver, Map<Flaw, Double> flaw_costs, Set<Flaw> flaws, Set<Flaw> inconsistencies) {
            this.resolver = resolver;
            this.flaw_costs = flaw_costs;
            this.flaws = flaws;
            this.inconsistencies = inconsistencies;
        }
    }

    static class FindSolution extends Resolver {

        FindSolution(Solver s) {
            super(s, s.network.newReal(0), null);
        }

        @Override
        protected boolean apply() {
            return true;
        }

        @Override
        public String toSimpleString() {
            return "solution";
        }
    }
}
