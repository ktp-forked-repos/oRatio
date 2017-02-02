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
package it.cnr.istc.core;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Method extends BaseScope {

    public final String name;
    public final Type return_type;
    public final Field[] parameters;

    public Method(Core c, IScope s, String n, Type rt, Field... pars) {
        super(c, s);
        this.name = n;
        this.return_type = rt;
        this.parameters = pars;

        fields.put(THIS, new Field((Type) scope, THIS, true));
        if (rt != null) {
            fields.put(RETURN, new Field(rt, RETURN, true));
        }
        for (Field par : pars) {
            fields.put(par.name, par);
        }
    }

    public abstract boolean invoke(IEnv env, IItem... expressions);
}
