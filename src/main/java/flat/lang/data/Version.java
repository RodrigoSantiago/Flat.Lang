package flat.lang.data;

import java.util.Objects;

/**
 * A Version is a increasing value that represent a Library update
 *
 * 1.0.0 - Specific Version
 * 1.0.* - Specific major and minor, but any revision
 * 1.*   - Specific major, but any minor or revision
 * *     - Any version
 */
public class Version {
    private final int major;
    private final int minor;
    private final int revision;

    public Version(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major && minor == version.minor && revision == version.revision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, revision);
    }
}
