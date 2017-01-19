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
package it.cnr.istc.oratio.solver2;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.BoolVar;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.Disjunction;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
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
    private Resolver resolver;
    private final LinkedList<Layer> layers = new LinkedList<>();
    final LinkedList<Resolver> resolvers = new LinkedList<>();
    private final Collection<SolverListener> listeners = new ArrayList<>();

    @Override
    public IEnumItem newEnum(IItem value) {
        return super.newEnum(value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean newFact(Atom atom) {
        return super.newFact(atom); //To change body of generated methods, choose Tools | Templates.
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
        return super.newGoal(atom); //To change body of generated methods, choose Tools | Templates.
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
        return super.newDisjunction(env, d); //To change body of generated methods, choose Tools | Templates.
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

    public boolean rootLevel() {
        return layers.isEmpty();
    }

    private void push() {
        // we create a new layer..
        Layer l = new Layer(resolver, flaw_costs, resolver_costs, flaws, inconsistencies);
        flaw_costs = new IdentityHashMap<>();
        resolver_costs = new IdentityHashMap<>();
        flaws = new HashSet<>(flaws);
        inconsistencies = new HashSet<>(inconsistencies);
        layers.add(l);

        // we also create a new layer in the constraint network..
        network.push();
    }

    private void pop() {
        // we restore the constraint network state..
        network.pop();

        // we also restore updated flaws and resolvers costs..
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

    public void addSolverListener(SolverListener listener) {
        listeners.add(listener);
        for (Resolver resolver : resolvers) {
            listener.newResolver(resolver);
        }
    }

    public void removeSolverListener(SolverListener listener) {
        listeners.remove(listener);
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
