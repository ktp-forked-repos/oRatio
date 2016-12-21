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

import it.cnr.istc.oratio.core.parser.oRatioBaseVisitor;
import it.cnr.istc.oratio.core.parser.oRatioParser;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeVisitor extends oRatioBaseVisitor<Type> {

    private final Core core;

    TypeVisitor(Core core) {
        this.core = core;
    }

    @Override
    public Type visitLiteral_expression(oRatioParser.Literal_expressionContext ctx) {
        if (ctx.literal().numeric != null) {
            return core.types.get(Core.REAL);
        } else if (ctx.literal().string != null) {
            return core.types.get(Core.STRING);
        } else if (ctx.literal().t != null || ctx.literal().f != null) {
            return core.types.get(Core.BOOL);
        } else {
            throw new AssertionError("the primitive type has not been found..");
        }
    }

    @Override
    public Type visitCast_expression(oRatioParser.Cast_expressionContext ctx) {
        return visit(ctx.type());
    }

    @Override
    public Type visitPrimitive_type(oRatioParser.Primitive_typeContext ctx) {
        return core.types.get(ctx.getText());
    }

    @Override
    public Type visitClass_type(oRatioParser.Class_typeContext ctx) {
        IScope s = core.scopes.get(ctx);
        for (TerminalNode id : ctx.ID()) {
            s = s.getType(id.getText());
            if (s == null) {
                core.parser.notifyErrorListeners(id.getSymbol(), "cannot find symbol..", null);
            }
        }
        return (Type) s;
    }

    @Override
    public Type visitQualified_id(oRatioParser.Qualified_idContext ctx) {
        IScope s = core.scopes.get(ctx);
        if (ctx.t != null) {
            s = s.getField(BaseScope.THIS).type;
        }
        for (TerminalNode id : ctx.ID()) {
            Field f = s.getField(id.getText());
            if (f == null) {
                core.parser.notifyErrorListeners(id.getSymbol(), "cannot find symbol..", null);
            }
            s = s.getField(id.getText()).type;
        }
        return (Type) s;
    }

    @Override
    public Type visitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        return visit(ctx.qualified_id());
    }

    @Override
    public Type visitConstructor_expression(oRatioParser.Constructor_expressionContext ctx) {
        return visit(ctx.type());
    }
}
