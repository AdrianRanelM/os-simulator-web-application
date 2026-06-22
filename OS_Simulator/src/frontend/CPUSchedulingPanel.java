package frontend;

import backend.CPUScheduling;
import backend.Process;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing front end for Module 4: CPU Scheduling.
 * Does not touch CPUScheduling.java's algorithm logic at all -
 * it only builds input data and calls the existing static methods,
 * then captures their System.out.println output into a text area.
 */
public class CPUSchedulingPanel extends JPanel {

    private JTextField numProcessesField;
    private JPanel processFieldsPanel;

    // Each process row holds: arrival, burst, priority fields
    private final List<JTextField> arrivalFields  = new ArrayList<>();
    private final List<JTextField> burstFields    = new ArrayList<>();
    private final List<JTextField> priorityFields = new ArrayList<>();

    private JComboBox<String> algorithmDropdown;
    private JTextField quantumField;
    private JLabel quantumLabel;
    private JTextArea outputArea;

    public CPUSchedulingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------ UI build

    private JPanel buildInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Row 1: number of processes + generate button
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countRow.add(new JLabel("Number of processes:"));
        numProcessesField = new JTextField(4);
        countRow.add(numProcessesField);
        JButton genBtn = new JButton("Generate Process Fields");
        genBtn.addActionListener(e -> generateProcessFields());
        countRow.add(genBtn);
        container.add(countRow);

        // Row 2: scrollable table of per-process fields
        processFieldsPanel = new JPanel();
        processFieldsPanel.setLayout(new BoxLayout(processFieldsPanel, BoxLayout.Y_AXIS));
        JScrollPane processScroll = new JScrollPane(processFieldsPanel);
        processScroll.setBorder(BorderFactory.createTitledBorder("Process Details"));
        processScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        processScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        processScroll.setPreferredSize(new Dimension(760, 180));
        container.add(processScroll);

        // Row 3: algorithm choice, optional quantum, run button
        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runRow.add(new JLabel("Algorithm:"));
        algorithmDropdown = new JComboBox<>(new String[]{"FCFS", "SJF", "Round Robin", "Priority"});
        algorithmDropdown.addActionListener(e -> toggleQuantumField());
        runRow.add(algorithmDropdown);

        quantumLabel = new JLabel("Time Quantum:");
        quantumField  = new JTextField(4);
        quantumLabel.setVisible(false);
        quantumField.setVisible(false);
        runRow.add(quantumLabel);
        runRow.add(quantumField);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(e -> runAlgorithm());
        runRow.add(runBtn);
        container.add(runRow);

        return container;
    }

    private JPanel buildOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    // ------------------------------------------------------------------ Dynamic fields

    private void generateProcessFields() {
        int count;
        try {
            count = Integer.parseInt(numProcessesField.getText().trim());
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of processes (>= 1).");
            return;
        }

        processFieldsPanel.removeAll();
        arrivalFields.clear();
        burstFields.clear();
        priorityFields.clear();

        // Header row
        JPanel header = new JPanel(new GridLayout(1, 4, 8, 0));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        header.add(new JLabel("Process", SwingConstants.CENTER));
        header.add(new JLabel("Arrival Time", SwingConstants.CENTER));
        header.add(new JLabel("Burst Time", SwingConstants.CENTER));
        header.add(new JLabel("Priority (lower = higher)", SwingConstants.CENTER));
        processFieldsPanel.add(header);

        for (int i = 1; i <= count; i++) {
            JPanel row = new JPanel(new GridLayout(1, 4, 8, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

            row.add(new JLabel("P" + i, SwingConstants.CENTER));

            JTextField arrival  = new JTextField(5);
            JTextField burst    = new JTextField(5);
            JTextField priority = new JTextField(5);

            arrivalFields.add(arrival);
            burstFields.add(burst);
            priorityFields.add(priority);

            row.add(arrival);
            row.add(burst);
            row.add(priority);
            processFieldsPanel.add(row);
        }

        processFieldsPanel.revalidate();
        processFieldsPanel.repaint();
    }

    private void toggleQuantumField() {
        boolean isRR = "Round Robin".equals(algorithmDropdown.getSelectedItem());
        quantumLabel.setVisible(isRR);
        quantumField.setVisible(isRR);
    }

    // ------------------------------------------------------------------ Run

    private void runAlgorithm() {
        if (arrivalFields.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate process fields first.");
            return;
        }

        List<Process> processes;
        try {
            processes = new ArrayList<>();
            for (int i = 0; i < arrivalFields.size(); i++) {
                int arrival  = Integer.parseInt(arrivalFields.get(i).getText().trim());
                int burst    = Integer.parseInt(burstFields.get(i).getText().trim());
                int priority = Integer.parseInt(priorityFields.get(i).getText().trim());

                if (arrival < 0 || burst <= 0 || priority < 0) {
                    JOptionPane.showMessageDialog(this,
                        "Arrival/Priority must be >= 0 and Burst must be >= 1 for P" + (i + 1) + ".");
                    return;
                }

                processes.add(new Process(i + 1, arrival, burst, priority));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please make sure all process fields are valid numbers.");
            return;
        }

        String selected = (String) algorithmDropdown.getSelectedItem();
        int quantum = 0;

        if ("Round Robin".equals(selected)) {
            try {
                quantum = Integer.parseInt(quantumField.getText().trim());
                if (quantum <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid time quantum (>= 1).");
                return;
            }
        }

        // Capture System.out so we never touch CPUScheduling.java's logic
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(captured));

        try {
            switch (selected) {
                case "FCFS"        -> CPUScheduling.runFCFS(processes);
                case "SJF"         -> CPUScheduling.runSJF(processes);
                case "Round Robin" -> CPUScheduling.runRoundRobin(processes, quantum);
                case "Priority"    -> CPUScheduling.runPriority(processes);
            }
        } finally {
            System.setOut(originalOut);
        }

        outputArea.setText(captured.toString());
    }
}
