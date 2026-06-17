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
        System.out.println("[STUB] FCFS scheduling not implemented yet.");
        // TODO: sort by arrival time, calculate completion/waiting/turnaround
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
