import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module 6: Virtual Memory
 * Page replacement algorithms: FIFO, LRU, Optimal
 */
public class VirtualMemory {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- Virtual Memory ---");
        System.out.println("1. FIFO");
        System.out.println("2. LRU");
        System.out.println("3. Optimal");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFIFO(getSampleReferenceString(), 3);
            case 2 -> runLRU(getSampleReferenceString(), 3);
            case 3 -> runOptimal(getSampleReferenceString(), 3);
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    // TODO: replace with real user input later
    private static List<Integer> getSampleReferenceString() {
        List<Integer> pages = new ArrayList<>();
        int[] sample = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
        for (int p : sample) pages.add(p);
        return pages;
    }

    public static void runFIFO(List<Integer> referenceString, int numFrames) {
        System.out.println("\n--- FIFO Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        java.util.Queue<Integer> fifoQueue = new java.util.LinkedList<>();
        
        int pageFaults = 0;

        // Print table header
        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int page : referenceString) {
            System.out.print(page + "\t| ");
            String status;

            // 1. Page Hit: Page is already present in frames
            if (frames.contains(page)) {
                status = "Hit";
            } 
            // 2. Page Fault: Page is missing
            else {
                pageFaults++;
                status = "Fault";

                // Memory still has free space
                if (frames.size() < numFrames) {
                    frames.add(page);
                    fifoQueue.add(page);
                } 
                // Memory is full -> Evict the oldest page (FIFO)
                else {
                    int oldest = fifoQueue.poll(); // Get and remove oldest page
                    int indexToReplace = frames.indexOf(oldest);
                    frames.set(indexToReplace, page); // Replace in frame layout
                    fifoQueue.add(page); // Add new page to track arrival order
                }
            }

            // Print current frame layout nicely
            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }

    public static void runLRU(List<Integer> referenceString, int numFrames) {
        System.out.println("[STUB] LRU page replacement not implemented yet.");
        // TODO: track last-used time per page, evict least recently used on fault
    }

    public static void runOptimal(List<Integer> referenceString, int numFrames) {
        System.out.println("[STUB] Optimal page replacement not implemented yet.");
        // TODO: evict page used farthest in the future (or never again) on fault
    }
}
