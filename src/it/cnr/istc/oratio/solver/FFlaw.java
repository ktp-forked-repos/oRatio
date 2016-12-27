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
package it.cnr.istc.oratio.solver;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class FFlaw extends Flaw {

    private final Atom atom;

    FFlaw(Solver s, Resolver c, Atom a) {
        super(s, c);
        this.atom = a;
    }

    @Override
    protected boolean computeResolvers(Collection<Resolver> rs) {
        AddFact af = new AddFact(solver, solver.network.newReal(0), this);
        af.fireNewResolver();
        rs.add(af);
        return true;
    }

    @Override
    public String toSimpleString() {
        return "fact " + atom.type.name;
    }

    private static class AddFact extends Resolver {

        AddFact(Solver s, ArithExpr c, Flaw e) {
            super(s, c, e);
        }

        @Override
        protected boolean apply() {
            return solver.network.add(solver.network.imply(in_plan, solver.network.eq(((FFlaw) effect).atom.state, AtomState.Active))) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "add fact";
        }
    }
}
