package it.cnr.istc.core.parser;

import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class oRatioParser extends Parser {

    static {
        RuntimeMetaData.checkVersion("4.6", RuntimeMetaData.VERSION);
    }
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache
            = new PredictionContextCache();
    public static final int TYPE_DEF = 1, REAL = 2, BOOL = 3, STRING = 4, ENUM = 5, CLASS = 6, GOAL = 7, FACT = 8,
            PREDICATE = 9, NEW = 10, OR = 11, THIS = 12, VOID = 13, TRUE = 14, FALSE = 15, RETURN = 16,
            DOT = 17, COMMA = 18, COLON = 19, SEMICOLON = 20, LPAREN = 21, RPAREN = 22, LBRACKET = 23,
            RBRACKET = 24, LBRACE = 25, RBRACE = 26, PLUS = 27, MINUS = 28, STAR = 29, SLASH = 30,
            AMP = 31, BAR = 32, EQUAL = 33, GT = 34, LT = 35, BANG = 36, EQEQ = 37, LTEQ = 38, GTEQ = 39,
            BANGEQ = 40, IMPLICATION = 41, CARET = 42, ID = 43, NumericLiteral = 44, StringLiteral = 45,
            LINE_COMMENT = 46, COMMENT = 47, WS = 48;
    public static final int RULE_compilation_unit = 0, RULE_type_declaration = 1, RULE_typedef_declaration = 2,
            RULE_enum_declaration = 3, RULE_enum_constants = 4, RULE_class_declaration = 5,
            RULE_member = 6, RULE_field_declaration = 7, RULE_variable_dec = 8, RULE_method_declaration = 9,
            RULE_constructor_declaration = 10, RULE_initializer_element = 11, RULE_predicate_declaration = 12,
            RULE_statement = 13, RULE_block = 14, RULE_assignment_statement = 15,
            RULE_local_variable_statement = 16, RULE_expression_statement = 17, RULE_disjunction_statement = 18,
            RULE_conjunction = 19, RULE_formula_statement = 20, RULE_return_statement = 21,
            RULE_assignment_list = 22, RULE_assignment = 23, RULE_expr = 24, RULE_expr_list = 25,
            RULE_literal = 26, RULE_qualified_id = 27, RULE_type = 28, RULE_class_type = 29,
            RULE_primitive_type = 30, RULE_type_list = 31, RULE_typed_list = 32;
    public static final String[] ruleNames = {
        "compilation_unit", "type_declaration", "typedef_declaration", "enum_declaration",
        "enum_constants", "class_declaration", "member", "field_declaration",
        "variable_dec", "method_declaration", "constructor_declaration", "initializer_element",
        "predicate_declaration", "statement", "block", "assignment_statement",
        "local_variable_statement", "expression_statement", "disjunction_statement",
        "conjunction", "formula_statement", "return_statement", "assignment_list",
        "assignment", "expr", "expr_list", "literal", "qualified_id", "type",
        "class_type", "primitive_type", "type_list", "typed_list"
    };
    private static final String[] _LITERAL_NAMES = {
        null, "'typedef'", "'real'", "'bool'", "'string'", "'enum'", "'class'",
        "'goal'", "'fact'", "'predicate'", "'new'", "'or'", "'this'", "'void'",
        "'true'", "'false'", "'return'", "'.'", "','", "':'", "';'", "'('", "')'",
        "'['", "']'", "'{'", "'}'", "'+'", "'-'", "'*'", "'/'", "'&'", "'|'",
        "'='", "'>'", "'<'", "'!'", "'=='", "'<='", "'>='", "'!='", "'->'", "'^'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
        null, "TYPE_DEF", "REAL", "BOOL", "STRING", "ENUM", "CLASS", "GOAL", "FACT",
        "PREDICATE", "NEW", "OR", "THIS", "VOID", "TRUE", "FALSE", "RETURN", "DOT",
        "COMMA", "COLON", "SEMICOLON", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET",
        "LBRACE", "RBRACE", "PLUS", "MINUS", "STAR", "SLASH", "AMP", "BAR", "EQUAL",
        "GT", "LT", "BANG", "EQEQ", "LTEQ", "GTEQ", "BANGEQ", "IMPLICATION", "CARET",
        "ID", "NumericLiteral", "StringLiteral", "LINE_COMMENT", "COMMENT", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "oRatio.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public oRatioParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class Compilation_unitContext extends ParserRuleContext {

        public TerminalNode EOF() {
            return getToken(oRatioParser.EOF, 0);
        }

        public List<Type_declarationContext> type_declaration() {
            return getRuleContexts(Type_declarationContext.class);
        }

        public Type_declarationContext type_declaration(int i) {
            return getRuleContext(Type_declarationContext.class, i);
        }

        public List<Method_declarationContext> method_declaration() {
            return getRuleContexts(Method_declarationContext.class);
        }

        public Method_declarationContext method_declaration(int i) {
            return getRuleContext(Method_declarationContext.class, i);
        }

        public List<Predicate_declarationContext> predicate_declaration() {
            return getRuleContexts(Predicate_declarationContext.class);
        }

        public Predicate_declarationContext predicate_declaration(int i) {
            return getRuleContext(Predicate_declarationContext.class, i);
        }

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public Compilation_unitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_compilation_unit;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterCompilation_unit(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitCompilation_unit(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitCompilation_unit(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Compilation_unitContext compilation_unit() throws RecognitionException {
        Compilation_unitContext _localctx = new Compilation_unitContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_compilation_unit);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(72);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPE_DEF) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ENUM) | (1L << CLASS) | (1L << GOAL) | (1L << FACT) | (1L << PREDICATE) | (1L << NEW) | (1L << THIS) | (1L << VOID) | (1L << TRUE) | (1L << FALSE) | (1L << RETURN) | (1L << LPAREN) | (1L << LBRACKET) | (1L << LBRACE) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                    {
                        setState(70);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                            case 1: {
                                setState(66);
                                type_declaration();
                            }
                            break;
                            case 2: {
                                setState(67);
                                method_declaration();
                            }
                            break;
                            case 3: {
                                setState(68);
                                predicate_declaration();
                            }
                            break;
                            case 4: {
                                setState(69);
                                statement();
                            }
                            break;
                        }
                    }
                    setState(74);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(75);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_declarationContext extends ParserRuleContext {

        public Typedef_declarationContext typedef_declaration() {
            return getRuleContext(Typedef_declarationContext.class, 0);
        }

        public Enum_declarationContext enum_declaration() {
            return getRuleContext(Enum_declarationContext.class, 0);
        }

        public Class_declarationContext class_declaration() {
            return getRuleContext(Class_declarationContext.class, 0);
        }

        public Type_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterType_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitType_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitType_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Type_declarationContext type_declaration() throws RecognitionException {
        Type_declarationContext _localctx = new Type_declarationContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_type_declaration);
        try {
            setState(80);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case TYPE_DEF:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(77);
                        typedef_declaration();
                    }
                    break;
                case ENUM:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(78);
                        enum_declaration();
                    }
                    break;
                case CLASS:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(79);
                        class_declaration();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Typedef_declarationContext extends ParserRuleContext {

        public Token name;

        public Primitive_typeContext primitive_type() {
            return getRuleContext(Primitive_typeContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Typedef_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_typedef_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterTypedef_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitTypedef_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitTypedef_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Typedef_declarationContext typedef_declaration() throws RecognitionException {
        Typedef_declarationContext _localctx = new Typedef_declarationContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_typedef_declaration);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(82);
                match(TYPE_DEF);
                setState(83);
                primitive_type();
                setState(84);
                expr(0);
                setState(85);
                _localctx.name = match(ID);
                setState(86);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Enum_declarationContext extends ParserRuleContext {

        public Token name;

        public List<Enum_constantsContext> enum_constants() {
            return getRuleContexts(Enum_constantsContext.class);
        }

        public Enum_constantsContext enum_constants(int i) {
            return getRuleContext(Enum_constantsContext.class, i);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Enum_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enum_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterEnum_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitEnum_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitEnum_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Enum_declarationContext enum_declaration() throws RecognitionException {
        Enum_declarationContext _localctx = new Enum_declarationContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_enum_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(88);
                match(ENUM);
                setState(89);
                _localctx.name = match(ID);
                setState(90);
                enum_constants();
                setState(95);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == BAR) {
                    {
                        {
                            setState(91);
                            match(BAR);
                            setState(92);
                            enum_constants();
                        }
                    }
                    setState(97);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(98);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Enum_constantsContext extends ParserRuleContext {

        public List<TerminalNode> StringLiteral() {
            return getTokens(oRatioParser.StringLiteral);
        }

        public TerminalNode StringLiteral(int i) {
            return getToken(oRatioParser.StringLiteral, i);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Enum_constantsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enum_constants;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterEnum_constants(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitEnum_constants(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitEnum_constants(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Enum_constantsContext enum_constants() throws RecognitionException {
        Enum_constantsContext _localctx = new Enum_constantsContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_enum_constants);
        int _la;
        try {
            setState(111);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case LBRACE:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(100);
                        match(LBRACE);
                        setState(101);
                        match(StringLiteral);
                        setState(106);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(102);
                                    match(COMMA);
                                    setState(103);
                                    match(StringLiteral);
                                }
                            }
                            setState(108);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(109);
                        match(RBRACE);
                    }
                    break;
                case REAL:
                case BOOL:
                case STRING:
                case ID:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(110);
                        type();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Class_declarationContext extends ParserRuleContext {

        public Token name;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Type_listContext type_list() {
            return getRuleContext(Type_listContext.class, 0);
        }

        public List<MemberContext> member() {
            return getRuleContexts(MemberContext.class);
        }

        public MemberContext member(int i) {
            return getRuleContext(MemberContext.class, i);
        }

        public Class_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_class_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterClass_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitClass_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitClass_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Class_declarationContext class_declaration() throws RecognitionException {
        Class_declarationContext _localctx = new Class_declarationContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_class_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(113);
                match(CLASS);
                setState(114);
                _localctx.name = match(ID);
                setState(117);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COLON) {
                    {
                        setState(115);
                        match(COLON);
                        setState(116);
                        type_list();
                    }
                }

                setState(119);
                match(LBRACE);
                setState(123);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TYPE_DEF) | (1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ENUM) | (1L << CLASS) | (1L << PREDICATE) | (1L << VOID) | (1L << ID))) != 0)) {
                    {
                        {
                            setState(120);
                            member();
                        }
                    }
                    setState(125);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(126);
                match(RBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class MemberContext extends ParserRuleContext {

        public Field_declarationContext field_declaration() {
            return getRuleContext(Field_declarationContext.class, 0);
        }

        public Method_declarationContext method_declaration() {
            return getRuleContext(Method_declarationContext.class, 0);
        }

        public Constructor_declarationContext constructor_declaration() {
            return getRuleContext(Constructor_declarationContext.class, 0);
        }

        public Predicate_declarationContext predicate_declaration() {
            return getRuleContext(Predicate_declarationContext.class, 0);
        }

        public Type_declarationContext type_declaration() {
            return getRuleContext(Type_declarationContext.class, 0);
        }

        public MemberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_member;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterMember(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitMember(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitMember(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final MemberContext member() throws RecognitionException {
        MemberContext _localctx = new MemberContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_member);
        try {
            setState(133);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(128);
                        field_declaration();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(129);
                        method_declaration();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(130);
                        constructor_declaration();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(131);
                        predicate_declaration();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                     {
                        setState(132);
                        type_declaration();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Field_declarationContext extends ParserRuleContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<Variable_decContext> variable_dec() {
            return getRuleContexts(Variable_decContext.class);
        }

        public Variable_decContext variable_dec(int i) {
            return getRuleContext(Variable_decContext.class, i);
        }

        public Field_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_field_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterField_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitField_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitField_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Field_declarationContext field_declaration() throws RecognitionException {
        Field_declarationContext _localctx = new Field_declarationContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_field_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(135);
                type();
                setState(136);
                variable_dec();
                setState(141);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(137);
                            match(COMMA);
                            setState(138);
                            variable_dec();
                        }
                    }
                    setState(143);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(144);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Variable_decContext extends ParserRuleContext {

        public Token name;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Variable_decContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variable_dec;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterVariable_dec(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitVariable_dec(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitVariable_dec(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Variable_decContext variable_dec() throws RecognitionException {
        Variable_decContext _localctx = new Variable_decContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_variable_dec);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(146);
                _localctx.name = match(ID);
                setState(149);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == EQUAL) {
                    {
                        setState(147);
                        match(EQUAL);
                        setState(148);
                        expr(0);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Method_declarationContext extends ParserRuleContext {

        public Method_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_method_declaration;
        }

        public Method_declarationContext() {
        }

        public void copyFrom(Method_declarationContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class Void_method_declarationContext extends Method_declarationContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Void_method_declarationContext(Method_declarationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterVoid_method_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitVoid_method_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitVoid_method_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Type_method_declarationContext extends Method_declarationContext {

        public Token name;

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Type_method_declarationContext(Method_declarationContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterType_method_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitType_method_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitType_method_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Method_declarationContext method_declaration() throws RecognitionException {
        Method_declarationContext _localctx = new Method_declarationContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_method_declaration);
        int _la;
        try {
            setState(173);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case VOID:
                    _localctx = new Void_method_declarationContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(151);
                        match(VOID);
                        setState(152);
                        ((Void_method_declarationContext) _localctx).name = match(ID);
                        setState(153);
                        match(LPAREN);
                        setState(155);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                            {
                                setState(154);
                                typed_list();
                            }
                        }

                        setState(157);
                        match(RPAREN);
                        setState(158);
                        match(LBRACE);
                        setState(159);
                        block();
                        setState(160);
                        match(RBRACE);
                    }
                    break;
                case REAL:
                case BOOL:
                case STRING:
                case ID:
                    _localctx = new Type_method_declarationContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(162);
                        type();
                        setState(163);
                        ((Type_method_declarationContext) _localctx).name = match(ID);
                        setState(164);
                        match(LPAREN);
                        setState(166);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                            {
                                setState(165);
                                typed_list();
                            }
                        }

                        setState(168);
                        match(RPAREN);
                        setState(169);
                        match(LBRACE);
                        setState(170);
                        block();
                        setState(171);
                        match(RBRACE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Constructor_declarationContext extends ParserRuleContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public List<Initializer_elementContext> initializer_element() {
            return getRuleContexts(Initializer_elementContext.class);
        }

        public Initializer_elementContext initializer_element(int i) {
            return getRuleContext(Initializer_elementContext.class, i);
        }

        public Constructor_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constructor_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterConstructor_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitConstructor_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitConstructor_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Constructor_declarationContext constructor_declaration() throws RecognitionException {
        Constructor_declarationContext _localctx = new Constructor_declarationContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_constructor_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(175);
                _localctx.name = match(ID);
                setState(176);
                match(LPAREN);
                setState(178);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                    {
                        setState(177);
                        typed_list();
                    }
                }

                setState(180);
                match(RPAREN);
                setState(190);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COLON) {
                    {
                        setState(181);
                        match(COLON);
                        setState(182);
                        initializer_element();
                        setState(187);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == COMMA) {
                            {
                                {
                                    setState(183);
                                    match(COMMA);
                                    setState(184);
                                    initializer_element();
                                }
                            }
                            setState(189);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(192);
                match(LBRACE);
                setState(193);
                block();
                setState(194);
                match(RBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Initializer_elementContext extends ParserRuleContext {

        public Token name;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Initializer_elementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_initializer_element;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterInitializer_element(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitInitializer_element(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitInitializer_element(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Initializer_elementContext initializer_element() throws RecognitionException {
        Initializer_elementContext _localctx = new Initializer_elementContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_initializer_element);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(196);
                _localctx.name = match(ID);
                setState(197);
                match(LPAREN);
                setState(199);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                    {
                        setState(198);
                        expr_list();
                    }
                }

                setState(201);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Predicate_declarationContext extends ParserRuleContext {

        public Token name;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Typed_listContext typed_list() {
            return getRuleContext(Typed_listContext.class, 0);
        }

        public Type_listContext type_list() {
            return getRuleContext(Type_listContext.class, 0);
        }

        public Predicate_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_predicate_declaration;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterPredicate_declaration(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitPredicate_declaration(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitPredicate_declaration(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Predicate_declarationContext predicate_declaration() throws RecognitionException {
        Predicate_declarationContext _localctx = new Predicate_declarationContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_predicate_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(203);
                match(PREDICATE);
                setState(204);
                _localctx.name = match(ID);
                setState(205);
                match(LPAREN);
                setState(207);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << ID))) != 0)) {
                    {
                        setState(206);
                        typed_list();
                    }
                }

                setState(209);
                match(RPAREN);
                setState(212);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COLON) {
                    {
                        setState(210);
                        match(COLON);
                        setState(211);
                        type_list();
                    }
                }

                setState(214);
                match(LBRACE);
                setState(215);
                block();
                setState(216);
                match(RBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StatementContext extends ParserRuleContext {

        public Assignment_statementContext assignment_statement() {
            return getRuleContext(Assignment_statementContext.class, 0);
        }

        public Local_variable_statementContext local_variable_statement() {
            return getRuleContext(Local_variable_statementContext.class, 0);
        }

        public Expression_statementContext expression_statement() {
            return getRuleContext(Expression_statementContext.class, 0);
        }

        public Disjunction_statementContext disjunction_statement() {
            return getRuleContext(Disjunction_statementContext.class, 0);
        }

        public Formula_statementContext formula_statement() {
            return getRuleContext(Formula_statementContext.class, 0);
        }

        public Return_statementContext return_statement() {
            return getRuleContext(Return_statementContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public StatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitStatement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitStatement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_statement);
        try {
            setState(228);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(218);
                        assignment_statement();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(219);
                        local_variable_statement();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(220);
                        expression_statement();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(221);
                        disjunction_statement();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                     {
                        setState(222);
                        formula_statement();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                     {
                        setState(223);
                        return_statement();
                    }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                     {
                        setState(224);
                        match(LBRACE);
                        setState(225);
                        block();
                        setState(226);
                        match(RBRACE);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BlockContext extends ParserRuleContext {

        public List<StatementContext> statement() {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i) {
            return getRuleContext(StatementContext.class, i);
        }

        public BlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_block;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterBlock(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitBlock(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitBlock(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final BlockContext block() throws RecognitionException {
        BlockContext _localctx = new BlockContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_block);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(233);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING) | (1L << GOAL) | (1L << FACT) | (1L << NEW) | (1L << THIS) | (1L << TRUE) | (1L << FALSE) | (1L << RETURN) | (1L << LPAREN) | (1L << LBRACKET) | (1L << LBRACE) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                    {
                        {
                            setState(230);
                            statement();
                        }
                    }
                    setState(235);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Assignment_statementContext extends ParserRuleContext {

        public Qualified_idContext object;
        public Token field;
        public ExprContext value;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Assignment_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterAssignment_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitAssignment_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitAssignment_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Assignment_statementContext assignment_statement() throws RecognitionException {
        Assignment_statementContext _localctx = new Assignment_statementContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_assignment_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(239);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
                    case 1: {
                        setState(236);
                        _localctx.object = qualified_id();
                        setState(237);
                        match(DOT);
                    }
                    break;
                }
                setState(241);
                _localctx.field = match(ID);
                setState(242);
                match(EQUAL);
                setState(243);
                _localctx.value = expr(0);
                setState(244);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Local_variable_statementContext extends ParserRuleContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<Variable_decContext> variable_dec() {
            return getRuleContexts(Variable_decContext.class);
        }

        public Variable_decContext variable_dec(int i) {
            return getRuleContext(Variable_decContext.class, i);
        }

        public Local_variable_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_local_variable_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterLocal_variable_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitLocal_variable_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitLocal_variable_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Local_variable_statementContext local_variable_statement() throws RecognitionException {
        Local_variable_statementContext _localctx = new Local_variable_statementContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_local_variable_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(246);
                type();
                setState(247);
                variable_dec();
                setState(252);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(248);
                            match(COMMA);
                            setState(249);
                            variable_dec();
                        }
                    }
                    setState(254);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(255);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Expression_statementContext extends ParserRuleContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Expression_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterExpression_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitExpression_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitExpression_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Expression_statementContext expression_statement() throws RecognitionException {
        Expression_statementContext _localctx = new Expression_statementContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_expression_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(257);
                expr(0);
                setState(258);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Disjunction_statementContext extends ParserRuleContext {

        public List<ConjunctionContext> conjunction() {
            return getRuleContexts(ConjunctionContext.class);
        }

        public ConjunctionContext conjunction(int i) {
            return getRuleContext(ConjunctionContext.class, i);
        }

        public Disjunction_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_disjunction_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterDisjunction_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitDisjunction_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitDisjunction_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Disjunction_statementContext disjunction_statement() throws RecognitionException {
        Disjunction_statementContext _localctx = new Disjunction_statementContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_disjunction_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(260);
                conjunction();
                setState(263);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(261);
                            match(OR);
                            setState(262);
                            conjunction();
                        }
                    }
                    setState(265);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == OR);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ConjunctionContext extends ParserRuleContext {

        public ExprContext cost;

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public ConjunctionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conjunction;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterConjunction(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitConjunction(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitConjunction(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ConjunctionContext conjunction() throws RecognitionException {
        ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_conjunction);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(267);
                match(LBRACE);
                setState(268);
                block();
                setState(269);
                match(RBRACE);
                setState(274);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 25, _ctx)) {
                    case 1: {
                        setState(270);
                        match(LBRACKET);
                        setState(271);
                        _localctx.cost = expr(0);
                        setState(272);
                        match(RBRACKET);
                    }
                    break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Formula_statementContext extends ParserRuleContext {

        public Token goal;
        public Token fact;
        public Token name;
        public Qualified_idContext object;
        public Token predicate;

        public List<TerminalNode> ID() {
            return getTokens(oRatioParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(oRatioParser.ID, i);
        }

        public Assignment_listContext assignment_list() {
            return getRuleContext(Assignment_listContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Formula_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_formula_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterFormula_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitFormula_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitFormula_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Formula_statementContext formula_statement() throws RecognitionException {
        Formula_statementContext _localctx = new Formula_statementContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_formula_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(278);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case GOAL: {
                        setState(276);
                        _localctx.goal = match(GOAL);
                    }
                    break;
                    case FACT: {
                        setState(277);
                        _localctx.fact = match(FACT);
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(280);
                _localctx.name = match(ID);
                setState(281);
                match(EQUAL);
                setState(282);
                match(NEW);
                setState(286);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
                    case 1: {
                        setState(283);
                        _localctx.object = qualified_id();
                        setState(284);
                        match(DOT);
                    }
                    break;
                }
                setState(288);
                _localctx.predicate = match(ID);
                setState(289);
                match(LPAREN);
                setState(291);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == ID) {
                    {
                        setState(290);
                        assignment_list();
                    }
                }

                setState(293);
                match(RPAREN);
                setState(294);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Return_statementContext extends ParserRuleContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Return_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_return_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterReturn_statement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitReturn_statement(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitReturn_statement(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Return_statementContext return_statement() throws RecognitionException {
        Return_statementContext _localctx = new Return_statementContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_return_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(296);
                match(RETURN);
                setState(297);
                expr(0);
                setState(298);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Assignment_listContext extends ParserRuleContext {

        public List<AssignmentContext> assignment() {
            return getRuleContexts(AssignmentContext.class);
        }

        public AssignmentContext assignment(int i) {
            return getRuleContext(AssignmentContext.class, i);
        }

        public Assignment_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterAssignment_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitAssignment_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitAssignment_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Assignment_listContext assignment_list() throws RecognitionException {
        Assignment_listContext _localctx = new Assignment_listContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_assignment_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(300);
                assignment();
                setState(305);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(301);
                            match(COMMA);
                            setState(302);
                            assignment();
                        }
                    }
                    setState(307);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentContext extends ParserRuleContext {

        public Token field;
        public ExprContext value;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public AssignmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterAssignment(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitAssignment(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitAssignment(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final AssignmentContext assignment() throws RecognitionException {
        AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_assignment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(308);
                _localctx.field = match(ID);
                setState(309);
                match(COLON);
                setState(310);
                _localctx.value = expr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprContext extends ParserRuleContext {

        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        public ExprContext() {
        }

        public void copyFrom(ExprContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class Cast_expressionContext extends ExprContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Cast_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterCast_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitCast_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitCast_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Qualified_id_expressionContext extends ExprContext {

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Qualified_id_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterQualified_id_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitQualified_id_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitQualified_id_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Division_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Division_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterDivision_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitDivision_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitDivision_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Subtraction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Subtraction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterSubtraction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitSubtraction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitSubtraction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Extc_one_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Extc_one_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterExtc_one_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitExtc_one_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitExtc_one_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Plus_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Plus_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterPlus_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitPlus_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitPlus_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Function_expressionContext extends ExprContext {

        public Qualified_idContext object;
        public Token function_name;

        public TerminalNode ID() {
            return getToken(oRatioParser.ID, 0);
        }

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Qualified_idContext qualified_id() {
            return getRuleContext(Qualified_idContext.class, 0);
        }

        public Function_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterFunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitFunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitFunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Addition_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Addition_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterAddition_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitAddition_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitAddition_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Parentheses_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Parentheses_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterParentheses_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitParentheses_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitParentheses_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Minus_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Minus_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterMinus_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitMinus_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitMinus_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Implication_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Implication_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterImplication_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitImplication_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitImplication_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Lt_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Lt_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterLt_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitLt_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitLt_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Not_expressionContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Not_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterNot_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitNot_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitNot_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Conjunction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Conjunction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterConjunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitConjunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitConjunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Geq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Geq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterGeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitGeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitGeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Range_expressionContext extends ExprContext {

        public ExprContext min;
        public ExprContext max;

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Range_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterRange_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitRange_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitRange_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Multiplication_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Multiplication_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterMultiplication_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitMultiplication_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitMultiplication_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Leq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Leq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterLeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitLeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitLeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Gt_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Gt_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterGt_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitGt_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitGt_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Constructor_expressionContext extends ExprContext {

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Expr_listContext expr_list() {
            return getRuleContext(Expr_listContext.class, 0);
        }

        public Constructor_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterConstructor_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitConstructor_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitConstructor_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Disjunction_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Disjunction_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterDisjunction_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitDisjunction_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitDisjunction_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Literal_expressionContext extends ExprContext {

        public LiteralContext literal() {
            return getRuleContext(LiteralContext.class, 0);
        }

        public Literal_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterLiteral_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitLiteral_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitLiteral_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Eq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Eq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterEq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitEq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitEq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class Neq_expressionContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Neq_expressionContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterNeq_expression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitNeq_expression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitNeq_expression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 48;
        enterRecursionRule(_localctx, 48, RULE_expr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(355);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 33, _ctx)) {
                    case 1: {
                        _localctx = new Literal_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;

                        setState(313);
                        literal();
                    }
                    break;
                    case 2: {
                        _localctx = new Parentheses_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(314);
                        match(LPAREN);
                        setState(315);
                        expr(0);
                        setState(316);
                        match(RPAREN);
                    }
                    break;
                    case 3: {
                        _localctx = new Plus_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(318);
                        match(PLUS);
                        setState(319);
                        expr(18);
                    }
                    break;
                    case 4: {
                        _localctx = new Minus_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(320);
                        match(MINUS);
                        setState(321);
                        expr(17);
                    }
                    break;
                    case 5: {
                        _localctx = new Not_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(322);
                        match(BANG);
                        setState(323);
                        expr(16);
                    }
                    break;
                    case 6: {
                        _localctx = new Qualified_id_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(324);
                        qualified_id();
                    }
                    break;
                    case 7: {
                        _localctx = new Function_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(328);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
                            case 1: {
                                setState(325);
                                ((Function_expressionContext) _localctx).object = qualified_id();
                                setState(326);
                                match(DOT);
                            }
                            break;
                        }
                        setState(330);
                        ((Function_expressionContext) _localctx).function_name = match(ID);
                        setState(331);
                        match(LPAREN);
                        setState(333);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(332);
                                expr_list();
                            }
                        }

                        setState(335);
                        match(RPAREN);
                    }
                    break;
                    case 8: {
                        _localctx = new Cast_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(336);
                        match(LPAREN);
                        setState(337);
                        type();
                        setState(338);
                        match(RPAREN);
                        setState(339);
                        expr(13);
                    }
                    break;
                    case 9: {
                        _localctx = new Range_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(341);
                        match(LBRACKET);
                        setState(342);
                        ((Range_expressionContext) _localctx).min = expr(0);
                        setState(343);
                        match(COMMA);
                        setState(344);
                        ((Range_expressionContext) _localctx).max = expr(0);
                        setState(345);
                        match(RBRACKET);
                    }
                    break;
                    case 10: {
                        _localctx = new Constructor_expressionContext(_localctx);
                        _ctx = _localctx;
                        _prevctx = _localctx;
                        setState(347);
                        match(NEW);
                        setState(348);
                        type();
                        setState(349);
                        match(LPAREN);
                        setState(351);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEW) | (1L << THIS) | (1L << TRUE) | (1L << FALSE) | (1L << LPAREN) | (1L << LBRACKET) | (1L << PLUS) | (1L << MINUS) | (1L << BANG) | (1L << ID) | (1L << NumericLiteral) | (1L << StringLiteral))) != 0)) {
                            {
                                setState(350);
                                expr_list();
                            }
                        }

                        setState(353);
                        match(RPAREN);
                    }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(425);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(423);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 40, _ctx)) {
                                case 1: {
                                    _localctx = new Division_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(357);
                                    if (!(precpred(_ctx, 21))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 21)");
                                    }
                                    setState(358);
                                    match(SLASH);
                                    setState(359);
                                    expr(22);
                                }
                                break;
                                case 2: {
                                    _localctx = new Eq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(360);
                                    if (!(precpred(_ctx, 10))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 10)");
                                    }
                                    setState(361);
                                    match(EQEQ);
                                    setState(362);
                                    expr(11);
                                }
                                break;
                                case 3: {
                                    _localctx = new Geq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(363);
                                    if (!(precpred(_ctx, 9))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    }
                                    setState(364);
                                    match(GTEQ);
                                    setState(365);
                                    expr(10);
                                }
                                break;
                                case 4: {
                                    _localctx = new Leq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(366);
                                    if (!(precpred(_ctx, 8))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    }
                                    setState(367);
                                    match(LTEQ);
                                    setState(368);
                                    expr(9);
                                }
                                break;
                                case 5: {
                                    _localctx = new Gt_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(369);
                                    if (!(precpred(_ctx, 7))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    }
                                    setState(370);
                                    match(GT);
                                    setState(371);
                                    expr(8);
                                }
                                break;
                                case 6: {
                                    _localctx = new Lt_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(372);
                                    if (!(precpred(_ctx, 6))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    }
                                    setState(373);
                                    match(LT);
                                    setState(374);
                                    expr(7);
                                }
                                break;
                                case 7: {
                                    _localctx = new Neq_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(375);
                                    if (!(precpred(_ctx, 5))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    }
                                    setState(376);
                                    match(BANGEQ);
                                    setState(377);
                                    expr(6);
                                }
                                break;
                                case 8: {
                                    _localctx = new Implication_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(378);
                                    if (!(precpred(_ctx, 4))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    }
                                    setState(379);
                                    match(IMPLICATION);
                                    setState(380);
                                    expr(5);
                                }
                                break;
                                case 9: {
                                    _localctx = new Multiplication_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(381);
                                    if (!(precpred(_ctx, 22))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 22)");
                                    }
                                    setState(384);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(382);
                                                    match(STAR);
                                                    setState(383);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(386);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 34, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 10: {
                                    _localctx = new Addition_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(388);
                                    if (!(precpred(_ctx, 20))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 20)");
                                    }
                                    setState(391);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(389);
                                                    match(PLUS);
                                                    setState(390);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(393);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 35, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 11: {
                                    _localctx = new Subtraction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(395);
                                    if (!(precpred(_ctx, 19))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 19)");
                                    }
                                    setState(398);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(396);
                                                    match(MINUS);
                                                    setState(397);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(400);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 36, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 12: {
                                    _localctx = new Disjunction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(402);
                                    if (!(precpred(_ctx, 3))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    }
                                    setState(405);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(403);
                                                    match(BAR);
                                                    setState(404);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(407);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 13: {
                                    _localctx = new Conjunction_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(409);
                                    if (!(precpred(_ctx, 2))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    }
                                    setState(412);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(410);
                                                    match(AMP);
                                                    setState(411);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(414);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 38, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                                case 14: {
                                    _localctx = new Extc_one_expressionContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(416);
                                    if (!(precpred(_ctx, 1))) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    }
                                    setState(419);
                                    _errHandler.sync(this);
                                    _alt = 1;
                                    do {
                                        switch (_alt) {
                                            case 1: {
                                                {
                                                    setState(417);
                                                    match(CARET);
                                                    setState(418);
                                                    expr(0);
                                                }
                                            }
                                            break;
                                            default:
                                                throw new NoViableAltException(this);
                                        }
                                        setState(421);
                                        _errHandler.sync(this);
                                        _alt = getInterpreter().adaptivePredict(_input, 39, _ctx);
                                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                                }
                                break;
                            }
                        }
                    }
                    setState(427);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class Expr_listContext extends ParserRuleContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public Expr_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterExpr_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitExpr_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitExpr_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Expr_listContext expr_list() throws RecognitionException {
        Expr_listContext _localctx = new Expr_listContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_expr_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(428);
                expr(0);
                setState(433);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(429);
                            match(COMMA);
                            setState(430);
                            expr(0);
                        }
                    }
                    setState(435);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LiteralContext extends ParserRuleContext {

        public Token numeric;
        public Token string;
        public Token t;
        public Token f;

        public TerminalNode NumericLiteral() {
            return getToken(oRatioParser.NumericLiteral, 0);
        }

        public TerminalNode StringLiteral() {
            return getToken(oRatioParser.StringLiteral, 0);
        }

        public LiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_literal;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterLiteral(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitLiteral(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitLiteral(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final LiteralContext literal() throws RecognitionException {
        LiteralContext _localctx = new LiteralContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_literal);
        try {
            setState(440);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NumericLiteral:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(436);
                        _localctx.numeric = match(NumericLiteral);
                    }
                    break;
                case StringLiteral:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(437);
                        _localctx.string = match(StringLiteral);
                    }
                    break;
                case TRUE:
                    enterOuterAlt(_localctx, 3);
                     {
                        setState(438);
                        _localctx.t = match(TRUE);
                    }
                    break;
                case FALSE:
                    enterOuterAlt(_localctx, 4);
                     {
                        setState(439);
                        _localctx.f = match(FALSE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Qualified_idContext extends ParserRuleContext {

        public Token t;

        public List<TerminalNode> ID() {
            return getTokens(oRatioParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(oRatioParser.ID, i);
        }

        public Qualified_idContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_qualified_id;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterQualified_id(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitQualified_id(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitQualified_id(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Qualified_idContext qualified_id() throws RecognitionException {
        Qualified_idContext _localctx = new Qualified_idContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_qualified_id);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(444);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case THIS: {
                        setState(442);
                        _localctx.t = match(THIS);
                    }
                    break;
                    case ID: {
                        setState(443);
                        match(ID);
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(450);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 45, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(446);
                                match(DOT);
                                setState(447);
                                match(ID);
                            }
                        }
                    }
                    setState(452);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 45, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeContext extends ParserRuleContext {

        public Class_typeContext class_type() {
            return getRuleContext(Class_typeContext.class, 0);
        }

        public Primitive_typeContext primitive_type() {
            return getRuleContext(Primitive_typeContext.class, 0);
        }

        public TypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterType(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitType(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitType(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final TypeContext type() throws RecognitionException {
        TypeContext _localctx = new TypeContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_type);
        try {
            setState(455);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case ID:
                    enterOuterAlt(_localctx, 1);
                     {
                        setState(453);
                        class_type();
                    }
                    break;
                case REAL:
                case BOOL:
                case STRING:
                    enterOuterAlt(_localctx, 2);
                     {
                        setState(454);
                        primitive_type();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Class_typeContext extends ParserRuleContext {

        public List<TerminalNode> ID() {
            return getTokens(oRatioParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(oRatioParser.ID, i);
        }

        public Class_typeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_class_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterClass_type(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitClass_type(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitClass_type(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Class_typeContext class_type() throws RecognitionException {
        Class_typeContext _localctx = new Class_typeContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_class_type);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(457);
                match(ID);
                setState(462);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == DOT) {
                    {
                        {
                            setState(458);
                            match(DOT);
                            setState(459);
                            match(ID);
                        }
                    }
                    setState(464);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Primitive_typeContext extends ParserRuleContext {

        public Primitive_typeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_primitive_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterPrimitive_type(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitPrimitive_type(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitPrimitive_type(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Primitive_typeContext primitive_type() throws RecognitionException {
        Primitive_typeContext _localctx = new Primitive_typeContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_primitive_type);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(465);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REAL) | (1L << BOOL) | (1L << STRING))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) {
                        matchedEOF = true;
                    }
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_listContext extends ParserRuleContext {

        public List<TypeContext> type() {
            return getRuleContexts(TypeContext.class);
        }

        public TypeContext type(int i) {
            return getRuleContext(TypeContext.class, i);
        }

        public Type_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterType_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitType_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitType_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Type_listContext type_list() throws RecognitionException {
        Type_listContext _localctx = new Type_listContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_type_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(467);
                type();
                setState(472);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(468);
                            match(COMMA);
                            setState(469);
                            type();
                        }
                    }
                    setState(474);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Typed_listContext extends ParserRuleContext {

        public List<TypeContext> type() {
            return getRuleContexts(TypeContext.class);
        }

        public TypeContext type(int i) {
            return getRuleContext(TypeContext.class, i);
        }

        public List<TerminalNode> ID() {
            return getTokens(oRatioParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(oRatioParser.ID, i);
        }

        public Typed_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_typed_list;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).enterTyped_list(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof oRatioListener) {
                ((oRatioListener) listener).exitTyped_list(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof oRatioVisitor) {
                return ((oRatioVisitor<? extends T>) visitor).visitTyped_list(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final Typed_listContext typed_list() throws RecognitionException {
        Typed_listContext _localctx = new Typed_listContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_typed_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(475);
                type();
                setState(476);
                match(ID);
                setState(483);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(477);
                            match(COMMA);
                            setState(478);
                            type();
                            setState(479);
                            match(ID);
                        }
                    }
                    setState(485);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 24:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 21);
            case 1:
                return precpred(_ctx, 10);
            case 2:
                return precpred(_ctx, 9);
            case 3:
                return precpred(_ctx, 8);
            case 4:
                return precpred(_ctx, 7);
            case 5:
                return precpred(_ctx, 6);
            case 6:
                return precpred(_ctx, 5);
            case 7:
                return precpred(_ctx, 4);
            case 8:
                return precpred(_ctx, 22);
            case 9:
                return precpred(_ctx, 20);
            case 10:
                return precpred(_ctx, 19);
            case 11:
                return precpred(_ctx, 3);
            case 12:
                return precpred(_ctx, 2);
            case 13:
                return precpred(_ctx, 1);
        }
        return true;
    }
    public static final String _serializedATN
            = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\62\u01e9\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
            + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
            + "\t!\4\"\t\"\3\2\3\2\3\2\3\2\7\2I\n\2\f\2\16\2L\13\2\3\2\3\2\3\3\3\3\3"
            + "\3\5\3S\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\7\5`\n\5\f\5\16"
            + "\5c\13\5\3\5\3\5\3\6\3\6\3\6\3\6\7\6k\n\6\f\6\16\6n\13\6\3\6\3\6\5\6r"
            + "\n\6\3\7\3\7\3\7\3\7\5\7x\n\7\3\7\3\7\7\7|\n\7\f\7\16\7\177\13\7\3\7\3"
            + "\7\3\b\3\b\3\b\3\b\3\b\5\b\u0088\n\b\3\t\3\t\3\t\3\t\7\t\u008e\n\t\f\t"
            + "\16\t\u0091\13\t\3\t\3\t\3\n\3\n\3\n\5\n\u0098\n\n\3\13\3\13\3\13\3\13"
            + "\5\13\u009e\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00a9"
            + "\n\13\3\13\3\13\3\13\3\13\3\13\5\13\u00b0\n\13\3\f\3\f\3\f\5\f\u00b5\n"
            + "\f\3\f\3\f\3\f\3\f\3\f\7\f\u00bc\n\f\f\f\16\f\u00bf\13\f\5\f\u00c1\n\f"
            + "\3\f\3\f\3\f\3\f\3\r\3\r\3\r\5\r\u00ca\n\r\3\r\3\r\3\16\3\16\3\16\3\16"
            + "\5\16\u00d2\n\16\3\16\3\16\3\16\5\16\u00d7\n\16\3\16\3\16\3\16\3\16\3"
            + "\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e7\n\17\3\20"
            + "\7\20\u00ea\n\20\f\20\16\20\u00ed\13\20\3\21\3\21\3\21\5\21\u00f2\n\21"
            + "\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\7\22\u00fd\n\22\f\22\16"
            + "\22\u0100\13\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\6\24\u010a\n\24"
            + "\r\24\16\24\u010b\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0115\n\25\3"
            + "\26\3\26\5\26\u0119\n\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u0121\n\26"
            + "\3\26\3\26\3\26\5\26\u0126\n\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30"
            + "\3\30\3\30\7\30\u0132\n\30\f\30\16\30\u0135\13\30\3\31\3\31\3\31\3\31"
            + "\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"
            + "\3\32\3\32\5\32\u014b\n\32\3\32\3\32\3\32\5\32\u0150\n\32\3\32\3\32\3"
            + "\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5"
            + "\32\u0162\n\32\3\32\3\32\5\32\u0166\n\32\3\32\3\32\3\32\3\32\3\32\3\32"
            + "\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"
            + "\3\32\3\32\3\32\3\32\3\32\3\32\3\32\6\32\u0183\n\32\r\32\16\32\u0184\3"
            + "\32\3\32\3\32\6\32\u018a\n\32\r\32\16\32\u018b\3\32\3\32\3\32\6\32\u0191"
            + "\n\32\r\32\16\32\u0192\3\32\3\32\3\32\6\32\u0198\n\32\r\32\16\32\u0199"
            + "\3\32\3\32\3\32\6\32\u019f\n\32\r\32\16\32\u01a0\3\32\3\32\3\32\6\32\u01a6"
            + "\n\32\r\32\16\32\u01a7\7\32\u01aa\n\32\f\32\16\32\u01ad\13\32\3\33\3\33"
            + "\3\33\7\33\u01b2\n\33\f\33\16\33\u01b5\13\33\3\34\3\34\3\34\3\34\5\34"
            + "\u01bb\n\34\3\35\3\35\5\35\u01bf\n\35\3\35\3\35\7\35\u01c3\n\35\f\35\16"
            + "\35\u01c6\13\35\3\36\3\36\5\36\u01ca\n\36\3\37\3\37\3\37\7\37\u01cf\n"
            + "\37\f\37\16\37\u01d2\13\37\3 \3 \3!\3!\3!\7!\u01d9\n!\f!\16!\u01dc\13"
            + "!\3\"\3\"\3\"\3\"\3\"\3\"\7\"\u01e4\n\"\f\"\16\"\u01e7\13\"\3\"\2\3\62"
            + "#\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@B\2"
            + "\3\3\2\4\6\u021a\2J\3\2\2\2\4R\3\2\2\2\6T\3\2\2\2\bZ\3\2\2\2\nq\3\2\2"
            + "\2\fs\3\2\2\2\16\u0087\3\2\2\2\20\u0089\3\2\2\2\22\u0094\3\2\2\2\24\u00af"
            + "\3\2\2\2\26\u00b1\3\2\2\2\30\u00c6\3\2\2\2\32\u00cd\3\2\2\2\34\u00e6\3"
            + "\2\2\2\36\u00eb\3\2\2\2 \u00f1\3\2\2\2\"\u00f8\3\2\2\2$\u0103\3\2\2\2"
            + "&\u0106\3\2\2\2(\u010d\3\2\2\2*\u0118\3\2\2\2,\u012a\3\2\2\2.\u012e\3"
            + "\2\2\2\60\u0136\3\2\2\2\62\u0165\3\2\2\2\64\u01ae\3\2\2\2\66\u01ba\3\2"
            + "\2\28\u01be\3\2\2\2:\u01c9\3\2\2\2<\u01cb\3\2\2\2>\u01d3\3\2\2\2@\u01d5"
            + "\3\2\2\2B\u01dd\3\2\2\2DI\5\4\3\2EI\5\24\13\2FI\5\32\16\2GI\5\34\17\2"
            + "HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2"
            + "KM\3\2\2\2LJ\3\2\2\2MN\7\2\2\3N\3\3\2\2\2OS\5\6\4\2PS\5\b\5\2QS\5\f\7"
            + "\2RO\3\2\2\2RP\3\2\2\2RQ\3\2\2\2S\5\3\2\2\2TU\7\3\2\2UV\5> \2VW\5\62\32"
            + "\2WX\7-\2\2XY\7\26\2\2Y\7\3\2\2\2Z[\7\7\2\2[\\\7-\2\2\\a\5\n\6\2]^\7\""
            + "\2\2^`\5\n\6\2_]\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3\2\2\2ca\3\2"
            + "\2\2de\7\26\2\2e\t\3\2\2\2fg\7\33\2\2gl\7/\2\2hi\7\24\2\2ik\7/\2\2jh\3"
            + "\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2mo\3\2\2\2nl\3\2\2\2or\7\34\2\2pr"
            + "\5:\36\2qf\3\2\2\2qp\3\2\2\2r\13\3\2\2\2st\7\b\2\2tw\7-\2\2uv\7\25\2\2"
            + "vx\5@!\2wu\3\2\2\2wx\3\2\2\2xy\3\2\2\2y}\7\33\2\2z|\5\16\b\2{z\3\2\2\2"
            + "|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0080\3\2\2\2\177}\3\2\2\2\u0080\u0081"
            + "\7\34\2\2\u0081\r\3\2\2\2\u0082\u0088\5\20\t\2\u0083\u0088\5\24\13\2\u0084"
            + "\u0088\5\26\f\2\u0085\u0088\5\32\16\2\u0086\u0088\5\4\3\2\u0087\u0082"
            + "\3\2\2\2\u0087\u0083\3\2\2\2\u0087\u0084\3\2\2\2\u0087\u0085\3\2\2\2\u0087"
            + "\u0086\3\2\2\2\u0088\17\3\2\2\2\u0089\u008a\5:\36\2\u008a\u008f\5\22\n"
            + "\2\u008b\u008c\7\24\2\2\u008c\u008e\5\22\n\2\u008d\u008b\3\2\2\2\u008e"
            + "\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\3\2"
            + "\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7\26\2\2\u0093\21\3\2\2\2\u0094\u0097"
            + "\7-\2\2\u0095\u0096\7#\2\2\u0096\u0098\5\62\32\2\u0097\u0095\3\2\2\2\u0097"
            + "\u0098\3\2\2\2\u0098\23\3\2\2\2\u0099\u009a\7\17\2\2\u009a\u009b\7-\2"
            + "\2\u009b\u009d\7\27\2\2\u009c\u009e\5B\"\2\u009d\u009c\3\2\2\2\u009d\u009e"
            + "\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7\30\2\2\u00a0\u00a1\7\33\2\2"
            + "\u00a1\u00a2\5\36\20\2\u00a2\u00a3\7\34\2\2\u00a3\u00b0\3\2\2\2\u00a4"
            + "\u00a5\5:\36\2\u00a5\u00a6\7-\2\2\u00a6\u00a8\7\27\2\2\u00a7\u00a9\5B"
            + "\"\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa"
            + "\u00ab\7\30\2\2\u00ab\u00ac\7\33\2\2\u00ac\u00ad\5\36\20\2\u00ad\u00ae"
            + "\7\34\2\2\u00ae\u00b0\3\2\2\2\u00af\u0099\3\2\2\2\u00af\u00a4\3\2\2\2"
            + "\u00b0\25\3\2\2\2\u00b1\u00b2\7-\2\2\u00b2\u00b4\7\27\2\2\u00b3\u00b5"
            + "\5B\"\2\u00b4\u00b3\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6"
            + "\u00c0\7\30\2\2\u00b7\u00b8\7\25\2\2\u00b8\u00bd\5\30\r\2\u00b9\u00ba"
            + "\7\24\2\2\u00ba\u00bc\5\30\r\2\u00bb\u00b9\3\2\2\2\u00bc\u00bf\3\2\2\2"
            + "\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd"
            + "\3\2\2\2\u00c0\u00b7\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2"
            + "\u00c3\7\33\2\2\u00c3\u00c4\5\36\20\2\u00c4\u00c5\7\34\2\2\u00c5\27\3"
            + "\2\2\2\u00c6\u00c7\7-\2\2\u00c7\u00c9\7\27\2\2\u00c8\u00ca\5\64\33\2\u00c9"
            + "\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\7\30"
            + "\2\2\u00cc\31\3\2\2\2\u00cd\u00ce\7\13\2\2\u00ce\u00cf\7-\2\2\u00cf\u00d1"
            + "\7\27\2\2\u00d0\u00d2\5B\"\2\u00d1\u00d0\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2"
            + "\u00d3\3\2\2\2\u00d3\u00d6\7\30\2\2\u00d4\u00d5\7\25\2\2\u00d5\u00d7\5"
            + "@!\2\u00d6\u00d4\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"
            + "\u00d9\7\33\2\2\u00d9\u00da\5\36\20\2\u00da\u00db\7\34\2\2\u00db\33\3"
            + "\2\2\2\u00dc\u00e7\5 \21\2\u00dd\u00e7\5\"\22\2\u00de\u00e7\5$\23\2\u00df"
            + "\u00e7\5&\24\2\u00e0\u00e7\5*\26\2\u00e1\u00e7\5,\27\2\u00e2\u00e3\7\33"
            + "\2\2\u00e3\u00e4\5\36\20\2\u00e4\u00e5\7\34\2\2\u00e5\u00e7\3\2\2\2\u00e6"
            + "\u00dc\3\2\2\2\u00e6\u00dd\3\2\2\2\u00e6\u00de\3\2\2\2\u00e6\u00df\3\2"
            + "\2\2\u00e6\u00e0\3\2\2\2\u00e6\u00e1\3\2\2\2\u00e6\u00e2\3\2\2\2\u00e7"
            + "\35\3\2\2\2\u00e8\u00ea\5\34\17\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3\2"
            + "\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\37\3\2\2\2\u00ed\u00eb"
            + "\3\2\2\2\u00ee\u00ef\58\35\2\u00ef\u00f0\7\23\2\2\u00f0\u00f2\3\2\2\2"
            + "\u00f1\u00ee\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4"
            + "\7-\2\2\u00f4\u00f5\7#\2\2\u00f5\u00f6\5\62\32\2\u00f6\u00f7\7\26\2\2"
            + "\u00f7!\3\2\2\2\u00f8\u00f9\5:\36\2\u00f9\u00fe\5\22\n\2\u00fa\u00fb\7"
            + "\24\2\2\u00fb\u00fd\5\22\n\2\u00fc\u00fa\3\2\2\2\u00fd\u0100\3\2\2\2\u00fe"
            + "\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0101\3\2\2\2\u0100\u00fe\3\2"
            + "\2\2\u0101\u0102\7\26\2\2\u0102#\3\2\2\2\u0103\u0104\5\62\32\2\u0104\u0105"
            + "\7\26\2\2\u0105%\3\2\2\2\u0106\u0109\5(\25\2\u0107\u0108\7\r\2\2\u0108"
            + "\u010a\5(\25\2\u0109\u0107\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u0109\3\2"
            + "\2\2\u010b\u010c\3\2\2\2\u010c\'\3\2\2\2\u010d\u010e\7\33\2\2\u010e\u010f"
            + "\5\36\20\2\u010f\u0114\7\34\2\2\u0110\u0111\7\31\2\2\u0111\u0112\5\62"
            + "\32\2\u0112\u0113\7\32\2\2\u0113\u0115\3\2\2\2\u0114\u0110\3\2\2\2\u0114"
            + "\u0115\3\2\2\2\u0115)\3\2\2\2\u0116\u0119\7\t\2\2\u0117\u0119\7\n\2\2"
            + "\u0118\u0116\3\2\2\2\u0118\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b"
            + "\7-\2\2\u011b\u011c\7#\2\2\u011c\u0120\7\f\2\2\u011d\u011e\58\35\2\u011e"
            + "\u011f\7\23\2\2\u011f\u0121\3\2\2\2\u0120\u011d\3\2\2\2\u0120\u0121\3"
            + "\2\2\2\u0121\u0122\3\2\2\2\u0122\u0123\7-\2\2\u0123\u0125\7\27\2\2\u0124"
            + "\u0126\5.\30\2\u0125\u0124\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0127\3\2"
            + "\2\2\u0127\u0128\7\30\2\2\u0128\u0129\7\26\2\2\u0129+\3\2\2\2\u012a\u012b"
            + "\7\22\2\2\u012b\u012c\5\62\32\2\u012c\u012d\7\26\2\2\u012d-\3\2\2\2\u012e"
            + "\u0133\5\60\31\2\u012f\u0130\7\24\2\2\u0130\u0132\5\60\31\2\u0131\u012f"
            + "\3\2\2\2\u0132\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134"
            + "/\3\2\2\2\u0135\u0133\3\2\2\2\u0136\u0137\7-\2\2\u0137\u0138\7\25\2\2"
            + "\u0138\u0139\5\62\32\2\u0139\61\3\2\2\2\u013a\u013b\b\32\1\2\u013b\u0166"
            + "\5\66\34\2\u013c\u013d\7\27\2\2\u013d\u013e\5\62\32\2\u013e\u013f\7\30"
            + "\2\2\u013f\u0166\3\2\2\2\u0140\u0141\7\35\2\2\u0141\u0166\5\62\32\24\u0142"
            + "\u0143\7\36\2\2\u0143\u0166\5\62\32\23\u0144\u0145\7&\2\2\u0145\u0166"
            + "\5\62\32\22\u0146\u0166\58\35\2\u0147\u0148\58\35\2\u0148\u0149\7\23\2"
            + "\2\u0149\u014b\3\2\2\2\u014a\u0147\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c"
            + "\3\2\2\2\u014c\u014d\7-\2\2\u014d\u014f\7\27\2\2\u014e\u0150\5\64\33\2"
            + "\u014f\u014e\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0166"
            + "\7\30\2\2\u0152\u0153\7\27\2\2\u0153\u0154\5:\36\2\u0154\u0155\7\30\2"
            + "\2\u0155\u0156\5\62\32\17\u0156\u0166\3\2\2\2\u0157\u0158\7\31\2\2\u0158"
            + "\u0159\5\62\32\2\u0159\u015a\7\24\2\2\u015a\u015b\5\62\32\2\u015b\u015c"
            + "\7\32\2\2\u015c\u0166\3\2\2\2\u015d\u015e\7\f\2\2\u015e\u015f\5:\36\2"
            + "\u015f\u0161\7\27\2\2\u0160\u0162\5\64\33\2\u0161\u0160\3\2\2\2\u0161"
            + "\u0162\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0164\7\30\2\2\u0164\u0166\3"
            + "\2\2\2\u0165\u013a\3\2\2\2\u0165\u013c\3\2\2\2\u0165\u0140\3\2\2\2\u0165"
            + "\u0142\3\2\2\2\u0165\u0144\3\2\2\2\u0165\u0146\3\2\2\2\u0165\u014a\3\2"
            + "\2\2\u0165\u0152\3\2\2\2\u0165\u0157\3\2\2\2\u0165\u015d\3\2\2\2\u0166"
            + "\u01ab\3\2\2\2\u0167\u0168\f\27\2\2\u0168\u0169\7 \2\2\u0169\u01aa\5\62"
            + "\32\30\u016a\u016b\f\f\2\2\u016b\u016c\7\'\2\2\u016c\u01aa\5\62\32\r\u016d"
            + "\u016e\f\13\2\2\u016e\u016f\7)\2\2\u016f\u01aa\5\62\32\f\u0170\u0171\f"
            + "\n\2\2\u0171\u0172\7(\2\2\u0172\u01aa\5\62\32\13\u0173\u0174\f\t\2\2\u0174"
            + "\u0175\7$\2\2\u0175\u01aa\5\62\32\n\u0176\u0177\f\b\2\2\u0177\u0178\7"
            + "%\2\2\u0178\u01aa\5\62\32\t\u0179\u017a\f\7\2\2\u017a\u017b\7*\2\2\u017b"
            + "\u01aa\5\62\32\b\u017c\u017d\f\6\2\2\u017d\u017e\7+\2\2\u017e\u01aa\5"
            + "\62\32\7\u017f\u0182\f\30\2\2\u0180\u0181\7\37\2\2\u0181\u0183\5\62\32"
            + "\2\u0182\u0180\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0182\3\2\2\2\u0184\u0185"
            + "\3\2\2\2\u0185\u01aa\3\2\2\2\u0186\u0189\f\26\2\2\u0187\u0188\7\35\2\2"
            + "\u0188\u018a\5\62\32\2\u0189\u0187\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u0189"
            + "\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u01aa\3\2\2\2\u018d\u0190\f\25\2\2"
            + "\u018e\u018f\7\36\2\2\u018f\u0191\5\62\32\2\u0190\u018e\3\2\2\2\u0191"
            + "\u0192\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u01aa\3\2"
            + "\2\2\u0194\u0197\f\5\2\2\u0195\u0196\7\"\2\2\u0196\u0198\5\62\32\2\u0197"
            + "\u0195\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u0197\3\2\2\2\u0199\u019a\3\2"
            + "\2\2\u019a\u01aa\3\2\2\2\u019b\u019e\f\4\2\2\u019c\u019d\7!\2\2\u019d"
            + "\u019f\5\62\32\2\u019e\u019c\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u019e\3"
            + "\2\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01aa\3\2\2\2\u01a2\u01a5\f\3\2\2\u01a3"
            + "\u01a4\7,\2\2\u01a4\u01a6\5\62\32\2\u01a5\u01a3\3\2\2\2\u01a6\u01a7\3"
            + "\2\2\2\u01a7\u01a5\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01aa\3\2\2\2\u01a9"
            + "\u0167\3\2\2\2\u01a9\u016a\3\2\2\2\u01a9\u016d\3\2\2\2\u01a9\u0170\3\2"
            + "\2\2\u01a9\u0173\3\2\2\2\u01a9\u0176\3\2\2\2\u01a9\u0179\3\2\2\2\u01a9"
            + "\u017c\3\2\2\2\u01a9\u017f\3\2\2\2\u01a9\u0186\3\2\2\2\u01a9\u018d\3\2"
            + "\2\2\u01a9\u0194\3\2\2\2\u01a9\u019b\3\2\2\2\u01a9\u01a2\3\2\2\2\u01aa"
            + "\u01ad\3\2\2\2\u01ab\u01a9\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\63\3\2\2"
            + "\2\u01ad\u01ab\3\2\2\2\u01ae\u01b3\5\62\32\2\u01af\u01b0\7\24\2\2\u01b0"
            + "\u01b2\5\62\32\2\u01b1\u01af\3\2\2\2\u01b2\u01b5\3\2\2\2\u01b3\u01b1\3"
            + "\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\65\3\2\2\2\u01b5\u01b3\3\2\2\2\u01b6"
            + "\u01bb\7.\2\2\u01b7\u01bb\7/\2\2\u01b8\u01bb\7\20\2\2\u01b9\u01bb\7\21"
            + "\2\2\u01ba\u01b6\3\2\2\2\u01ba\u01b7\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba"
            + "\u01b9\3\2\2\2\u01bb\67\3\2\2\2\u01bc\u01bf\7\16\2\2\u01bd\u01bf\7-\2"
            + "\2\u01be\u01bc\3\2\2\2\u01be\u01bd\3\2\2\2\u01bf\u01c4\3\2\2\2\u01c0\u01c1"
            + "\7\23\2\2\u01c1\u01c3\7-\2\2\u01c2\u01c0\3\2\2\2\u01c3\u01c6\3\2\2\2\u01c4"
            + "\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c59\3\2\2\2\u01c6\u01c4\3\2\2\2"
            + "\u01c7\u01ca\5<\37\2\u01c8\u01ca\5> \2\u01c9\u01c7\3\2\2\2\u01c9\u01c8"
            + "\3\2\2\2\u01ca;\3\2\2\2\u01cb\u01d0\7-\2\2\u01cc\u01cd\7\23\2\2\u01cd"
            + "\u01cf\7-\2\2\u01ce\u01cc\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01ce\3\2"
            + "\2\2\u01d0\u01d1\3\2\2\2\u01d1=\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d3\u01d4"
            + "\t\2\2\2\u01d4?\3\2\2\2\u01d5\u01da\5:\36\2\u01d6\u01d7\7\24\2\2\u01d7"
            + "\u01d9\5:\36\2\u01d8\u01d6\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2"
            + "\2\2\u01da\u01db\3\2\2\2\u01dbA\3\2\2\2\u01dc\u01da\3\2\2\2\u01dd\u01de"
            + "\5:\36\2\u01de\u01e5\7-\2\2\u01df\u01e0\7\24\2\2\u01e0\u01e1\5:\36\2\u01e1"
            + "\u01e2\7-\2\2\u01e2\u01e4\3\2\2\2\u01e3\u01df\3\2\2\2\u01e4\u01e7\3\2"
            + "\2\2\u01e5\u01e3\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6C\3\2\2\2\u01e7\u01e5"
            + "\3\2\2\2\64HJRalqw}\u0087\u008f\u0097\u009d\u00a8\u00af\u00b4\u00bd\u00c0"
            + "\u00c9\u00d1\u00d6\u00e6\u00eb\u00f1\u00fe\u010b\u0114\u0118\u0120\u0125"
            + "\u0133\u014a\u014f\u0161\u0165\u0184\u018b\u0192\u0199\u01a0\u01a7\u01a9"
            + "\u01ab\u01b3\u01ba\u01be\u01c4\u01c9\u01d0\u01da\u01e5";
    public static final ATN _ATN
            = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
