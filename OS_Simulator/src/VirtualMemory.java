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
        System.out.println("\n--- LRU Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        // Tracks the order of recent usage (end of list = most recently used)
        List<Integer> recentUsage = new ArrayList<>();
        
        int pageFaults = 0;

        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int page : referenceString) {
            System.out.print(page + "\t| ");
            String status;

            // 1. Page Hit
            if (frames.contains(page)) {
                status = "Hit";
                // Move the page to the end of recentUsage to mark it as newest
                recentUsage.remove((Integer) page);
                recentUsage.add(page);
            } 
            // 2. Page Fault
            else {
                pageFaults++;
                status = "Fault";

                // If there's open space in frames
                if (frames.size() < numFrames) {
                    frames.add(page);
                    recentUsage.add(page);
                } 
                // Memory is full -> Evict Least Recently Used
                else {
                    // Find which page in 'frames' appears first (oldest usage) in recentUsage
                    int lruPage = -1;
                    for (int uPage : recentUsage) {
                        if (frames.contains(uPage)) {
                            lruPage = uPage;
                            break; // First one found is the least recently used
                        }
                    }

                    int indexToReplace = frames.indexOf(lruPage);
                    frames.set(indexToReplace, page);
                    
                    // Update usage tracker
                    recentUsage.remove((Integer) lruPage);
                    recentUsage.add(page);
                }
            }

            // Print layout
            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }

    public static void runOptimal(List<Integer> referenceString, int numFrames) {
        System.out.println("\n--- Optimal Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        int pageFaults = 0;

        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < referenceString.size(); i++) {
            int page = referenceString.get(i);
            System.out.print(page + "\t| ");
            String status;

            // 1. Page Hit
            if (frames.contains(page)) {
                status = "Hit";
            } 
            // 2. Page Fault
            else {
                pageFaults++;
                status = "Fault";

                // Space available
                if (frames.size() < numFrames) {
                    frames.add(page);
                } 
                // Memory full -> Look into the future to find the best page to evict
                else {
                    int indexToReplace = -1;
                    int farthestFutureUse = -1;

                    for (int f = 0; f < frames.size(); f++) {
                        int currentFramePage = frames.get(f);
                        int nextUseIndex = Integer.MAX_VALUE; // Default to infinity if never used again

                        // Scan ahead in reference string
                        for (int j = i + 1; j < referenceString.size(); j++) {
                            if (referenceString.get(j) == currentFramePage) {
                                nextUseIndex = j;
                                break;
                            }
                        }

                        // If this page is never used again, evict it immediately
                        if (nextUseIndex == Integer.MAX_VALUE) {
                            indexToReplace = f;
                            break;
                        }

                        // Track the page that is used farthest in the future
                        if (nextUseIndex > farthestFutureUse) {
                            farthestFutureUse = nextUseIndex;
                            indexToReplace = f;
                        }
                    }

                    // Replace the optimal target page
                    frames.set(indexToReplace, page);
                }
            }

            // Print layout
            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }
}
