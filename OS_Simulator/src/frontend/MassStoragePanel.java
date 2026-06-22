package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MassStoragePanel extends JPanel {

    private JTextField numRequestsField;
    private JTextField headPositionField;
    private JPanel requestFieldsPanel;
    private List<JTextField> requestFields = new ArrayList<>();
    private JComboBox<String> algorithmDropdown;
    private JComboBox<String> directionDropdown;
    private JLabel directionLabel;
    private JTextArea outputArea;

    public MassStoragePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
    }

    private JPanel buildInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Row 1: number of requests, head start, generate
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countRow.add(new JLabel("Number of requests:"));
        numRequestsField = new JTextField(4);
        countRow.add(numRequestsField);
        JButton genBtn = new JButton("Generate Request Fields");
        genBtn.addActionListener(e -> generateRequestFields());
        countRow.add(genBtn);

        countRow.add(Box.createHorizontalStrut(20));
        countRow.add(new JLabel("Head start position:"));
        headPositionField = new JTextField(5);
        countRow.add(headPositionField);
        container.add(countRow);

        // Row 2: scrollable track number fields
        requestFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane requestScroll = new JScrollPane(requestFieldsPanel);
        requestScroll.setBorder(BorderFactory.createTitledBorder("Track Requests"));
        requestScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        requestScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        requestScroll.setPreferredSize(new Dimension(760, 70));
        container.add(requestScroll);

        // Row 3: algorithm + direction (conditional) + run
        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runRow.add(new JLabel("Algorithm:"));
        algorithmDropdown = new JComboBox<>(new String[]{"FCFS", "SSTF", "SCAN", "C-SCAN", "LOOK", "C-LOOK"});
        algorithmDropdown.addActionListener(e -> {
            String sel = (String) algorithmDropdown.getSelectedItem();
            boolean needsDir = "LOOK".equals(sel) || "C-LOOK".equals(sel);
            directionLabel.setVisible(needsDir);
            directionDropdown.setVisible(needsDir);
        });
        runRow.add(algorithmDropdown);

        directionLabel = new JLabel("  Direction:");
        directionDropdown = new JComboBox<>(new String[]{"right", "left"});
        directionLabel.setVisible(false);
        directionDropdown.setVisible(false);
        runRow.add(directionLabel);
        runRow.add(directionDropdown);

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

    private void generateRequestFields() {
        try {
            int count = Integer.parseInt(numRequestsField.getText().trim());
            requestFieldsPanel.removeAll();
            requestFields.clear();

            for (int i = 1; i <= count; i++) {
                requestFieldsPanel.add(new JLabel("R" + i + ":"));
                JTextField f = new JTextField(5);
                requestFields.add(f);
                requestFieldsPanel.add(f);
            }

            requestFieldsPanel.setPreferredSize(new Dimension(count * 100, 50));
            requestFieldsPanel.revalidate();
            requestFieldsPanel.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of requests.");
        }
    }

    private void runAlgorithm() {
        if (requestFields.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate request fields first.");
            return;
        }

        int headStart;
        try {
            headStart = Integer.parseInt(headPositionField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid head start position.");
            return;
        }

        List<DiskRequest> requests = new ArrayList<>();
        try {
            for (JTextField f : requestFields) {
                int track = Integer.parseInt(f.getText().trim());
                requests.add(new DiskRequest(track)); // DiskRequest takes only track number
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please make sure all track values are valid numbers.");
            return;
        }

        String selected = (String) algorithmDropdown.getSelectedItem();
        String direction = (String) directionDropdown.getSelectedItem();

        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(captured));

        try {
            switch (selected) {
                case "FCFS" -> MassStorage.runFCFS(requests, headStart);
                case "SSTF" -> MassStorage.runSSTF(requests, headStart);
                case "SCAN" -> MassStorage.runSCAN(requests, headStart, 200);  // diskSize = 200
                case "C-SCAN" -> MassStorage.runCSCAN(requests, headStart, 200); // diskSize = 200
                case "LOOK" -> MassStorage.runLOOK(requests, headStart, direction);
                case "C-LOOK" -> MassStorage.runCLOOK(requests, headStart, direction);
            }
        } finally {
            System.setOut(originalOut);
        }

        outputArea.setText(captured.toString());
    }
}