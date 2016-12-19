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
import it.cnr.istc.ac.EnumVar;
import it.cnr.istc.ac.LBool;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BoolEnum extends Item implements IBoolItem, IEnumItem<IBoolItem> {

    final BoolExpr bool_var;
    final EnumVar<IBoolItem> enum_var;
    final Map<IItem, BoolExpr> eqs = new IdentityHashMap<>();

    BoolEnum(Core c, Type t, BoolExpr bv, EnumVar<IBoolItem> ev) {
        super(c, c, t);
        this.bool_var = bv;
        this.enum_var = ev;
        for (IBoolItem v : ev.evaluate().getAllowedValues()) {
            eqs.put(v, core.network.eq(bool_var, v.getBoolVar()));
        }
    }

    @Override
    public BoolExpr getBoolVar() {
        return bool_var;
    }

    @Override
    public EnumVar<IBoolItem> getEnumVar() {
        return enum_var;
    }

    @Override
    public BoolExpr allows(IBoolItem val) {
        return eqs.get(val);
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.network.newBool(true);
        } else if (item instanceof IBoolItem) {
            return core.network.eq(bool_var, ((IBoolItem) item).getBoolVar());
        } else {
            return core.network.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof IBoolItem) {
            LBool left_d = bool_var.evaluate();
            LBool right_d = ((IBoolItem) item).getBoolVar().evaluate();
            return !((left_d == LBool.L_TRUE && right_d == LBool.L_FALSE) || (left_d == LBool.L_FALSE && right_d == LBool.L_TRUE));
        } else {
            return false;
        }
    }
}
