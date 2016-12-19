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

import it.cnr.istc.ac.BoolVar;
import it.cnr.istc.ac.EnumVar;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class BoolEnum extends Item implements IBoolItem, IEnumItem<IBoolItem> {

    final BoolVar bool_var;
    final EnumVar<IBoolItem> enum_var;

    BoolEnum(Core c, Type t, BoolVar bv, EnumVar<IBoolItem> ev) {
        super(c, c, t);
        this.bool_var = bv;
        this.enum_var = ev;
    }

    @Override
    public BoolVar getBoolVar() {
        return bool_var;
    }

    @Override
    public EnumVar<IBoolItem> getEnumVar() {
        return enum_var;
    }
}
