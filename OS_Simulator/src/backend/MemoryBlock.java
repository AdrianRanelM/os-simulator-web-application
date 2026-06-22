package backend;
/**
 * Represents a block of memory for Memory Management (Module 5).
 * Pure data holder - no logic here, just fields.
 */
public class MemoryBlock {
    int id;
    int size;
    boolean free;
    int processId;       // -1 if no process is allocated
    int processSize;      // size of the allocated process, for fragmentation calc

    public MemoryBlock(int id, int size) {
        this.id = id;
        this.size = size;
        this.free = true;
        this.processId = -1;
        this.processSize = 0;
    }

    @Override
    public String toString() {
        return "Block" + id + " [size=" + size + ", free=" + free + ", processId=" + processId + "]";
    }
}
