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
public interface IEnv {

    /**
     * Returns the core enclosing this environment.
     *
     * @return the core enclosing this environment.
     */
    public Core getCore();

    /**
     * Returns the environment enclosing this environment.
     *
     * @return the environment enclosing this environment.
     */
    public IEnv getEnv();

    /**
     * Returns the item of this environment, or any enclosing environment,
     * associated to the given name.
     *
     * @param <T> the type of the item.
     * @param name the name of the object to be retrieved.
     * @return the object associated to the given name.
     */
    public <T extends IItem> T get(String name);
}
