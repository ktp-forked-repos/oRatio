// Generated from oRatio.g4 by ANTLR 4.6
package it.cnr.istc.oratio.core.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class oRatioLexer extends Lexer {

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
    public static String[] modeNames = {
        "DEFAULT_MODE"
    };
    public static final String[] ruleNames = {
        "TYPE_DEF", "REAL", "BOOL", "STRING", "ENUM", "CLASS", "GOAL", "FACT",
        "PREDICATE", "NEW", "OR", "THIS", "VOID", "TRUE", "FALSE", "RETURN", "DOT",
        "COMMA", "COLON", "SEMICOLON", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET",
        "LBRACE", "RBRACE", "PLUS", "MINUS", "STAR", "SLASH", "AMP", "BAR", "EQUAL",
        "GT", "LT", "BANG", "EQEQ", "LTEQ", "GTEQ", "BANGEQ", "IMPLICATION", "CARET",
        "ID", "NumericLiteral", "StringLiteral", "ESC", "LINE_COMMENT", "COMMENT",
        "WS"
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

    public oRatioLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
    public static final String _serializedATN
            = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\62\u014a\b\1\4\2"
            + "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"
            + "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
            + "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"
            + "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"
            + " \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"
            + "+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\3\2\3\2\3\2\3"
            + "\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5"
            + "\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3"
            + "\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"
            + "\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"
            + "\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3"
            + "\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3"
            + "\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3"
            + "\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&"
            + "\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3,\3,\7,\u00fc\n,"
            + "\f,\16,\u00ff\13,\3-\6-\u0102\n-\r-\16-\u0103\3-\3-\6-\u0108\n-\r-\16"
            + "-\u0109\5-\u010c\n-\3-\3-\6-\u0110\n-\r-\16-\u0111\5-\u0114\n-\3.\3.\3"
            + ".\7.\u0119\n.\f.\16.\u011c\13.\3.\3.\3/\3/\3/\3/\5/\u0124\n/\3\60\3\60"
            + "\3\60\3\60\7\60\u012a\n\60\f\60\16\60\u012d\13\60\3\60\5\60\u0130\n\60"
            + "\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\7\61\u013a\n\61\f\61\16\61\u013d"
            + "\13\61\3\61\3\61\3\61\3\61\3\61\3\62\6\62\u0145\n\62\r\62\16\62\u0146"
            + "\3\62\3\62\5\u011a\u012b\u013b\2\63\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"
            + "\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"
            + "/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["
            + "/]\2_\60a\61c\62\3\2\6\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\5\2\13\f\16\17"
            + "\"\"\u0155\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"
            + "\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"
            + "\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"
            + "\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"
            + "\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"
            + "\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"
            + "\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"
            + "\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2_\3\2\2\2\2a\3\2"
            + "\2\2\2c\3\2\2\2\3e\3\2\2\2\5m\3\2\2\2\7r\3\2\2\2\tw\3\2\2\2\13~\3\2\2"
            + "\2\r\u0083\3\2\2\2\17\u0089\3\2\2\2\21\u008e\3\2\2\2\23\u0093\3\2\2\2"
            + "\25\u009d\3\2\2\2\27\u00a1\3\2\2\2\31\u00a4\3\2\2\2\33\u00a9\3\2\2\2\35"
            + "\u00ae\3\2\2\2\37\u00b3\3\2\2\2!\u00b9\3\2\2\2#\u00c0\3\2\2\2%\u00c2\3"
            + "\2\2\2\'\u00c4\3\2\2\2)\u00c6\3\2\2\2+\u00c8\3\2\2\2-\u00ca\3\2\2\2/\u00cc"
            + "\3\2\2\2\61\u00ce\3\2\2\2\63\u00d0\3\2\2\2\65\u00d2\3\2\2\2\67\u00d4\3"
            + "\2\2\29\u00d6\3\2\2\2;\u00d8\3\2\2\2=\u00da\3\2\2\2?\u00dc\3\2\2\2A\u00de"
            + "\3\2\2\2C\u00e0\3\2\2\2E\u00e2\3\2\2\2G\u00e4\3\2\2\2I\u00e6\3\2\2\2K"
            + "\u00e8\3\2\2\2M\u00eb\3\2\2\2O\u00ee\3\2\2\2Q\u00f1\3\2\2\2S\u00f4\3\2"
            + "\2\2U\u00f7\3\2\2\2W\u00f9\3\2\2\2Y\u0113\3\2\2\2[\u0115\3\2\2\2]\u0123"
            + "\3\2\2\2_\u0125\3\2\2\2a\u0135\3\2\2\2c\u0144\3\2\2\2ef\7v\2\2fg\7{\2"
            + "\2gh\7r\2\2hi\7g\2\2ij\7f\2\2jk\7g\2\2kl\7h\2\2l\4\3\2\2\2mn\7t\2\2no"
            + "\7g\2\2op\7c\2\2pq\7n\2\2q\6\3\2\2\2rs\7d\2\2st\7q\2\2tu\7q\2\2uv\7n\2"
            + "\2v\b\3\2\2\2wx\7u\2\2xy\7v\2\2yz\7t\2\2z{\7k\2\2{|\7p\2\2|}\7i\2\2}\n"
            + "\3\2\2\2~\177\7g\2\2\177\u0080\7p\2\2\u0080\u0081\7w\2\2\u0081\u0082\7"
            + "o\2\2\u0082\f\3\2\2\2\u0083\u0084\7e\2\2\u0084\u0085\7n\2\2\u0085\u0086"
            + "\7c\2\2\u0086\u0087\7u\2\2\u0087\u0088\7u\2\2\u0088\16\3\2\2\2\u0089\u008a"
            + "\7i\2\2\u008a\u008b\7q\2\2\u008b\u008c\7c\2\2\u008c\u008d\7n\2\2\u008d"
            + "\20\3\2\2\2\u008e\u008f\7h\2\2\u008f\u0090\7c\2\2\u0090\u0091\7e\2\2\u0091"
            + "\u0092\7v\2\2\u0092\22\3\2\2\2\u0093\u0094\7r\2\2\u0094\u0095\7t\2\2\u0095"
            + "\u0096\7g\2\2\u0096\u0097\7f\2\2\u0097\u0098\7k\2\2\u0098\u0099\7e\2\2"
            + "\u0099\u009a\7c\2\2\u009a\u009b\7v\2\2\u009b\u009c\7g\2\2\u009c\24\3\2"
            + "\2\2\u009d\u009e\7p\2\2\u009e\u009f\7g\2\2\u009f\u00a0\7y\2\2\u00a0\26"
            + "\3\2\2\2\u00a1\u00a2\7q\2\2\u00a2\u00a3\7t\2\2\u00a3\30\3\2\2\2\u00a4"
            + "\u00a5\7v\2\2\u00a5\u00a6\7j\2\2\u00a6\u00a7\7k\2\2\u00a7\u00a8\7u\2\2"
            + "\u00a8\32\3\2\2\2\u00a9\u00aa\7x\2\2\u00aa\u00ab\7q\2\2\u00ab\u00ac\7"
            + "k\2\2\u00ac\u00ad\7f\2\2\u00ad\34\3\2\2\2\u00ae\u00af\7v\2\2\u00af\u00b0"
            + "\7t\2\2\u00b0\u00b1\7w\2\2\u00b1\u00b2\7g\2\2\u00b2\36\3\2\2\2\u00b3\u00b4"
            + "\7h\2\2\u00b4\u00b5\7c\2\2\u00b5\u00b6\7n\2\2\u00b6\u00b7\7u\2\2\u00b7"
            + "\u00b8\7g\2\2\u00b8 \3\2\2\2\u00b9\u00ba\7t\2\2\u00ba\u00bb\7g\2\2\u00bb"
            + "\u00bc\7v\2\2\u00bc\u00bd\7w\2\2\u00bd\u00be\7t\2\2\u00be\u00bf\7p\2\2"
            + "\u00bf\"\3\2\2\2\u00c0\u00c1\7\60\2\2\u00c1$\3\2\2\2\u00c2\u00c3\7.\2"
            + "\2\u00c3&\3\2\2\2\u00c4\u00c5\7<\2\2\u00c5(\3\2\2\2\u00c6\u00c7\7=\2\2"
            + "\u00c7*\3\2\2\2\u00c8\u00c9\7*\2\2\u00c9,\3\2\2\2\u00ca\u00cb\7+\2\2\u00cb"
            + ".\3\2\2\2\u00cc\u00cd\7]\2\2\u00cd\60\3\2\2\2\u00ce\u00cf\7_\2\2\u00cf"
            + "\62\3\2\2\2\u00d0\u00d1\7}\2\2\u00d1\64\3\2\2\2\u00d2\u00d3\7\177\2\2"
            + "\u00d3\66\3\2\2\2\u00d4\u00d5\7-\2\2\u00d58\3\2\2\2\u00d6\u00d7\7/\2\2"
            + "\u00d7:\3\2\2\2\u00d8\u00d9\7,\2\2\u00d9<\3\2\2\2\u00da\u00db\7\61\2\2"
            + "\u00db>\3\2\2\2\u00dc\u00dd\7(\2\2\u00dd@\3\2\2\2\u00de\u00df\7~\2\2\u00df"
            + "B\3\2\2\2\u00e0\u00e1\7?\2\2\u00e1D\3\2\2\2\u00e2\u00e3\7@\2\2\u00e3F"
            + "\3\2\2\2\u00e4\u00e5\7>\2\2\u00e5H\3\2\2\2\u00e6\u00e7\7#\2\2\u00e7J\3"
            + "\2\2\2\u00e8\u00e9\7?\2\2\u00e9\u00ea\7?\2\2\u00eaL\3\2\2\2\u00eb\u00ec"
            + "\7>\2\2\u00ec\u00ed\7?\2\2\u00edN\3\2\2\2\u00ee\u00ef\7@\2\2\u00ef\u00f0"
            + "\7?\2\2\u00f0P\3\2\2\2\u00f1\u00f2\7#\2\2\u00f2\u00f3\7?\2\2\u00f3R\3"
            + "\2\2\2\u00f4\u00f5\7/\2\2\u00f5\u00f6\7@\2\2\u00f6T\3\2\2\2\u00f7\u00f8"
            + "\7`\2\2\u00f8V\3\2\2\2\u00f9\u00fd\t\2\2\2\u00fa\u00fc\t\3\2\2\u00fb\u00fa"
            + "\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"
            + "X\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0102\t\4\2\2\u0101\u0100\3\2\2\2"
            + "\u0102\u0103\3\2\2\2\u0103\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u010b"
            + "\3\2\2\2\u0105\u0107\7\60\2\2\u0106\u0108\t\4\2\2\u0107\u0106\3\2\2\2"
            + "\u0108\u0109\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u010c"
            + "\3\2\2\2\u010b\u0105\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u0114\3\2\2\2\u010d"
            + "\u010f\7\60\2\2\u010e\u0110\t\4\2\2\u010f\u010e\3\2\2\2\u0110\u0111\3"
            + "\2\2\2\u0111\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0114\3\2\2\2\u0113"
            + "\u0101\3\2\2\2\u0113\u010d\3\2\2\2\u0114Z\3\2\2\2\u0115\u011a\7$\2\2\u0116"
            + "\u0119\5]/\2\u0117\u0119\13\2\2\2\u0118\u0116\3\2\2\2\u0118\u0117\3\2"
            + "\2\2\u0119\u011c\3\2\2\2\u011a\u011b\3\2\2\2\u011a\u0118\3\2\2\2\u011b"
            + "\u011d\3\2\2\2\u011c\u011a\3\2\2\2\u011d\u011e\7$\2\2\u011e\\\3\2\2\2"
            + "\u011f\u0120\7^\2\2\u0120\u0124\7$\2\2\u0121\u0122\7^\2\2\u0122\u0124"
            + "\7^\2\2\u0123\u011f\3\2\2\2\u0123\u0121\3\2\2\2\u0124^\3\2\2\2\u0125\u0126"
            + "\7\61\2\2\u0126\u0127\7\61\2\2\u0127\u012b\3\2\2\2\u0128\u012a\13\2\2"
            + "\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u012c\3\2\2\2\u012b\u0129"
            + "\3\2\2\2\u012c\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u0130\7\17\2\2"
            + "\u012f\u012e\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132"
            + "\7\f\2\2\u0132\u0133\3\2\2\2\u0133\u0134\b\60\2\2\u0134`\3\2\2\2\u0135"
            + "\u0136\7\61\2\2\u0136\u0137\7,\2\2\u0137\u013b\3\2\2\2\u0138\u013a\13"
            + "\2\2\2\u0139\u0138\3\2\2\2\u013a\u013d\3\2\2\2\u013b\u013c\3\2\2\2\u013b"
            + "\u0139\3\2\2\2\u013c\u013e\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u013f\7,"
            + "\2\2\u013f\u0140\7\61\2\2\u0140\u0141\3\2\2\2\u0141\u0142\b\61\2\2\u0142"
            + "b\3\2\2\2\u0143\u0145\t\5\2\2\u0144\u0143\3\2\2\2\u0145\u0146\3\2\2\2"
            + "\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0148\3\2\2\2\u0148\u0149"
            + "\b\62\2\2\u0149d\3\2\2\2\20\2\u00fd\u0103\u0109\u010b\u0111\u0113\u0118"
            + "\u011a\u0123\u012b\u012f\u013b\u0146\3\b\2\2";
    public static final ATN _ATN
            = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
