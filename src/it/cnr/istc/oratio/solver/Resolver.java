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

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolVar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Resolver {

    protected final Solver solver;
    protected final BoolVar in_plan;
    protected final ArithExpr cost;
    private final Collection<Flaw> preconditions = new ArrayList<>();
    protected final Flaw effect;

    public Resolver(Solver s, ArithExpr c, Flaw e) {
        this.solver = s;
        this.cost = c;
        this.effect = e;
        this.in_plan = s.network.newBool();
        this.solver.fireNewResolver(this);
    }

    public Solver getSolver() {
        return solver;
    }

    public BoolVar getInPlan() {
        return in_plan;
    }

    public ArithExpr getCost() {
        return cost;
    }

    boolean addPrecondition(Flaw f) {
        preconditions.add(f);
        solver.fireNewCausalLink(f, this);
        // if this choice is in plan, its preconditions must be in plan as well..
        return solver.network.add(solver.network.imply(in_plan, f.in_plan));
    }

    public Collection<Flaw> getPreconditions() {
        return Collections.unmodifiableCollection(preconditions);
    }

    public Flaw getEffect() {
        return effect;
    }

    protected abstract boolean apply();

    public JComponent getDetails() {
        return new JPanel();
    }

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + preconditions.stream().mapToDouble(pre -> pre.estimated_cost).max().orElse(0) + " + " + solver.network.evaluate(cost);
    }
}
