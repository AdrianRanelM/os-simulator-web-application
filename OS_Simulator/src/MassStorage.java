import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module 7: Mass Storage Management
 * Disk scheduling algorithms: FCFS, SSTF, SCAN, C-SCAN, LOOK, C-LOOK
 */
public class MassStorage {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- Mass Storage Management ---");
        System.out.println("1. FCFS");
        System.out.println("2. SSTF");
        System.out.println("3. SCAN");
        System.out.println("4. C-SCAN");
        System.out.println("5. LOOK");
        System.out.println("6. C-LOOK");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFCFS(getSampleRequests(), 50);
            case 2 -> runSSTF(getSampleRequests(), 50);
            case 3 -> runSCAN(getSampleRequests(), 50, 200);
            case 4 -> runCSCAN(getSampleRequests(), 50, 200);
            case 5 -> runLOOK(getSampleRequests(), 50, "right");
            case 6 -> runCLOOK(getSampleRequests(), 50, "right");
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
        System.out.println("\n--- FCFS Disk Scheduling ---");

        int totalMovement = 0;
        int current = headStart;
        List<Integer> sequence = new ArrayList<>();

        for (DiskRequest req : requests) {
            int distance = Math.abs(req.trackNumber - current);
            totalMovement += distance;
            current = req.trackNumber;
            sequence.add(current);
        }

        printResult(sequence, totalMovement);
    }

    private static void printResult(List<Integer> sequence, int totalMovement) {
        System.out.println("Seek sequence: " + sequence);
        System.out.println("Total head movement: " + totalMovement);
        double avgSeek = (double) totalMovement / sequence.size();
        System.out.printf("Average seek length: %.2f%n", avgSeek);
    }

    public static void runSSTF(List<DiskRequest> requests, int headStart) {
        System.out.println("\n--- SSTF Disk Scheduling ---");

        List<DiskRequest> remaining = new ArrayList<>(requests);
        int current = headStart;
        int totalMovement = 0;
        List<Integer> sequence = new ArrayList<>();

        while (!remaining.isEmpty()) {
            DiskRequest nearest = null;
            int minDistance = Integer.MAX_VALUE;

            for (DiskRequest req : remaining) {
                int distance = Math.abs(req.trackNumber - current);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = req;
                }
            }

            totalMovement += minDistance;
            current = nearest.trackNumber;
            sequence.add(current);
            remaining.remove(nearest);
        }

        printResult(sequence, totalMovement);
    }

    public static void runSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("\n--- SCAN Disk Scheduling ---");

        List<Integer> smaller = new ArrayList<>();
        List<Integer> larger = new ArrayList<>();

        for (DiskRequest req : requests) {
            if (req.trackNumber < headStart) {
                smaller.add(req.trackNumber);
            } else {
                larger.add(req.trackNumber);
            }
        }

        java.util.Collections.sort(smaller);
        java.util.Collections.sort(larger);

        List<Integer> sequence = new ArrayList<>();
        int current = headStart;
        int totalMovement = 0;

        for (int track : larger) {
            totalMovement += Math.abs(track - current);
            current = track;
            sequence.add(current);
        }

        totalMovement += Math.abs((diskSize - 1) - current);
        current = diskSize - 1;
        sequence.add(current);

        for (int i = smaller.size() - 1; i >= 0; i--) {
            int track = smaller.get(i);
            totalMovement += Math.abs(track - current);
            current = track;
            sequence.add(current);
        }

        printResult(sequence, totalMovement);
    }

    public static void runCSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("[STUB] C-SCAN disk scheduling not implemented yet.");
        // TODO: sweep one direction, jump to track 0 after reaching end
    }

    public static void runLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("[STUB] LOOK disk scheduling not implemented yet.");
        // TODO: sweep one direction servicing requests, reverse at the LAST request
        //       (not the disk boundary) - see pseudocode below this method
    }

    public static void runCLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("[STUB] C-LOOK disk scheduling not implemented yet.");
        // TODO: sweep one direction servicing requests, then jump to the SMALLEST
        //       remaining request (not track 0) and continue sweeping
    }
}
