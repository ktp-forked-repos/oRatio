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
public class Not implements BoolExpr {

    final BoolVar var;

    public Not(BoolVar expr) {
        this.var = expr;
    }

    @Override
    public String id() {
        return "!" + var.name;
    }

    @Override
    public LBool evaluate() {
        return var.domain.not();
    }

    @Override
    public Var<LBool> to_var(Network n) {
        String id = id();
        if (n.bool_vars.containsKey(id)) {
            // we can recycle an existing var..
            return n.bool_vars.get(id);
        } else if (n.bool_vars.containsKey(var.name.substring(1))) {
            // we have a double negation..
            return n.bool_vars.get(var.name.substring(1));
        } else {
            // we need to create a new variable..
            BoolVar not;
            if (n.rootLevel()) {
                not = new BoolVar(n, id, evaluate());
            } else {
                not = new BoolVar(n, id);
                not.intersect(evaluate(), null);
            }
            n.bool_vars.put(id, not);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    return new Var<?>[]{var, not};
                }

                @Override
                public boolean propagate(Var<?> v) {
                    switch (not.domain) {
                        case L_TRUE:
                            return var.intersect(LBool.L_FALSE, this);
                        case L_FALSE:
                            return var.intersect(LBool.L_TRUE, this);
                        case L_UNKNOWN:
                            return not.intersect(evaluate(), this);
                        default:
                            throw new AssertionError(not.domain.name());
                    }
                }

                @Override
                public String toString() {
                    return not.name;
                }
            });
            return not;
        }
    }

    @Override
    public String toString() {
        return id();
    }
}
