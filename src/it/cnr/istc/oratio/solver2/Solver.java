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

import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.Disjunction;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    final Map<Atom, Flaw> reasons = new IdentityHashMap<>();
    Map<Flaw, Double> flaw_costs;
    Map<Resolver, Double> resolver_costs;
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

    public boolean rootLevel() {
        return layers.isEmpty();
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
