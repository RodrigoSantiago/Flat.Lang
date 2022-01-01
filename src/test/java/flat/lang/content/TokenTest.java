package flat.lang.content;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void properties() {
        String source = " token ";
        Token token = new Token(source, 1, source.length() - 1);

        assertEquals(1, token.getStart(), "Invalid Start");
        assertEquals(5, token.getLength(), "Invalid Length");
        assertEquals(6, token.getEnd(), "Invalid End");
        assertSame(source, token.getSource(), "Invalid Source");
    }

    @Test
    void childParent() {
        String source1 = "parent";
        Token parentToken = new Token(source1, 0, source1.length());

        String source2 = "child";
        Token childToken = new Token(source2, 0, source2.length());

        parentToken.setChild(childToken);
        assertEquals(childToken, parentToken.getChild(), "Invalid child token");
        assertEquals(parentToken, childToken.getParent(),"Invalid parent token");
    }

    @Test
    void nextPrev() {
        String source1 = "parent";
        Token prevToken = new Token(source1, 0, source1.length());

        String source2 = "child";
        Token nextToken = new Token(source2, 0, source2.length());

        prevToken.setNext(nextToken);
        assertEquals(nextToken, prevToken.getNext(), "Invalid child token");
        assertEquals(prevToken, nextToken.getPrev(),"Invalid parent token");
    }

    @Test
    void equalsIgnoreCase() {
        String source1 = "Happy Day";
        Token token1 = new Token(source1, 0, source1.length());

        String source2 = "happy day";
        Token token2 = new Token(source2, 0, source2.length());

        String source3 = "happyday ";
        Token token3 = new Token(source3, 0, source3.length());

        Token token4 = new Token(source1, 0, source1.length());

        assertTrue(token1.equalsIgnoreCase(token2), "EqualsIgnoreCase should ignore uppercase differences");
        assertTrue(token2.equalsIgnoreCase(token1), "EqualsIgnoreCase should ignore uppercase differences");
        assertFalse(token3.equalsIgnoreCase(token1), "EqualsIgnoreCase should ignore length differences");
        assertFalse(token3.equalsIgnoreCase(token2), "EqualsIgnoreCase should ignore length differences");
        assertTrue(token1.equalsIgnoreCase(token4), "Invalid EqualsIgnoreCase");
    }

    @Test
    void equalsIgnoreCaseWithKeywords() {
        String source1 = "true";
        Token token1 = new Token(source1, 0, source1.length(), Key.True);

        String source2 = " true ";
        Token token2 = new Token(source2, 1, source2.length() - 1, Key.True);

        String source3 = "word";
        Token token3 = new Token(source3, 0, source3.length(), Key.Word);

        String source4 = "not a word";
        Token token4 = new Token(source4, 0, source4.length(), Key.Word);

        assertTrue(token1.equalsIgnoreCase(token2), "Invalid EqualsIgnoreCase");
        assertFalse(token3.equalsIgnoreCase(token4), "Invalid EqualsIgnoreCase");
    }

    @Test
    void testEquals() {
        String source1 = "Happy Day";
        Token token1 = new Token(source1, 0, source1.length());

        String source2 = "happy day";
        Token token2 = new Token(source2, 0, source2.length());

        String source3 = " Happy Day ";
        Token token3 = new Token(source3, 1, source3.length() - 1);

        Token token4 = new Token(source1, 0, source1.length());

        assertNotEquals(token1, token2, "Equals should not ignore case");
        assertNotEquals(token2, token1, "Equals should be equivalent");
        assertEquals(token3, token1, "Equals should work at different sources");
        assertNotEquals(token3, token2, "Equals should work at different sources");
        assertEquals(token1, token4, "Invalid Equals");

        token1.hashCode();
        token2.hashCode();
        token3.hashCode();

        assertNotEquals(token1, token2, "Invalid Equals after hashcode");
        assertEquals(token3, token1, "Invalid Equals after hashcode");
    }

    @Test
    void equalsWithKeywords() {
        String source1 = "true";
        Token token1 = new Token(source1, 0, source1.length(), Key.True);

        String source2 = " true ";
        Token token2 = new Token(source2, 1, source2.length() - 1, Key.True);

        String source3 = "word";
        Token token3 = new Token(source3, 0, source3.length(), Key.Word);

        String source4 = "not a word";
        Token token4 = new Token(source4, 0, source4.length(), Key.Word);

        String source5 = "false";
        Token token5 = new Token(source5, 0, source5.length(), Key.False);

        String source6 = "true";
        Token token6 = new Token(source6, 0, source6.length(), Key.True);

        assertEquals(token1, token2, "Invalid Equals when using keywords");
        assertNotEquals(token3, token4, "Invalid Equals when using keywords");
        assertNotEquals(token5, token6, "Invalid Equals when using different keywords");
    }

    @Test
    void testHashCode() {
        String source1 = "Happy Day";
        Token token1 = new Token(source1, 0, source1.length());

        String source2 = " Happy Day ";
        Token token2 = new Token(source2, 1, source2.length() - 1);

        assertEquals(token1.hashCode(), token2.hashCode(), "Equals tokens should have equals hashcodes");
    }

    @Test
    void testToString() {
        String source1 = "Happy Day";
        Token token1 = new Token(source1, 0, source1.length());

        String source2 = " Happy Day ";
        Token token2 = new Token(source2, 1, source2.length() - 1);

        assertEquals(token1.toString(), "Happy Day", "Invalid toString");
        assertEquals(token2.toString(), "Happy Day", "Invalid toString");
    }
}