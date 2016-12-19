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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class TypeVisitor extends oRatioBaseVisitor<Boolean> {

    private final Core core;

    TypeVisitor(Core core) {
        this.core = core;
    }

    @Override
    public Boolean visitLiteral_expression(oRatioParser.Literal_expressionContext ctx) {
        return super.visitLiteral_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitCast_expression(oRatioParser.Cast_expressionContext ctx) {
        return super.visitCast_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitPrimitive_type(oRatioParser.Primitive_typeContext ctx) {
        return super.visitPrimitive_type(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitClass_type(oRatioParser.Class_typeContext ctx) {
        return super.visitClass_type(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitQualified_id(oRatioParser.Qualified_idContext ctx) {
        return super.visitQualified_id(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        return super.visitQualified_id_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitConstructor_expression(oRatioParser.Constructor_expressionContext ctx) {
        return super.visitConstructor_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }
}
