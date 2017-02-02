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

import it.cnr.istc.core.parser.oRatioParser;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Typedef extends Type {

    final Type base_type;
    final oRatioParser.ExprContext expr;

    Typedef(Core c, IScope s, String n, Type base_type, oRatioParser.ExprContext expr) {
        super(c, s, n);
        this.base_type = base_type;
        this.expr = expr;
    }

    @Override
    public IItem newInstance(IEnv env) {
        IItem i = base_type.newInstance(env);
        boolean add = core.add(core.eq(i, new ExpressionVisitor(core, env).visit(expr)));
        assert add;
        return i;
    }
}
