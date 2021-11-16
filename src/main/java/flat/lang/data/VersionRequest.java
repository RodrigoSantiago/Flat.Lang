package flat.lang.data;

public class VersionRequest {
    private final int major;
    private final int minor;
    private final int revision;
    private final boolean greaterAllowed;
    private final boolean smallerAllowed;

    public VersionRequest(int major, int minor, int revision) {
        this(major, minor, revision, false, false);
    }

    public VersionRequest(int major, int minor, int revision, boolean greaterAllowed, boolean smallerAllowed) {
        this.major = major < 0 ? -1 : major;
        this.minor = this.major == -1 || minor < 0 ? -1 : minor;
        this.revision = this.minor == -1 || revision < 0 ? -1 : revision;
        this.greaterAllowed = greaterAllowed;
        this.smallerAllowed = smallerAllowed;
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

    public boolean isGreaterAllowed() {
        return greaterAllowed;
    }

    public boolean isSmallerAllowed() {
        return smallerAllowed;
    }

    public boolean isAnyAllowed() {
        return major == -1;
    }

    public boolean checkVersion(Version version) {
        if (isAnyAllowed()) {
            return true;
        } else {
            return checkMajor(version);
        }
    }

    private boolean checkMajor(Version version) {
        if (getMajor() == -1) {
            return true;
        } else if (isGreaterAllowed() && version.getMajor() > getMajor()) {
            return true;
        } else if (isSmallerAllowed() && version.getMajor() < getMajor()) {
            return true;
        } else if (version.getMajor() == getMajor()) {
            return checkMinor(version);
        }

        return false;
    }

    private boolean checkMinor(Version version) {
        if (getMinor() == -1) {
            return true;
        } else if (isGreaterAllowed() && version.getMinor() > getMinor()) {
            return true;
        } else if (isSmallerAllowed() && version.getMinor() < getMinor()) {
            return true;
        } else if (version.getMinor() == getMinor()) {
            return checkRevision(version);
        }

        return false;
    }

    private boolean checkRevision(Version version) {
        if (getRevision() == -1) {
            return true;
        } else if (isGreaterAllowed() && version.getRevision() > getRevision()) {
            return true;
        } else if (isSmallerAllowed() && version.getRevision() < getRevision()) {
            return true;
        } else if (version.getRevision() == getRevision()) {
            return true;
        }

        return false;
    }
}
