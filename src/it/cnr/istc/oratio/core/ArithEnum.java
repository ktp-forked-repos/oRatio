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

import it.cnr.istc.ac.ArithVar;
import it.cnr.istc.ac.EnumVar;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ArithEnum extends Item implements IArithItem, IEnumItem<IArithItem> {

    final ArithVar arith_var;
    final EnumVar<IBoolItem> enum_var;

    ArithEnum(Core c, IEnv e, Type t, ArithVar av, EnumVar<IBoolItem> ev) {
        super(c, e, t);
        this.arith_var = av;
        this.enum_var = ev;
    }

    @Override
    public ArithVar getArithVar() {
        return arith_var;
    }

    @Override
    public EnumVar<IArithItem> getEnumVar() {
        return getEnumVar();
    }
}
