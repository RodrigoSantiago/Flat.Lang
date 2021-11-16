package flat.lang.data;

import flat.lang.library.SourceFile;

public class Error {

    private final SourceFile sFile;
    private final int start;
    private final int length;
    private final ErrorLevel level;
    private final String message;

    public Error(SourceFile sFile, int start, int length, ErrorLevel level, String message) {
        this.sFile = sFile;
        this.start = start;
        this.length = length;
        this.level = level;
        this.message = message;
    }

    public SourceFile getSourceFile() {
        return sFile;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public ErrorLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
