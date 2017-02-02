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
package it.cnr.istc.iloc;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.core.Conjunction;
import it.cnr.istc.core.Disjunction;
import it.cnr.istc.core.IEnv;
import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DisjunctionFlaw extends Flaw {

    private final IEnv env;
    private final Disjunction disjunction;

    DisjunctionFlaw(Solver s, IEnv env, Disjunction d) {
        super(s, true);
        this.env = env;
        this.disjunction = d;
    }

    @Override
    protected void computeResolvers(Collection<Resolver> rs) {
        for (Conjunction conjunction : disjunction.getConjunctions()) {
            rs.add(new ChooseConjunction(solver, conjunction.getCost(), this, env, conjunction));
        }
    }

    private static class ChooseConjunction extends Resolver {

        private final IEnv env;
        private final Conjunction conjunction;

        ChooseConjunction(Solver s, ArithExpr c, Flaw e, IEnv env, Conjunction conjunction) {
            super(s, c, e);
            this.env = env;
            this.conjunction = conjunction;
        }

        @Override
        protected boolean apply() {
            return conjunction.apply(env);
        }
    }
}
