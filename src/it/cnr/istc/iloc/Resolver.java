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
import it.cnr.istc.ac.BoolVar;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Resolver {

    protected final Solver solver;
    protected final BoolVar in_plan;
    protected final ArithExpr cost;
    protected final Flaw effect;

    public Resolver(Solver s, ArithExpr c, Flaw e) {
        this.solver = s;
        this.in_plan = s.newBool();
        this.cost = c;
        this.effect = e;
    }

    protected abstract boolean apply();

    public abstract String toSimpleString();
}
