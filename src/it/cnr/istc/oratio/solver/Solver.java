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
    Set<Flaw> inconsistencies = new HashSet<>();
    private final LinkedList<Layer> layers = new LinkedList<>();
    private Resolver resolver;
    private final LinkedList<Flaw> flaw_q = new LinkedList<>();
    private final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        resolver = new FindSolution(this);
        ctr_var = resolver.in_plan;
        boolean propagate = network.add(ctr_var) && network.propagate();
        assert propagate;

        try {
            boolean read = read(new File(Solver.class.getResource("time.rddl").getPath()));
            assert read;
        } catch (IOException ex) {
            Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
        }

        types.put(StateVariable.NAME, new StateVariable(this));
        types.put(ReusableResource.NAME, new ReusableResource(this));
        types.put(Agent.NAME, new Agent(this));
        types.put(ImpulsiveAgent.NAME, new ImpulsiveAgent(this));
        types.put(PropositionalAgent.NAME, new PropositionalAgent(this));
        types.put(PropositionalImpulsiveAgent.NAME, new PropositionalImpulsiveAgent(this));
    }

    @Override
    public IEnumItem newEnum(Type type, IItem... values) {
        IEnumItem c_enum = super.newEnum(type, values);
        EnumFlaw flaw = new EnumFlaw(this, resolver, c_enum);
        fireNewFlaw(flaw);
        if (!resolver.addPrecondition(flaw)) {
            LOG.log(Level.INFO, "cannot create enum {0}: inconsistent problem..", flaw.toSimpleString());
            return null;
        }
        if (flaw.cause.effect == null) {
            // we have a top-level flaw..
            flaws.add(flaw);
        }
        flaw_q.add(flaw);
        return c_enum;
    }

    @Override
    protected boolean newFact(Atom atom) {
        if (super.newFact(atom)) {
            AtomFlaw flaw = new AtomFlaw(this, resolver, atom, true);
            reasons.put(atom, flaw);
            fireNewFlaw(flaw);
            if (!resolver.addPrecondition(flaw)) {
                LOG.log(Level.INFO, "cannot create fact {0}: inconsistent problem..", flaw.toSimpleString());
                return false;
            }
            if (flaw.cause.effect == null) {
                // we have a top-level flaw..
                flaws.add(flaw);
            }
            flaw_q.add(flaw);
            return true;
        } else {
            return false;
        }
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
        if (super.newGoal(atom)) {
            AtomFlaw flaw = new AtomFlaw(this, resolver, atom, false);
            reasons.put(atom, flaw);
            fireNewFlaw(flaw);
            if (!resolver.addPrecondition(flaw)) {
                LOG.log(Level.INFO, "cannot create goal {0}: inconsistent problem..", flaw.toSimpleString());
                return false;
            }
            if (flaw.cause.effect == null) {
                // we have a top-level flaw..
                flaws.add(flaw);
            }
            flaw_q.add(flaw);
            return true;
        } else {
            return false;
        }
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
        if (super.newDisjunction(env, d)) {
            DisjunctionFlaw flaw = new DisjunctionFlaw(this, resolver, env, d);
            fireNewFlaw(flaw);
            if (!resolver.addPrecondition(flaw)) {
                LOG.log(Level.INFO, "cannot create goal {0}: inconsistent problem..", flaw.toSimpleString());
                return false;
            }
            if (flaw.cause.effect == null) {
                // we have a top-level flaw..
                flaws.add(flaw);
            }
            flaw_q.add(flaw);
            return true;
        } else {
            return false;
        }
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
            // we clean up trivial flaws..
            clear_flaws(flaws);
            clear_flaws(inconsistencies);

            // we remove the inconsistencies..
            if (!inconsistencies.isEmpty()) {
                // we create a new layer..
                Layer l = new Layer(resolver, flaw_costs, resolver_costs, flaws, inconsistencies);
                flaw_costs = new IdentityHashMap<>();
                resolver_costs = new IdentityHashMap<>();
                inconsistencies = new HashSet<>(inconsistencies);
                layers.add(l);

                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = inconsistencies.stream().max((Flaw f0, Flaw f1) -> Double.compare(f0.estimated_cost, f1.estimated_cost)).get();
                fireCurrentFlaw(most_expensive_flaw);
                inconsistencies.remove(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.estimated_cost, r1.estimated_cost)).get();
                fireCurrentResolver(resolver);

                // we try to enforce the resolver..
                network.push();
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
            if (!inconsistencies.isEmpty()) {
                for (Flaw f : inconsistencies) {
                    fireNewFlaw(f);
                    if (!resolver.addPrecondition(f)) {
                        // the problem is unsolvable..
                        LOG.log(Level.INFO, "cannot create flaw {0}: inconsistent problem..", f.toSimpleString());
                        return false;
                    }
                    flaw_q.add(f);
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
                // we remove a resolution flaw..
                // we create a new layer..
                Layer l = new Layer(resolver, flaw_costs, resolver_costs, flaws, inconsistencies);
                flaw_costs = new IdentityHashMap<>();
                resolver_costs = new IdentityHashMap<>();
                flaws = new HashSet<>(flaws);
                layers.add(l);

                // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
                Flaw most_expensive_flaw = flaws.stream().max((Flaw f0, Flaw f1) -> Double.compare(f0.estimated_cost, f1.estimated_cost)).get();
                fireCurrentFlaw(most_expensive_flaw);
                flaws.remove(most_expensive_flaw);

                // we select the least expensive resolver (i.e., the most promising for finding a solution)..
                resolver = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.estimated_cost, r1.estimated_cost)).get();
                fireCurrentResolver(resolver);

                // we try to enforce the resolver..
                network.push();
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
     * Returns an instance of {@code Resolver} representing the current
     * resolver.
     *
     * @return the current resolver.
     */
    public Resolver getResolver() {
        return resolver;
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
        while (tmp_r.estimated_cost == Double.POSITIVE_INFINITY && !flaw_q.isEmpty()) {
            Flaw flaw = flaw_q.pollFirst();
            if (!flaw.isExpanded()) {
                // not all flaws require to be expanded..
                boolean requires_expansion = true;
                Flaw c_f = flaw;
                while (c_f.cause != tmp_r) {
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
        while (!(network.add(no_good) && network.propagate())) {
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
            resolver = l_l.resolver;
            flaw_costs = l_l.flaw_costs;
            resolver_costs = l_l.resolver_costs;
            flaws = l_l.flaws;
            inconsistencies = l_l.inconsistencies;
            layers.pollLast();
        }

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

        private final Resolver resolver;
        private final Map<Flaw, Double> flaw_costs;
        private final Map<Resolver, Double> resolver_costs;
        private final Set<Flaw> flaws;
        private final Set<Flaw> inconsistencies;

        Layer(Resolver resolver, Map<Flaw, Double> flaw_costs, Map<Resolver, Double> resolver_costs, Set<Flaw> flaws, Set<Flaw> inconsistencies) {
            this.resolver = resolver;
            this.flaw_costs = flaw_costs;
            this.resolver_costs = resolver_costs;
            this.flaws = flaws;
            this.inconsistencies = inconsistencies;
        }
    }
}
