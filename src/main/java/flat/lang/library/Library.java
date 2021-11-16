package flat.lang.library;

import flat.lang.data.Configuration;

public class Library {

    private final Configuration config;

    public Library(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }

    public String getName() {
        return config.getName();
    }
}
