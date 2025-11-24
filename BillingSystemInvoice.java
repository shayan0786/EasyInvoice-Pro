// File: BillingSystemInvoice.java (with MP3 playback using JLayer)

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import javazoom.jl.player.Player;

public class BillingSystemInvoice extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public BillingSystemInvoice() {
        setTitle("Billing & inventory System");
        setSize(900, 650);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Allow custom exit animation
        setLocationRelativeTo(null);
        // Set custom logo icon if available
        try {
            setIconImage(new ImageIcon("logo.jpg").getImage());
        } catch (Exception e) {
            // Ignore if logo not found
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Show closing animation dialog
                JDialog closingDialog = new JDialog(BillingSystemInvoice.this, "Closing", true);
                JPanel closingPanel = new JPanel(new BorderLayout());
                JLabel closingLabel = new JLabel("Closing Application...", SwingConstants.CENTER);
                closingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                closingPanel.add(closingLabel, BorderLayout.CENTER);
                closingDialog.getContentPane().add(closingPanel);
                closingDialog.setSize(350, 100);
                closingDialog.setLocationRelativeTo(BillingSystemInvoice.this);
                closingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                // Show the dialog in a separate thread to avoid blocking EDT
                SwingUtilities.invokeLater(() -> closingDialog.setVisible(true));

                playMP3("exit.mp3");
                // Wait for 6 seconds before exiting
                new Timer(6000, evt -> {
                    closingDialog.setVisible(false);
                    closingDialog.dispose();
                    BillingSystemInvoice.this.dispose(); // Dispose window after animation
                    System.exit(0);
                }) {{ setRepeats(false); }}.start();
            }
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginSignupPanel loginSignup = new LoginSignupPanel(username -> {
            playMP3("start.mp3");
            // Show loading animation after successful login
            JDialog loadingDialog = new JDialog(this, "Loading", true);
            JPanel loadingPanel = new JPanel(new BorderLayout());
            JLabel loadingLabel = new JLabel("Loading Billing Panel...", SwingConstants.CENTER);
            loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(loadingLabel, BorderLayout.NORTH);
            loadingPanel.add(progressBar, BorderLayout.CENTER);
            loadingDialog.getContentPane().add(loadingPanel);
            loadingDialog.setSize(350, 120);
            loadingDialog.setLocationRelativeTo(this);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            // Show the dialog in a separate thread to avoid blocking EDT
            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));

            // Simulate loading for 2 seconds, then show billing panel
            new Timer(2000, evt -> {
                loadingDialog.setVisible(false);
                loadingDialog.dispose();
                JPanel billingPanel = createBillingUI(username);
                billingPanel.setVisible(false);
                mainPanel.add(billingPanel, "billing");
                cardLayout.show(mainPanel, "billing");

                Timer fadeIn = new Timer(20, null);
                fadeIn.addActionListener(new ActionListener() {
                    float opacity = 0f;
                    public void actionPerformed(ActionEvent evt) {
                        opacity += 0.1f;
                        billingPanel.setVisible(true);
                        billingPanel.repaint();
                        if (opacity >= 1f) fadeIn.stop();
                    }
                });
                fadeIn.start();
            }) {{ setRepeats(false); }}.start();
        });

        mainPanel.add(loginSignup, "auth");
        add(mainPanel);
        cardLayout.show(mainPanel, "auth");
        setVisible(true);
    }

    private JPanel createBillingUI(String username) {
        JPanel billingPanel = new JPanel(new BorderLayout(10, 10));
        billingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        billingPanel.setBackground(Color.decode("#f4f6f8"));

        // Title
        JLabel title = new JLabel("Billing & inventory System", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        billingPanel.add(title, BorderLayout.NORTH);

        // Add Product Section
        JPanel addProductPanel = new JPanel(new GridBagLayout());
        addProductPanel.setBackground(Color.decode("#f4f6f8"));
        addProductPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel productLabel = new JLabel("Product Name:");
        JLabel qtyLabel = new JLabel("Quantity:");
        JLabel priceLabel = new JLabel("Price per Unit:");
        JLabel discountLabel = new JLabel("Discount:");
        JLabel discountTypeLabel = new JLabel("Discount Type:");
        JLabel taxLabel = new JLabel("Tax:");
        JLabel taxTypeLabel = new JLabel("Tax Type:");
        JLabel paymentLabel = new JLabel("Mode of Payment:");
        JTextField productField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField discountField = new JTextField("0");
        JComboBox<String> discountType = new JComboBox<>(new String[]{"₹", "%"});
        JTextField taxField = new JTextField("0");
        JComboBox<String> taxType = new JComboBox<>(new String[]{"₹", "%"});
        JComboBox<String> paymentBox = new JComboBox<>(new String[]{"Cash", "Card", "UPI"});

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; addProductPanel.add(productLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1; addProductPanel.add(productField, gbc);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; addProductPanel.add(qtyLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1; addProductPanel.add(qtyField, gbc);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; addProductPanel.add(priceLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1; addProductPanel.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; addProductPanel.add(discountLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; addProductPanel.add(discountField, gbc);
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; addProductPanel.add(discountType, gbc);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; addProductPanel.add(taxLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1; addProductPanel.add(taxField, gbc);
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; addProductPanel.add(taxType, gbc);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; addProductPanel.add(paymentLabel, gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1; addProductPanel.add(paymentBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton addBtn = createButton("Add to Bill");
        JButton clearBtn = createButton("Clear Bill");
        JButton logoutBtn = createButton("Logout");
        buttonPanel.add(addBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(logoutBtn);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; gbc.weightx = 1; addProductPanel.add(buttonPanel, gbc);

        // Bill Details Section
        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        billArea.setEditable(false);
        JScrollPane billScroll = new JScrollPane(billArea);
        billScroll.setBorder(BorderFactory.createTitledBorder("Bill Details"));

        // Total and Print
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalLabel = new JLabel("Total: ₹0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JButton printBtn = createButton("Print Bill");
        bottomPanel.add(totalLabel, BorderLayout.EAST);
        bottomPanel.add(printBtn, BorderLayout.SOUTH);
        bottomPanel.setBackground(Color.decode("#f4f6f8"));

        // Layout
        billingPanel.add(addProductPanel, BorderLayout.NORTH);
        billingPanel.add(billScroll, BorderLayout.CENTER);
        billingPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Data
        StringBuilder invoiceData = new StringBuilder();
        final double[] totalAmount = {0.0};

        // Bill Header
        String billHeader = String.format("%-12s %6s %10s %10s %10s %10s\n", "Product", "Qty", "Price", "Discount", "Tax", "Total") +
                "-------------------------------------------------------------------------------\n";
        billArea.setText(billHeader);

        // Add to Bill Action
        addBtn.addActionListener(e -> {
            try {
                String pname = productField.getText().trim();
                int qty = Integer.parseInt(qtyField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double discount = Double.parseDouble(discountField.getText().trim());
                double tax = Double.parseDouble(taxField.getText().trim());
                double lineTotal = price * qty;
                if (discountType.getSelectedItem().equals("%"))
                    lineTotal -= (lineTotal * discount / 100);
                else
                    lineTotal -= discount;
                if (taxType.getSelectedItem().equals("%"))
                    lineTotal += (lineTotal * tax / 100);
                else
                    lineTotal += tax;
                invoiceData.append(String.format("%-12s %6d %10.2f %10.2f %10.2f %10.2f\n", pname, qty, price, discount, tax, lineTotal));
                totalAmount[0] += lineTotal;
                totalLabel.setText(String.format("Total: ₹%.2f", totalAmount[0]));
                billArea.setText(billHeader + invoiceData.toString());
            } catch (Exception ex) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(billingPanel, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Clear Bill Action
        clearBtn.addActionListener(e -> {
            invoiceData.setLength(0);
            totalAmount[0] = 0.0;
            totalLabel.setText("Total: ₹0.00");
            billArea.setText(billHeader);
        });

        // Print Bill Action
        printBtn.addActionListener(e -> {
            invoiceData.append("\nMode of Payment: " + paymentBox.getSelectedItem());
            invoiceData.append(String.format("\nFinal Total: ₹%.2f\n", totalAmount[0]));
            invoiceData.append("Thank you for shopping!");
            billArea.setText(billHeader + invoiceData.toString());
            playMP3("print.mp3");
            try {
                boolean done = billArea.print();
                if (done) {
                    JOptionPane.showMessageDialog(billingPanel, "Invoice printed successfully!");
                }
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        });

        // Logout Action
        logoutBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "auth");
        });

        return billingPanel;
    }

    private void playMP3(String filename) {
        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(filename);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Player player = new Player(bis);
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(59, 130, 246));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(30, 100, 230));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(59, 130, 246));
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingSystemInvoice::new);
    }
}
