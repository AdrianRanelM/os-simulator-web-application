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
        System.out.println("[STUB] FIFO page replacement not implemented yet.");
        // TODO: queue of frames, evict oldest on fault
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
