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
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Propagator;
import it.cnr.istc.ac.Var;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
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
        this.solver = s;
        this.cost = c;
        this.effect = e;
        this.in_plan = s.network.newBool();
        this.solver.fireNewResolver(this);
        this.solver.network.store(new Propagator() {
            @Override
            public Var<?>[] getArgs() {
                return new Var<?>[]{in_plan};
            }

            @Override
            public boolean propagate(Var<?> v) {
                if (in_plan.evaluate() == LBool.L_FALSE) {
                    Map<Resolver, Double> costs = e.getResolvers().stream().filter(r -> r.in_plan.evaluate() != LBool.L_FALSE).collect(Collectors.toMap(r -> r, r -> r.getPreconditions().stream().mapToDouble(pre -> solver.getCost(pre)).max().orElse(0) + solver.network.evaluate(r.cost)));
                    if (costs.isEmpty()) {
                        solver.setCost(e, Double.POSITIVE_INFINITY);
                    } else {
                        solver.setCost(e, costs.get(costs.keySet().stream().min((Resolver r0, Resolver r1) -> Double.compare(costs.get(r0), costs.get(r1))).get()));
                    }
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

    boolean addPrecondition(Flaw f) {
        preconditions.add(f);
        solver.fireNewCausalLink(f, this);
        // if this choice is in plan, its preconditions must be in plan as well..
        return solver.add(solver.network.imply(in_plan, f.in_plan));
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
        return toSimpleString() + " " + in_plan.evaluate() + " " + preconditions.stream().mapToDouble(pre -> solver.getCost(pre)).max().orElse(0) + " + " + solver.network.evaluate(cost);
    }
}
