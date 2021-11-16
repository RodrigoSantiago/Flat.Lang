package flat.lang.data;

import java.util.Objects;

public class Dependence {
    private final String name;
    private final VersionRequest version;

    public Dependence(String name, VersionRequest version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public VersionRequest getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependence that = (Dependence) o;
        return Objects.equals(name, that.name) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
