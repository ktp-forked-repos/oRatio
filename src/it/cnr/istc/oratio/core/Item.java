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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Item extends BaseEnv implements IItem {

    public final Type type;

    public Item(Core c, IEnv e, Type t) {
        super(c, e);
        this.type = t;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public BoolExpr eq(IItem item) {
        if (this == item) {
            return core.network.newBool(true);
        } else if (item instanceof IEnumItem) {
            return item.eq(this);
        } else {
            // We have two items of the same type
            // All the fields of the first item should be equal to all the fields of second item..

            Collection<BoolExpr> exprs = new ArrayList<>();
            LinkedList<Type> queue = new LinkedList<>();
            queue.add(type);
            while (!queue.isEmpty()) {
                Type c_type = queue.pollFirst();
                queue.addAll(c_type.superclasses);
                for (Field f : c_type.fields.values()) {
                    if (!f.synthetic) {
                        exprs.add(items.get(f.name).eq(item.get(f.name)));
                    }
                }
            }

            if (exprs.isEmpty()) {
                return core.network.newBool(true);
            } else if (exprs.size() == 1) {
                return exprs.iterator().next();
            } else {
                return core.network.and(exprs.toArray(new BoolExpr[exprs.size()]));
            }
        }
    }

    @Override
    public boolean equates(IItem item) {
        if (this == item) {
            return true;
        } else if (item instanceof IEnumItem) {
            return item.equates(this);
        } else {
            // We have two items of the same type
            // All the fields of the first item should equate to all the fields of second item..

            LinkedList<Type> queue = new LinkedList<>();
            queue.add(type);
            while (!queue.isEmpty()) {
                Type c_type = queue.pollFirst();
                queue.addAll(c_type.superclasses);
                for (Field f : c_type.fields.values()) {
                    if (!f.synthetic) {
                        if (!items.get(f.name).equates(item.get(f.name))) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
}
