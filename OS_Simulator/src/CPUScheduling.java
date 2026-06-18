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
        System.out.println("[STUB] SJF scheduling not implemented yet.");
        // TODO: at each free slot, pick arrived process with min burst time
    }

    public static void runRoundRobin(List<Process> processes, int quantum) {
        System.out.println("[STUB] Round Robin scheduling not implemented yet.");
        // TODO: circular queue, run min(quantum, remaining) per turn
    }

    public static void runPriority(List<Process> processes) {
        System.out.println("[STUB] Priority scheduling not implemented yet.");
        // TODO: pick lowest/highest priority among arrived processes
    }
}
