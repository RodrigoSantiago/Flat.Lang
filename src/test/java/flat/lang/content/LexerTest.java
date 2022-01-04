package flat.lang.content;

import flat.lang.support.TokenChain;
import org.junit.jupiter.api.Test;

import static flat.lang.support.TokenChain.*;

class LexerTest {

    @Test
    void readSimple() {
        String source = "simple";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("simple")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readIgnoreLineComment() {
        String source = "simple // comment\n other";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("simple").word("other")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readIgnoreMultilineComment() {
        String source = "simple /*comment*/ other";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("simple").word("other")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readSIgnoreIncorrectClosureMultilineComment() {
        String source = "simple /*/ comment here";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("simple")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readNumber() {
        String source = "123456";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .number("123456")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readComplexNumber() {
        String source = "123.456";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .number("123.456")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readComplexExpNumber() {
        String source = "123.456e-10";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .number("123.456e-10")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readString() {
        String source = "\"str\"";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .string("\"str\"")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readSingleString() {
        String source = "'str'";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .string("'str'")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readStringScapped() {
        String source = "\"str\\\"s\"";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .string("\"str\\\"s\"")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readDouble() {
        String source = "simple double";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("simple")
                .word("double")
                .get();
        assertChain(expected, token,"Unexpected token chain result");
    }

    @Test
    void readComplexName() {
        String source = "complex::name";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("complex::name")
                .get();
        assertChain(expected, token,"Unexpected token chain result");
    }

    @Test
    void readComplexNameClearStart() {
        String source = "::name";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("::name")
                .get();
        assertChain(expected, token,"Unexpected token chain result");
    }

    @Test
    void readComplexNameIncorrectEnding() {
        String source = "name::";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("name").keyword(Key.InvalidOp, "::")
                .get();
        assertChain(expected, token,"Unexpected token chain result");
    }

    @Test
    void readOperator() {
        String source = "1+1";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .number("1").key(Key.Add).number("1")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readSplitter() {
        String source = "A.B";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("A").key(Key.Dot).word("B")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readInvalid() {
        String source = "$";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .keyword(Key.Invalid, "$")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readComplexLine() {
        String source = "word \"string\" 123456 == > .;$";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();
        Token expected = new TokenChain()
                .word("word")
                .string("\"string\"")
                .number("123456")
                .key(Key.Equal)
                .key(Key.More)
                .key(Key.Dot)
                .key(Key.Semicolon)
                .keyword(Key.Invalid, "$")
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readBraces() {
        String source = "simple {brace}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().word("brace").key(Key.CBrace).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readNestedBraces() {
        String source = "simple {brace {inner}}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace).child(new TokenChain()
                        .word("brace").key(Key.Brace).child(new TokenChain()
                                .word("inner")
                                .key(Key.CBrace).get())
                        .key(Key.CBrace).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readParam() {
        String source = "simple (param)";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Param)
                .child(new TokenChain().word("param").key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readNestedParam() {
        String source = "simple (param (inner))";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Param).child(new TokenChain()
                        .word("param").key(Key.Param).child(new TokenChain()
                                .word("inner")
                                .key(Key.CParam).get())
                        .key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readIndexer() {
        String source = "simple [index]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Index)
                .child(new TokenChain().word("index").key(Key.CIndex).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readNestedIndexer() {
        String source = "simple [index[inner]]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Index).child(new TokenChain()
                        .word("index").key(Key.Index).child(new TokenChain()
                                .word("inner")
                                .key(Key.CIndex).get())
                        .key(Key.CIndex).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readNestedBlocks() {
        String source = "simple {braces(param[index])}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace).child(new TokenChain()
                        .word("braces").key(Key.Param).child(new TokenChain()
                                .word("param").key(Key.Index).child(new TokenChain()
                                        .word("index").key(Key.CIndex).get())
                                .key(Key.CParam).get())
                        .key(Key.CBrace).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readSiblings() {
        String source = "simple {brace}{other}(param)";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().word("brace").key(Key.CBrace).get())
                .key(Key.Brace)
                .child(new TokenChain().word("other").key(Key.CBrace).get())
                .key(Key.Param)
                .child(new TokenChain().word("param").key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readOpenBrace() {
        String source = "simple {brace";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().word("brace").get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readOpenDoubleBrace() {
        String source = "{brace1{brace2";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Brace).child(new TokenChain()
                        .word("brace1").key(Key.Brace).child(new TokenChain()
                                .word("brace2").get())
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readInnerOpenBlock() {
        String source = "simple {brace)}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().word("brace").key(Key.CParam).key(Key.CBrace).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readOpenBlockWithBreakPattern() {
        String source = "simple (brace})";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Param).word("brace").key(Key.CBrace).key(Key.CParam)
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readInnerOpenBlockWithBreakPatternBraceParam() {
        String source = "simple {(brace})";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().key(Key.Param).word("brace").key(Key.CBrace).get())
                .key(Key.CParam)
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readInnerOpenBlockWithBreakPatternBraceIndex() {
        String source = "simple {[brace}]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Brace)
                .child(new TokenChain().key(Key.Index).word("brace").key(Key.CBrace).get())
                .key(Key.CIndex)
                .get();

        assertChain(expected, token,"Unexpected token chain result");
    }

    @Test
    void readInnerOpenBlockWithBreakPatternParamIndex() {
        String source = "simple ([param)]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("simple").key(Key.Param)
                .child(new TokenChain().key(Key.Index).word("param").key(Key.CParam).get())
                .key(Key.CIndex)
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericBlock() {
        String source = "<generic>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Generic)
                .child(new TokenChain().word("generic").key(Key.CGeneric).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericComma() {
        String source = "<generic, other>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Generic)
                .child(new TokenChain().word("generic").key(Key.Comma).word("other").key(Key.CGeneric).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaMethodCall() {
        String source = "methodCall (a < b , c > d)";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("methodCall").key(Key.Param)
                .child(new TokenChain().word("a").key(Key.Less).word("b").key(Key.Comma).word("c").key(Key.More).word("d").key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaMethodCallNew() {
        String source = "methodCall (new a < b , c > (d))";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("methodCall").key(Key.Param)
                .child(new TokenChain()
                        .key(Key.New).word("a").key(Key.Generic)
                        .child(new TokenChain()
                                .word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .key(Key.Param).child(new TokenChain().word("d").key(Key.CParam).get())
                        .key(Key.CParam)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaMethodStatement() {
        String source = "type methodStatement (a < b , c > d)";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").word("methodStatement").key(Key.Param)
                .child(new TokenChain().word("a").key(Key.Generic)
                        .child(new TokenChain().word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .word("d").key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaMethodStatementGeneric() {
        String source = "type<a> methodStatement (a < b , c > d)";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").key(Key.Generic)
                .child(new TokenChain()
                        .word("a").key(Key.CGeneric).get())
                .word("methodStatement").key(Key.Param)
                .child(new TokenChain().word("a").key(Key.Generic)
                        .child(new TokenChain().word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .word("d").key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaIndexStatement() {
        String source = "type this [a < b , c > d]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").key(Key.This).key(Key.Index)
                .child(new TokenChain().word("a").key(Key.Generic)
                        .child(new TokenChain().word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .word("d").key(Key.CIndex).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaIndexStatementGeneric() {
        String source = "type<a> this [a < b , c > d]";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").key(Key.Generic)
                .child(new TokenChain()
                        .word("a").key(Key.CGeneric).get())
                .key(Key.This).key(Key.Index)
                .child(new TokenChain().word("a").key(Key.Generic)
                        .child(new TokenChain().word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .word("d").key(Key.CIndex).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaIndexUse() {
        String source = "int a = this[a < b , c > d];";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("int").word("a").key(Key.SetVal).key(Key.This).key(Key.Index)
                .child(new TokenChain()
                        .word("a").key(Key.Less).word("b").key(Key.Comma).word("c").key(Key.More).word("d")
                        .key(Key.CIndex).get())
                .key(Key.Semicolon)
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readBraceGenericVar() {
        String source = "{a < b , c > d;}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Brace)
                .child(new TokenChain()
                        .word("a").key(Key.Generic)
                        .child(new TokenChain()
                                .word("b").key(Key.Comma).word("c").key(Key.CGeneric).get())
                        .word("d")
                        .key(Key.Semicolon)
                        .key(Key.CBrace)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readBraceArray() {
        String source = "[]{a < b , c > d;}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Index).child(new TokenChain().key(Key.CIndex).get())
                .key(Key.Brace)
                .child(new TokenChain()
                        .word("a").key(Key.Less).word("b").key(Key.Comma).word("c").key(Key.More).word("d")
                        .key(Key.Semicolon)
                        .key(Key.CBrace)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readBraceArrayColon() {
        String source = "[]{c ? a < b : c > d;}";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Index).child(new TokenChain().key(Key.CIndex).get())
                .key(Key.Brace)
                .child(new TokenChain()
                        .word("c").key(Key.Quest)
                        .word("a").key(Key.Less).word("b").key(Key.Colon).word("c").key(Key.More).word("d")
                        .key(Key.Semicolon)
                        .key(Key.CBrace)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaClassStatement() {
        String source = "class name<K: V>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Class).word("name").key(Key.Generic)
                .child(new TokenChain()
                        .word("K").key(Key.Colon).word("V").key(Key.CGeneric)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaInterfaceStatement() {
        String source = "interface name<K: V>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Interface).word("name").key(Key.Generic)
                .child(new TokenChain()
                        .word("K").key(Key.Colon).word("V").key(Key.CGeneric)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaStructStatement() {
        String source = "struct name<K: V>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Struct).word("name").key(Key.Generic)
                .child(new TokenChain()
                        .word("K").key(Key.Colon).word("V").key(Key.CGeneric)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaEnumStatement() {
        String source = "enum name<K: V>";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .key(Key.Enum).word("name").key(Key.Generic)
                .child(new TokenChain()
                        .word("K").key(Key.Colon).word("V").key(Key.CGeneric)
                        .get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericCommaGenericMethod() {
        String source = "type methodStatement <K, V>()";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").word("methodStatement")
                .key(Key.Generic).child(new TokenChain().word("K").key(Key.Comma).word("V").key(Key.CGeneric).get())
                .key(Key.Param).child(new TokenChain().key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericColonGenericMethod() {
        String source = "type methodStatement <K: V>()";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").word("methodStatement")
                .key(Key.Generic).child(new TokenChain().word("K").key(Key.Colon).word("V").key(Key.CGeneric).get())
                .key(Key.Param).child(new TokenChain().key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readGenericColonGenericMethodGenericReturn() {
        String source = "type<a> methodStatement <K: V>()";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("type").key(Key.Generic).child(new TokenChain().word("a").key(Key.CGeneric).get()).word("methodStatement")
                .key(Key.Generic).child(new TokenChain().word("K").key(Key.Colon).word("V").key(Key.CGeneric).get())
                .key(Key.Param).child(new TokenChain().key(Key.CParam).get())
                .get();

        assertChain(expected, token, "Unexpected token chain result");
    }

    @Test
    void readBreakGenericOnColon() {
        String source = "bool a = b ? c < d : e > f;";
        Lexer lexer = new Lexer(source);
        Token token = lexer.read();

        Token expected = new TokenChain()
                .word("bool").word("a")
                .key(Key.SetVal).word("b").key(Key.Quest)
                .word("c").key(Key.Less).word("d").key(Key.Colon).word("e").key(Key.More).word("f").key(Key.Semicolon).get();

        assertChain(expected, token, "Unexpected token chain result");
    }
}