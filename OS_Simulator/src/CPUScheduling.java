import java.util.ArrayList;
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
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFCFS(getUserProcesses(scanner));
            case 2 -> runSJF(getUserProcesses(scanner));
            case 3 -> {
                List<Process> processes = getUserProcesses(scanner);
                System.out.print("Time quantum: ");
                int quantum = Integer.parseInt(scanner.nextLine().trim());
                runRoundRobin(processes, quantum);
            }
            case 4 -> runPriority(getUserProcesses(scanner));
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static List<Process> getUserProcesses(Scanner scanner) {
        System.out.print("How many processes? ");
        int numProcesses = Integer.parseInt(scanner.nextLine().trim());

        List<Process> processes = new ArrayList<>();
        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("Process " + i + ":");
            System.out.print("  Arrival time: ");
            int arrival = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("  Burst time: ");
            int burst = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("  Priority (lower number = higher priority): ");
            int priority = Integer.parseInt(scanner.nextLine().trim());
            processes.add(new Process(i, arrival, burst, priority));
        }
        return processes;
    }

    public static void runFCFS(List<Process> processes) {
        System.out.println("\n--- FCFS Scheduling ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        processes.sort((p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");

        for (Process p : processes) {
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

        printSchedulingTable(processes, totalWaitingTime, totalTurnaroundTime);
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
                    }
                    else if (p.burstTime == minBurst) {
                        if (p.arrivalTime < processes.get(idx).arrivalTime) {
                            idx = i;
                        }
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

    public static void runRoundRobin(List<Process> processes, int quantum) {
        System.out.println("\n--- Round Robin Scheduling (Quantum: " + quantum + "s) ---");

        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        int n = processes.size();
        processes.sort((p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));

        java.util.Queue<Process> readyQueue = new java.util.LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();
        boolean[] inQueue = new boolean[n];

        int currentTime = processes.get(0).arrivalTime;
        readyQueue.add(processes.get(0));
        inQueue[0] = true;

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
                Process nextP = processes.get(i);
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
                for (int i = 0; i < n; i++) {
                    if (processes.get(i).remainingTime > 0) {
                        System.out.print(" IDLE (" + (processes.get(i).arrivalTime - currentTime) + "s) |");
                        currentTime = processes.get(i).arrivalTime;
                        readyQueue.add(processes.get(i));
                        inQueue[i] = true;
                        break;
                    }
                }
            }
        }
        System.out.println(" (End: " + currentTime + "s)");

        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }

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
                    }
                    else if (p.priority == highestPriority) {
                        if (p.arrivalTime < processes.get(idx).arrivalTime) {
                            idx = i;
                        }
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