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
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.Disjunction;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Type;
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
    final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        resolver = new FindSolution(this);
        ctr_var = resolver.in_plan;
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
        EFlaw flaw = new EFlaw(this, resolver, c_enum);
        listeners.parallelStream().forEach(l -> l.newFlaw(flaw));
        if (!resolver.addPrecondition(flaw)) {
            LOG.info("cannot create enum: inconsistent problem..");
        }
        flaws.add(flaw);
        flaw_q.add(flaw);
        return c_enum;
    }

    @Override
    public boolean newFact(Atom atom) {
        if (!super.newFact(atom)) {
            return false;
        }
        FFlaw flaw = new FFlaw(this, resolver, atom);
        listeners.parallelStream().forEach(l -> l.newFlaw(flaw));
        if (!resolver.addPrecondition(flaw)) {
            LOG.info("cannot create fact: inconsistent problem..");
            return false;
        }
        flaws.add(flaw);
        flaw_q.add(flaw);
        return true;
    }

    @Override
    public boolean newGoal(Atom atom) {
        if (!super.newGoal(atom)) {
            return false;
        }
        GFlaw flaw = new GFlaw(this, resolver, atom);
        listeners.parallelStream().forEach(l -> l.newFlaw(flaw));
        if (!resolver.addPrecondition(flaw)) {
            LOG.info("cannot create goal: inconsistent problem..");
            return false;
        }
        flaws.add(flaw);
        flaw_q.add(flaw);
        return true;
    }

    @Override
    public boolean newDisjunction(IEnv env, Disjunction d) {
        if (!super.newDisjunction(env, d)) {
            return false;
        }
        DFlaw flaw = new DFlaw(this, resolver, env, d);
        listeners.parallelStream().forEach(l -> l.newFlaw(flaw));
        if (!resolver.addPrecondition(flaw)) {
            LOG.info("cannot create disjunction: inconsistent problem..");
            return false;
        }
        flaws.add(flaw);
        flaw_q.add(flaw);
        return true;
    }

    public boolean solve() {
        LOG.info("solving the problem..");

        // we update the planning graph with the inconsistencies..
        for (Flaw f : get_inconsistencies()) {
            if (resolver.addPrecondition(f)) {
                flaws.add(f);
                flaw_q.add(f);
            } else {
                // the problem is unsolvable..
                return false;
            }
        }
        if (flaws.isEmpty()) {
            // we have found a solution..
            return true;
        } else if (!build_planning_graph()) {
            // the problem is unsolvable..
            return false;
        }

        while (!flaws.isEmpty()) {
            assert flaws.stream().allMatch(flaw -> flaw.isExpanded() && flaw.isSolved() && flaw.in_plan.evaluate() == LBool.L_TRUE);

            // we select the most expensive flaw (i.e., the nearest to the top level flaws)..
            Flaw most_expensive_flaw = flaws.stream().max((Flaw f0, Flaw f1) -> Double.compare(f0.estimated_cost, f1.estimated_cost)).get();
            flaws.remove(most_expensive_flaw);

            // we select the least expensive resolver (i.e., the most promising for finding a solution)..
            Optional<Resolver> min = most_expensive_flaw.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).min((Resolver r0, Resolver r1) -> Double.compare(r0.estimated_cost, r1.estimated_cost));

            if (min.isPresent()) {
                resolver = min.get();
                Layer l = new Layer(flaw_costs, resolver_costs, flaws);
                flaw_costs = new IdentityHashMap<>();
                resolver_costs = new IdentityHashMap<>();
                flaws = new HashSet<>(flaws);
                layers.add(l);

                // we try to enforce the resolver..
                if (network.assign(resolver.in_plan)) {
                    // we add sub-goals..
                    flaws.addAll(resolver.getPreconditions());
                } else {
                    // we compute the unsat-core..
                    Collection<BoolVar> unsat_core = network.getUnsatCore();

                    // we build a no-good..
                    Collection<BoolVar> ng_vars = new ArrayList<>(unsat_core.size());
                    for (BoolVar v : unsat_core) {
                        ng_vars.add((BoolVar) network.not(v).to_var(network));
                    }
                    BoolExpr no_good = network.or(ng_vars.toArray(new BoolVar[ng_vars.size()]));

                    // we backjump.. 
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
                    network.add(no_good);
                    boolean propagate = network.propagate();
                    assert propagate;
                }
            }
        }

        for (Flaw f : get_inconsistencies()) {
            if (resolver.addPrecondition(f)) {
                flaws.add(f);
                flaw_q.add(f);
            } else {
                // the problem is unsolvable..
                return false;
            }
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
                        listeners.parallelStream().forEach(l -> l.newResolver(resolver));
                        resolver = r;
                        ctr_var = resolver.in_plan;
                        if (!r.apply()) {
                            return false;
                        }
                        if (r.estimated_cost < Double.POSITIVE_INFINITY) {
                            resolver.updateCosts(new HashSet<>());
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
        Collection<Flaw> c_flaws = new HashSet<>();
        for (Type type : c_types) {
            if (type instanceof SmartType) {
                c_flaws.addAll(((SmartType) type).getInconsistencies());
            }
        }
        return c_flaws;
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
        boolean apply() {
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
