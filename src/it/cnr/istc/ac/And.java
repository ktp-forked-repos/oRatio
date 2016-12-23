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
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class And implements BoolExpr {

    final Map<BoolVar, Boolean> vars;

    public And() {
        this.vars = new IdentityHashMap<>();
    }

    public And(BoolVar... vars) {
        this.vars = new IdentityHashMap<>(vars.length);
        for (BoolVar var : vars) {
            assert !this.vars.containsKey(var) || this.vars.get(var);
            this.vars.put(var, true);
        }
    }

    public And(And and) {
        this.vars = new IdentityHashMap<>(and.vars);
    }

    public void add(BoolVar var, Boolean polarity) {
        vars.put(var, polarity);
    }

    @Override
    public String id() {
        String and = new String();
        BoolVar[] c_vs = vars.keySet().toArray(new BoolVar[vars.size()]);
        Arrays.sort(c_vs, (BoolVar v0, BoolVar v1) -> v0.name.compareTo(v1.name));
        for (int i = 0; i < c_vs.length; i++) {
            BoolVar v = c_vs[i];
            if (vars.get(v)) {
                if (i == 0) {
                    and += v.name;
                } else {
                    and += " & " + v.name;
                }
            } else if (i == 0) {
                and += "!" + v.name;
            } else {
                and += " & !" + v.name;
            }
        }
        return and;
    }

    @Override
    public LBool evaluate() {
        LBool[] vals = vars.entrySet().stream().map(entry -> entry.getValue() ? entry.getKey().domain : entry.getKey().domain.not()).toArray(LBool[]::new);
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
                and = new BoolVar(n, "b" + n.n_bool_vars++);
                and.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, and);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    ArrayList<Var<?>> args = new ArrayList<>(vars.size() + 1);
                    args.addAll(vars.keySet());
                    args.add(and);
                    return args.toArray(new Var<?>[args.size()]);
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (and.domain) {
                        case L_TRUE:
                            // The constraint must be satisfied..
                            for (Map.Entry<BoolVar, Boolean> entry : vars.entrySet()) {
                                if (entry.getValue()) {
                                    if (!entry.getKey().intersect(LBool.L_TRUE, this)) {
                                        return false;
                                    }
                                } else {
                                    if (!entry.getKey().intersect(LBool.L_FALSE, this)) {
                                        return false;
                                    }
                                }
                            }
                            return true;
                        case L_FALSE:
                            // The constraint must be not satisfied..
                            Collection<BoolVar> false_or_free_vars = new ArrayList<>(vars.size());
                            for (Map.Entry<BoolVar, Boolean> entry : vars.entrySet()) {
                                if (entry.getValue()) {
                                    if (entry.getKey().domain != LBool.L_TRUE) {
                                        false_or_free_vars.add(entry.getKey());
                                    }
                                } else {
                                    if (entry.getKey().domain != LBool.L_FALSE) {
                                        false_or_free_vars.add(entry.getKey());
                                    }
                                }
                            }
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
