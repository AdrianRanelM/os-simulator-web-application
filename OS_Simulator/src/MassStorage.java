import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module 7: Mass Storage Management
 * Disk scheduling algorithms: FCFS, SSTF, SCAN, C-SCAN
 */
public class MassStorage {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- Mass Storage Management ---");
        System.out.println("1. FCFS");
        System.out.println("2. SSTF");
        System.out.println("3. SCAN");
        System.out.println("4. C-SCAN");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFCFS(getSampleRequests(), 50);
            case 2 -> runSSTF(getSampleRequests(), 50);
            case 3 -> runSCAN(getSampleRequests(), 50, 200);
            case 4 -> runCSCAN(getSampleRequests(), 50, 200);
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    // TODO: replace with real user input later
    private static List<DiskRequest> getSampleRequests() {
        List<DiskRequest> requests = new ArrayList<>();
        int[] sample = {98, 183, 37, 122, 14, 124, 65, 67};
        for (int t : sample) requests.add(new DiskRequest(t));
        return requests;
    }

    public static void runFCFS(List<DiskRequest> requests, int headStart) {
        System.out.println("[STUB] FCFS disk scheduling not implemented yet.");
        // TODO: service requests in arrival order, sum absolute differences
    }

    public static void runSSTF(List<DiskRequest> requests, int headStart) {
        System.out.println("[STUB] SSTF disk scheduling not implemented yet.");
        // TODO: repeatedly pick nearest unvisited request to current head
    }

    public static void runSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("[STUB] SCAN disk scheduling not implemented yet.");
        // TODO: sweep one direction, reverse at disk boundary
    }

    public static void runCSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("[STUB] C-SCAN disk scheduling not implemented yet.");
        // TODO: sweep one direction, jump to track 0 after reaching end
    }
}
