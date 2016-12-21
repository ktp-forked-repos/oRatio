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

import java.util.LinkedList;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Predicate extends Type {

    public Predicate(Core c, IScope s, String n, Field... pars) {
        super(c, s, n);
        if (scope instanceof Type) {
            this.fields.put(THIS, new Field((Type) scope, THIS, true));
        }
        for (Field par : pars) {
            this.fields.put(par.name, par);
        }
    }

    @Override
    public Atom newInstance(IEnv env) {
        Atom atom = new Atom(core, env, this);

        LinkedList<Type> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            c_type.instances.add(atom);
            queue.addAll(c_type.superclasses);
        }

        return atom;
    }

    public abstract boolean apply(Atom atom);
}
