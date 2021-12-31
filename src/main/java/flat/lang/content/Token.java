package flat.lang.content;

public class Token {

    private final int start;
    private final int end;
    private final int length;
    private final String source;
    private final Key key;

    private Token next;
    private Token prev;

    private Token parent;
    private Token child;

    private int hash;

    public Token(String source, int start, int end) {
        this(source, start, end, Key.None);
    }

    public Token(String source, int start, int end, Key key) {
        this.source = source;
        this.start = start;
        this.end = end;
        this.length = end - start;
        this.key = key;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return length;
    }

    public String getSource() {
        return source;
    }

    public Token getNext() {
        return next;
    }

    public void setNext(Token next) {
        this.next = next;
        if (this.next != null) {
            this.next.prev = this;
        }
    }

    public Token getPrev() {
        return prev;
    }

    public Token getChild() {
        return child;
    }

    public void setChild(Token child) {
        this.child = child;
        if (this.child != null) {
            this.child.parent = this;
        }
    }

    public Token getParent() {
        return parent;
    }

    /**
     * Compare changing all characters to uppercase
     *
     * @param other Token
     * @return true-false
     */
    public boolean equalsIgnoreCase(Token other) {
        if (other == this) return true;
        if (other == null) return false;

        if (other.source == source && other.start == start && other.end == end) {
            return true;

        } else if (key == other.key && !key.name.equals("")) {
            return true;

        } else if (other.length == length) {
            final int off = other.start - start;
            for (int i = start; i < end; i++) {
                if (Character.toUpperCase(source.charAt(i)) !=
                        Character.toUpperCase(other.source.charAt(i + off))) {
                    return false;
                }
            }
            return true;

        } else {
            return false;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token other = (Token) o;

        if (key != other.key) {
            return false;

        } else if (!key.name.equals("")) {
            return true;

        } else if (other.source == source && other.start == start && other.end == end) {
            return true;

        } else if (other.length == length) {
            if (hash != 0 && other.hash != 0 && hash != other.hash) {
                return false;
            }

            final int off = other.start - start;
            for (int i = start; i < end; i++) {
                if (source.charAt(i) != other.source.charAt(i + off)) {
                    return false;
                }
            }
            return true;

        } else {
            return false;

        }
    }

    @Override
    public int hashCode() {
        if (hash == 0 && length > 0) {
            for (int i = start; i < end; i++) {
                hash = 31 * hash + source.charAt(i);
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        return start == 0 && end == source.length() ? source : source.substring(start, end);
    }
}
