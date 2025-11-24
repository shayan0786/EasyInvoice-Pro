// File: LoginSignupPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginSignupPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField loginUserField, signupUserField;
    private JPasswordField loginPassField, signupPassField;
    private JButton loginButton, signupButton, switchToSignup, switchToLogin;
    private ArrayList<User> users;
    private LoginListener listener;

    public interface LoginListener {
        void onLoginSuccess(String username);
    }

    public LoginSignupPanel(LoginListener listener) {
        this.listener = listener;
        users = new ArrayList<>();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createSignupPanel(), "signup");
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = createStyledPanel("Login");

        loginUserField = createTextField();
        loginPassField = createPasswordField();
        loginButton = createButton("Login");
        switchToSignup = createFlatButton("No account? Sign up");

        loginButton.addActionListener(e -> {
            String user = loginUserField.getText().trim();
            String pass = new String(loginPassField.getPassword()).trim();
            boolean found = users.stream().anyMatch(u -> u.username.equals(user) && u.password.equals(pass));
            if (found) {
                listener.onLoginSuccess(user);
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        switchToSignup.addActionListener(e -> cardLayout.show(mainPanel, "signup"));

        panel.add(new JLabel("Username:"), gbc(0, 0));
        panel.add(loginUserField, gbc(1, 0));
        panel.add(new JLabel("Password:"), gbc(0, 1));
        panel.add(loginPassField, gbc(1, 1));
        panel.add(loginButton, gbc(1, 2));
        panel.add(switchToSignup, gbc(1, 3));

        return wrapCenter(panel);
    }

    private JPanel createSignupPanel() {
        JPanel panel = createStyledPanel("Sign Up");

        signupUserField = createTextField();
        signupPassField = createPasswordField();
        signupButton = createButton("Register");
        switchToLogin = createFlatButton("Back to Login");

        signupButton.addActionListener(e -> {
            String user = signupUserField.getText().trim();
            String pass = new String(signupPassField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (users.size() >= 10) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "User limit reached!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            users.add(new User(user, pass));
            JOptionPane.showMessageDialog(this, "Account created! Now login.");
            cardLayout.show(mainPanel, "login");
        });

        switchToLogin.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(new JLabel("New Username:"), gbc(0, 0));
        panel.add(signupUserField, gbc(1, 0));
        panel.add(new JLabel("New Password:"), gbc(0, 1));
        panel.add(signupPassField, gbc(1, 1));
        panel.add(signupButton, gbc(1, 2));
        panel.add(switchToLogin, gbc(1, 3));

        return wrapCenter(panel);
    }

    // === UI COMPONENT HELPERS ===

    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), title, 0, 0, new Font("SansSerif", Font.BOLD, 18)));
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(30, 144, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 120, 215));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(30, 144, 255));
            }
        });

        return btn;
    }

    private JButton createFlatButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private JPanel wrapCenter(JPanel panel) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(230, 230, 255));
        outer.add(panel);
        return outer;
    }

    static class User {
        String username, password;
        User(String u, String p) {
            username = u;
            password = p;
        }
    }
}
