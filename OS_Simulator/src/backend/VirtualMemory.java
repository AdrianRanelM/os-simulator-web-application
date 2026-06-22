package backend;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module 6: Virtual Memory
 * Page replacement algorithms: FIFO, LRU, Optimal
 */
public class VirtualMemory {

    public static void menu(Scanner scanner) {
        System.out.println("\n--- Virtual Memory ---");
        System.out.println("1. FIFO");
        System.out.println("2. LRU");
        System.out.println("3. Optimal");
        System.out.println("0. Back to main menu");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> {
                List<Integer> refString = getUserReferenceString(scanner);
                int frames = getNumFrames(scanner);
                runFIFO(refString, frames);
            }
            case 2 -> {
                List<Integer> refString = getUserReferenceString(scanner);
                int frames = getNumFrames(scanner);
                runLRU(refString, frames);
            }
            case 3 -> {
                List<Integer> refString = getUserReferenceString(scanner);
                int frames = getNumFrames(scanner);
                runOptimal(refString, frames);
            }
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static List<Integer> getUserReferenceString(Scanner scanner) {
        System.out.print("How many pages in the reference string? ");
        int numPages = Integer.parseInt(scanner.nextLine().trim());

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            System.out.print("Page " + i + ": ");
            int page = Integer.parseInt(scanner.nextLine().trim());
            pages.add(page);
        }
        return pages;
    }

    private static int getNumFrames(Scanner scanner) {
        System.out.print("Number of frames: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public static void runFIFO(List<Integer> referenceString, int numFrames) {
        System.out.println("\n--- FIFO Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        java.util.Queue<Integer> fifoQueue = new java.util.LinkedList<>();

        int pageFaults = 0;

        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int page : referenceString) {
            System.out.print(page + "\t| ");
            String status;

            if (frames.contains(page)) {
                status = "Hit";
            }
            else {
                pageFaults++;
                status = "Fault";

                if (frames.size() < numFrames) {
                    frames.add(page);
                    fifoQueue.add(page);
                }
                else {
                    Integer oldest = fifoQueue.poll();
                    if (oldest != null) {
                        int indexToReplace = frames.indexOf(oldest);
                        frames.set(indexToReplace, page);
                        fifoQueue.add(page);
                    }
                }
            }

            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }

    public static void runLRU(List<Integer> referenceString, int numFrames) {
        System.out.println("\n--- LRU Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        List<Integer> recentUsage = new ArrayList<>();

        int pageFaults = 0;

        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int page : referenceString) {
            System.out.print(page + "\t| ");
            String status;

            if (frames.contains(page)) {
                status = "Hit";
                recentUsage.remove((Integer) page);
                recentUsage.add(page);
            }
            else {
                pageFaults++;
                status = "Fault";

                if (frames.size() < numFrames) {
                    frames.add(page);
                    recentUsage.add(page);
                }
                else {
                    int lruPage = -1;
                    for (int uPage : recentUsage) {
                        if (frames.contains(uPage)) {
                            lruPage = uPage;
                            break;
                        }
                    }

                    int indexToReplace = frames.indexOf(lruPage);
                    frames.set(indexToReplace, page);

                    recentUsage.remove((Integer) lruPage);
                    recentUsage.add(page);
                }
            }

            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }

    public static void runOptimal(List<Integer> referenceString, int numFrames) {
        System.out.println("\n--- Optimal Page Replacement ---");
        System.out.println("Reference String: " + referenceString);
        System.out.println("Number of Frames: " + numFrames + "\n");

        List<Integer> frames = new ArrayList<>();
        int pageFaults = 0;

        System.out.print("Page\t| Memory Frames\t\t| Status\n");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < referenceString.size(); i++) {
            int page = referenceString.get(i);
            System.out.print(page + "\t| ");
            String status;

            if (frames.contains(page)) {
                status = "Hit";
            }
            else {
                pageFaults++;
                status = "Fault";

                if (frames.size() < numFrames) {
                    frames.add(page);
                }
                else {
                    int indexToReplace = -1;
                    int farthestFutureUse = -1;

                    for (int f = 0; f < frames.size(); f++) {
                        int currentFramePage = frames.get(f);
                        int nextUseIndex = Integer.MAX_VALUE;

                        for (int j = i + 1; j < referenceString.size(); j++) {
                            if (referenceString.get(j) == currentFramePage) {
                                nextUseIndex = j;
                                break;
                            }
                        }

                        if (nextUseIndex == Integer.MAX_VALUE) {
                            indexToReplace = f;
                            break;
                        }

                        if (nextUseIndex > farthestFutureUse) {
                            farthestFutureUse = nextUseIndex;
                            indexToReplace = f;
                        }
                    }

                    frames.set(indexToReplace, page);
                }
            }

            System.out.print(frames + "\t".repeat(Math.max(1, 3 - frames.size())) + "| " + status + "\n");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + (referenceString.size() - pageFaults));
    }
}