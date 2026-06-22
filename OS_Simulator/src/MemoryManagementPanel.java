import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing front end for Module 5: Memory Management.
 * Does not touch MemoryManagement.java's algorithm logic at all -
 * it only builds input data and calls the existing static methods,
 * then captures their System.out.println output into a text area.
 */
public class MemoryManagementPanel extends JPanel {

    private JTextField numBlocksField;
    private JTextField numProcessesField;
    private JPanel blockFieldsPanel;
    private JPanel processFieldsPanel;
    private List<JTextField> blockSizeFields = new ArrayList<>();
    private List<JTextField> processSizeFields = new ArrayList<>();
    private JComboBox<String> algorithmDropdown;
    private JTextArea outputArea;

    public MemoryManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
    }

    private JPanel buildInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Row 1: how many blocks / processes, with generate buttons
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countRow.add(new JLabel("Number of blocks:"));
        numBlocksField = new JTextField(4);
        countRow.add(numBlocksField);
        JButton genBlocksBtn = new JButton("Generate Block Fields");
        genBlocksBtn.addActionListener(e -> generateBlockFields());
        countRow.add(genBlocksBtn);

        countRow.add(Box.createHorizontalStrut(20));

        countRow.add(new JLabel("Number of processes:"));
        numProcessesField = new JTextField(4);
        countRow.add(numProcessesField);
        JButton genProcessesBtn = new JButton("Generate Process Fields");
        genProcessesBtn.addActionListener(e -> generateProcessFields());
        countRow.add(genProcessesBtn);

        container.add(countRow);

        // Row 2: dynamically generated block size fields
        blockFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane blockScroll = new JScrollPane(blockFieldsPanel);
        blockScroll.setBorder(BorderFactory.createTitledBorder("Block Sizes"));
        blockScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        blockScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        blockScroll.setPreferredSize(new Dimension(760, 70));
        container.add(blockScroll);

        // Row 3: dynamically generated process size fields
        processFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane processScroll = new JScrollPane(processFieldsPanel);
        processScroll.setBorder(BorderFactory.createTitledBorder("Process Sizes"));
        processScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        processScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        processScroll.setPreferredSize(new Dimension(760, 70));
        container.add(processScroll);

        // Row 4: algorithm choice + run button
        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runRow.add(new JLabel("Algorithm:"));
        algorithmDropdown = new JComboBox<>(new String[]{"First Fit", "Best Fit", "Worst Fit"});
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

    private void generateBlockFields() {
        try {
            int count = Integer.parseInt(numBlocksField.getText().trim());
            blockFieldsPanel.removeAll();
            blockSizeFields.clear();

            for (int i = 1; i <= count; i++) {
                blockFieldsPanel.add(new JLabel("Block " + i + ":"));
                JTextField field = new JTextField(5);
                blockSizeFields.add(field);
                blockFieldsPanel.add(field);
            }

            blockFieldsPanel.setPreferredSize(new Dimension(count * 110, 50));
            blockFieldsPanel.revalidate();
            blockFieldsPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of blocks.");
        }
    }

    private void generateProcessFields() {
        try {
            int count = Integer.parseInt(numProcessesField.getText().trim());
            processFieldsPanel.removeAll();
            processSizeFields.clear();

            for (int i = 1; i <= count; i++) {
                processFieldsPanel.add(new JLabel("Process " + i + ":"));
                JTextField field = new JTextField(5);
                processSizeFields.add(field);
                processFieldsPanel.add(field);
            }

            processFieldsPanel.setPreferredSize(new Dimension(count * 120, 50));
            processFieldsPanel.revalidate();
            processFieldsPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of processes.");
        }
    }

    private void runAlgorithm() {
        if (blockSizeFields.isEmpty() || processSizeFields.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate block and process fields first.");
            return;
        }

        List<MemoryBlock> blocks;
        List<Integer> processSizes;

        try {
            blocks = new ArrayList<>();
            for (int i = 0; i < blockSizeFields.size(); i++) {
                int size = Integer.parseInt(blockSizeFields.get(i).getText().trim());
                blocks.add(new MemoryBlock(i + 1, size));
            }

            processSizes = new ArrayList<>();
            for (JTextField field : processSizeFields) {
                processSizes.add(Integer.parseInt(field.getText().trim()));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please make sure all sizes are valid numbers.");
            return;
        }

        String selected = (String) algorithmDropdown.getSelectedItem();

        // Capture System.out output so we don't have to touch MemoryManagement.java at all
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(captured));

        try {
            switch (selected) {
                case "First Fit" -> MemoryManagement.runFirstFit(blocks, processSizes);
                case "Best Fit" -> MemoryManagement.runBestFit(blocks, processSizes);
                case "Worst Fit" -> MemoryManagement.runWorstFit(blocks, processSizes);
            }
        } finally {
            System.setOut(originalOut);
        }

        outputArea.setText(captured.toString());
    }
}