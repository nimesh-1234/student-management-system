import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AAA {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/IT2022db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        initializeDatabase();
        SwingUtilities.invokeLater(AAA::showLoginPage);
    }

    private static void showLoginPage() {
        JFrame loginFrame = new JFrame("Login - Diwya Academy");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLayout(null);
        loginFrame.getContentPane().setBackground(new Color(245, 245, 255));

        JLabel titleLabel = new JLabel("Welcome to Diwya Academy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(60, 20, 300, 25);
        loginFrame.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 25);
        loginFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 25);
        loginFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 25);
        loginFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 180, 80, 30);
        loginFrame.add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(200, 180, 80, 30);
        loginFrame.add(exitButton);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.dispose();
                showRegistrationForm();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private static boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void showRegistrationForm() {
        JFrame regFrame = new JFrame("Student Registration");
        regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regFrame.setSize(500, 450);
        regFrame.setLayout(null);
        regFrame.getContentPane().setBackground(new Color(250, 250, 255));

        JLabel titleLabel = new JLabel("Student Registration Form");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBounds(120, 20, 300, 30);
        regFrame.add(titleLabel);

        String[] labels = {"Name:", "Age:", "Gender:", "Course:", "Contact No:"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setBounds(50, 80 + i * 50, 100, 30);
            regFrame.add(label);

            if (i == 2) { // Gender dropdown
                String[] genders = {"Male", "Female", "Other"};
                JComboBox<String> genderBox = new JComboBox<>(genders);
                genderBox.setBounds(150, 80 + i * 50, 250, 30);
                regFrame.add(genderBox);
                fields[i] = new JTextField(); // Dummy for gender
                fields[i].setText("Male"); // Default
                genderBox.addActionListener(e -> fields[2].setText((String) genderBox.getSelectedItem()));
            } else {
                fields[i] = new JTextField();
                fields[i].setBounds(150, 80 + i * 50, 250, 30);
                regFrame.add(fields[i]);
            }
        }

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(150, 340, 100, 30);
        regFrame.add(submitBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(270, 340, 100, 30);
        regFrame.add(backBtn);

        regFrame.setLocationRelativeTo(null);
        regFrame.setVisible(true);

        submitBtn.addActionListener(e -> {
            try {
                String name = fields[0].getText().trim();
                int age = Integer.parseInt(fields[1].getText().trim());
                String gender = fields[2].getText();
                String course = fields[3].getText().trim();
                String contact = fields[4].getText().trim();

                if (name.isEmpty() || course.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(regFrame, "All fields must be filled!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                saveStudentData(name, age, gender, course, contact);
                JOptionPane.showMessageDialog(regFrame, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                regFrame.dispose();
                showLoginPage();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(regFrame, "Invalid age value!", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(regFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            regFrame.dispose();
            showLoginPage();
        });
    }

    private static void saveStudentData(String name, int age, String gender, String course, String contact) throws Exception {
        String query = "INSERT INTO student (name, age, gender, course, contact_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, course);
            pstmt.setString(5, contact);
            pstmt.executeUpdate();
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS IT2022db");
            stmt.executeUpdate("USE IT2022db");

            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(50) NOT NULL)";
            stmt.executeUpdate(usersTable);

            String studentTable = "CREATE TABLE IF NOT EXISTS student (" +
                    "student_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "age INT," +
                    "gender VARCHAR(50)," +
                    "course VARCHAR(50)," +
                    "contact_no VARCHAR(15))";
            stmt.executeUpdate(studentTable);

            stmt.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin123')");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DB Initialization Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
