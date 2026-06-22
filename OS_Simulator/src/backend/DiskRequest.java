package backend;
/**
 * Represents a single disk track request for Mass Storage Management (Module 7).
 * Pure data holder - no logic here, just fields.
 */
public class DiskRequest {
    int trackNumber;

    public DiskRequest(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    @Override
    public String toString() {
        return "Track" + trackNumber;
    }
}
