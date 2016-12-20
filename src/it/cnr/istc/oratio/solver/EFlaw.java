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
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IItem;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EFlaw extends Flaw {

    private final IEnumItem ei;

    EFlaw(Solver s, Resolver c, IEnumItem ei) {
        super(s, c);
        this.ei = ei;
    }

    @Override
    boolean computeResolvers(Collection<Resolver> rs) {
        Set<? extends IItem> vals = ei.getEnumVar().evaluate().getAllowedValues();
        for (IItem v : vals) {
            ei.allows(v);
            rs.add(new ChooseValue(solver, solver.network.newReal(1.0 / vals.size()), this, ei.allows(v)));
        }
        return true;
    }

    private static class ChooseValue extends Resolver {

        private final BoolExpr eq_v;

        ChooseValue(Solver s, ArithExpr c, Flaw e, BoolExpr eq_v) {
            super(s, c, e);
            this.eq_v = eq_v;
        }

        @Override
        boolean apply() {
            estimated_cost = 0;
            solver.network.add(solver.network.imply(in_plan, eq_v));
            return solver.network.propagate();
        }
    }
}
