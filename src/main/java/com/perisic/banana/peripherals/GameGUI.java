package com.perisic.banana.peripherals;

import com.perisic.banana.engine.GameEngine;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class GameGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = -107785653906635L;
    private JLabel questArea;
    private GameEngine myGame;
    private BufferedImage currentGame;
    private JTextArea infoArea;
    private int score = 0;
    private JLabel feedbackLabel;
    private JLabel usernameLabel;
    private JPanel leaderboardPanel;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeRemaining = 20;
    private String puname; // To store username
    private JButton[] numberButtons = new JButton[10]; // Array to store number buttons

    public GameGUI(String player) {
        super();
        this.puname = player; // Set the username
        initGame(player);
    }

    private void initGame(String player) {
        setTitle("Banana Game");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(230, 184, 0));

        JLabel title = new JLabel("Solve the Equation!");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);

        mainPanel.add(Box.createVerticalStrut(10));

        myGame = new GameEngine(player);
        currentGame = myGame.nextGame();

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        userPanel.setBackground(new Color(16, 91, 145));

        usernameLabel = new JLabel("Player: " + player);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        usernameLabel.setForeground(Color.WHITE);
        userPanel.add(usernameLabel);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        scorePanel.setBackground(new Color(230, 184, 0));

        infoArea = new JTextArea("Score: 0");
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.BOLD, 20));
        infoArea.setBackground(new Color(230, 184, 0));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        scorePanel.add(infoArea);

        mainPanel.add(userPanel);
        mainPanel.add(scorePanel);

        questArea = new JLabel(new ImageIcon(currentGame), JLabel.CENTER);
        questArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(questArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 5, 10, 10));
        buttonPanel.setBackground(new Color(230, 184, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize the number buttons and add them to the panel
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createNumberButton(String.valueOf(i));
            buttonPanel.add(numberButtons[i]);
        }
        mainPanel.add(buttonPanel);

        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(new Color(245, 245, 245));
        leaderboardPanel.setPreferredSize(new Dimension(250, getHeight()));

        refreshLeaderboard();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(leaderboardPanel, BorderLayout.EAST);

        getContentPane().add(contentPanel);

        // Start the countdown here
        startCountdown();
    }

    private void refreshLeaderboard() {
        leaderboardPanel.removeAll();

        JButton logoutButton = new JButton("Home");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBackground(new Color(16, 91, 145));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> home());

        JPanel buttonPanelForLogout = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanelForLogout.setBackground(new Color(245, 245, 245));
        buttonPanelForLogout.add(logoutButton);
        leaderboardPanel.add(buttonPanelForLogout);

        timerLabel = new JLabel("Time: " + timeRemaining);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(new Color(16, 91, 145));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setBackground(new Color(245, 245, 245));
        timerPanel.add(timerLabel);
        leaderboardPanel.add(timerPanel);

        leaderboardPanel.add(Box.createVerticalStrut(20));

        JLabel leaderboardHeading = new JLabel("Leaderboard");
        leaderboardHeading.setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardPanel.add(leaderboardHeading);

        LoginData loginData = new LoginData();
        List<String[]> leaderboardList = loginData.getLeaderboard();

        String[][] leaderboardData = new String[leaderboardList.size()][2];
        for (int i = 0; i < leaderboardList.size(); i++) {
            leaderboardData[i] = leaderboardList.get(i);
        }

        JTable leaderboardTable = new JTable(leaderboardData, new String[]{"Player", "Score"});
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setBackground(Color.WHITE);
        leaderboardTable.setForeground(new Color(0, 51, 102));

        JTableHeader header = leaderboardTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBackground(new Color(16, 91, 145));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        leaderboardTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        leaderboardPanel.add(scrollPane);

        leaderboardPanel.revalidate();
        leaderboardPanel.repaint();
    }

    private JButton createNumberButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(16, 91, 145));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the time is up, do nothing (disable further interaction)
        if (timeRemaining == 0) {
            return;
        }

        int solution = Integer.parseInt(e.getActionCommand());
        boolean correct = myGame.checkSolution(solution);
        score = myGame.getScore();

        if (correct) {
            currentGame = myGame.nextGame();
            questArea.setIcon(new ImageIcon(currentGame));
            infoArea.setText("Good! Score: " + score);

            LoginData loginData = new LoginData();
            loginData.updateScore(usernameLabel.getText().replace("Player: ", ""), score);

            refreshLeaderboard();
        } else {
            infoArea.setText("Try again! Score: " + score);
        }
    }

    public void startCountdown() {
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    timerLabel.setText("Time: " + timeRemaining);
                } else {
                    countdownTimer.stop();
                    showGameOverWindow();
                }
            }
        });
        countdownTimer.start();
    }

    private void showGameOverWindow() {
        this.dispose();
        new GameOverFrame(score, puname).setVisible(true);
    }

private void home() {
        this.dispose(); // Close the current game window
        new MainMenuGUI(puname); // Open the home screen or another GUI
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameGUI("Player1"); // Start the game

            }
        });
    }
}
