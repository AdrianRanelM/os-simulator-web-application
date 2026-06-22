OS SIMULATOR - PROJECT
=================================


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

SUBMISSION CHECKLIST
----------------------
[ ] OS_Simulator.exe
[ ] Full src/ folder (all .java files)
[ ] This README.txt
