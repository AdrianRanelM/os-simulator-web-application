import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module 5: Memory Management
 * Algorithms: First Fit, Best Fit, Worst Fit
 */
public class MemoryManagement {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- Memory Management ---");
        System.out.println("1. First Fit");
        System.out.println("2. Best Fit");
        System.out.println("3. Worst Fit");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> runFirstFit(getUserBlocks(scanner), getUserProcessSizes(scanner));
            case 2 -> runBestFit(getUserBlocks(scanner), getUserProcessSizes(scanner));
            case 3 -> runWorstFit(getUserBlocks(scanner), getUserProcessSizes(scanner));
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    // TODO: replace with real user input later
    private static List<MemoryBlock> getUserBlocks(Scanner scanner) {
        System.out.print("How many memory blocks? ");
        int numBlocks = Integer.parseInt(scanner.nextLine().trim());

        List<MemoryBlock> blocks = new ArrayList<>();
        for (int i = 1; i <= numBlocks; i++) {
            System.out.print("Size of Block " + i + ": ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            blocks.add(new MemoryBlock(i, size));
        }
        return blocks;
    }

    private static List<Integer> getUserProcessSizes(Scanner scanner) {
        System.out.print("How many processes? ");
        int numProcesses = Integer.parseInt(scanner.nextLine().trim());

        List<Integer> sizes = new ArrayList<>();
        for (int i = 1; i <= numProcesses; i++) {
            System.out.print("Size of Process " + i + ": ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            sizes.add(size);
        }
        return sizes;
    }

    public static void runFirstFit(List<MemoryBlock> blocks, List<Integer> processSizes) {
        System.out.println("\n--- First Fit Allocation ---");

        for (int i = 0; i < processSizes.size(); i++) {
            int processSize = processSizes.get(i);
            int processId = i + 1;

            MemoryBlock block = findFirstFit(blocks, processSize);

            if (block != null) {
                block.free = false;
                block.processId = processId;
                block.processSize = processSize;
                System.out.println("Process " + processId + " (" + processSize + ") -> Block " + block.id);
            } else {
                System.out.println("Process " + processId + " (" + processSize + ") -> NOT PLACED (no suitable block)");
            }
        }

        printMemoryMap(blocks);
        printFragmentation(blocks);
    }

    private static MemoryBlock findFirstFit(List<MemoryBlock> blocks, int processSize) {
        for (MemoryBlock block : blocks) {
            if (block.free && block.size >= processSize) {
                return block;
            }
        }
        return null;

    }



    public static void runBestFit(List<MemoryBlock> blocks, List<Integer> processSizes) {
        System.out.println("\n--- Best Fit Allocation ---");

        for (int i = 0; i < processSizes.size(); i++) {
            int processSize = processSizes.get(i);
            int processId = i + 1;

            MemoryBlock block = findBestFit(blocks, processSize);

            if (block != null) {
                block.free = false;
                block.processId = processId;
                block.processSize = processSize;
                System.out.println("Process " + processId + " (" + processSize + ") -> Block " + block.id);
            } else {
                System.out.println("Process " + processId + " (" + processSize + ") -> NOT PLACED (no suitable block)");
            }
        }

        printMemoryMap(blocks);
        printFragmentation(blocks);
    }

    private static MemoryBlock findBestFit(List<MemoryBlock> blocks, int processSize) {
        MemoryBlock bestBlock = null;

        for (MemoryBlock block : blocks) {
            if (block.free && block.size >= processSize) {
                if (bestBlock == null || block.size < bestBlock.size) {
                    bestBlock = block;
                }
            }
        }

        return bestBlock;
    }

    public static void runWorstFit(List<MemoryBlock> blocks, List<Integer> processSizes) {
        System.out.println("\n--- Worst Fit Allocation ---");

        for (int i = 0; i < processSizes.size(); i++) {
            int processSize = processSizes.get(i);
            int processId = i + 1;

            MemoryBlock block = findWorstFit(blocks, processSize);

            if (block != null) {
                block.free = false;
                block.processId = processId;
                block.processSize = processSize;
                System.out.println("Process " + processId + " (" + processSize + ") -> Block " + block.id);
            } else {
                System.out.println("Process " + processId + " (" + processSize + ") -> NOT PLACED (no suitable block)");
            }
        }

        printMemoryMap(blocks);
        printFragmentation(blocks);
    }

    private static MemoryBlock findWorstFit(List<MemoryBlock> blocks, int processSize) {
        MemoryBlock worstBlock = null;

        for (MemoryBlock block : blocks) {
            if (block.free && block.size >= processSize) {
                if (worstBlock == null || block.size > worstBlock.size) {
                    worstBlock = block;
                }
            }
        }

        return worstBlock;
    }
    private static void printMemoryMap(List<MemoryBlock> blocks) {
        System.out.println("\nMemory Map:");
        for (MemoryBlock block : blocks) {
            if (block.free) {
                System.out.println("Block " + block.id + " | Size: " + block.size + " | FREE");
            } else {
                int waste = block.size - block.processSize;
                System.out.println("Block " + block.id + " | Size: " + block.size +
                        " | Process " + block.processId + " | Waste: " + waste);
            }
        }
    }

    private static void printFragmentation(List<MemoryBlock> blocks) {
        int internalFrag = 0;
        int externalFrag = 0;

        for (MemoryBlock block : blocks) {
            if (block.free) {
                externalFrag += block.size;
            } else {
                internalFrag += (block.size - block.processSize);
            }
        }

        System.out.println("\nInternal fragmentation: " + internalFrag);
        System.out.println("External fragmentation: " + externalFrag);
    }

}
