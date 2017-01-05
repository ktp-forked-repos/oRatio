/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
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
package it.cnr.istc.oratio.solver.types;

import it.cnr.istc.ac.DomainListener;
import it.cnr.istc.ac.Var;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import it.cnr.istc.oratio.core.Constructor;
import static it.cnr.istc.oratio.core.Core.BOOL;
import it.cnr.istc.oratio.core.Field;
import it.cnr.istc.oratio.core.IArithItem;
import it.cnr.istc.oratio.core.IBoolItem;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Predicate;
import it.cnr.istc.oratio.core.Type;
import it.cnr.istc.oratio.solver.Flaw;
import it.cnr.istc.oratio.solver.SmartType;
import it.cnr.istc.oratio.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PropositionalState extends SmartType {

    public static final String NAME = "PropositionalState";
    public static final String POLARITY = "polarity";
    private final Collection<Predicate> defined_predicates = new ArrayList<>();
    private final Set<Atom> to_check = new HashSet<>();

    public PropositionalState(Solver s) {
        super(s, s, NAME);
        constructors.add(new Constructor(core, this) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                return true;
            }
        });
        predicates.put("PropositionalStatePredicate", new Predicate(core, this, "PropositionalStatePredicate", new Field(core.getType(BOOL), POLARITY)) {
            @Override
            public boolean apply(Atom atom) {
                return true;
            }
        });
    }

    @Override
    protected void predicateDefined(Predicate predicate) {
        if (!defined_predicates.contains(predicate)) {
            extendPredicate(predicate, core.getPredicate("IntervalPredicate"));
            extendPredicate(predicate, predicates.get("PropositionalStatePredicate"));
            defined_predicates.add(predicate);
        }
    }

    @Override
    protected boolean factActivated(Atom atom) {
        if (super.factActivated(atom)) {
            core.network.addDomainListener(new AtomListener(atom));
            to_check.add(atom);
            return core.getPredicate("IntervalPredicate").apply(atom) && predicates.get("PropositionalStatePredicate").apply(atom);
        } else {
            return false;
        }
    }

    @Override
    protected boolean goalActivated(Atom atom) {
        if (super.goalActivated(atom)) {
            core.network.addDomainListener(new AtomListener(atom));
            to_check.add(atom);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<Flaw> getInconsistencies() {
        if (to_check.isEmpty()) {
            // nothing has changed since last inconsistency check..
            return Collections.emptyList();
        }

        Collection<Flaw> fs = new ArrayList<>();
        Map<Type, List<Atom>> c_atoms = to_check.stream().filter(a -> a.state.evaluate().isSingleton() && a.state.evaluate().contains(AtomState.Active)).collect(Collectors.groupingBy(Atom::getType));
        for (Type type : c_atoms.keySet()) {
            List<Atom> atoms = c_atoms.get(type);
            for (Atom a0 : atoms) {
                for (Atom a1 : atoms) {
                    if (a0 != a1) {
                    }
                }
            }
        }

        to_check.clear();
        return fs;
    }

    private class AtomListener implements DomainListener {

        private final Atom atom;

        AtomListener(Atom atom) {
            this.atom = atom;
        }

        @Override
        public Var<?>[] getVars() {
            return atom.getItems().values().stream().filter(i -> (i instanceof IBoolItem || i instanceof IArithItem || i instanceof IEnumItem)).map(i -> {
                if (i instanceof IBoolItem) {
                    return ((IBoolItem) i).getBoolVar().to_var(core.network);
                } else if (i instanceof IArithItem) {
                    return ((IArithItem) i).getArithVar().to_var(core.network);
                } else {
                    return ((IEnumItem) i).getEnumVar().to_var(core.network);
                }
            }).toArray(Var<?>[]::new);
        }

        @Override
        public void domainChange(Var<?> var) {
            to_check.add(atom);
        }
    }
}
