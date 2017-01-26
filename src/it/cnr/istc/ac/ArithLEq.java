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
package it.cnr.istc.ac;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ArithLEq implements BoolExpr {

    final ArithVar left;
    final double right;

    public ArithLEq(ArithVar left, double right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String id() {
        return left.name + " <= " + right;
    }

    @Override
    public boolean isConst() {
        return left.isConst();
    }

    @Override
    public LBool evaluate() {
        if (left.domain.leq(right)) {
            return LBool.L_TRUE;
        } else if (left.domain.gt(right)) {
            return LBool.L_FALSE;
        } else {
            return LBool.L_UNKNOWN;
        }
    }

    @Override
    public Var<LBool> to_var(Network n) {
        String id = id();
        if (n.bool_vars.containsKey(id)) {
            // we can recycle an existing var..
            return n.bool_vars.get(id);
        } else {
            // we need to create a new variable..
            BoolVar leq;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                leq = new BoolVar(n, "b" + n.n_bool_vars++, evaluate());
            } else {
                leq = new BoolVar(n, "b" + n.n_bool_vars++) {
                    @Override
                    protected void reevaluate() {
                        intersect(ArithLEq.this.evaluate(), null);
                    }
                };
                leq.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, leq);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    return new Var<?>[]{left, leq};
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (leq.domain) {
                        case L_TRUE:
                            // we enforce the constraint..
                            return n.assertUpper(left, right, leq, this);
                        case L_FALSE:
                            throw new UnsupportedOperationException("strict inequalities are not supported yet..");
                        case L_UNKNOWN:
                            return leq.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(leq.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return leq.name;
                }
            });
            return leq;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
