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
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ExctOne implements BoolExpr {

    final Map<BoolVar, Boolean> vars;

    public ExctOne() {
        this.vars = new IdentityHashMap<>();
    }

    public ExctOne(BoolVar... vars) {
        this.vars = new IdentityHashMap<>(vars.length);
        for (BoolVar var : vars) {
            assert !this.vars.containsKey(var) || this.vars.get(var);
            this.vars.put(var, true);
        }
    }

    public ExctOne(And and) {
        this.vars = new IdentityHashMap<>(and.vars);
    }

    public void add(BoolVar var, Boolean polarity) {
        vars.put(var, polarity);
    }

    @Override
    public String id() {
        String xct_o = new String();
        BoolVar[] c_vs = vars.keySet().toArray(new BoolVar[vars.size()]);
        Arrays.sort(c_vs, (BoolVar v0, BoolVar v1) -> v0.name.compareTo(v1.name));
        for (int i = 0; i < c_vs.length; i++) {
            BoolVar v = c_vs[i];
            if (vars.get(v)) {
                if (i == 0) {
                    xct_o += v.name;
                } else {
                    xct_o += " ^ " + v.name;
                }
            } else if (i == 0) {
                xct_o += "!" + v.name;
            } else {
                xct_o += " ^ !" + v.name;
            }
        }
        return xct_o;
    }

    @Override
    public boolean isConst() {
        return vars.keySet().stream().allMatch(var -> var.isConst());
    }

    @Override
    public LBool evaluate() {
        LBool[] vals = vars.entrySet().stream().map(entry -> entry.getValue() ? entry.getKey().domain : entry.getKey().domain.not()).toArray(LBool[]::new);
        int n_trues = 0;
        int n_unknown = 0;
        for (LBool val : vals) {
            switch (val) {
                case L_TRUE:
                    n_trues++;
                    break;
                case L_FALSE:
                    break;
                case L_UNKNOWN:
                    n_unknown++;
                    break;
                default:
                    throw new AssertionError(val.name());
            }
        }
        if (n_unknown == 0) {
            return n_trues == 1 ? LBool.L_TRUE : LBool.L_FALSE;
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
            BoolVar xct_o;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                xct_o = new BoolVar(n, "b" + n.n_bool_vars++, evaluate());
            } else {
                xct_o = new BoolVar(n, "b" + n.n_bool_vars++);
                xct_o.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, xct_o);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    ArrayList<Var<?>> args = new ArrayList<>(vars.size() + 1);
                    args.addAll(vars.keySet());
                    args.add(xct_o);
                    return args.toArray(new Var<?>[args.size()]);
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (xct_o.domain) {
                        case L_TRUE: {
                            // The constraint must be satisfied..
                            Map<BoolVar, LBool> vals = vars.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() ? entry.getKey().domain : entry.getKey().domain.not()));
                            int n_trues = 0;
                            int n_unknown = 0;
                            BoolVar the_true = null;
                            BoolVar the_unknown = null;
                            for (Map.Entry<BoolVar, LBool> entry : vals.entrySet()) {
                                switch (entry.getValue()) {
                                    case L_TRUE:
                                        n_trues++;
                                        the_true = entry.getKey();
                                        break;
                                    case L_FALSE:
                                        break;
                                    case L_UNKNOWN:
                                        n_unknown++;
                                        the_unknown = entry.getKey();
                                        break;
                                    default:
                                        throw new AssertionError(entry.getValue().name());
                                }
                            }
                            if (n_trues == 1 && n_unknown == vals.size() - 1) {
                                for (Map.Entry<BoolVar, Boolean> entry : vars.entrySet()) {
                                    if (entry.getKey() != the_true) {
                                        if (entry.getValue()) {
                                            if (!entry.getKey().intersect(LBool.L_FALSE, this)) {
                                                return false;
                                            }
                                        } else {
                                            if (!entry.getKey().intersect(LBool.L_TRUE, this)) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                                return true;
                            } else {
                                switch (n_unknown) {
                                    case 0:
                                        return n_trues == 1;
                                    case 1:
                                        switch (n_trues) {
                                            case 0:
                                                return the_unknown.intersect(LBool.L_TRUE, this);
                                            case 1:
                                                return the_unknown.intersect(LBool.L_FALSE, this);
                                            default:
                                                return false;
                                        }
                                    default:
                                        return true;
                                }
                            }
                        }
                        case L_FALSE: {
                            // The constraint must be not satisfied..
                            Map<BoolVar, LBool> vals = vars.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() ? entry.getKey().domain : entry.getKey().domain.not()));
                            int n_trues = 0;
                            int n_unknown = 0;
                            BoolVar unknown = null;
                            for (Map.Entry<BoolVar, LBool> entry : vals.entrySet()) {
                                switch (entry.getValue()) {
                                    case L_TRUE:
                                        n_trues++;
                                        break;
                                    case L_FALSE:
                                        break;
                                    case L_UNKNOWN:
                                        n_unknown++;
                                        unknown = entry.getKey();
                                        break;
                                    default:
                                        throw new AssertionError(entry.getValue().name());
                                }
                            }
                            switch (n_unknown) {
                                case 0:
                                    return n_trues == 1;
                                case 1:
                                    switch (n_trues) {
                                        case 0:
                                            return unknown.intersect(LBool.L_TRUE, this);
                                        case 1:
                                            return unknown.intersect(LBool.L_FALSE, this);
                                        default:
                                            return false;
                                    }
                                default:
                                    return true;
                            }
                        }
                        case L_UNKNOWN:
                            return xct_o.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(xct_o.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return xct_o.name;
                }
            });
            return xct_o;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
