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
 * @param <D> the domain type.
 */
public abstract class Var<D extends Domain> implements Expr<D> {

    public final Network network;
    public final String name;
    D domain;
    private boolean is_const;

    public Var(Network network, String name, D domain) {
        this.network = network;
        this.name = name;
        this.domain = domain;
        this.is_const = network.rootLevel() && domain.isSingleton();
    }

    @Override
    public String id() {
        return name;
    }

    @Override
    public boolean isConst() {
        if (is_const) {
            return true;
        } else {
            is_const = network.rootLevel() && isSingleton();
            return is_const;
        }
    }

    @Override
    public D evaluate() {
        return domain;
    }

    @Override
    public Var<D> to_var(Network n) {
        return this;
    }

    public boolean isSingleton() {
        return domain.isSingleton();
    }

    @SuppressWarnings("unchecked")
    void restore() {
        this.domain = (D) network.domains.get(this);
    }

    protected void reevaluate() {
    }

    @Override
    public String toString() {
        return name + " = " + domain.toString();
    }
}
