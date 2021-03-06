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

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolVar;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Propagator;
import it.cnr.istc.ac.Var;
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
    protected final Collection<Flaw> preconditions = new ArrayList<>();
    protected final Flaw effect;

    public Resolver(Solver s, ArithExpr c, Flaw e) {
        assert s != null;
        assert c != null;
        assert e != null;

        this.solver = s;
        this.cost = c;
        this.effect = e;
        this.in_plan = s.newBool();

        this.solver.fireNewResolver(this);

        this.solver.store(new Propagator() {
            @Override
            public Var<?>[] getArgs() {
                return new Var<?>[]{in_plan};
            }

            @Override
            public boolean propagate(Var<?> v) {
                if (in_plan.evaluate() == LBool.L_FALSE) {
                    // we update the cost of the effect..
                    solver.setCost(effect, effect.getResolvers().stream().mapToDouble(r -> solver.getCost(r)).min().orElse(Double.POSITIVE_INFINITY));
                }
                return true;
            }
        });
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

    /**
     * Adds a precondition to this resolver. If this resolver is in the
     * solution, its preconditions must be in the solution as well. Conversely,
     * if the precondition is not in the solution, this resolver must not be in
     * the solution as well.
     *
     * @param f the {@link Flaw} representing the precondition of this resolver.
     * @return {@code true} if the precondition can be successfully added to the
     * resolver.
     */
    boolean addPrecondition(Flaw f) {
        preconditions.add(f);
        f.supports.add(this);
        solver.fireNewCausalLink(f, this);
        return solver.add(solver.imply(in_plan, f.in_plan));
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
        return toSimpleString() + " " + in_plan.evaluate() + " " + preconditions.stream().mapToDouble(pre -> solver.getCost(pre)).max().orElse(0) + " + " + solver.evaluate(cost);
    }
}
