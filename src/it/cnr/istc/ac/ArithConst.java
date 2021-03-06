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
package it.cnr.istc.ac;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ArithConst implements ArithExpr {

    public final double val;

    public ArithConst(double val) {
        this.val = val;
    }

    @Override
    public String id() {
        return Double.toString(val);
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public Interval root() {
        return new Interval(val);
    }

    @Override
    public Interval evaluate() {
        return new Interval(val);
    }

    @Override
    public Var<Interval> to_var(Network n) {
        return new ArithVar(n, id(), new Interval(val));
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }
}
