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
import it.cnr.istc.ac.Propagator;
import it.cnr.istc.ac.Var;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Flaw implements Propagator {

    protected final Solver solver;
    protected final Resolver cause;
    protected final BoolVar in_plan;
    private final Collection<Resolver> resolvers = new ArrayList<>();
    protected double estimated_cost = Double.POSITIVE_INFINITY;
    private boolean expanded = false;

    public Flaw(Solver s, Resolver c) {
        this.solver = s;
        this.cause = c;
        this.in_plan = s.network.newBool();
    }

    boolean isExpanded() {
        return expanded;
    }

    boolean isSolved() {
        return estimated_cost < Double.POSITIVE_INFINITY;
    }

    public Collection<Resolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    boolean expand() {
        assert !expanded;

        boolean solved = computeResolvers(resolvers);
        expanded = true;

        if (resolvers.isEmpty()) {
            assert !solved;
            solver.network.add(solver.network.not(in_plan));
            if (!solver.network.propagate()) {
                return false;
            }
        } else if (resolvers.size() == 1) {
            solver.network.add(solver.network.imply(in_plan, resolvers.iterator().next().in_plan));
            if (!solver.network.propagate()) {
                return false;
            }
        } else {
            solver.network.add(solver.network.imply(in_plan, solver.network.exct_one(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new))));
            if (!solver.network.propagate()) {
                return false;
            }
        }
        return true;
    }

    abstract boolean computeResolvers(Collection<Resolver> rs);

    void updateCosts(Set<Flaw> visited) {
        if (!visited.contains(this)) {
            visited.add(this);
            double c_cost = Double.POSITIVE_INFINITY;
            for (Resolver r : resolvers) {
                double r_cost = solver.network.evaluate(r.cost);
                if (r.estimated_cost + r_cost < c_cost) {
                    c_cost = r.estimated_cost + r_cost;
                }
            }
            if (c_cost != estimated_cost) {
                estimated_cost = c_cost;
                if (cause != null) {
                    cause.updateCosts(new HashSet<>(visited));
                }
            }
        }
    }

    @Override
    public Var<?>[] getArgs() {
        return new Var<?>[]{in_plan};
    }

    @Override
    public boolean propagate(Var<?> v) {
        if (in_plan.evaluate() == LBool.L_FALSE) {
            estimated_cost = Double.POSITIVE_INFINITY;
            if (cause != null) {
                cause.updateCosts(new HashSet<>(Arrays.asList(this)));
            }
        }
        return true;
    }
}
