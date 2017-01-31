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

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.EnumVar;
import it.cnr.istc.ac.Interval;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ArithEnum extends Item implements IArithItem, IEnumItem {

    final ArithExpr arith_var;
    final EnumVar<IItem> enum_var;
    final Map<IItem, BoolExpr> eqs = new IdentityHashMap<>();

    ArithEnum(Core c, Type t, ArithExpr av, EnumVar<IItem> ev) {
        super(c, c, t);
        this.arith_var = av;
        this.enum_var = ev;
        for (IItem v : ev.root().getAllowedValues()) {
            eqs.put(v, core.eq(arith_var, ((IArithItem) v).getArithVar()));
        }
    }

    @Override
    public ArithExpr getArithVar() {
        return arith_var;
    }

    @Override
    public EnumVar<IItem> getEnumVar() {
        return enum_var;
    }

    @Override
    public BoolExpr allows(IItem val) {
        return eqs.get(val);
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.newBool(true);
        } else if (item instanceof IArithItem) {
            return core.eq(arith_var, ((IArithItem) item).getArithVar());
        } else {
            return core.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof IBoolItem) {
            Interval left_d = arith_var.evaluate();
            Interval right_d = ((IArithItem) item).getArithVar().evaluate();
            return left_d.intersects(right_d);
        } else {
            return false;
        }
    }
}
