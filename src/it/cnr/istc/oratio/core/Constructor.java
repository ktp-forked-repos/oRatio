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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Constructor extends BaseScope {

    public final Field[] parameters;

    public Constructor(Core c, IScope s, Field... parameters) {
        super(c, s);
        this.parameters = parameters;

        fields.put(THIS, new Field((Type) s, THIS, true));
        for (Field par : parameters) {
            fields.put(par.name, par);
        }
    }

    public IItem newInstance(IEnv env, IItem... expressions) {
        assert parameters.length == expressions.length;

        Type type = (Type) scope;
        IItem item = type.newInstance(env);

        invoke(item, expressions);

        return item;
    }

    public abstract boolean invoke(IItem item, IItem... expressions);
}
