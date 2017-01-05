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

import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Constructor;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Predicate;
import it.cnr.istc.oratio.solver.Flaw;
import it.cnr.istc.oratio.solver.SmartType;
import it.cnr.istc.oratio.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Agent extends SmartType {

    public static final String NAME = "Agent";
    private final Collection<Predicate> defined_predicates = new ArrayList<>();

    public Agent(Solver s) {
        super(s, s, NAME);
        constructors.add(new Constructor(core, this) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                return true;
            }
        });
    }

    @Override
    protected void predicateDefined(Predicate predicate) {
        if (!defined_predicates.contains(predicate)) {
            extendPredicate(predicate, core.getPredicate("IntervalPredicate"));
            defined_predicates.add(predicate);
        }
    }

    @Override
    protected boolean factActivated(Atom atom) {
        if (super.factActivated(atom)) {
            return core.getPredicate("IntervalPredicate").apply(atom);
        } else {
            return false;
        }
    }

    @Override
    public Collection<Flaw> getInconsistencies() {
        return Collections.emptyList();
    }
}
