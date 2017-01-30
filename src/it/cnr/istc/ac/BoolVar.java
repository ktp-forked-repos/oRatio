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
public class BoolVar extends Var<LBool> implements BoolExpr {

    public BoolVar(Network network, String name) {
        this(network, name, LBool.L_UNKNOWN);
    }

    public BoolVar(Network network, String name, LBool domain) {
        super(network, name, domain, domain);
    }

    boolean intersect(LBool val, Propagator propagator) {
        if (val == LBool.L_UNKNOWN || domain == val) {
            return true;
        } else if (domain != LBool.L_UNKNOWN) {
            return false;
        } else {
            LBool old_domain = domain;
            domain = domain == LBool.L_UNKNOWN ? val : null;
            if (network.rootLevel()) {
                root = root == LBool.L_UNKNOWN ? val : null;
            }
            network.enqueue(this, old_domain, propagator);
            return true;
        }
    }
}
