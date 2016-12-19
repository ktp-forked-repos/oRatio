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
class StatementVisitor extends oRatioBaseVisitor<Boolean> {

    private final Core core;
    private final IEnv env;

    StatementVisitor(Core core, IEnv env) {
        this.core = core;
        this.env = env;
    }

    @Override
    public Boolean visitCompilation_unit(oRatioParser.Compilation_unitContext ctx) {
        return super.visitCompilation_unit(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitBlock(oRatioParser.BlockContext ctx) {
        return super.visitBlock(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitConjunction(oRatioParser.ConjunctionContext ctx) {
        return super.visitConjunction(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitAssignment_statement(oRatioParser.Assignment_statementContext ctx) {
        return super.visitAssignment_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx) {
        return super.visitLocal_variable_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitExpression_statement(oRatioParser.Expression_statementContext ctx) {
        return super.visitExpression_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitFormula_statement(oRatioParser.Formula_statementContext ctx) {
        return super.visitFormula_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitReturn_statement(oRatioParser.Return_statementContext ctx) {
        return super.visitReturn_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean visitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx) {
        return super.visitDisjunction_statement(ctx); //To change body of generated methods, choose Tools | Templates.
    }
}
