package flat.lang.data;

import java.util.*;

/**
 * Config.json
 * {
 *     "name" : "Library Name",
 *     "description" : "Library Description",
 *     "version" : "1.0.0",
 *     "dependencies" : {
 *         "dependence name" : "1.0.0"
 *     }
 * }
 */
public class Configuration {
    private final String name;
    private final String description;
    private final Version version;
    private final HashMap<String, Dependence> dependencies;

    public Configuration(String name, String description, Version version, HashMap<String, Dependence> dependencies) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Version getVersion() {
        return version;
    }

    public Map<String, Dependence> getDependencies() {
        return dependencies;
    }
}
