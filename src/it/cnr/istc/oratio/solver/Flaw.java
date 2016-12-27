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

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isSolved() {
        return estimated_cost < Double.POSITIVE_INFINITY;
    }

    public double getEstimatedCost() {
        return estimated_cost;
    }

    public Collection<Resolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    public Resolver getCause() {
        return cause;
    }

    boolean expand() {
        assert !expanded;
        assert resolvers.isEmpty();

        boolean solved = computeResolvers(resolvers);
        expanded = true;

        switch (resolvers.size()) {
            case 0:
                // there is no way for solving this flaw..
                assert !solved;
                return solver.network.add(solver.network.not(in_plan)) && solver.network.propagate();
            case 1:
                // there is a unique way for solving this flaw: this is a trivial flaw..
                return solver.network.add(solver.network.imply(in_plan, resolvers.iterator().next().in_plan)) && solver.network.propagate();
            default:
                // we need to take a decision for solving this flaw..
                return solver.network.add(solver.network.imply(in_plan, solver.network.exct_one(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new)))) && solver.network.propagate();
        }
    }

    /**
     * Computes the resolvers for this flaw, by adding them to the {@code rs}
     * collection, and returns a boolean indicating if a solution has been found
     * for the current flaw.
     *
     * @param rs an initially empty {@code Collection} of resolvers to be filled
     * by available resolvers.
     * @return {@code true} if a solution has been found for the flaw.
     */
    protected abstract boolean computeResolvers(Collection<Resolver> rs);

    void updateCosts(Set<Flaw> visited) {
        if (!visited.contains(this)) {
            visited.add(this);
            double computed_cost = resolvers.stream().mapToDouble(r -> r.estimated_cost).min().orElse(Double.POSITIVE_INFINITY);
            if (computed_cost != estimated_cost) {
                if (!solver.rootLevel() && !solver.flaw_costs.containsKey(this)) {
                    solver.flaw_costs.put(this, estimated_cost);
                }
                estimated_cost = computed_cost;
                fireFlawUpdate();
                if (cause != null) {
                    cause.updateCosts(new HashSet<>(visited));
                }
            }
        }
    }

    protected void fireNewFlaw() {
        solver.newFlaw(this);
    }

    protected void fireFlawUpdate() {
        solver.fireFlawUpdate(this);
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

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + estimated_cost;
    }
}
