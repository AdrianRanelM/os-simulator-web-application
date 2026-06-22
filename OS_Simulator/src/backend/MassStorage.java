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

    // Counts how many moves went right (+) and left (-) given the full sequence including headStart
    private static int[] countDirections(int headStart, List<Integer> sequence) {
        int rightCount = 0;
        int leftCount  = 0;
        int current = headStart;
        for (int next : sequence) {
            if (next > current) rightCount++;
            else if (next < current) leftCount++;
            current = next;
        }
        return new int[]{rightCount, leftCount};
    }

    private static void printResult(int headStart, List<Integer> sequence, int totalMovement) {
        int[] directions = countDirections(headStart, sequence);
        System.out.println("Seek sequence: " + sequence);
        System.out.println("Total head movement: " + totalMovement);
        double avgSeek = (double) totalMovement / sequence.size();
        System.out.printf("Average seek length: %.2f%n", avgSeek);
        System.out.println("Moves to the right:  " + directions[0]);
        System.out.println("Moves to the left:   " + directions[1]);
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

        printResult(headStart, sequence, totalMovement);
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

        printResult(headStart, sequence, totalMovement);
    }

    public static void runSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("\n--- SCAN Disk Scheduling ---");

        List<Integer> smaller = new ArrayList<>();
        List<Integer> larger  = new ArrayList<>();

        for (DiskRequest req : requests) {
            if (req.trackNumber < headStart) smaller.add(req.trackNumber);
            else                             larger.add(req.trackNumber);
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

        printResult(headStart, sequence, totalMovement);
    }

    public static void runCSCAN(List<DiskRequest> requests, int headStart, int diskSize) {
        System.out.println("\n--- C-SCAN Disk Scheduling ---");

        List<Integer> smaller = new ArrayList<>();
        List<Integer> larger  = new ArrayList<>();

        for (DiskRequest req : requests) {
            if (req.trackNumber < headStart) smaller.add(req.trackNumber);
            else                             larger.add(req.trackNumber);
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

        printResult(headStart, sequence, totalMovement);
    }

    public static void runLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("\n--- LOOK Disk Scheduling ---");

        List<Integer> smaller = new ArrayList<>();
        List<Integer> larger  = new ArrayList<>();

        for (DiskRequest req : requests) {
            if (req.trackNumber < headStart) smaller.add(req.trackNumber);
            else                             larger.add(req.trackNumber);
        }

        java.util.Collections.sort(smaller);
        java.util.Collections.sort(larger);

        List<Integer> sequence = new ArrayList<>();
        int current = headStart;
        int totalMovement = 0;

        if (direction.equalsIgnoreCase("right")) {
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
        } else {
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
        }

        printResult(headStart, sequence, totalMovement);
    }

    public static void runCLOOK(List<DiskRequest> requests, int headStart, String direction) {
        System.out.println("\n--- C-LOOK Disk Scheduling ---");

        List<Integer> smaller = new ArrayList<>();
        List<Integer> larger  = new ArrayList<>();

        for (DiskRequest req : requests) {
            if (req.trackNumber < headStart) smaller.add(req.trackNumber);
            else                             larger.add(req.trackNumber);
        }

        java.util.Collections.sort(smaller);
        java.util.Collections.sort(larger);

        List<Integer> sequence = new ArrayList<>();
        int current = headStart;
        int totalMovement = 0;

        if (direction.equalsIgnoreCase("right")) {
            for (int track : larger) {
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            if (!smaller.isEmpty()) {
                for (int track : smaller) {
                    totalMovement += Math.abs(track - current);
                    current = track;
                    sequence.add(current);
                }
            }
        } else {
            for (int i = smaller.size() - 1; i >= 0; i--) {
                int track = smaller.get(i);
                totalMovement += Math.abs(track - current);
                current = track;
                sequence.add(current);
            }
            if (!larger.isEmpty()) {
                for (int i = larger.size() - 1; i >= 0; i--) {
                    int track = larger.get(i);
                    totalMovement += Math.abs(track - current);
                    current = track;
                    sequence.add(current);
                }
            }
        }

        printResult(headStart, sequence, totalMovement);
    }
}