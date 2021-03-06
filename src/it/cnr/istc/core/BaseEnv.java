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
package it.cnr.istc.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class BaseEnv implements IEnv {

    public final Core core;
    public final IEnv env;
    protected final Map<String, IItem> items = new LinkedHashMap<>();

    public BaseEnv(Core c, IEnv e) {
        this.core = c;
        this.env = e;
    }

    @Override
    public Core getCore() {
        return core;
    }

    @Override
    public IEnv getEnv() {
        return env;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IItem> T get(String name) {
        IItem item = items.get(name);
        if (item != null) {
            return (T) item;
        }

        // if not here, check any enclosing environment
        if (env != null) {
            item = env.get(name);
            if (item != null) {
                return (T) item;
            }
        }

        // not found
        return null;
    }

    public Map<String, IItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
