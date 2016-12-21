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

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolVar;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Propagator;
import it.cnr.istc.ac.Var;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Resolver implements Propagator {

    protected final Solver solver;
    protected final BoolVar in_plan;
    protected final ArithExpr cost;
    private final Collection<Flaw> preconditions = new ArrayList<>();
    protected final Flaw effect;
    protected double estimated_cost = Double.POSITIVE_INFINITY;

    public Resolver(Solver s, ArithExpr c, Flaw e) {
        this.solver = s;
        this.cost = c;
        this.effect = e;
        this.in_plan = s.network.newBool();
    }

    public Collection<Flaw> getPreconditions() {
        return Collections.unmodifiableCollection(preconditions);
    }

    public Flaw getEffect() {
        return effect;
    }

    public double getEstimatedCost() {
        return estimated_cost;
    }

    protected boolean addPrecondition(Flaw f) {
        preconditions.add(f);
        solver.listeners.parallelStream().forEach(l -> l.newCausalLink(f, this));
        // if this choice is in plan, its preconditions must be in plan as well..
        solver.network.add(solver.network.imply(in_plan, f.in_plan));
        return solver.network.propagate();
    }

    protected void updateCosts(Set<Flaw> visited) {
        double c_cost = Double.NEGATIVE_INFINITY;
        for (Flaw f : preconditions) {
            if (f.estimated_cost > c_cost) {
                c_cost = f.estimated_cost;
            }
        }
        if (c_cost != estimated_cost) {
            if (!solver.rootLevel() && !solver.resolver_costs.containsKey(this)) {
                solver.resolver_costs.put(this, estimated_cost);
            }
            estimated_cost = c_cost;
            solver.listeners.parallelStream().forEach(l -> l.updateResolver(this));
            if (effect != null) {
                effect.updateCosts(visited);
            }
        }
    }

    abstract boolean apply();

    @Override
    public Var<?>[] getArgs() {
        return new Var<?>[]{in_plan};
    }

    @Override
    public boolean propagate(Var<?> v) {
        if (in_plan.evaluate() == LBool.L_FALSE) {
            estimated_cost = Double.POSITIVE_INFINITY;
            if (effect != null) {
                effect.updateCosts(new HashSet<>());
            }
        }
        return true;
    }

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + estimated_cost;
    }
}
