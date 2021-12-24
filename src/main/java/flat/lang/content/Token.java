package flat.lang.content;

public class Token {
    private int start;
    private int length;
    private String source;
    private Key key;

    private Token prev;
    private Token next;
    private Token child;
    private Token parent;
}
