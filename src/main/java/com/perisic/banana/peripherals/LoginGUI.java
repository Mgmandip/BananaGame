package com.perisic.banana.peripherals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class LoginGUI extends JFrame {

    private static final long serialVersionUID = -6921462126880570161L;

    JButton blogin = new JButton("Login");
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
    JPasswordField pass = new JPasswordField(15);
    JButton togglePasswordVisibility = new JButton(); // Button to toggle password visibility

    LoginData ldata = new LoginData();

    LoginGUI() {
        super("Login Authentication");

        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        txuser.setFont(new Font("Arial", Font.PLAIN, 14));
        txuser.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(txuser, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setPreferredSize(new Dimension(250, 30));

        pass.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordPanel.add(pass, BorderLayout.CENTER);

        togglePasswordVisibility.setPreferredSize(new Dimension(30, 30));
        togglePasswordVisibility.setContentAreaFilled(false);
        togglePasswordVisibility.setBorderPainted(false);
        togglePasswordVisibility.setFocusPainted(false);

        // Load the eye icons
        try {
            Image eyeIcon = ImageIO.read(getClass().getClassLoader().getResource("com/perisic/banana/assets/eye.png"));
            Image eyeHideIcon = ImageIO.read(getClass().getClassLoader().getResource("com/perisic/banana/assets/eye_hide.png"));
            togglePasswordVisibility.setIcon(new ImageIcon(eyeHideIcon));

            togglePasswordVisibility.addActionListener(e -> {
                if (pass.getEchoChar() != '\u0000') {
                    pass.setEchoChar('\u0000'); // Show password
                    togglePasswordVisibility.setIcon(new ImageIcon(eyeIcon));
                } else {
                    pass.setEchoChar('*'); // Hide password
                    togglePasswordVisibility.setIcon(new ImageIcon(eyeHideIcon));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        passwordPanel.add(togglePasswordVisibility, BorderLayout.EAST);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordPanel, gbc);

        // Login button
        blogin.setFont(new Font("Arial", Font.BOLD, 14));
        blogin.setBackground(new Color(41, 171, 43));
        blogin.setForeground(Color.WHITE);
        gbc.insets = new Insets(15, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(blogin, gbc);

        // Action for login
        actionlogin();


        // Text: Already Have an Account?
        JLabel dontHaveAccountLabel = new JLabel("Don't Have an Account?");
        dontHaveAccountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dontHaveAccountLabel.setForeground(new Color(16, 91, 145));
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 65, 0, 0);
        panel.add(dontHaveAccountLabel, gbc);

        // Add MouseListener to JLabel to make it clickable
        dontHaveAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterGUI registerGUI = new RegisterGUI();
                registerGUI.setVisible(true);
                dispose();
            }
        });

        getContentPane().add(panel);
        setVisible(true);
    }

    public void actionlogin() {
        blogin.addActionListener(ae -> {
            String puname = txuser.getText();
            String ppaswd = String.valueOf(pass.getPassword());
            if (ldata.checkPassword(puname, ppaswd)) {
                MainMenuGUI theGame = new MainMenuGUI(puname);
                theGame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Wrong Password / Username", "Error", JOptionPane.ERROR_MESSAGE);
                txuser.setText("");
                pass.setText("");
                txuser.requestFocus();
            }
        });
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
