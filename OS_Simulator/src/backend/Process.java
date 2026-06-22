package backend;
/**
 * Represents a process for CPU Scheduling (Module 4).
 * Pure data holder - no logic here, just fields.
 */
public class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int remainingTime;   // used by Round Robin
    int completionTime;
    int waitingTime;
    int turnaroundTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    @Override
    public String toString() {
        return "P" + pid + " [arrival=" + arrivalTime + ", burst=" + burstTime + ", priority=" + priority + "]";
    }
}
