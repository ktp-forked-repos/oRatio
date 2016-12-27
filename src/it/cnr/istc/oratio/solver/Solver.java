/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());
    final Map<Atom, Flaw> reasons = new IdentityHashMap<>();
    Map<Flaw, Double> flaw_costs;
    Map<Resolver, Double> resolver_costs;
    Set<Flaw> flaws = new HashSet<>();
    private final LinkedList<Layer> layers = new LinkedList<>();
    private Resolver resolver;
    private final LinkedList<Flaw> flaw_q = new LinkedList<>();
    private final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        resolver = new FindSolution(this);
        ctr_var = resolver.in_plan;
        boolean propagate = network.add(ctr_var) && network.propagate();
        assert propagate;

        types.put(StateVariable.NAME, new StateVariable(this));

        try {
            boolean read = read(new File(Solver.class.getResource("time.rddl").getPath()));
            assert read;
        } catch (IOException ex) {
            Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public IEnumItem newEnum(Type type, IItem... values) {
        IEnumItem c_enum = super.newEnum(type, values);
        if (newFlaw(new EFlaw(this, resolver, c_enum))) {
            return c_enum;
        } else {
            return null;
        }
    }

    @Override
    public boolean newFact(Atom atom) {
        FFlaw flaw = new FFlaw(this, resolver, atom);
        reasons.put(atom, flaw);
        return newFlaw(flaw);
    }

    @Override
    public boolean newGoal(Atom atom) {
        GFlaw flaw = new GFlaw(this, resolver, atom);
        reasons.put(atom, flaw);
        return newFlaw(flaw);
    }

    @Override
    public boolean newDisjunction(IEnv env, Disjunction d) {
        return super.newDisjunction(env, d) && newFlaw(new DFlaw(this, resolver, env, d));
    }

    public boolean newFlaw(Flaw f) {
        fireNewFlaw(f);
        if (!resolver.addPrecondition(f)) {
            LOG.log(Level.INFO, "cannot create flaw {0}: inconsistent problem..", f.toSimpleString());
            return false;
        }
        if (f.cause.effect == null) {
            // we have a top-level flaw..
            flaws.add(f);
        }
        flaw_q.add(f);
        return true;
    }

    void fireNewFlaw(Flaw f) {
        listeners.parallelStream().forEach(l -> l.newFlaw(f));
    }

    void fireNewResolver(Resolver r) {
        listeners.parallelStream().forEach(l -> l.newResolver(r));
    }

    void fireFlawUpdate(Flaw f) {
        listeners.parallelStream().forEach(l -> l.updateFlaw(f));
    }

    void fireResolverUpdate(Resolver r) {
        listeners.parallelStream().forEach(l -> l.updateResolver(r));
    }

    void fireNewCausalLink(Flaw f, Resolver r) {
        listeners.parallelStream().forEach(l -> l.newCausalLink(f, r));
    }

    void fireCurrentFlaw(Flaw f) {
        listeners.parallelStream().forEach(l -> l.currentFlaw(f));
    }

    void fireCurrentResolver(Resolver r) {
        listeners.parallelStream().forEach(l -> l.currentResolver(r));
    }

    boolean fireFactLinked(Atom atom) {
        return super.newFact(atom);
    }

    boolean fireGoalLinked(Atom atom) {
        return super.newGoal(atom);
    }

    /**
     * Returns an instance of {@code Resolver} representing the current
     * resolver.
     *
     * @return the current resolver.
     */
    public Resolver getResolver() {
        return resolver;
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

        if (!build_planning_graph()) {
            // the problem is unsolvable..
            return false;
        }

        // we clean up trivial flaws..
        clear_flaws(flaws);

        while (!flaws.isEmpty()) {
            // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
            Flaw most_expensive_flaw = flaws.stream().max((Flaw f0, Flaw f1) -> Double.compare(f0.estimated_cost, f1.estimated_cost)).get();
            fireCurrentFlaw(most_expensive_flaw);
            flaws.remove(most_expensive_flaw);

            // we select the least expensive resolver (i.e., the most promising for finding a solution)..
            Resolver least_expensive_resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.estimated_cost, r1.estimated_cost)).get();
            fireCurrentResolver(least_expensive_resolver);

            // we create a new layer..
            Layer l = new Layer(flaw_costs, resolver_costs, flaws);
            flaw_costs = new IdentityHashMap<>();
            resolver_costs = new IdentityHashMap<>();
            layers.add(l);

            // we try to enforce the resolver..
            if (network.assign(least_expensive_resolver.in_plan)) {
                // we add sub-goals..
                flaws.addAll(least_expensive_resolver.getPreconditions());

                // we clean up trivial flaws..
                clear_flaws(flaws);

                // we remove the inconsistencies..
                if (!remove_inconsistencies()) {
                    // we need to backjump..
                    if (!backjump()) {
                        // the problem is unsolvable..
                        return false;
                    }
                }
            } else {
                // we need to back-jump..
                if (!backjump()) {
                    // the problem is unsolvable..
                    return false;
                }
            }
        }

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
                network.push();
                if (network.assign(expr)) {
                    network.pop();
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

    private boolean build_planning_graph() {
        LOG.info("building the planning graph..");
        assert network.rootLevel();

        if (flaw_q.isEmpty()) {
            // there is nothing to reason on..
            return true;
        }

        Resolver tmp_r = resolver;
        while (tmp_r.estimated_cost == Double.POSITIVE_INFINITY && !flaw_q.isEmpty()) {
            Flaw flaw = flaw_q.pollFirst();
            if (!flaw.isExpanded()) {
                // not all flaws require to be expanded..
                boolean requires_expansion = true;
                Flaw c_f = flaw;
                while (c_f.cause.effect != null) {
                    if (c_f.cause.effect.isSolved()) {
                        requires_expansion = false;
                        break;
                    } else {
                        c_f = c_f.cause.effect;
                    }
                }

                if (requires_expansion) {
                    if (!flaw.expand()) {
                        return false;
                    }
                    for (Resolver r : flaw.getResolvers()) {
                        resolver = r;
                        ctr_var = resolver.in_plan;
                        if (!resolver.apply()) {
                            return false;
                        }
                        if (resolver.getPreconditions().isEmpty()) {
                            // there are no requirements for this resolver..
                            resolver.estimated_cost = 0;
                            fireResolverUpdate(resolver);
                            flaw.updateCosts(new HashSet<>());
                        }
                    }
                } else {
                    // we postpone the expansion..
                    flaw_q.add(flaw);
                }
            }
        }

        assert tmp_r.estimated_cost < Double.POSITIVE_INFINITY;

        resolver = tmp_r;
        ctr_var = resolver.in_plan;
        return true;
    }

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
     * Groups all the inconsistencies and tries to solve all of them.
     *
     * @return {@code true} if there are no inconsistencies in the items and
     * {@code false} if the problem is inconsistent.
     */
    private boolean remove_inconsistencies() {
        LOG.info("removing inconsistencies..");
        // we update the planning graph with the inconsistencies..
        Set<Flaw> incs = get_inconsistencies();
        while (!incs.isEmpty()) {
            for (Flaw f : incs) {
                if (resolver.addPrecondition(f)) {
                    flaw_q.add(f);
                } else {
                    // the problem is unsolvable..
                    return false;
                }
            }

            if (!build_planning_graph()) {
                // the problem is unsolvable..
                return false;
            }

            // we clean up trivial flaws..
            clear_flaws(incs);

            while (!incs.isEmpty()) {
                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = incs.stream().max((Flaw f0, Flaw f1) -> Double.compare(f0.estimated_cost, f1.estimated_cost)).get();
                fireCurrentFlaw(most_expensive_flaw);
                incs.remove(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                Resolver least_expensive_resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.estimated_cost, r1.estimated_cost)).get();
                fireCurrentResolver(least_expensive_resolver);

                // we create a new layer..
                Layer l = new Layer(flaw_costs, resolver_costs, flaws);
                flaw_costs = new IdentityHashMap<>();
                resolver_costs = new IdentityHashMap<>();
                layers.add(l);

                // we try to enforce the resolver..
                if (network.assign(least_expensive_resolver.in_plan)) {
                    // we add sub-goals..
                    incs.addAll(least_expensive_resolver.getPreconditions());

                    // we clean up trivial flaws..
                    clear_flaws(incs);
                } else {
                    // we need to back-jump..
                    return false;
                }
            }
            incs = get_inconsistencies();
        }

        return true;
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
        BoolExpr no_good = network.or(ng_vars.toArray(new BoolVar[ng_vars.size()]));

        // we backtrack till we can enforce the no-good.. 
        while (no_good.evaluate() == LBool.L_FALSE) {
            if (rootLevel()) {
                // the problem is inconsistent..
                return false;
            }

            // we restore the constraint network state..
            network.pop();

            // we restore updated flaws and resolvers costs..
            for (Map.Entry<Flaw, Double> entry : flaw_costs.entrySet()) {
                entry.getKey().estimated_cost = entry.getValue();
            }
            for (Map.Entry<Resolver, Double> entry : resolver_costs.entrySet()) {
                entry.getKey().estimated_cost = entry.getValue();
            }

            Layer l_l = layers.getLast();
            flaw_costs = l_l.flaw_costs;
            resolver_costs = l_l.resolver_costs;
            flaws = l_l.flaws;
            layers.pollLast();
        }

        // we add the no-good..
        boolean propagate = network.add(no_good) && network.propagate();
        assert propagate;

        return true;
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
            assert trivial_flaw.get().getResolvers().iterator().next().in_plan.evaluate() == LBool.L_TRUE;
            assert trivial_flaw.get().getResolvers().iterator().next().getPreconditions().stream().allMatch(flaw -> flaw.in_plan.evaluate() == LBool.L_TRUE);
            c_flaws.remove(trivial_flaw.get());
            c_flaws.addAll(trivial_flaw.get().getResolvers().iterator().next().getPreconditions());
            trivial_flaw = c_flaws.stream().filter(f -> f.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).count() == 1).findAny();
        }
    }

    public boolean rootLevel() {
        return layers.isEmpty();
    }

    public void addSolverListener(SolverListener listener) {
        listeners.add(listener);
        listener.newResolver(resolver);
    }

    public void removeSolverListener(SolverListener listener) {
        listeners.remove(listener);
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

    static class Layer {

        private final Map<Flaw, Double> flaw_costs;
        private final Map<Resolver, Double> resolver_costs;
        private final Set<Flaw> flaws;

        Layer(Map<Flaw, Double> flaw_costs, Map<Resolver, Double> resolver_costs, Set<Flaw> flaws) {
            this.flaw_costs = flaw_costs;
            this.resolver_costs = resolver_costs;
            this.flaws = flaws;
        }
    }
}
