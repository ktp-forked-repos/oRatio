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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class EnumDomain<T> implements Domain {

    final Set<T> allowed_vals;

    public EnumDomain(T val) {
        this.allowed_vals = new HashSet<>(Arrays.asList(val));
    }

    public EnumDomain(Set<T> allowed_vals) {
        this.allowed_vals = allowed_vals;
    }

    @SafeVarargs
    public EnumDomain(T... allowed_vals) {
        this.allowed_vals = new HashSet<>(Arrays.asList(allowed_vals));
    }

    public Set<T> getAllowedValues() {
        return Collections.unmodifiableSet(allowed_vals);
    }

    @Override
    public boolean isSingleton() {
        return allowed_vals.size() == 1;
    }

    public boolean contains(T val) {
        return allowed_vals.contains(val);
    }

    public boolean isIntersecting(EnumDomain<T> dom) {
        for (T val : allowed_vals) {
            if (dom.allowed_vals.contains(val)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (isSingleton()) {
            return allowed_vals.iterator().next().toString();
        } else {
            return "{" + allowed_vals.stream().map(val -> val.toString()).collect(Collectors.joining(", ")) + "}";
        }
    }
}
