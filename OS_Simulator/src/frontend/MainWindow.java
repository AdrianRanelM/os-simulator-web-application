package frontend;

import javax.swing.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OS Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800,600);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("CPU Scheduling",    new CPUSchedulingPanel());
            tabs.addTab("Memory Management", new MemoryManagementPanel());

            frame.add(tabs);
            frame.setVisible(true);
        });
    }
}