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
class TypeRefinementListener extends oRatioBaseListener {

    private final Core core;
    private IScope scope;

    TypeRefinementListener(Core core) {
        this.core = core;
    }

    @Override
    public void enterCompilation_unit(oRatioParser.Compilation_unitContext ctx) {
        super.enterCompilation_unit(ctx); //To change body of generated methods, choose Tools | Templates.
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
    public void enterField_declaration(oRatioParser.Field_declarationContext ctx) {
        super.enterField_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterConstructor_declaration(oRatioParser.Constructor_declarationContext ctx) {
        super.enterConstructor_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitConstructor_declaration(oRatioParser.Constructor_declarationContext ctx) {
        super.exitConstructor_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx) {
        super.enterVoid_method_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx) {
        super.exitVoid_method_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterType_method_declaration(oRatioParser.Type_method_declarationContext ctx) {
        super.enterType_method_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitType_method_declaration(oRatioParser.Type_method_declarationContext ctx) {
        super.exitType_method_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterPredicate_declaration(oRatioParser.Predicate_declarationContext ctx) {
        super.enterPredicate_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitPredicate_declaration(oRatioParser.Predicate_declarationContext ctx) {
        super.exitPredicate_declaration(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        super.enterDisjunction_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        super.exitDisjunction_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterConjunction(oRatioParser.ConjunctionContext ctx) {
        super.enterConjunction(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitConjunction(oRatioParser.ConjunctionContext ctx) {
        super.exitConjunction(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx) {
        super.enterLocal_variable_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterAssignment_statement(oRatioParser.Assignment_statementContext ctx) {
        super.enterAssignment_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterExpression_statement(oRatioParser.Expression_statementContext ctx) {
        super.enterExpression_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterFormula_statement(oRatioParser.Formula_statementContext ctx) {
        super.enterFormula_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterReturn_statement(oRatioParser.Return_statementContext ctx) {
        super.enterReturn_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        super.enterQualified_id_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enterFunction_expression(oRatioParser.Function_expressionContext ctx) {
        super.enterFunction_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }
}
