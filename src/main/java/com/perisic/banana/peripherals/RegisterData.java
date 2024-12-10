package com.perisic.banana.peripherals;

import javax.swing.*;
import java.sql.*;

public class RegisterData {

    public boolean registerUser(String username,String email, String password) {
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bananagame", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String hashPassword(String password) {
        // Basic password hashing (consider using BCrypt in real applications)
        return String.valueOf(password.hashCode());
    }
}
