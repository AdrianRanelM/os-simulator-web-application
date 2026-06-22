package frontend;

import backend.VirtualMemory;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing front end for Module 6: Virtual Memory.
 * Does not touch VirtualMemory.java's algorithm logic at all -
 * it only builds input data and calls the existing static methods,
 * then captures their System.out.println output into a text area.
 */
public class VirtualMemoryPanel extends JPanel {

    private JTextField numPagesField;
    private JPanel pageFieldsPanel;
    private List<JTextField> pageFields = new ArrayList<>();

    private JTextField numFramesField;
    private JComboBox<String> algorithmDropdown;
    private JTextArea outputArea;

    public VirtualMemoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------ UI build

    private JPanel buildInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Row 1: number of pages + generate button
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countRow.add(new JLabel("Number of pages in reference string:"));
        numPagesField = new JTextField(4);
        countRow.add(numPagesField);
        JButton genBtn = new JButton("Generate Page Fields");
        genBtn.addActionListener(e -> generatePageFields());
        countRow.add(genBtn);
        container.add(countRow);

        // Row 2: dynamically generated page reference fields
        pageFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane pageScroll = new JScrollPane(pageFieldsPanel);
        pageScroll.setBorder(BorderFactory.createTitledBorder("Reference String"));
        pageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pageScroll.setPreferredSize(new Dimension(760, 70));
        container.add(pageScroll);

        // Row 3: number of frames, algorithm, run button
        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runRow.add(new JLabel("Number of frames:"));
        numFramesField = new JTextField(4);
        runRow.add(numFramesField);

        runRow.add(Box.createHorizontalStrut(20));

        runRow.add(new JLabel("Algorithm:"));
        algorithmDropdown = new JComboBox<>(new String[]{"FIFO", "LRU", "Optimal"});
        runRow.add(algorithmDropdown);

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

    private void generatePageFields() {
        int count;
        try {
            count = Integer.parseInt(numPagesField.getText().trim());
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of pages (>= 1).");
            return;
        }

        pageFieldsPanel.removeAll();
        pageFields.clear();

        for (int i = 1; i <= count; i++) {
            pageFieldsPanel.add(new JLabel("Page " + i + ":"));
            JTextField field = new JTextField(4);
            pageFields.add(field);
            pageFieldsPanel.add(field);
        }

        pageFieldsPanel.setPreferredSize(new Dimension(count * 100, 50));
        pageFieldsPanel.revalidate();
        pageFieldsPanel.repaint();
    }

    // ------------------------------------------------------------------ Run

    private void runAlgorithm() {
        if (pageFields.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate page fields first.");
            return;
        }

        List<Integer> referenceString;
        int numFrames;

        try {
            referenceString = new ArrayList<>();
            for (int i = 0; i < pageFields.size(); i++) {
                int page = Integer.parseInt(pageFields.get(i).getText().trim());
                referenceString.add(page);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please make sure all page values are valid integers.");
            return;
        }

        try {
            numFrames = Integer.parseInt(numFramesField.getText().trim());
            if (numFrames <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of frames (>= 1).");
            return;
        }

        String selected = (String) algorithmDropdown.getSelectedItem();

        // Capture System.out so we never touch VirtualMemory.java's logic
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(captured));

        try {
            switch (selected) {
                case "FIFO"    -> VirtualMemory.runFIFO(referenceString, numFrames);
                case "LRU"     -> VirtualMemory.runLRU(referenceString, numFrames);
                case "Optimal" -> VirtualMemory.runOptimal(referenceString, numFrames);
            }
        } finally {
            System.setOut(originalOut);
        }

        outputArea.setText(captured.toString());
    }
}
