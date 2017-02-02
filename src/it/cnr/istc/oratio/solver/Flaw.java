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
import java.util.Collection;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Flaw {

    protected final Solver solver;
    protected final BoolVar in_plan;
    private final boolean disjunctive;
    private final Collection<Resolver> resolvers = new ArrayList<>();
    private final Collection<Resolver> causes;
    final Collection<Resolver> supports;
    private boolean expanded = false;

    public Flaw(Solver s, boolean disjunctive) {
        assert s != null;

        this.solver = s;
        this.causes = new ArrayList<>(s.resolvers);
        for (Resolver cause : causes) {
            cause.preconditions.add(this);
        }
        this.supports = new ArrayList<>(s.resolvers);

        switch (causes.size()) {
            case 0:
                this.in_plan = (BoolVar) s.newBool(true).to_var(s);
                break;
            case 1:
                this.in_plan = causes.iterator().next().in_plan;
                break;
            default:
                this.in_plan = (BoolVar) s.and(causes.stream().map(resolver -> resolver.in_plan).toArray(BoolVar[]::new)).to_var(s);
                break;
        }
        assert in_plan.evaluate() != LBool.L_FALSE;
        this.disjunctive = disjunctive;

        this.solver.fireNewFlaw(this);

        this.solver.store(new Propagator() {
            @Override
            public Var<?>[] getArgs() {
                return new Var<?>[]{in_plan};
            }

            @Override
            public boolean propagate(Var<?> v) {
                if (in_plan.evaluate() == LBool.L_FALSE) {
                    solver.setCost(Flaw.this, Double.POSITIVE_INFINITY);
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

    public boolean isDisjunctive() {
        return disjunctive;
    }

    /**
     * Returns the collection of resolvers that resolve this flaw. This
     * collection is computed, depending on the flaw, by expanding the flaw
     * through the {@link #expand()} method.
     *
     * @return the collection of resolvers that resolve this flaw.
     */
    public Collection<Resolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    /**
     * Returns the collection of resolvers that caused this flaw.
     *
     * @return the collection of resolvers that caused this flaw.
     */
    public Collection<Resolver> getCauses() {
        return Collections.unmodifiableCollection(causes);
    }

    /**
     * Returns the collection of resolvers supported by this flaw.
     *
     * @return the collection of resolvers supported by this flaw.
     */
    public Collection<Resolver> getSupports() {
        return Collections.unmodifiableCollection(supports);
    }

    public boolean isExpanded() {
        return expanded;
    }

    boolean expand() {
        assert !expanded;
        assert resolvers.isEmpty();

        if (!computeResolvers(resolvers)) {
            return false;
        }
        expanded = true;

        BoolExpr expr;
        switch (resolvers.size()) {
            case 0:
                // there is no way for solving this flaw..
                expr = solver.not(in_plan);
                break;
            case 1:
                // there is a unique way for solving this flaw: this is a trivial flaw..
                expr = solver.imply(in_plan, resolvers.iterator().next().in_plan);
                break;
            default:
                // we need to take a decision for solving this flaw..
                expr = solver.imply(in_plan, disjunctive ? solver.exct_one(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new)) : solver.or(resolvers.stream().map(res -> res.in_plan).toArray(BoolExpr[]::new)));
        }

        return solver.add(expr);
    }

    /**
     * Computes the resolvers for this flaw, by adding them to the {@code rs}
     * collection.
     *
     * @param rs an initially empty {@code Collection} of resolvers to be filled
     * by available resolvers.
     * @return {@code flase} if an inconsistency has been found.
     */
    protected abstract boolean computeResolvers(Collection<Resolver> rs);

    public JComponent getDetails() {
        return new JPanel();
    }

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + solver.getCost(this);
    }
}
