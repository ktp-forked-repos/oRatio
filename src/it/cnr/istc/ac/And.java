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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class And implements BoolExpr {

    final BoolVar[] vars;

    public And(BoolVar... vars) {
        this.vars = vars;
        Arrays.sort(vars, (BoolVar v0, BoolVar v1) -> v0.name.compareTo(v1.name));
    }

    @Override
    public String id() {
        String and = new String();
        for (int i = 0; i < vars.length; i++) {
            if (i == 0) {
                and += vars[i].name;
            } else {
                and += " & " + vars[i].name;
            }
        }
        return and;
    }

    @Override
    public boolean isConst() {
        return Stream.of(vars).allMatch(var -> var.isConst());
    }

    @Override
    public LBool evaluate() {
        LBool[] vals = Stream.of(vars).map(var -> var.domain).toArray(LBool[]::new);
        if (Stream.of(vals).allMatch(val -> val == LBool.L_TRUE)) {
            return LBool.L_TRUE;
        } else if (Stream.of(vals).anyMatch(val -> val == LBool.L_FALSE)) {
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
            BoolVar and;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                and = new BoolVar(n, "b" + n.n_bool_vars++, evaluate());
            } else {
                and = new BoolVar(n, "b" + n.n_bool_vars++) {
                    @Override
                    public void reevaluate() {
                        intersect(And.this.evaluate(), null);
                    }
                };
                and.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, and);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    ArrayList<Var<?>> args = new ArrayList<>(vars.length + 1);
                    args.addAll(Arrays.asList(vars));
                    args.add(and);
                    return args.toArray(new Var<?>[args.size()]);
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (and.domain) {
                        case L_TRUE:
                            // The constraint must be satisfied..
                            for (BoolVar var : vars) {
                                if (!var.intersect(LBool.L_TRUE, this)) {
                                    return false;
                                }
                            }
                            return true;
                        case L_FALSE:
                            // The constraint must be not satisfied..
                            Collection<BoolVar> false_or_free_vars = Stream.of(vars).filter(var -> var.domain != LBool.L_TRUE).collect(Collectors.toList());
                            if (false_or_free_vars.isEmpty()) {
                                return false;
                            } else if (false_or_free_vars.size() == 1 && !false_or_free_vars.iterator().next().intersect(LBool.L_FALSE, this)) {
                                return false;
                            }
                            return true;
                        case L_UNKNOWN:
                            return and.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(and.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return and.name;
                }
            });
            return and;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
