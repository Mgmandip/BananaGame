package com.perisic.banana.peripherals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class RegisterGUI extends JFrame {

    private static final long serialVersionUID = -6921462126880570161L;

    JButton registerButton = new JButton("Register");
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

    JTextField txuser = new JTextField(15);
    JTextField txemail = new JTextField(15);
    JPasswordField pass = new JPasswordField(15);
    JLabel togglePasswordVisibility = new JLabel();

    public RegisterGUI() {
        super("Register Authentication");

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel registrationLabel = new JLabel("Registration");
        registrationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 40, 20, 10);
        panel.add(registrationLabel, gbc);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(usernameLabel, gbc);

        txuser.setFont(new Font("Arial", Font.PLAIN, 14));
        txuser.setPreferredSize(new Dimension(300, 30));
        gbc.gridy = 3;
        panel.add(txuser, gbc);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);

        txemail.setFont(new Font("Arial", Font.PLAIN, 14));
        txemail.setPreferredSize(new Dimension(250, 30));
        gbc.gridy = 5;
        panel.add(txemail, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 6;
        panel.add(passwordLabel, gbc);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);

        pass.setFont(new Font("Arial", Font.PLAIN, 14));
        pass.setPreferredSize(new Dimension(250, 30));
        passwordPanel.add(pass, BorderLayout.CENTER);

        JPanel eyeIconPanel = new JPanel();
        eyeIconPanel.setBackground(Color.WHITE);
        eyeIconPanel.setOpaque(true);
        eyeIconPanel.setLayout(new BorderLayout());

        try {
            ImageIcon eyeIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("com/perisic/banana/assets/eye.png")));
            ImageIcon eyeHideIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("com/perisic/banana/assets/eye_hide.png")));

            togglePasswordVisibility.setIcon(eyeHideIcon);
            togglePasswordVisibility.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            eyeIconPanel.add(togglePasswordVisibility, BorderLayout.CENTER);

            togglePasswordVisibility.addMouseListener(new MouseAdapter() {
                private boolean passwordVisible = false;

                @Override
                public void mouseClicked(MouseEvent e) {
                    passwordVisible = !passwordVisible;
                    pass.setEchoChar(passwordVisible ? (char) 0 : '*');
                    togglePasswordVisibility.setIcon(passwordVisible ? eyeIcon : eyeHideIcon);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        passwordPanel.add(eyeIconPanel, BorderLayout.EAST);
        gbc.gridy = 7;
        panel.add(passwordPanel, gbc);

        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(41, 171, 43));
        registerButton.setForeground(Color.WHITE);
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        // Text: Already Have an Account?
        JLabel dontHaveAccountLabel = new JLabel("Already Have an Account?");
        dontHaveAccountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dontHaveAccountLabel.setForeground(new Color(16, 91, 145));
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 25, 0, 0);
        panel.add(dontHaveAccountLabel, gbc);

        // Add MouseListener to JLabel to make it clickable
        dontHaveAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
                dispose();
            }
        });


        getContentPane().add(panel);
        setVisible(true);

        registerButton.addActionListener(e -> registerUser());
    }

    private void registerUser() {
        String username = txuser.getText();
        String email = txemail.getText();
        String password = new String(pass.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RegisterData registerData = new RegisterData();

        if (registerData.registerUser(username, email, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            new LoginGUI().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new RegisterGUI();
    }
}
