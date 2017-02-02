// Generated from oRatio.g4 by ANTLR 4.6
package it.cnr.istc.core.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link oRatioParser}.
 */
public interface oRatioListener extends ParseTreeListener {

    /**
     * Enter a parse tree produced by {@link oRatioParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void enterCompilation_unit(oRatioParser.Compilation_unitContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#compilation_unit}.
     *
     * @param ctx the parse tree
     */
    void exitCompilation_unit(oRatioParser.Compilation_unitContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#type_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterType_declaration(oRatioParser.Type_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#type_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitType_declaration(oRatioParser.Type_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterTypedef_declaration(oRatioParser.Typedef_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#typedef_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitTypedef_declaration(oRatioParser.Typedef_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#enum_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterEnum_declaration(oRatioParser.Enum_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#enum_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitEnum_declaration(oRatioParser.Enum_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#enum_constants}.
     *
     * @param ctx the parse tree
     */
    void enterEnum_constants(oRatioParser.Enum_constantsContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#enum_constants}.
     *
     * @param ctx the parse tree
     */
    void exitEnum_constants(oRatioParser.Enum_constantsContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#class_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterClass_declaration(oRatioParser.Class_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#class_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitClass_declaration(oRatioParser.Class_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#member}.
     *
     * @param ctx the parse tree
     */
    void enterMember(oRatioParser.MemberContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#member}.
     *
     * @param ctx the parse tree
     */
    void exitMember(oRatioParser.MemberContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#field_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterField_declaration(oRatioParser.Field_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#field_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitField_declaration(oRatioParser.Field_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#variable_dec}.
     *
     * @param ctx the parse tree
     */
    void enterVariable_dec(oRatioParser.Variable_decContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#variable_dec}.
     *
     * @param ctx the parse tree
     */
    void exitVariable_dec(oRatioParser.Variable_decContext ctx);

    /**
     * Enter a parse tree produced by the {@code void_method_declaration}
     * labeled alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx);

    /**
     * Exit a parse tree produced by the {@code void_method_declaration} labeled
     * alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitVoid_method_declaration(oRatioParser.Void_method_declarationContext ctx);

    /**
     * Enter a parse tree produced by the {@code type_method_declaration}
     * labeled alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterType_method_declaration(oRatioParser.Type_method_declarationContext ctx);

    /**
     * Exit a parse tree produced by the {@code type_method_declaration} labeled
     * alternative in {@link oRatioParser#method_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitType_method_declaration(oRatioParser.Type_method_declarationContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link oRatioParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterConstructor_declaration(oRatioParser.Constructor_declarationContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link oRatioParser#constructor_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitConstructor_declaration(oRatioParser.Constructor_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#initializer_element}.
     *
     * @param ctx the parse tree
     */
    void enterInitializer_element(oRatioParser.Initializer_elementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#initializer_element}.
     *
     * @param ctx the parse tree
     */
    void exitInitializer_element(oRatioParser.Initializer_elementContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link oRatioParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     */
    void enterPredicate_declaration(oRatioParser.Predicate_declarationContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#predicate_declaration}.
     *
     * @param ctx the parse tree
     */
    void exitPredicate_declaration(oRatioParser.Predicate_declarationContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterStatement(oRatioParser.StatementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitStatement(oRatioParser.StatementContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#block}.
     *
     * @param ctx the parse tree
     */
    void enterBlock(oRatioParser.BlockContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#block}.
     *
     * @param ctx the parse tree
     */
    void exitBlock(oRatioParser.BlockContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#assignment_statement}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment_statement(oRatioParser.Assignment_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#assignment_statement}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment_statement(oRatioParser.Assignment_statementContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link oRatioParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     */
    void enterLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link oRatioParser#local_variable_statement}.
     *
     * @param ctx the parse tree
     */
    void exitLocal_variable_statement(oRatioParser.Local_variable_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#expression_statement}.
     *
     * @param ctx the parse tree
     */
    void enterExpression_statement(oRatioParser.Expression_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#expression_statement}.
     *
     * @param ctx the parse tree
     */
    void exitExpression_statement(oRatioParser.Expression_statementContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link oRatioParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     */
    void enterDisjunction_statement(oRatioParser.Disjunction_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#disjunction_statement}.
     *
     * @param ctx the parse tree
     */
    void exitDisjunction_statement(oRatioParser.Disjunction_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#conjunction}.
     *
     * @param ctx the parse tree
     */
    void enterConjunction(oRatioParser.ConjunctionContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#conjunction}.
     *
     * @param ctx the parse tree
     */
    void exitConjunction(oRatioParser.ConjunctionContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#formula_statement}.
     *
     * @param ctx the parse tree
     */
    void enterFormula_statement(oRatioParser.Formula_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#formula_statement}.
     *
     * @param ctx the parse tree
     */
    void exitFormula_statement(oRatioParser.Formula_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#return_statement}.
     *
     * @param ctx the parse tree
     */
    void enterReturn_statement(oRatioParser.Return_statementContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#return_statement}.
     *
     * @param ctx the parse tree
     */
    void exitReturn_statement(oRatioParser.Return_statementContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#assignment_list}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment_list(oRatioParser.Assignment_listContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#assignment_list}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment_list(oRatioParser.Assignment_listContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#assignment}.
     *
     * @param ctx the parse tree
     */
    void enterAssignment(oRatioParser.AssignmentContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#assignment}.
     *
     * @param ctx the parse tree
     */
    void exitAssignment(oRatioParser.AssignmentContext ctx);

    /**
     * Enter a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterCast_expression(oRatioParser.Cast_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code cast_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitCast_expression(oRatioParser.Cast_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code qualified_id_expression}
     * labeled alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code qualified_id_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitQualified_id_expression(oRatioParser.Qualified_id_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterDivision_expression(oRatioParser.Division_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code division_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitDivision_expression(oRatioParser.Division_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterSubtraction_expression(oRatioParser.Subtraction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code subtraction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitSubtraction_expression(oRatioParser.Subtraction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code extc_one_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterExtc_one_expression(oRatioParser.Extc_one_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code extc_one_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitExtc_one_expression(oRatioParser.Extc_one_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterPlus_expression(oRatioParser.Plus_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code plus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitPlus_expression(oRatioParser.Plus_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterFunction_expression(oRatioParser.Function_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code function_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitFunction_expression(oRatioParser.Function_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterAddition_expression(oRatioParser.Addition_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code addition_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitAddition_expression(oRatioParser.Addition_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterParentheses_expression(oRatioParser.Parentheses_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code parentheses_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitParentheses_expression(oRatioParser.Parentheses_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterMinus_expression(oRatioParser.Minus_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code minus_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitMinus_expression(oRatioParser.Minus_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterImplication_expression(oRatioParser.Implication_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code implication_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitImplication_expression(oRatioParser.Implication_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLt_expression(oRatioParser.Lt_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code lt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLt_expression(oRatioParser.Lt_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterNot_expression(oRatioParser.Not_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code not_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitNot_expression(oRatioParser.Not_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterConjunction_expression(oRatioParser.Conjunction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code conjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitConjunction_expression(oRatioParser.Conjunction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterGeq_expression(oRatioParser.Geq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code geq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitGeq_expression(oRatioParser.Geq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterRange_expression(oRatioParser.Range_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code range_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitRange_expression(oRatioParser.Range_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterMultiplication_expression(oRatioParser.Multiplication_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code multiplication_expression}
     * labeled alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitMultiplication_expression(oRatioParser.Multiplication_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLeq_expression(oRatioParser.Leq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code leq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLeq_expression(oRatioParser.Leq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterGt_expression(oRatioParser.Gt_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code gt_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitGt_expression(oRatioParser.Gt_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterConstructor_expression(oRatioParser.Constructor_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code constructor_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitConstructor_expression(oRatioParser.Constructor_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterDisjunction_expression(oRatioParser.Disjunction_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code disjunction_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitDisjunction_expression(oRatioParser.Disjunction_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterLiteral_expression(oRatioParser.Literal_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code literal_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitLiteral_expression(oRatioParser.Literal_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterEq_expression(oRatioParser.Eq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code eq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitEq_expression(oRatioParser.Eq_expressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterNeq_expression(oRatioParser.Neq_expressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code neq_expression} labeled
     * alternative in {@link oRatioParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitNeq_expression(oRatioParser.Neq_expressionContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#expr_list}.
     *
     * @param ctx the parse tree
     */
    void enterExpr_list(oRatioParser.Expr_listContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#expr_list}.
     *
     * @param ctx the parse tree
     */
    void exitExpr_list(oRatioParser.Expr_listContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#literal}.
     *
     * @param ctx the parse tree
     */
    void enterLiteral(oRatioParser.LiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#literal}.
     *
     * @param ctx the parse tree
     */
    void exitLiteral(oRatioParser.LiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#qualified_id}.
     *
     * @param ctx the parse tree
     */
    void enterQualified_id(oRatioParser.Qualified_idContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#qualified_id}.
     *
     * @param ctx the parse tree
     */
    void exitQualified_id(oRatioParser.Qualified_idContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterType(oRatioParser.TypeContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitType(oRatioParser.TypeContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#class_type}.
     *
     * @param ctx the parse tree
     */
    void enterClass_type(oRatioParser.Class_typeContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#class_type}.
     *
     * @param ctx the parse tree
     */
    void exitClass_type(oRatioParser.Class_typeContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#primitive_type}.
     *
     * @param ctx the parse tree
     */
    void enterPrimitive_type(oRatioParser.Primitive_typeContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#primitive_type}.
     *
     * @param ctx the parse tree
     */
    void exitPrimitive_type(oRatioParser.Primitive_typeContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#type_list}.
     *
     * @param ctx the parse tree
     */
    void enterType_list(oRatioParser.Type_listContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#type_list}.
     *
     * @param ctx the parse tree
     */
    void exitType_list(oRatioParser.Type_listContext ctx);

    /**
     * Enter a parse tree produced by {@link oRatioParser#typed_list}.
     *
     * @param ctx the parse tree
     */
    void enterTyped_list(oRatioParser.Typed_listContext ctx);

    /**
     * Exit a parse tree produced by {@link oRatioParser#typed_list}.
     *
     * @param ctx the parse tree
     */
    void exitTyped_list(oRatioParser.Typed_listContext ctx);
}
