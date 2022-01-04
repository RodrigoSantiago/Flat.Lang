package flat.lang.support;

import flat.lang.content.Key;
import flat.lang.content.Token;
import org.opentest4j.AssertionFailedError;

public class TokenChain {

    private Token start;
    private Token end;

    private void addToken(Token token) {
        if (start == null) {
            start = token;
            end = start;
        } else {
            end.setNext(token);
            end = end.getNext();
        }
    }

    public Token get() {
        return start;
    }

    public TokenChain child(Token token) {
        end.setChild(token);
        return this;
    }

    public TokenChain key(Key key) {
        addToken(new Token(key.name, 0, key.name.length(), key));
        return this;
    }

    public TokenChain keyword(Key key, String word) {
        addToken(new Token(word, 0, word.length(), key));
        return this;
    }

    public TokenChain word(String word) {
        addToken(new Token(word, 0, word.length(), Key.Word));
        return this;
    }

    public TokenChain number(String number) {
        addToken(new Token(number, 0, number.length(), Key.Number));
        return this;
    }

    public TokenChain string(String string) {
        addToken(new Token(string, 0, string.length(), Key.String));
        return this;
    }

    public static void assertChain(Token expected, Token actual, String message) {
        Token a = expected;
        Token b = actual;
        while (a != null && b != null) {
            if (!a.equals(b) || ((a.getChild() == null) != (b.getChild() == null))) {
                throw new AssertionFailedError(message, chainString(expected), chainString(actual));
            }
            if (a.getChild() != null && b.getChild() != null) {
                assertChildChain(expected, actual, a.getChild(), b.getChild(), message);
            }
            a = a.getNext();
            b = b.getNext();
        }
        if ((a == null) != (b == null)) {
            throw new AssertionFailedError(message, chainString(expected), chainString(actual));
        }
    }

    private static void assertChildChain(Token pExpected, Token pActual, Token expected, Token actual, String message) {
        Token a = expected;
        Token b = actual;
        while (a != null && b != null) {
            if (!a.equals(b) || ((a.getChild() == null) != (b.getChild() == null))) {
                throw new AssertionFailedError(message, chainString(pExpected), chainString(pActual));
            }
            if (a.getChild() != null && b.getChild() != null) {
                assertChildChain(pExpected, pActual, a.getChild(), b.getChild(), message);
            }
            a = a.getNext();
            b = b.getNext();
        }
        if ((a == null) != (b == null)) {
            throw new AssertionFailedError(message, chainString(pExpected), chainString(pActual));
        }
    }

    private static String chainString(Token token) {
        StringBuilder sb = new StringBuilder();
        while (token != null) {
            if (token.getKey() == Key.Generic || token.getKey() == Key.CGeneric) sb.append("$");
            sb.append(token);
            if (token.getChild() != null) {
                sb.append(" ").append(chainString(token.getChild()));
            }
            token = token.getNext();
            if (token != null) sb.append(" ");
        }
        return sb.toString();
    }
}
