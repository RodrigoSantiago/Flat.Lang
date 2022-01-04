package flat.lang.content;

/**
 * The initial reader, responsible for breaking the string into a token chain, facilitating further analysis
 */
public class Lexer {

    private final String source;
    private int chr;
    private int nChr;
    private int index;
    private int nextIndex;

    public Lexer(String source) {
        this.source = source;
        readNextChar();
    }

    /**
     * Execute the reading
     * @return Start token chain
     */
    public Token read() {
        Token start = new Token(source, 0, 0);
        Token token = start;
        while (!eof()) {
            if (readNext(token, null, null)) {
                while (token.getNext() != null) {
                    token = token.getNext();
                }
            } else {
                break;
            }
        }
        return start.ownNext();
    }

    private boolean readNext(Token prev, Token parent, Token owner) {
        while (!eof()) {
            if (isSpace(chr)) {
                consumeSpaces();
                continue;

            } else if (chr == '/' && nChr == '/') {
                consumeNextLineComment();
                continue;

            } else if (chr == '/' && nChr == '*') {
                consumeNextBlockComment();
                continue;

            }

            if (isBreakCharacter(parent, owner)) {
                return false;

            } else if (isOpener(chr) || (chr == '<' && !isOperator(nChr))) {
                readNextBlock(prev, parent);

            } else if (isCloser(chr) || (chr == '>' && parent != null && parent.getKey() == Key.Generic)) {
                prev.setNext(readNextCloser());

            } else if (isLetter(chr) || (chr == ':' && nChr == ':' && isLetter(previewFutureNextChar()))) {
                prev.setNext(readNextWord());

            } else if (isNumber(chr)) {
                prev.setNext(readNextNumber());

            } else if (isQuot(chr)) {
                prev.setNext(readNextString());

            } else if (isSplitter(chr)) {
                prev.setNext(readNextSplitter());

            } else if (isOperator(chr)) {
                prev.setNext(readNextOperator());

            } else {
                prev.setNext(readInvalidChars());
            }
            return true;
        }
        return false;
    }

    private void readNextChar() {
        index = nextIndex;
        if (index >= source.length()) {
            chr = 0;
            nChr = 0;
        } else {
            chr = source.codePointAt(index);
            nextIndex = index + Character.charCount(chr);
            nChr = nextIndex < source.length() ? source.codePointAt(nextIndex) : 0;
        }
    }

    private int previewFutureNextChar() {
        int n = 0;
        if (nextIndex < source.length()) {
            int next = nextIndex + Character.charCount(nChr);
            n = next < source.length() ? source.codePointAt(next) : 0;
        }
        return n;
    }

    private void consumeSpaces() {
        while (!eof() && isSpace(chr)) {
            readNextChar();
        }
    }

    private void consumeNextLineComment() {
        readNextChar();
        readNextChar();
        while (!eof()) {
            if (chr == '\n') {
                readNextChar();
                return;
            }
            readNextChar();
        }
    }

    private void consumeNextBlockComment() {
        readNextChar();
        readNextChar();
        while (!eof()) {
            if (chr == '*' && nChr == '/') {
                readNextChar();
                readNextChar();
                return;
            }
            readNextChar();
        }
    }

    private void readNextBlock(Token prev, Token owner) {
        Key openKey = Key.readKey(source, index, nextIndex);
        prev.setNext(new Token(source, index, nextIndex, openKey));
        Token parent = prev.getNext();

        Token token = parent;
        readNextChar();
        while (!eof()) {
            if (readNext(token, parent, owner)) {
                while (token.getNext() != null) {
                    token = token.getNext();
                }
                if (isClosure(openKey, token.getKey()) || eof()) {
                    parent.setNextAsChild(token);
                    break;
                }
            } else {
                if (openKey == Key.Generic) parent.setKey(Key.Less);
                break;
            }
        }
    }

    private Token readNextCloser() {
        int start = index;
        readNextChar();
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index, key == null ? Key.InvalidOp : key);
    }

    private boolean isBreakCharacter(Token parent, Token owner) {
        if (parent == null) return false;

        Key block = parent.getKey();
        if (block == Key.Param) return chr == '}';
        else if (block == Key.Index) return chr == ')' || chr == '}';
        else if (block == Key.Generic) {
            if (chr == '>') {
                return false;
            } else if (isLetter(chr) || (chr == ':' && nChr == ':' && isLetter(previewFutureNextChar()))) {
                return false;
            } else if (chr == ',') {
                Token pPrev = parent.getPrev();
                Key pPrevKey = pPrev != null ? pPrev.getKey() : null;

                Token pPrevPrev = pPrev != null ? pPrev.getPrev() : null;
                Key pPrevPrevKey = pPrevPrev != null ? pPrevPrev.getKey() : null;

                Key oKey = owner != null ? owner.getKey() : null;

                Token oPrev = owner != null ? owner.getPrev() : null;
                Key oPrevKey = oPrev != null ? oPrev.getKey() : null;

                Token oPrevPrev = oPrev != null ? oPrev.getPrev() : null;
                Key oPrevPrevKey = oPrevPrev != null ? oPrevPrev.getKey() : null;

                if (pPrevKey == Key.Word && pPrevPrevKey == Key.New) {
                    return false;
                } else if (oKey == Key.Param) {
                    return oPrevKey == Key.Word &&
                            (oPrevPrevKey != Key.Word && oPrevPrevKey != Key.Generic);
                } else if (oKey == Key.Index) {
                    return oPrevKey != Key.This ||
                            (oPrevPrevKey != Key.Word && oPrevPrevKey != Key.Generic);
                } else if (oKey == Key.Brace) {
                    return (oPrevKey == Key.Index);
                } else {
                    return false;
                }
            } else if (chr == ':' && !isOperator(nChr)) {
                Token pPrev = parent.getPrev();
                Key pPrevKey = pPrev != null ? pPrev.getKey() : null;

                Token pPrevPrev = pPrev != null ? pPrev.getPrev() : null;
                Key pPrevPrevKey = pPrevPrev != null ? pPrevPrev.getKey() : null;

                if (pPrevKey == Key.Word &&
                        (pPrevPrevKey == Key.Class || pPrevPrevKey == Key.Interface ||
                        pPrevPrevKey == Key.Struct || pPrevPrevKey == Key.Enum)) {
                    return false;
                } else if (pPrevKey == Key.Word && (pPrevPrevKey == Key.Word || pPrevPrevKey == Key.Generic)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Token readNextWord() {
        int start = index;
        while (!eof()) {
            if (isChar(chr)) {
                readNextChar();

            } else if (chr == ':' && nChr == ':' && isLetter(previewFutureNextChar())) {
                readNextChar();
                readNextChar();

            } else {
                break;
            }
        }
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index, key == null ? Key.Word : key);
    }

    private Token readNextNumber() {
        int start = index;
        while (!eof()) {
            if (chr == 'e' && (nChr == '-' || nChr == '+')) {
                readNextChar();
                readNextChar();
            } else if (isLiteral(chr)) {
                readNextChar();
            } else {
                break;
            }
        }
        return new Token(source, start, index, Key.Number);
    }

    private Token readNextString() {
        int split = chr;
        int start = index;
        boolean invert = false;
        readNextChar();
        while (!eof()) {
            if (chr == '\\') {
                invert = !invert;
            } else if ((chr == split && !invert) || chr == '\n') {
                readNextChar();
                break;
            }

            readNextChar();
        }
        return new Token(source, start, index, Key.String);
    }

    private Token readNextSplitter() {
        int start = index;
        readNextChar();
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index, key == null ? Key.InvalidOp : key);
    }

    private Token readNextOperator() {
        int start = index;
        while (!eof() && (isOperator(chr) || chr == '>')) {
            readNextChar();
        }
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index,
                key == null ? Key.InvalidOp :
                        key == Key.Generic ? Key.Less : key == Key.CGeneric ? Key.More : key);
    }

    private Token readInvalidChars() {
        int start = index;
        while (!eof() && !isValidChar(chr)) {
            readNextChar();
        }

        return new Token(source, start, index, Key.Invalid);
    }

    private boolean eof() {
        return index >= source.length();
    }

    private static boolean isSpace(int chr) {
        return Character.isSpaceChar(chr) || Character.isWhitespace(chr);
    }

    private static boolean isNumber(int chr) {
        return (chr >= '0' && chr <= '9');
    }

    private static boolean isLetter(int chr) {
        return (chr >= 'A' && chr <= 'Z') || (chr >= 'a' && chr <= 'z') || chr == '_';
    }

    private static boolean isOpener(int chr) {
        return chr == '{' || chr == '(' || chr == '[';
    }

    private static boolean isCloser(int chr) {
        return chr == '}' || chr == ')' || chr == ']';
    }

    private static boolean isOperator(int chr) {
        return chr == '+' || chr == '-' || chr == '*' || chr == '/' || chr == '%'
                || chr == '=' || chr == '!' || chr == '|' || chr == '&' || chr == '^' || chr == ':' || chr == '?'
                || chr == '~' || chr == '<' || chr == '>';
    }

    private static boolean isSplitter(int chr) {
        return chr == '.' || chr == ',' || chr == ';';
    }

    private static boolean isQuot(int chr) {
        return chr == '"' || chr == '\'';
    }

    private static boolean isValidChar(int chr) {
        return isSpace(chr) || isNumber(chr) || isLetter(chr) ||
                isOperator(chr) || isQuot(chr) || isSplitter(chr) || isOpener(chr) || isCloser(chr);
    }

    private static boolean isChar(int chr) {
        return isNumber(chr) || isLetter(chr);
    }

    private static boolean isLiteral(int chr) {
        return (chr >= '0' && chr <= '9') ||
                (chr >= 'A' && chr <= 'F') ||
                (chr >= 'a' && chr <= 'f') ||
                chr == '.' || chr == 'x' || chr == 'X' || chr == 'l' || chr == 'L';
    }

    private static boolean isClosure(Key open, Key current) {
        boolean closer = false;
        if (open == Key.Brace) closer = current == Key.CBrace;
        else if (open == Key.Param) closer = current == Key.CParam;
        else if (open == Key.Index) closer = current == Key.CIndex;
        else if (open == Key.Generic) closer = current == Key.CGeneric;
        return closer;
    }
}
