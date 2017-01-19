/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
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
package it.cnr.istc.oratio.solver2;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IItem;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnumFlaw extends Flaw {

    private final IEnumItem enum_item;

    EnumFlaw(Solver s, IEnumItem ei) {
        super(s);
        this.enum_item = ei;
    }

    @Override
    protected void computeResolvers(Collection<Resolver> rs) {
        Set<? extends IItem> vals = enum_item.getEnumVar().evaluate().getAllowedValues();
        for (IItem v : vals) {
            rs.add(new ChooseValue(solver, solver.network.newReal(1.0 / vals.size()), this, enum_item.allows(v)));
        }
    }

    @Override
    public String toSimpleString() {
        return "enum";
    }

    private static class ChooseValue extends Resolver {

        private final BoolExpr eq_v;

        ChooseValue(Solver s, ArithExpr c, Flaw e, BoolExpr eq_v) {
            super(s, c, e);
            this.eq_v = eq_v;
        }

        @Override
        protected boolean apply() {
            return solver.network.add(solver.network.imply(in_plan, eq_v));
        }

        @Override
        public String toSimpleString() {
            return "val";
        }
    }
}
