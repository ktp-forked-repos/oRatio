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

import it.cnr.istc.ac.EnumDomain;
import it.cnr.istc.ac.Expr;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnumItem extends Item implements IEnumItem<IItem> {

    final Expr<EnumDomain<IItem>> var;

    EnumItem(Core c, Type t, Expr<EnumDomain<IItem>> v) {
        super(c, c, t);
        this.var = v;
    }

    @Override
    public Expr<EnumDomain<IItem>> getEnumVar() {
        return var;
    }
}
