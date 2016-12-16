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
public class ArithVar extends Var<Interval> {

    double val;

    public ArithVar(Network network, String name) {
        this(network, name, new Interval());
    }

    public ArithVar(Network network, String name, Interval domain) {
        super(network, name, domain);
        if (domain.isSingleton()) {
            val = domain.lb;
        }
    }

    boolean intersect(Interval interval, Propagator propagator) {
        if (interval.contains(domain)) {
            return true;
        } else if (!domain.intersects(interval)) {
            return false;
        } else {
            Interval old_domain = new Interval(domain);
            domain.lb = Math.max(domain.lb, interval.lb);
            domain.ub = Math.min(domain.ub, interval.ub);
            network.enqueue(this, old_domain, propagator);
            return true;
        }
    }
}
