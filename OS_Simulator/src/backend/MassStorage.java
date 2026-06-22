package backend;
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
            case 1 -> runFCFS(getUserRequests(scanner), getHeadStart(scanner));
            case 2 -> runSSTF(getUserRequests(scanner), getHeadStart(scanner));
            case 3 -> runSCAN(getUserRequests(scanner), getHeadStart(scanner), 200);
            case 4 -> runCSCAN(getUserRequests(scanner), getHeadStart(scanner), 200);
            case 5 -> runLOOK(getUserRequests(scanner), getHeadStart(scanner), getDirection(scanner));
            case 6 -> runCLOOK(getUserRequests(scanner), getHeadStart(scanner), getDirection(scanner));
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static List<DiskRequest> getUserRequests(Scanner scanner) {
        System.out.print("How many disk requests? ");
        int numRequests = Integer.parseInt(scanner.nextLine().trim());

        List<DiskRequest> requests = new ArrayList<>();
        for (int i = 1; i <= numRequests; i++) {
            System.out.print("Track number for request " + i + ": ");
            int track = Integer.parseInt(scanner.nextLine().trim());
            requests.add(new DiskRequest(track));
        }
        return requests;
    }

    private static int getHeadStart(Scanner scanner) {
        System.out.print("Initial head position: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static String getDirection(Scanner scanner) {
        System.out.print("Initial direction (right/left): ");
        return scanner.nextLine().trim();
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
            DiskRequest nearest = remaining.get(0);
            int minDistance = Math.abs(nearest.trackNumber - current);

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
        System.out.println("\n--- C-SCAN Disk Scheduling ---");

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

        totalMovement += Math.abs(-current);
        current = 0;
        sequence.add(current);

        for (int track : smaller) {
            totalMovement += Math.abs(track - current);
            current = track;
            sequence.add(current);
        }

        printResult(sequence, totalMovement);
    }

    public static void runLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("\n--- LOOK Disk Scheduling ---");

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

        if (direction.equalsIgnoreCase("right")) {
            // Go right first (larger tracks)
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            // Reverse and go left (smaller tracks, highest to lowest)
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
        } else {
            // Go left first (smaller tracks, highest to lowest)
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            // Reverse and go right (larger tracks, lowest to highest)
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
        }

        printResult(sequence, totalMovement);
    }

    public static void runCLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("\n--- C-LOOK Disk Scheduling ---");

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

        if (direction.equalsIgnoreCase("right")) {
            // Service larger tracks heading right (e.g., 50 -> 65 -> 67 -> 98 -> 122 -> 124 -> 183)
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            // Circular jump: wrap around to the smallest remaining track on the left
            // Then continue moving right (e.g., 14 -> 37)
            if (!smaller.isEmpty()) {
                for (int track : smaller) {
                    totalMovement += Math.abs(track - current);
                    current = track;
                    sequence.add(current);
                }
            }
        } else {
            // Service smaller tracks heading left (e.g., 50 -> 37 -> 14)
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            // Circular jump: wrap around to the largest remaining track on the right
            // Then continue moving left (e.g., 183 -> 124 -> 122 -> 98 -> 67 -> 65)
            if (!larger.isEmpty()) {
                for (int i = larger.size() - 1; i >= 0; i--) {
                    int track = larger.get(i);
                    totalMovement += Math.abs(track - current);
                    current = track;
                    sequence.add(current);
                }
            }
        }

        printResult(sequence, totalMovement);
    }
}