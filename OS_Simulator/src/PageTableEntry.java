/**
 * Represents a single entry in a page table for Virtual Memory (Module 6).
 * Pure data holder - no logic here, just fields.
 */
public class PageTableEntry {
    int pageNum;
    int frameNum;
    boolean valid;
    int lastUsedTime;    // used by LRU
    int loadedTime;      // used by FIFO

    public PageTableEntry(int pageNum) {
        this.pageNum = pageNum;
        this.frameNum = -1;
        this.valid = false;
        this.lastUsedTime = -1;
        this.loadedTime = -1;
    }

    @Override
    public String toString() {
        return "Page" + pageNum + " [frame=" + frameNum + ", valid=" + valid + "]";
    }
}
