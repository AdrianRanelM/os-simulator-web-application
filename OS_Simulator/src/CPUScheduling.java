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
        System.out.println("2. SJF (Non-preemptive)");
        System.out.println("3. Round Robin");
        System.out.println("4. Priority");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFCFS(getSampleProcesses());
            case 2 -> runSJF(getSampleProcesses());
            case 3 -> runRoundRobin(getSampleProcesses(), 2);
            case 4 -> runPriority(getSampleProcesses());
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    // TODO: replace with real user input later
    private static List<Process> getSampleProcesses() {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5, 2));
        processes.add(new Process(2, 1, 3, 1));
        processes.add(new Process(3, 2, 8, 3));
        return processes;
    }

    public static void runFCFS(List<Process> processes) {
        System.out.println("\n--- FCFS Scheduling ---");
        
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        // 1. Sort processes by arrivalTime using your original field name
        processes.sort((p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));
        
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        
        System.out.println("\nGantt Chart Timeline:");
        System.out.print("|");
        
        for (Process p : processes) {
            // If the CPU is idle waiting for the next process to arrive
            if (currentTime < p.arrivalTime) {
                System.out.print(" IDLE (" + (p.arrivalTime - currentTime) + "s) |");
                currentTime = p.arrivalTime;
            }
            
            // Execute current process using p.pid and p.burstTime
            System.out.print(" P" + p.pid + " (" + p.burstTime + "s) |");
            
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            
            // Accumulate totals for averages
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }
        System.out.println(" (End: " + currentTime + "s)");
        
        // 2. Display the evaluation results table
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
        System.out.println("\n--- SJF (Non-preemptive) Scheduling ---");
        
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
            
            // Find the arrived process with the minimum burst time
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
                    if (p.burstTime < minBurst) {
                        minBurst = p.burstTime;
                        idx = i;
                    }
                    // Tie-breaker: If burst times are equal, pick the one that arrived first
                    else if (p.burstTime == minBurst) {
                        if (p.arrivalTime < processes.get(idx).arrivalTime) {
                            idx = i;
                        }
                    }
                }
            }
            
            // If no process has arrived yet, CPU is idle until the next closest arrival
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
            
            // Execute the selected shortest job
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
        
        // Print the evaluation metrics table using our shared helper method
        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }

    public static void runRoundRobin(List<Process> processes, int quantum) {
        System.out.println("\n--- Round Robin Scheduling (Quantum: " + quantum + "s) ---");
        
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        int n = processes.size();
        // Sort initially by arrival time to properly handle initialization
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
            
            // Determine runtime duration (min of quantum or remaining time)
            int executionTime = Math.min(quantum, p.remainingTime);
            System.out.print(" P" + p.pid + " (" + executionTime + "s) |");

            p.remainingTime -= executionTime;
            currentTime += executionTime;

            // 1. Check for newly arrived processes during this execution window and queue them
            for (int i = 0; i < n; i++) {
                Process nextP = processes.get(i);
                if (nextP.arrivalTime <= currentTime && !inQueue[i] && nextP.remainingTime > 0) {
                    readyQueue.add(nextP);
                    inQueue[i] = true;
                }
            }

            // 2. If current process is finished, calculate metrics
            if (p.remainingTime == 0) {
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                totalWaitingTime += p.waitingTime;
                totalTurnaroundTime += p.turnaroundTime;
                completedProcesses.add(p);
            } else {
                // If it still has burst time remaining, send it to the back of the queue
                readyQueue.add(p);
            }

            // 3. System Idle handling: if queue is empty but total jobs aren't done
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

        // Print final statistics table
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
            int highestPriority = Integer.MAX_VALUE; // Lower number = higher priority
            
            // Find the arrived process with the highest priority (lowest priority number)
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
                    if (p.priority < highestPriority) {
                        highestPriority = p.priority;
                        idx = i;
                    }
                    // Tie-breaker: If priorities are identical, pick the one that arrived first
                    else if (p.priority == highestPriority) {
                        if (p.arrivalTime < processes.get(idx).arrivalTime) {
                            idx = i;
                        }
                    }
                }
            }
            
            // If no process has arrived yet, the CPU sits idle
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
            
            // Execute the selected process
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
        
        // Print the final evaluation stats table
        printSchedulingTable(completedProcesses, totalWaitingTime, totalTurnaroundTime);
    }
}
