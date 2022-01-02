package flat.lang.content;

import flat.lang.data.Error;
import flat.lang.data.ErrorLevel;
import flat.lang.library.SourceFile;

public class Lexer {

    private SourceFile sFile;
    private String source;
    private int chr;
    private int nChr;
    private int pChr;
    private int index;
    private int nextIndex;
    private Token current;

    public Lexer(SourceFile sFile, String source) {
        this.sFile = sFile;
        this.source = source;
        readNextChar();
    }

    public Token read() {
        Token start = new Token(source, 0, 0);
        Token token = start;
        while (!eof()) {
            if (readNext(token, null)) {
                while (token.getNext() != null) {
                    token = token.getNext();
                }
            } else {
                break;
            }
        }
        return start.getNext();
    }

    boolean readNext(Token token, Token parent) {
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

            if (isBreakCharacter(parent)) {
                return false;

            } else if (chr == '{' || chr == '(' || chr == '[' || chr == '<') {
                token.setNext(readNextBlock());
                return true;
            } else if (isLetter(chr) || (chr == ':' && nChr == ':' && isLetter(previewFutureNextChar()))) {
                token.setNext(readNextWord());
                return true;
            } else if (isNumber(chr)) {
                token.setNext(readNextNumber());
                return true;
            } else if (isStringSplit(chr)) {
                token.setNext(readNextString());
                return true;
            } else if (isSplitter(chr)) {
                token.setNext(readNextSplitter());
                return true;
            } else if (isOperator(chr)) {
                token.setNext(readNextOperator());
                return true;
            } else {
                consumeInvalidCharacters();
            }
        }
        return false;
    }

    void readNextChar() {
        index = nextIndex;
        if (index >= source.length()) {
            pChr = 0;
            chr = 0;
            nChr = 0;
        } else {
            pChr = chr;
            chr = source.codePointAt(index);
            nextIndex = index + Character.charCount(chr);
            nChr = nextIndex < source.length() ? source.codePointAt(nextIndex) : 0;
        }
    }

    int previewFutureNextChar() {
        if (nextIndex < source.length()) {
            int next = nextIndex + Character.charCount(nChr);
            return next < source.length() ? source.codePointAt(next) : 0;
        }
        return 0;
    }

    void consumeSpaces() {
        while (!eof() && isSpace(chr)) {
            readNextChar();
        }
    }

    void consumeNextLineComment() {
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

    void consumeNextBlockComment() {
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

    void consumeInvalidCharacters() {
        int start = index;
        while (!eof() && !isValidChar(chr)) {
            readNextChar();
        }
        sFile.addError(new Error(sFile, start, index, ErrorLevel.LEXER, "Invalid character"));
    }

    Token readNextBlock() {
        Key openKey = Key.readKey(source, index, nextIndex);
        Token parent = new Token(source, index, nextIndex, openKey);

        Token token = parent;
        readNextChar();
        while (!eof()) {
            if (readNext(token, parent)) {
                while (token.getNext() != null) {
                    token = token.getNext();
                }
                if (isClosure(openKey, token.getKey()) || eof()) {
                    parent.setNextAsParent(token);
                    break;
                }
            } else {
                break;
            }
        }

        return parent;
    }

    private boolean isBreakCharacter(Token parent) {
        Key block = parent.getKey();
        if (block == Key.Brace) return false;
        if (block == Key.Param) return chr == '}';
        if (block == Key.Index) return chr == ')' || chr == '}';
        if (block == Key.Generic) {
            if (isLetter(chr) || (chr == ':' && nChr == ':' && isLetter(previewFutureNextChar()))) {
                return false;
            } else {
                Token pPrev = parent.getPrev();
                Key pPrevKey = pPrev != null ? pPrev.getKey() : null;

                Token pPrevPrev = pPrev != null ? parent.getPrev() : null;
                Key pPrevPrevKey = pPrevPrev != null ? pPrevPrev.getKey() : null;

                if (chr == ',') {
                    Token pParent = parent.getParent();
                    Key pParentKey = pParent != null ? pParent.getKey() : null;

                    Token pParentPrev = pParent != null ? pParent.getPrev() : null;
                    Key pParentPrevKey = pParentPrev != null ? pParentPrev.getKey() : null;

                    Key pParentPrevPrevKey = pParentPrev != null && pParentPrev.getPrev() != null ?
                            pParentPrev.getPrev().getKey() : null;

                    if (pPrevKey == Key.Word && pParentPrevPrevKey == Key.New) {
                        // new Type<A, B>()
                        return false;
                    } else if (pParentKey == Key.Param) {
                        // method(a < b, c > d)
                        return pParentPrevKey == Key.Word &&
                                (pParentPrevPrevKey != Key.Word && pParentPrevPrevKey != Key.Generic);
                    } else if (pParentKey == Key.Index) {
                        // int this[Type<A, B> d]
                        // Array<int> this[Type<A, B> d]
                        return pParentPrevKey != Key.This ||
                                (pParentPrevPrevKey != Key.Word && pParentPrevPrevKey != Key.Generic);
                    } else if (pParentKey == Key.Brace) {
                        // new bool[]{a < b, c > d}
                        return (pParentPrevPrevKey == Key.Index);
                    } else {
                        return false;
                    }
                } else if (chr == ':' && isLetter(nChr)) {
                    if (pPrevKey == Key.Word && (pPrevPrevKey == Key.Class || pPrevPrevKey == Key.Interface ||
                            pPrevPrevKey == Key.Struct || pPrevPrevKey == Key.Enum)) {
                        // class name <T : Type>
                        return false;
                    } else if (pPrevKey == Key.Word && (pPrevPrevKey == Key.Word || pPrevPrevKey == Key.Generic)) {
                        // int method<T : Type>(T object)
                        // Array<T> method<T : Type>(T object)
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    Token readNextWord() {
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

    Token readNextNumber() {
        int start = index;
        while (!eof() && isLiteral(chr)) {
            readNextChar();
        }
        return new Token(source, start, index, Key.Number);
    }

    Token readNextString() {
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

    Token readNextSplitter() {
        int start = index;
        readNextChar();
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index, key == null ? Key.InvalidOp : key);
    }

    Token readNextOperator() {
        int start = index;
        while (!eof() && (isOperator(chr) || chr == '>')) {
            readNextChar();
        }
        Key key = Key.readKey(source, start, index);
        return new Token(source, start, index, key == null ? Key.InvalidOp : key);
    }

    public boolean eof() {
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

    private static boolean isOperator(int chr) {
        return chr == '+' || chr == '-' || chr == '*' || chr == '/' || chr == '%'
                || chr == '=' || chr == '!' || chr == '|' || chr == '&' || chr == '^' || chr == ':' || chr == '?'
                || chr == '~' || chr == '<';
    }

    private static boolean isSplitter(int chr) {
        return chr == '(' || chr == ')' || chr == '{' || chr == '}' || chr == '[' || chr == ']' || chr == '.'
                || chr == ',' || chr == ';' || chr == '>';
    }

    private static boolean isStringSplit(int chr) {
        return chr == '"' || chr == '\'';
    }

    private static boolean isValidChar(int chr) {
        return isSpace(chr) || isNumber(chr) || isLetter(chr) ||
                isOperator(chr) || isStringSplit(chr) || isSplitter(chr);
    }

    private static boolean isChar(int chr) {
        return isNumber(chr) || isLetter(chr);
    }

    private static boolean isLiteral(int chr) {
        return (chr >= '0' && chr <= '9') ||
                (chr >= 'A' && chr <= 'F') ||
                (chr >= 'a' && chr <= 'f') ||
                chr == '.' || chr == '+' || chr == '-' ||
                chr == 'x' || chr == 'X' ||
                chr == 'l' || chr == 'L';
    }
    private static boolean isClosure(Key open, Key current) {
        if (open == Key.Brace) return current == Key.CBrace;
        if (open == Key.Param) return current == Key.CParam;
        if (open == Key.Index) return current == Key.CIndex;
        if (open == Key.Generic) return current == Key.CGeneric;
        return false;
    }
}
