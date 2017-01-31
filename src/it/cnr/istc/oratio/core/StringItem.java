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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StringItem extends Item implements IStringItem {

    String val;

    StringItem(Core c, Type t, String val) {
        super(c, c, t);
        this.val = val;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.newBool(true);
        } else if (item instanceof StringItem) {
            return core.newBool(val.equals(((StringItem) item).val));
        } else {
            return core.newBool(false);
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof StringItem) {
            return val.equals(((StringItem) item).val);
        } else {
            return false;
        }
    }
}
