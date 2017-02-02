// Generated from oRatio.g4 by ANTLR 4.6
package it.cnr.istc.core.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link oRatioParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface oRatioVisitor<T> extends ParseTreeVisitor<T> {

    /**
     * Visit a parse tree produced by {@link oRatioParser#compilation_unit}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompilation_unit(oRatioParser.Compilation_unitContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#type_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType_declaration(oRatioParser.Type_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypedef_declaration(oRatioParser.Typedef_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#enum_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEnum_declaration(oRatioParser.Enum_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#enum_constants}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEnum_constants(oRatioParser.Enum_constantsContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#class_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClass_declaration(oRatioParser.Class_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#member}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMember(oRatioParser.MemberContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#field_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitField_declaration(oRatioParser.Field_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#variable_dec}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVariable_dec(oRatioParser.Variable_decContext ctx);

    /**
     * Visit a parse tree produced by the {@code void_method_declaration}
     * labeled alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx);

    /**
     * Visit a parse tree produced by the {@code type_method_declaration}
     * labeled alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType_method_declaration(oRatioParser.Type_method_declarationContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link oRatioParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstructor_declaration(oRatioParser.Constructor_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#initializer_element}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInitializer_element(oRatioParser.Initializer_elementContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link oRatioParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPredicate_declaration(oRatioParser.Predicate_declarationContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatement(oRatioParser.StatementContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#block}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBlock(oRatioParser.BlockContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#assignment_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment_statement(oRatioParser.Assignment_statementContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link oRatioParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#expression_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpression_statement(oRatioParser.Expression_statementContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link oRatioParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#conjunction}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConjunction(oRatioParser.ConjunctionContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#formula_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFormula_statement(oRatioParser.Formula_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#return_statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitReturn_statement(oRatioParser.Return_statementContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#assignment_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment_list(oRatioParser.Assignment_listContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#assignment}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignment(oRatioParser.AssignmentContext ctx);

    /**
     * Visit a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCast_expression(oRatioParser.Cast_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code qualified_id_expression}
     * labeled alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDivision_expression(oRatioParser.Division_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSubtraction_expression(oRatioParser.Subtraction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code extc_one_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExtc_one_expression(oRatioParser.Extc_one_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPlus_expression(oRatioParser.Plus_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunction_expression(oRatioParser.Function_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAddition_expression(oRatioParser.Addition_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParentheses_expression(oRatioParser.Parentheses_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMinus_expression(oRatioParser.Minus_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImplication_expression(oRatioParser.Implication_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLt_expression(oRatioParser.Lt_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNot_expression(oRatioParser.Not_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConjunction_expression(oRatioParser.Conjunction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGeq_expression(oRatioParser.Geq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRange_expression(oRatioParser.Range_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiplication_expression(oRatioParser.Multiplication_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLeq_expression(oRatioParser.Leq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGt_expression(oRatioParser.Gt_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstructor_expression(oRatioParser.Constructor_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDisjunction_expression(oRatioParser.Disjunction_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLiteral_expression(oRatioParser.Literal_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEq_expression(oRatioParser.Eq_expressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNeq_expression(oRatioParser.Neq_expressionContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#expr_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpr_list(oRatioParser.Expr_listContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#literal}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLiteral(oRatioParser.LiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#qualified_id}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQualified_id(oRatioParser.Qualified_idContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType(oRatioParser.TypeContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#class_type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClass_type(oRatioParser.Class_typeContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#primitive_type}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrimitive_type(oRatioParser.Primitive_typeContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#type_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitType_list(oRatioParser.Type_listContext ctx);

    /**
     * Visit a parse tree produced by {@link oRatioParser#typed_list}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTyped_list(oRatioParser.Typed_listContext ctx);
}
