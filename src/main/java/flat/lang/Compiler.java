package flat.lang;

import flat.lang.library.Library;

import java.io.File;
import java.util.HashMap;

public class Compiler {

    private HashMap<String, Library> libraries = new HashMap<>();

    /**
     * Load a Library at given directory
     *
     * - Root Directory
     *   config.json
     *   - src
     *     *.lang
     *   - lib
     *     *.h
     *     *.cpp
     *     *.hpp
     *   - out
     *     *.o
     *
     * @param dir Root Directory
     */
    public void loadLibrary(File dir) {

    }
}
