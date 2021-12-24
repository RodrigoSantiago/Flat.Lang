package flat.lang.entity;

import flat.lang.entity.type.Type;
import flat.lang.library.SourceFile;

public class Name {
    private String name; // Represent all namespaces and types
    private SourceFile sFile;

    private Namespace cacheNamespace;
    private Type cacheType;
}
