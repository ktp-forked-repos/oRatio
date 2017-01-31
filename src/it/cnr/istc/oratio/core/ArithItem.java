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
import it.cnr.istc.ac.Interval;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ArithItem extends Item implements IArithItem {

    final ArithExpr expr;

    ArithItem(Core c, Type t, ArithExpr xp) {
        super(c, c, t);
        this.expr = xp;
    }

    @Override
    public ArithExpr getArithVar() {
        return expr;
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.newBool(true);
        } else if (item instanceof IArithItem) {
            return core.eq(expr, ((IArithItem) item).getArithVar());
        } else {
            return core.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof IArithItem) {
            Interval left_d = expr.evaluate();
            Interval right_d = ((IArithItem) item).getArithVar().evaluate();
            return left_d.intersects(right_d);
        } else {
            return false;
        }
    }
}
