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

import it.cnr.istc.oratio.core.parser.oRatioBaseListener;
import it.cnr.istc.oratio.core.parser.oRatioParser;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeDeclarationListener extends oRatioBaseListener {

    private final Core core;
    private IScope scope;

    TypeDeclarationListener(Core core) {
        this.core = core;
    }

    @Override
    public void enterCompilation_unit(oRatioParser.Compilation_unitContext ctx) {
        core.scopes.put(ctx, core);
        scope = core;
    }

    @Override
    public void enterTypedef_declaration(oRatioParser.Typedef_declarationContext ctx) {
        Typedef td = new Typedef(core, scope, ctx.name.getText(), new TypeVisitor(core).visit(ctx.primitive_type()), ctx.expr());
        core.scopes.put(ctx, td);

        if (scope == core) {
            core.types.put(td.name, td);
        } else {
            ((Type) scope).types.put(td.name, td);
        }
    }

    @Override
    public void enterEnum_declaration(oRatioParser.Enum_declarationContext ctx) {
        EnumType et = new EnumType(core, scope, ctx.name.getText());
        core.scopes.put(ctx, et);

        // We add the enum values..
        for (oRatioParser.Enum_constantsContext c : ctx.enum_constants()) {
            for (TerminalNode l : c.StringLiteral()) {
                et.addEnum(core.newStringItem(l.getText()));
            }
        }

        if (scope == core) {
            core.types.put(et.name, et);
        } else {
            ((Type) scope).types.put(et.name, et);
        }
    }

    @Override
    public void enterClass_declaration(oRatioParser.Class_declarationContext ctx) {
        // A new type has been declared..
        Type t = new Type(core, scope, ctx.name.getText());
        core.scopes.put(ctx, t);

        if (scope == core) {
            core.types.put(t.name, t);
        } else {
            ((Type) scope).types.put(t.name, t);
        }

        scope = t;
    }

    @Override
    public void exitClass_declaration(oRatioParser.Class_declarationContext ctx) {
        scope = scope.getScope();
    }

    @Override
    public void enterClass_type(oRatioParser.Class_typeContext ctx) {
        core.scopes.put(ctx, scope);
    }
}
