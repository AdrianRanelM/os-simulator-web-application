import java.util.Scanner;

/**
 * OS Simulator - Main entry point.
 * Routes user to one of four modules based on menu selection.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("        OS SIMULATOR - MAIN MENU");
            System.out.println("========================================");
            System.out.println("4. CPU Scheduling");
            System.out.println("5. Memory Management");
            System.out.println("6. Virtual Memory");
            System.out.println("7. Mass Storage Management");
            System.out.println("0. Exit");
            System.out.print("Choose a module: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 4 -> CPUScheduling.menu(scanner);
                case 5 -> MemoryManagement.menu(scanner);
                case 6 -> VirtualMemory.menu(scanner);
                case 7 -> MassStorage.menu(scanner);
                case 0 -> {
                    System.out.println("Exiting OS Simulator. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please select 4, 5, 6, 7, or 0.");
            }
        }

        scanner.close();
    }
}
