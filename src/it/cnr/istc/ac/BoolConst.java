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
public class BoolConst implements BoolExpr {

    public final LBool val;

    public BoolConst(LBool val) {
        assert val != LBool.L_UNKNOWN;
        this.val = val;
    }

    @Override
    public String id() {
        return val == LBool.L_TRUE ? Boolean.toString(true) : Boolean.toString(false);
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public LBool evaluate() {
        return val;
    }

    @Override
    public Var<LBool> to_var(Network n) {
        return new BoolVar(n, id(), val);
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
