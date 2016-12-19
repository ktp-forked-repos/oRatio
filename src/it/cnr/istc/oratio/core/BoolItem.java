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
import it.cnr.istc.ac.LBool;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BoolItem extends Item implements IBoolItem {

    final BoolExpr expr;

    BoolItem(Core c, Type t, BoolExpr xp) {
        super(c, c, t);
        this.expr = xp;
    }

    @Override
    public BoolExpr getBoolVar() {
        return expr;
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.network.newBool(true);
        } else if (item instanceof IBoolItem) {
            return core.network.eq(expr, ((IBoolItem) item).getBoolVar());
        } else {
            return core.network.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof IBoolItem) {
            LBool left_d = expr.evaluate();
            LBool right_d = ((IBoolItem) item).getBoolVar().evaluate();
            return !((left_d == LBool.L_TRUE && right_d == LBool.L_FALSE) || (left_d == LBool.L_FALSE && right_d == LBool.L_TRUE));
        } else {
            return false;
        }
    }
}
