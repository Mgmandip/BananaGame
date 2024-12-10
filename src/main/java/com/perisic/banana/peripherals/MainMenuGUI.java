package com.perisic.banana.peripherals;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainMenuGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    JButton playButton = new JButton("Play");
    JButton logoutButton = new JButton("Logout");

    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                Image backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("com/perisic/banana/assets/BGauth.jpg"));
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    // Constructor for MainMenuGUI that takes username as argument
    public MainMenuGUI(String puname) {
        super("Main Menu");

        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.CENTER);

        // Display welcome message with username
        JLabel titleLabel = new JLabel("Welcome, " + puname, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Leaderboard Panel
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setPreferredSize(new Dimension(400, 200));
        leaderboardPanel.setBackground(new Color(0, 0, 0, 0));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.YELLOW, 1), "Leaderboard"));

        populateLeaderboard(leaderboardPanel);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(leaderboardPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setBackground(new Color(41, 171, 43));
        playButton.setForeground(Color.WHITE);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(e -> playGame(puname));  // Pass the username to play game
        buttonPanel.add(playButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        logoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        logoutButton.setBackground(new Color(16, 91, 145));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(e -> logout());  // Logout action
        buttonPanel.add(logoutButton);

        gbc.gridy = 2;
        panel.add(buttonPanel, gbc);

        setVisible(true);
    }

    // Method to populate leaderboard with data from the database
    private void populateLeaderboard(JPanel leaderboardPanel) {
        String[] columnNames = {"Rank", "Username", "Score"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable leaderboardTable = new JTable(tableModel);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bananagame", "root", "");
            Statement statement = connection.createStatement();
            String query = "SELECT username, score FROM users ORDER BY score DESC LIMIT 10";
            ResultSet resultSet = statement.executeQuery(query);

            int rank = 1;
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int score = resultSet.getInt("score");
                tableModel.addRow(new Object[]{rank++, username, score});
            }

            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));
        leaderboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        leaderboardTable.setRowHeight(25);

        // Enable sorting and add a hover effect
        leaderboardTable.setAutoCreateRowSorter(true);
        leaderboardTable.setSelectionBackground(new Color(136, 188, 219));
        leaderboardTable.setSelectionForeground(Color.BLACK);

        // Custom row coloring
        leaderboardTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(224, 242, 255)); // Light blue
                    }
                }
                return c;
            }
        });

        // Custom table header rendering
        JTableHeader header = leaderboardTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setBackground(new Color(230, 184, 0));
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // Method to handle play button action
    private void playGame(String puname) {
        dispose();  // Close the current MainMenuGUI
        new GameGUI(puname).setVisible(true);  // Pass the username to the GameGUI
    }

    // Method to handle logout action
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginGUI();  // Open LoginGUI
        dispose();  // Close MainMenuGUI
    }

    public static void main(String[] args) {
        new MainMenuGUI("Player1");
    }
}
