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
package it.cnr.istc.oratio.core;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.EnumDomain;
import it.cnr.istc.ac.Expr;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnumItem extends Item implements IEnumItem {

    final Expr<EnumDomain<IItem>> expr;
    final Map<IItem, BoolExpr> eqs = new IdentityHashMap<>();

    EnumItem(Core c, Type t, Expr<EnumDomain<IItem>> xp) {
        super(c, c, t);
        this.expr = xp;
        for (IItem v : xp.root().getAllowedValues()) {
            eqs.put(v, core.eq(expr, v));
        }
    }

    @Override
    public Expr<EnumDomain<IItem>> getEnumVar() {
        return expr;
    }

    @Override
    public BoolExpr allows(IItem val) {
        return eqs.get(val);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IItem> T get(String name) {
        if (!type.fields.containsKey(name)) {
            return env.get(name);
        } else if (!items.containsKey(name)) {
            EnumDomain<IItem> e_dom = expr.root();
            if (e_dom.isSingleton()) {
                return (T) e_dom.getAllowedValues().iterator().next().get(name);
            } else {
                List<IItem> c_vals = new ArrayList<>();
                List<IItem> f_vals = new ArrayList<>();
                for (IItem val : e_dom.getAllowedValues()) {
                    c_vals.add(val);
                    f_vals.add(val.get(name));
                    if (val.get(name) instanceof IEnumItem) {
                        throw new AssertionError("invalid use: enum of enums..");
                    }
                }
                IEnumItem ei = core.newEnumItem(type.getField(name).type, f_vals.toArray(new IItem[f_vals.size()]));
                for (int i = 0; i < c_vals.size(); i++) {
                    boolean add = core.add(core.eq(allows(c_vals.get(i)), ei.allows(f_vals.get(i))));
                    assert add;
                }

                items.put(name, ei);
            }
        }
        return (T) items.get(name);
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.newBool(true);
        } else if (eqs.containsKey(item)) {
            return eqs.get(item);
        } else if (item instanceof IEnumItem) {
            return core.eq(expr, ((IEnumItem) item).getEnumVar());
        } else {
            return core.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (eqs.containsKey(item)) {
            return true;
        } else if (item instanceof IEnumItem) {
            return expr.evaluate().isIntersecting(((IEnumItem) item).getEnumVar().evaluate());
        } else {
            return false;
        }
    }
}
