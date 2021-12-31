package flat.lang.content;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void testReadValidKey() {
        String keyword = "true";
        Key actualKey = Key.readKey(keyword, 0, keyword.length());

        assertEquals(Key.True, actualKey, "Incorrect keyword found");
    }

    @Test
    void testReadInvalidKeys() {
        String keyword = "###";
        Key actualKey = Key.readKey(keyword, 0, keyword.length());

        assertEquals(Key.InvalidOp, actualKey, "Incorrect keyword found");
    }

    @Test
    void testReadHashCollideKeys() {
        String keyword = ";{"; // Same hash and length as '=='
        Key actualKey = Key.readKey(keyword, 0, keyword.length());

        assertEquals(keyword.hashCode(), "==".hashCode(), "Incorrect arrange, hash should collide");
        assertNotEquals(Key.Equal, actualKey, "Incorrect keyword found");
    }

    @Test
    void testReadHashCollideDifferentLengthKeys() {
        String keyword = "\u0000\u002E"; // Same hash and different length as '.'
        Key actualKey = Key.readKey(keyword, 0, keyword.length());

        assertEquals(keyword.hashCode(), ".".hashCode(), "Incorrect arrange, hash should collide");
        assertNotEquals(Key.Equal, actualKey, "Incorrect keyword found");
    }

    @Test
    void testReadAllKeys() {
        String[] keywords = {
                "true", "false", "null",

                "(", "{", "[", "<",
                ")", "}", "]", ">",

                "namespace", "using", "class", "struct", "interface", "enum",

                "public", "private", "static", "final", "abstract", "operator",

                "if", "else", "for", "while", "do", "switch", "native",

                "break", "case", "continue", "default", "let", "new", "return", "super", "this", "var", "void", "yield",

                ";", ":", ".", ",", "?", "->", "*", "/", "%", "+", "-", "++", "--", ">>", "<<",
                // ">", "<",
                ">=", "<=", "==", "!=", "&", "^", "|", "~", "&&", "||", "!",
                "=", "+=", "-=", "*=", "/=", "%=", ">>=", "<<=", "&=", "|=", "~=", "^=",

                "is", "!is", "auto"
        };
        assertEquals(Key.keywordsCount(), keywords.length, "Unexpected keywords count");

        for (String keyword : keywords) {
            Key actualKey = Key.readKey(keyword, 0, keyword.length());

            if (actualKey == null || !actualKey.name.equals(keyword)) {
                fail("Incorrect keyword found ==>\nExpected :Key[" + keyword + "]\nActual   :" + actualKey);
            }
        }
    }

    @Test
    void testToString() {
        String expected = "Key[true]";

        assertEquals(expected, Key.True.toString(), "Invalid toString");
    }
}