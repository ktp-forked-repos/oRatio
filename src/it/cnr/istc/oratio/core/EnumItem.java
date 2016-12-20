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
import java.util.IdentityHashMap;
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
        for (IItem v : xp.evaluate().getAllowedValues()) {
            eqs.put(v, core.network.eq(expr, v));
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
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.network.newBool(true);
        } else if (eqs.containsKey(item)) {
            return eqs.get(item);
        } else if (item instanceof IEnumItem) {
            return core.network.eq(expr, ((IEnumItem) item).getEnumVar());
        } else {
            return core.network.newBool(false);
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
