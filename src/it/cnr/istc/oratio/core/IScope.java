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
package it.cnr.istc.oratio.core;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IScope {

    public static final String THIS = "this";
    public static final String RETURN = "return";

    /**
     * Returns the core enclosing this scope.
     *
     * @return the core enclosing this scope.
     */
    public Core getCore();

    /**
     * Returns the immediately enclosing scope of this scope. If the enclosing
     * scope is a top level scope this method returns {@code null}.
     *
     * @return the immediately enclosing scope of this scope.
     */
    public IScope getScope();

    /**
     * Returns a {@code Field} object that reflects the specified public member
     * field of this {@code IScope} object. The {@code name} parameter is a
     * {@code String} specifying the simple name of the desired field.
     *
     * @param name the field name.
     * @return the {@code Field} object of this class specified by {@code name}.
     */
    public Field getField(String name);

    /**
     * Returns a {@code Method} object that reflects the specified public member
     * method of the class or interface represented by this {@code IScope}
     * object. The {@code name} parameter is a {@code String} specifying the
     * simple name of the desired method. The {@code parameterTypes} parameter
     * is an array of {@code Type} objects that identify the method's formal
     * parameter types, in declared order.
     *
     * @param name the name of the method.
     * @param parameterTypes the list of parameters.
     * @return the {@code Method} object that matches the specified {@code name}
     * and {@code parameterTypes}.
     */
    public default Method getMethod(String name, Type... parameterTypes) {
        return getScope().getMethod(name, parameterTypes);
    }

    /**
     * Returns a {@code Predicate} object that reflects the specified public
     * member predicate of the class or interface represented by this
     * {@code IScope} object.
     *
     * @param name the name of the predicate to find.
     * @return the {@code Predicate} object that matches the specified
     * {@code name}.
     */
    public default Predicate getPredicate(String name) {
        return getScope().getPredicate(name);
    }

    /**
     * Returns a {@code Type} object that reflects the specified public member
     * type of the class or interface represented by this {@code IScope} object.
     *
     * @param name the name of the type to find.
     * @return a type having the given name.
     */
    public default Type getType(String name) {
        return getScope().getType(name);
    }
}
