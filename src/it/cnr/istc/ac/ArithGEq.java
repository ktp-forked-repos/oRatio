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
public class ArithGEq implements BoolExpr {

    final ArithVar left;
    final double right;

    public ArithGEq(ArithVar left, double right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String id() {
        return left.name + " >= " + right;
    }

    @Override
    public boolean isConst() {
        return left.isConst();
    }

    @Override
    public LBool root() {
        if (left.root.geq(right)) {
            return LBool.L_TRUE;
        } else if (left.root.lt(right)) {
            return LBool.L_FALSE;
        } else {
            return LBool.L_UNKNOWN;
        }
    }

    @Override
    public LBool evaluate() {
        if (left.domain.geq(right)) {
            return LBool.L_TRUE;
        } else if (left.domain.lt(right)) {
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
            BoolVar geq;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                geq = new BoolVar(n, "b" + n.n_bool_vars++, evaluate());
            } else {
                geq = new BoolVar(n, "b" + n.n_bool_vars++) {
                    @Override
                    protected void reevaluate() {
                        intersect(ArithGEq.this.evaluate(), null);
                    }
                };
                geq.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, geq);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    return new Var<?>[]{left, geq};
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (geq.domain) {
                        case L_TRUE:
                            // we enforce the constraint..
                            return n.assertLower(left, right, geq, this);
                        case L_FALSE:
                            if (!left.domain.lt(right)) {
                                throw new UnsupportedOperationException("strict inequalities are not supported yet..");
                            }
                            return true;
                        case L_UNKNOWN:
                            return geq.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(geq.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return geq.name;
                }
            });
            return geq;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
