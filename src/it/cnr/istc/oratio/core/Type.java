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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Type extends BaseScope {

    public final String name;
    public final boolean primitive;
    protected final Collection<Type> superclasses = new ArrayList<>();
    protected final Collection<Constructor> constructors = new ArrayList<>();
    protected final Map<String, Collection<Method>> methods = new LinkedHashMap<>();
    protected final Map<String, Type> types = new LinkedHashMap<>();
    protected final Map<String, Predicate> predicates = new LinkedHashMap<>();
    protected final Collection<IItem> instances = new ArrayList<>();

    public Type(Core c, IScope s, String n) {
        this(c, s, n, false);
    }

    Type(Core c, IScope s, String n, boolean p) {
        super(c, s);
        this.name = n;
        this.primitive = p;
    }

    public boolean isAssignableFrom(Type type) {
        LinkedList<Type> queue = new LinkedList<>();
        queue.add(type);
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (c_type == this) {
                return true;
            }
            queue.addAll(c_type.superclasses);
        }
        return false;
    }

    public IItem newInstance(IEnv env) {
        Item item = new Item(core, env, this);

        LinkedList<Type> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            c_type.instances.add(item);
            queue.addAll(c_type.superclasses);
        }

        return item;
    }

    public IItem newExistential() {
        assert !instances.isEmpty();
        if (instances.size() == 1) {
            return instances.iterator().next();
        } else {
            return core.newEnum(this, instances.toArray(new IItem[instances.size()]));
        }
    }

    public Collection<IItem> getInstances() {
        return Collections.unmodifiableCollection(instances);
    }

    protected void predicateDefined(Predicate predicate) {
        for (Type st : superclasses) {
            st.predicateDefined(predicate);
        }
    }

    protected void enrichPredicate(Predicate p, Field f) {
        p.fields.put(f.name, f);
    }

    protected void extendPredicate(Predicate derived, Predicate base) {
        derived.superclasses.add(base);
    }

    protected boolean factCreated(Atom atom) {
        for (Type superclass : superclasses) {
            if (!superclass.factCreated(atom)) {
                return false;
            }
        }
        return true;
    }

    protected boolean goalCreated(Atom atom) {
        for (Type superclass : superclasses) {
            if (!superclass.goalCreated(atom)) {
                return false;
            }
        }
        return true;
    }

    public Collection<Type> getSuperclasses() {
        return Collections.unmodifiableCollection(superclasses);
    }

    public Constructor getConstructor(Type... parameter_types) {
        assert Stream.of(parameter_types).noneMatch(Objects::isNull);
        boolean isCorrect;
        for (Constructor c : constructors) {
            if (c.parameters.length == parameter_types.length) {
                isCorrect = true;
                for (int i = 0; i < c.parameters.length; i++) {
                    if (!c.parameters[i].type.isAssignableFrom(parameter_types[i])) {
                        isCorrect = false;
                        break;
                    }
                }
                if (isCorrect) {
                    return c;
                }
            }
        }

        // not found
        return null;
    }

    public Collection<Constructor> getConstructors() {
        return Collections.unmodifiableCollection(constructors);
    }

    @Override
    public Field getField(String name) {
        Field field = fields.get(name);
        if (field != null) {
            return field;
        }

        // if not here, check any enclosing scope
        field = scope.getField(name);
        if (field != null) {
            return field;
        }

        // if not in any enclosing scope, check any superclass
        for (Type superclass : superclasses) {
            field = superclass.getField(name);
            if (field != null) {
                return field;
            }
        }

        // not found
        return null;
    }

    @Override
    public Method getMethod(String name, Type... parameter_types) {
        assert Stream.of(parameter_types).noneMatch(Objects::isNull);
        boolean isCorrect;
        if (methods.containsKey(name)) {
            for (Method m : methods.get(name)) {
                if (m.parameters.length == parameter_types.length) {
                    isCorrect = true;
                    for (int i = 0; i < m.parameters.length; i++) {
                        if (!m.parameters[i].type.isAssignableFrom(parameter_types[i])) {
                            isCorrect = false;
                            break;
                        }
                    }
                    if (isCorrect) {
                        return m;
                    }
                }
            }
        }

        // if not here, check any enclosing scope
        Method m = scope.getMethod(name, parameter_types);
        if (m != null) {
            return m;
        }

        // if not in any enclosing scope, check any superclass
        for (Type superclass : superclasses) {
            m = superclass.getMethod(name, parameter_types);
            if (m != null) {
                return m;
            }
        }

        // not found
        return null;
    }

    @Override
    public Collection<Method> getMethods() {
        Collection<Method> c_methods = new ArrayList<>();
        for (Collection<Method> ms : methods.values()) {
            c_methods.addAll(ms);
        }
        return Collections.unmodifiableCollection(c_methods);
    }

    @Override
    public Predicate getPredicate(String name) {
        Predicate predicate = predicates.get(name);
        if (predicate != null) {
            return predicate;
        }

        // if not here, check any enclosing scope
        if (scope != null) {
            predicate = scope.getPredicate(name);
            if (predicate != null) {
                return predicate;
            }
        }

        // if not in any enclosing scope, check any superclass
        for (Type superclass : superclasses) {
            predicate = superclass.getPredicate(name);
            if (predicate != null) {
                return predicate;
            }
        }

        // not found
        return null;
    }

    @Override
    public Collection<Predicate> getPredicates() {
        return Collections.unmodifiableCollection(predicates.values());
    }

    @Override
    public Type getType(String name) {
        Type type = types.get(name);
        if (type != null) {
            return type;
        }

        // if not here, check any enclosing scope
        if (scope != null) {
            type = scope.getType(name);
            if (type != null) {
                return type;
            }
        }

        for (Type superclass : superclasses) {
            type = superclass.getType(name);
            if (type != null) {
                return type;
            }
        }

        // not found
        return null;
    }

    @Override
    public Collection<Type> getTypes() {
        return Collections.unmodifiableCollection(types.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
