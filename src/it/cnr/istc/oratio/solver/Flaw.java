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
import it.cnr.istc.ac.Propagator;
import it.cnr.istc.ac.Var;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Flaw implements Propagator {

    protected final Solver solver;
    protected final BoolVar in_plan;
    private final boolean disjunctive;
    private final Collection<Resolver> resolvers = new ArrayList<>();
    private final Collection<Resolver> causes;
    protected double estimated_cost = Double.POSITIVE_INFINITY;
    private boolean expanded = false;
    boolean deferrable = false;

    public Flaw(Solver s, boolean disjunctive) {
        this.solver = s;
        this.in_plan = s.resolvers.size() == 1 ? s.resolvers.iterator().next().in_plan : (BoolVar) s.network.and(s.resolvers.stream().map(resolver -> resolver.in_plan).toArray(BoolVar[]::new)).to_var(s.network);
        this.causes = new ArrayList<>(s.resolvers);
        this.solver.fireNewFlaw(this);
        this.disjunctive = disjunctive;
    }

    public BoolVar getInPlan() {
        return in_plan;
    }

    public boolean isDisjunctive() {
        return disjunctive;
    }

    public Collection<Resolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    public Collection<Resolver> getCauses() {
        return Collections.unmodifiableCollection(causes);
    }

    public double getEstimatedCost() {
        return estimated_cost;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isDeferrable() {
        return deferrable;
    }

    boolean expand() {
        assert !expanded;
        assert resolvers.isEmpty();

        computeResolvers(resolvers);
        expanded = true;

        switch (resolvers.size()) {
            case 0:
                // there is no way for solving this flaw..
                return solver.network.add(solver.network.not(in_plan));
            case 1:
                // there is a unique way for solving this flaw: this is a trivial flaw..
                return solver.network.add(solver.network.imply(in_plan, resolvers.iterator().next().in_plan));
            default:
                // we need to take a decision for solving this flaw..
                return solver.network.add(solver.network.imply(in_plan, disjunctive ? solver.network.exct_one(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new)) : solver.network.or(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new))));
        }
    }

    void updateCosts(Set<Flaw> visited) {
        if (!visited.contains(this)) {
            visited.add(this);
            double computed_cost = resolvers.stream().mapToDouble(r -> r.estimated_cost).min().orElse(Double.POSITIVE_INFINITY);
            if (computed_cost != estimated_cost) {
                if (!solver.rootLevel() && !solver.flaw_costs.containsKey(this)) {
                    solver.flaw_costs.put(this, estimated_cost);
                }
                estimated_cost = computed_cost;
                solver.fireFlawUpdate(this);
                for (Resolver cause : causes) {
                    cause.updateCosts(new HashSet<>(visited));
                }
                updateRequiresExpansion(new HashSet<>());
            }
        }
    }

    void updateRequiresExpansion(Set<Flaw> visited) {
        if (!visited.contains(this)) {
            visited.add(this);
            boolean c_deferrable = estimated_cost < Double.POSITIVE_INFINITY || causes.stream().anyMatch(cause -> cause.effect.deferrable);
            if (deferrable != c_deferrable) {
                if (!solver.rootLevel() && !solver.deferrable_flaws.containsKey(this)) {
                    solver.deferrable_flaws.put(this, deferrable);
                }
                deferrable = c_deferrable;
                solver.fireFlawUpdate(this);
                for (Resolver resolver : resolvers) {
                    for (Flaw precondition : resolver.getPreconditions()) {
                        precondition.updateRequiresExpansion(new HashSet<>(visited));
                    }
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
            double computed_cost = Double.POSITIVE_INFINITY;
            if (computed_cost != estimated_cost) {
                if (!solver.rootLevel() && !solver.flaw_costs.containsKey(this)) {
                    solver.flaw_costs.put(this, estimated_cost);
                }
                estimated_cost = computed_cost;
                solver.fireFlawUpdate(this);
                for (Resolver cause : causes) {
                    cause.updateCosts(new HashSet<>(Arrays.asList(this)));
                }
            }
        }
        return true;
    }

    /**
     * Computes the resolvers for this flaw, by adding them to the {@code rs}
     * collection.
     *
     * @param rs an initially empty {@code Collection} of resolvers to be filled
     * by available resolvers.
     */
    protected abstract void computeResolvers(Collection<Resolver> rs);

    public JComponent getDetails() {
        return new JPanel();
    }

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + estimated_cost;
    }
}
