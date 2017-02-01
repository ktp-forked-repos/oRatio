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
    private boolean expanded = false;

    public Flaw(Solver s, boolean disjunctive) {
        assert s != null;

        this.solver = s;
        this.in_plan = s.newBool();
        this.disjunctive = disjunctive;
        this.causes = new ArrayList<>(s.resolvers);

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

        if (causes.isEmpty()) {
            boolean add = solver.add(in_plan);
            assert add;
        } else {
            for (Resolver cause : causes) {
                boolean add_precondition = cause.addPrecondition(this);
                assert add_precondition : "this flaw instance should not have been created..";
            }
        }
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

    public Collection<Resolver> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    public Collection<Resolver> getCauses() {
        return Collections.unmodifiableCollection(causes);
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

    protected void fireFlawUpdate() {
        solver.fireFlawUpdate(this);
    }

    public JComponent getDetails() {
        return new JPanel();
    }

    public abstract String toSimpleString();

    @Override
    public String toString() {
        return toSimpleString() + " " + in_plan.evaluate() + " " + solver.getCost(this);
    }
}
