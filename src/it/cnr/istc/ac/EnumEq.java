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
public class EnumEq<T> implements BoolExpr {

    final EnumVar<T> left;
    final EnumVar<T> right;

    public EnumEq(EnumVar<T> left, EnumVar<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String id() {
        return left.id() + " == " + right.id();
    }

    @Override
    public boolean isConst() {
        return left.isConst() && right.isConst();
    }

    @Override
    public LBool root() {
        EnumDomain<T> left_d = left.root;
        EnumDomain<T> right_d = right.root;
        if (!left_d.isIntersecting(right_d)) {
            return LBool.L_FALSE;
        } else if (left_d.isSingleton() && right_d.isSingleton()) {
            return LBool.L_TRUE;
        } else {
            return LBool.L_UNKNOWN;
        }
    }

    @Override
    public LBool evaluate() {
        EnumDomain<T> left_d = left.domain;
        EnumDomain<T> right_d = right.domain;
        if (!left_d.isIntersecting(right_d)) {
            return LBool.L_FALSE;
        } else if (left_d.isSingleton() && right_d.isSingleton()) {
            return LBool.L_TRUE;
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
            BoolVar eq;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                eq = new BoolVar(n, "b" + n.n_bool_vars++, evaluate());
            } else {
                eq = new BoolVar(n, "b" + n.n_bool_vars++) {
                    @Override
                    protected void reevaluate() {
                        intersect(EnumEq.this.evaluate(), null);
                    }
                };
                eq.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, eq);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    return new Var<?>[]{left, right, eq};
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (eq.domain) {
                        case L_TRUE:
                            // The constraint must be satisfied..
                            if (!left.intersect(new EnumDomain<>(right.domain.allowed_vals), this)) {
                                return false;
                            }
                            if (!right.intersect(new EnumDomain<>(left.domain.allowed_vals), this)) {
                                return false;
                            }
                            return true;
                        case L_FALSE:
                            // The constraint must be not satisfied..
                            if (right.isSingleton() && !left.complement(right.domain.allowed_vals.iterator().next(), this)) {
                                return false;
                            }
                            if (left.isSingleton() && !right.complement(left.domain.allowed_vals.iterator().next(), this)) {
                                return false;
                            }
                            return true;
                        case L_UNKNOWN:
                            return eq.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(eq.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return eq.name;
                }
            });
            return eq;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
