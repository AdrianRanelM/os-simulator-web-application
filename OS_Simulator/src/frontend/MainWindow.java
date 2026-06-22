package frontend;

import java.awt.*;
import javax.swing.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OS Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 650);
            frame.add(buildHomePanel(frame));
            frame.setVisible(true);
        });
    }

    private static JPanel buildHomePanel(JFrame frame) {
        JPanel home = new JPanel(new GridBagLayout());
        home.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("OS Simulator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        home.add(title, gbc);

        JLabel subtitle = new JLabel("Select a module to continue:", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        home.add(subtitle, gbc);

        String[][] modules = {
            {"1", "CPU Scheduling"},
            {"2", "Memory Management"},
            {"3", "Virtual Memory"}
        };

        gbc.insets = new Insets(8, 60, 8, 60);

        for (int i = 0; i < modules.length; i++) {
            String key   = modules[i][0];
            String label = modules[i][1];

            JButton btn = new JButton("[" + key + "]  " + label);
            btn.setFont(new Font("Monospaced", Font.PLAIN, 15));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> navigateTo(frame, label));

            // Register number key shortcut (1, 2, 3)
            home.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key.charAt(0)), "open_" + key);
            home.getActionMap()
                .put("open_" + key, new AbstractAction() {
                    @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                        navigateTo(frame, label);
                    }
                });

            gbc.gridy = 2 + i;
            home.add(btn, gbc);
        }

        return home;
    }

    private static void navigateTo(JFrame frame, String moduleName) {
        JPanel modulePanel = switch (moduleName) {
            case "CPU Scheduling"    -> new CPUSchedulingPanel();
            case "Memory Management" -> new MemoryManagementPanel();
            case "Virtual Memory"    -> new VirtualMemoryPanel();
            default -> null;
        };
        if (modulePanel == null) return;

        // Back button
        JButton backBtn = new JButton("← Back to Home");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(buildHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        JPanel wrapper = new JPanel(new BorderLayout(5, 5));
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(backBtn);
        wrapper.add(topBar, BorderLayout.NORTH);
        wrapper.add(modulePanel, BorderLayout.CENTER);

        frame.getContentPane().removeAll();
        frame.add(wrapper);
        frame.revalidate();
        frame.repaint();
    }
}