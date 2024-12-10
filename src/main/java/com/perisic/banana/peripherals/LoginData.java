package com.perisic.banana.peripherals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginData {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bananagame";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkPassword(String username, String passwd) {
        String hashedPassword = hashPassword(passwd);

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(hashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }


    // Leaderboard

    public List<String[]> getLeaderboard() {
        List<String[]> leaderboard = new ArrayList<>();
        String query = "SELECT username, score FROM users ORDER BY score DESC LIMIT 10";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String score = String.valueOf(rs.getInt("score"));
                leaderboard.add(new String[]{username, score});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }

    public void updateScore(String username, int score) {
        String query = "UPDATE users SET score = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
