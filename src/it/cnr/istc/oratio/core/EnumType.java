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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnumType extends Type {

    final Collection<EnumType> enums = new ArrayList<>();

    EnumType(Core c, IScope s, String n) {
        super(c, s, n, true);
    }

    Collection<IItem> getEnums() {
        Set<IItem> items = new HashSet<>(instances);
        for (EnumType et : enums) {
            items.addAll(et.getEnums());
        }
        return items;
    }

    @Override
    public IItem newInstance(IEnv env) {
        Collection<IItem> items = getEnums();
        return core.newEnum(this, items.toArray(new IItem[items.size()]));
    }

    void addEnum(IStringItem item) {
        instances.add(item);
    }
}
