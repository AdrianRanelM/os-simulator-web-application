import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Module 4: CPU Scheduling
 * Algorithms: FCFS, SJF, Round Robin, Priority
 */
public class CPUScheduling {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- CPU Scheduling ---");
        System.out.println("1. FCFS");
        System.out.println("2. SJF ");
        System.out.println("3. Round Robin");
        System.out.println("4. Priority");
        System.out.println("0. Back to main menu");

        int choice = readInt(scanner, "Choose: ");

        switch (choice) {
            case 1 -> runFCFS(getUserProcesses(scanner));
            case 2 -> runSJF(getUserProcesses(scanner));
            case 3 -> {
                List<Process> processes = getUserProcesses(scanner);
                int quantum = readPositiveInt(scanner, "Time quantum: ");
                runRoundRobin(processes, quantum);
            }
            case 4 -> runPriority(getUserProcesses(scanner));
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---------- Input helpers ----------

    /** Reads an integer, re-prompting on invalid (non-numeric) input. */
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }

    /** Reads an integer >= 0, re-prompting on invalid or negative input. */
    private static int readNonNegativeInt(Scanner scanner, String prompt) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value < 0) {
                System.out.println("Value cannot be negative, please try again.");
                continue;
            }
            return value;
        }
    }

    /** Reads an integer >= 1, re-prompting on invalid, zero, or negative input. */
    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value <= 0) {
                System.out.println("Value must be greater than 0, please try again.");
                continue;
            }
            return value;
        }
    }

    private static List<Process> getUserProcesses(Scanner scanner) {
        int numProcesses = readPositiveInt(scanner, "How many processes? ");

        List<Process> processes = new ArrayList<>();
        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("Process " + i + ":");
            int arrival = readNonNegativeInt(scanner, "  Arrival time: ");
            int burst = readPositiveInt(scanner, "  Burst time: ");
            int priority = readNonNegativeInt(scanner, "  Priority (lower number = higher priority): ");
            processes.add(new Process(i, arrival, burst, priority));
        }
        return processes;
    }

    // ---------- FCFS ----------

    public static void runFCFS(List<Process> processes) {
        System.out.println("\n--- FCFS Scheduling ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");

        for (Process p : sorted) {
            if (currentTime < p.arrivalTime) {
                System.out.print(" IDLE (" + (p.arrivalTime - currentTime) + "s) |");
                currentTime = p.arrivalTime;
            }

            System.out.print(" P" + p.pid + " (" + p.burstTime + "s) |");

            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;

            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }
        System.out.println(" (End: " + currentTime + "s)");

        printSchedulingTable(sorted, totalWaitingTime, totalTurnaroundTime);
    }

    private static void printSchedulingTable(List<Process> processes, int totalWT, int totalTAT) {
        System.out.println("\nProcess\tArrival\tBurst\tPriority\tExit\tTurnaround\tWaiting");
        for (Process p : processes) {
            System.out.println("P" + p.pid + "\t" +
                    p.arrivalTime + "\t" +
                    p.burstTime + "\t" +
                    p.priority + "\t\t" +
                    p.completionTime + "\t" +
                    p.turnaroundTime + "\t\t" +
                    p.waitingTime);
        }

        double avgWT = (double) totalWT / processes.size();
        double avgTAT = (double) totalTAT / processes.size();
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
    }

    // ---------- SJF (non-preemptive) ----------

    public static void runSJF(List<Process> processes) {
        System.out.println("\n--- SJF Scheduling ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        int n = processes.size();
        List<Process> completedProcesses = new ArrayList<>();
        boolean[] isCompleted = new boolean[n];

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");

        while (completedProcesses.size() < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
                    if (p.burstTime < minBurst) {
                        minBurst = p.burstTime;
                        idx = i;
                    } else if (p.burstTime == minBurst && idx != -1
                            && p.arrivalTime < processes.get(idx).arrivalTime) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                int nextArrivalTime = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!isCompleted[i] && processes.get(i).arrivalTime < nextArrivalTime) {
                        nextArrivalTime = processes.get(i).arrivalTime;
                    }
                }
                System.out.print(" IDLE (" + (nextArrivalTime - currentTime) + "s) |");
                currentTime = nextArrivalTime;
                continue;
            }

            Process p = processes.get(idx);
            System.out.print(" P" + p.pid + " (" + p.burstTime + "s) |");

            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;

            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;

            isCompleted[idx] = true;
            completedProcesses.add(p);
        }
        System.out.println(" (End: " + currentTime + "s)");

        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }

    // ---------- Round Robin ----------

    public static void runRoundRobin(List<Process> processes, int quantum) {
        System.out.println("\n--- Round Robin Scheduling (Quantum: " + quantum + "s) ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }
        if (quantum <= 0) {
            System.out.println("Quantum must be greater than 0.");
            return;
        }

        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int n = sorted.size();

        java.util.Queue<Process> readyQueue = new java.util.LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();
        boolean[] inQueue = new boolean[n];

        int currentTime = sorted.get(0).arrivalTime;

        // Seed the queue with every process that has arrived by currentTime
        // (handles multiple processes arriving at the same earliest time)
        for (int i = 0; i < n; i++) {
            if (sorted.get(i).arrivalTime <= currentTime) {
                readyQueue.add(sorted.get(i));
                inQueue[i] = true;
            }
        }

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");

        while (!readyQueue.isEmpty()) {
            Process p = readyQueue.poll();

            int executionTime = Math.min(quantum, p.remainingTime);
            System.out.print(" P" + p.pid + " (" + executionTime + "s) |");

            p.remainingTime -= executionTime;
            currentTime += executionTime;

            for (int i = 0; i < n; i++) {
                Process nextP = sorted.get(i);
                if (nextP.arrivalTime <= currentTime && !inQueue[i] && nextP.remainingTime > 0) {
                    readyQueue.add(nextP);
                    inQueue[i] = true;
                }
            }

            if (p.remainingTime == 0) {
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                totalWaitingTime += p.waitingTime;
                totalTurnaroundTime += p.turnaroundTime;
                completedProcesses.add(p);
            } else {
                readyQueue.add(p);
            }

            if (readyQueue.isEmpty() && completedProcesses.size() < n) {
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!inQueue[i] && sorted.get(i).remainingTime > 0
                            && sorted.get(i).arrivalTime < nextArrival) {
                        nextArrival = sorted.get(i).arrivalTime;
                    }
                }

                if (nextArrival != Integer.MAX_VALUE) {
                    System.out.print(" IDLE (" + (nextArrival - currentTime) + "s) |");
                    currentTime = nextArrival;

                    // Add EVERY process that arrives at this time, not just one
                    for (int i = 0; i < n; i++) {
                        if (!inQueue[i] && sorted.get(i).remainingTime > 0
                                && sorted.get(i).arrivalTime == nextArrival) {
                            readyQueue.add(sorted.get(i));
                            inQueue[i] = true;
                        }
                    }
                }
            }
        }
        System.out.println(" (End: " + currentTime + "s)");

        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }

    // ---------- Priority (non-preemptive) ----------

    public static void runPriority(List<Process> processes) {
        System.out.println("\n--- Priority (Non-preemptive) Scheduling ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        int n = processes.size();
        List<Process> completedProcesses = new ArrayList<>();
        boolean[] isCompleted = new boolean[n];

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");

        while (completedProcesses.size() < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
                    if (p.priority < highestPriority) {
                        highestPriority = p.priority;
                        idx = i;
                    } else if (p.priority == highestPriority && idx != -1
                            && p.arrivalTime < processes.get(idx).arrivalTime) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                int nextArrivalTime = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!isCompleted[i] && processes.get(i).arrivalTime < nextArrivalTime) {
                        nextArrivalTime = processes.get(i).arrivalTime;
                    }
                }
                System.out.print(" IDLE (" + (nextArrivalTime - currentTime) + "s) |");
                currentTime = nextArrivalTime;
                continue;
            }

            Process p = processes.get(idx);
            System.out.print(" P" + p.pid + " (" + p.burstTime + "s) |");

            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;

            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;

            isCompleted[idx] = true;
            completedProcesses.add(p);
        }
        System.out.println(" (End: " + currentTime + "s)");

        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }
}