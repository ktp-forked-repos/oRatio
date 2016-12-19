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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class BaseScope implements IScope {

    public static final String THIS = "this";
    public static final String RETURN = "return";
    public final Core core;
    public final IScope scope;
    final Map<String, Field> fields = new HashMap<>();

    public BaseScope(Core c, IScope s) {
        this.core = c;
        this.scope = s;
    }

    @Override
    public Core getCore() {
        return core;
    }

    @Override
    public IScope getScope() {
        return scope;
    }

    @Override
    public Field getField(String name) {
        Field field = fields.get(name);
        if (field != null) {
            return field;
        }

        // if not here, check any enclosing environment
        if (scope != null) {
            field = scope.getField(name);
            if (field != null) {
                return field;
            }
        }

        // not found
        return null;
    }
}
