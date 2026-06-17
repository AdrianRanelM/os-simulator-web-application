OS SIMULATOR - PROJECT SKELETON
=================================

WHAT THIS IS
------------
This is Step 1 of the build: the project skeleton. It compiles and runs,
but each algorithm (FCFS, SJF, First Fit, FIFO, SCAN, etc.) is a stub that
just prints "[STUB] ... not implemented yet." Your job from here is to fill
in the TODO comments inside each module class - Main.java and the shared
data classes (Process, MemoryBlock, PageTableEntry, DiskRequest) should NOT
need to change again.

FOLDER STRUCTURE
-----------------
OS_Simulator/
  src/
    Main.java               - menu loop, routes to each module
    Process.java             - shared struct (Module 4)
    MemoryBlock.java          - shared struct (Module 5)
    PageTableEntry.java       - shared struct (Module 6)
    DiskRequest.java          - shared struct (Module 7)
    CPUScheduling.java        - Module 4 (FCFS, SJF, Round Robin, Priority)
    MemoryManagement.java     - Module 5 (First Fit, Best Fit, Worst Fit)
    VirtualMemory.java        - Module 6 (FIFO, LRU, Optimal)
    MassStorage.java          - Module 7 (FCFS, SSTF, SCAN, C-SCAN)
  bin/                       - compiled .class files go here (created on build)
  README.txt

HOW TO COMPILE
---------------
From the OS_Simulator folder, run:

    javac -d bin src/*.java

This compiles every .java file in src/ and puts the .class files in bin/.

HOW TO RUN
-----------
    java -cp bin Main

You should see the main menu:

    ========================================
            OS SIMULATOR - MAIN MENU
    ========================================
    4. CPU Scheduling
    5. Memory Management
    6. Virtual Memory
    7. Mass Storage Management
    0. Exit
    Choose a module:

Pick a module number, then pick an algorithm inside it. Right now every
algorithm just prints a stub message - that's expected at this stage.

WHAT TO DO NEXT
-----------------
Pick ONE module and fill in its algorithm logic (the TODO comments inside
e.g. CPUScheduling.java). Because Main.java already knows how to call every
method, and the data structures (Process, MemoryBlock, etc.) are already
fixed, you can build out one module at a time without breaking anything
else. Suggested order: Module 4 (CPU Scheduling) first, since it's the one
already partially planned in your project board.

PACKAGING TO .EXE (DO THIS LAST, ONCE ALL 4 MODULES WORK)
------------------------------------------------------------
1. Compile as above (javac -d bin src/*.java)
2. Create a runnable jar:
       jar cfe OS_Simulator.jar Main -C bin .
3. Convert jar to exe using one of:
   - jpackage (built into JDK 14+):
       jpackage --input . --main-jar OS_Simulator.jar --main-class Main --type exe --name OS_Simulator
   - launch4j (GUI tool, download separately): point it at OS_Simulator.jar

SUBMISSION CHECKLIST
----------------------
[ ] OS_Simulator.exe
[ ] Full src/ folder (all .java files)
[ ] This README.txt
