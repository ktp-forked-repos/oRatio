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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class EnumVar<T> extends Var<EnumDomain<T>> {

    public EnumVar(Network network, String name, EnumDomain<T> domain) {
        super(network, name, new EnumDomain<T>(new HashSet<T>(domain.allowed_vals)), new EnumDomain<T>(new HashSet<T>(domain.allowed_vals)));
    }

    boolean complement(T val, Propagator propagator) {
        if (!domain.contains(val)) {
            return true;
        } else if (domain.allowed_vals.size() == 1) {
            return false;
        } else {
            EnumDomain<T> old_domain = new EnumDomain<>(new HashSet<>(domain.allowed_vals));
            domain.allowed_vals.remove(val);
            if (network.rootLevel()) {
                root.allowed_vals.remove(val);
            }
            network.enqueue(this, old_domain, propagator);
            return true;
        }
    }

    boolean intersect(EnumDomain<T> enum_domain, Propagator propagator) {
        Set<T> to_remove = new HashSet<>(domain.allowed_vals.size());
        for (T val : domain.allowed_vals) {
            if (!enum_domain.contains(val)) {
                to_remove.add(val);
            }
        }
        if (to_remove.isEmpty()) {
            return true;
        } else if (domain.allowed_vals.size() == to_remove.size()) {
            return false;
        } else {
            EnumDomain<T> old_domain = new EnumDomain<>(new HashSet<>(domain.allowed_vals));
            domain.allowed_vals.removeAll(to_remove);
            if (network.rootLevel()) {
                root.allowed_vals.removeAll(to_remove);
            }
            network.enqueue(this, old_domain, propagator);
            return true;
        }
    }

    boolean intersect(T val, Propagator propagator) {
        if (domain.contains(val) && domain.isSingleton()) {
            return true;
        } else {
            EnumDomain<T> old_domain = new EnumDomain<>(new HashSet<>(domain.allowed_vals));
            domain.allowed_vals.retainAll(Arrays.asList(val));
            if (network.rootLevel()) {
                root.allowed_vals.retainAll(Arrays.asList(val));
            }
            network.enqueue(this, old_domain, propagator);
            return true;
        }
    }
}
