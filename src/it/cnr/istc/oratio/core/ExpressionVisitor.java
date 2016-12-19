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
class ExpressionVisitor extends oRatioBaseVisitor<IItem> {

    private final Core core;
    private final IEnv env;

    ExpressionVisitor(Core core, IEnv env) {
        this.core = core;
        this.env = env;
    }

    @Override
    public IItem visitLiteral_expression(oRatioParser.Literal_expressionContext ctx) {
        return super.visitLiteral_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitParentheses_expression(oRatioParser.Parentheses_expressionContext ctx) {
        return super.visitParentheses_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitMultiplication_expression(oRatioParser.Multiplication_expressionContext ctx) {
        return super.visitMultiplication_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitDivision_expression(oRatioParser.Division_expressionContext ctx) {
        return super.visitDivision_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitAddition_expression(oRatioParser.Addition_expressionContext ctx) {
        return super.visitAddition_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitSubtraction_expression(oRatioParser.Subtraction_expressionContext ctx) {
        return super.visitSubtraction_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitMinus_expression(oRatioParser.Minus_expressionContext ctx) {
        return super.visitMinus_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitNot_expression(oRatioParser.Not_expressionContext ctx) {
        return super.visitNot_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx) {
        return super.visitQualified_id_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitFunction_expression(oRatioParser.Function_expressionContext ctx) {
        return super.visitFunction_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitRange_expression(oRatioParser.Range_expressionContext ctx) {
        return super.visitRange_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitConstructor_expression(oRatioParser.Constructor_expressionContext ctx) {
        return super.visitConstructor_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitLt_expression(oRatioParser.Lt_expressionContext ctx) {
        return super.visitLt_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitLeq_expression(oRatioParser.Leq_expressionContext ctx) {
        return super.visitLeq_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitEq_expression(oRatioParser.Eq_expressionContext ctx) {
        return super.visitEq_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitGeq_expression(oRatioParser.Geq_expressionContext ctx) {
        return super.visitGeq_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitGt_expression(oRatioParser.Gt_expressionContext ctx) {
        return super.visitGt_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitNeq_expression(oRatioParser.Neq_expressionContext ctx) {
        return super.visitNeq_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitImplication_expression(oRatioParser.Implication_expressionContext ctx) {
        return super.visitImplication_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitConjunction_expression(oRatioParser.Conjunction_expressionContext ctx) {
        return super.visitConjunction_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitDisjunction_expression(oRatioParser.Disjunction_expressionContext ctx) {
        return super.visitDisjunction_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IItem visitExtc_one_expression(oRatioParser.Extc_one_expressionContext ctx) {
        return super.visitExtc_one_expression(ctx); //To change body of generated methods, choose Tools | Templates.
    }
}
