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
    }

    @Override
    public void enterTypedef_declaration(oRatioParser.Typedef_declarationContext ctx) {
        super.enterTypedef_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterEnum_declaration(oRatioParser.Enum_declarationContext ctx) {
        super.enterEnum_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterClass_declaration(oRatioParser.Class_declarationContext ctx) {
        super.enterClass_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitClass_declaration(oRatioParser.Class_declarationContext ctx) {
        super.exitClass_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterClass_type(oRatioParser.Class_typeContext ctx) {
        super.enterClass_type(ctx); //To change body of generated methods, choose Tools | Templates.
    }
}
