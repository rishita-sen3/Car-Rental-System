import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class CarRentalGUI extends JFrame {
    private JTextField nameField, daysField;
    private JComboBox<String> carDropdown;
    private JTextArea outputArea;

    private HashMap<String, String> cars = new HashMap<>();
    private HashMap<String, Double> carPrices = new HashMap<>();

    public CarRentalGUI() {
        initializeCars();

        // Fullscreen setup
        setTitle("🚗 Car Rental System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dark Gradient Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(30, 30, 30), 
                        getWidth(), getHeight(), new Color(60, 60, 60));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Header
        JLabel title = new JLabel("🚗 Aesthetic Car Rentals", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        backgroundPanel.add(title, BorderLayout.NORTH);

        // Form Panel with Glass Effect
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        backgroundPanel.add(formWrapper, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        formPanel.setBackground(new Color(0, 0, 0, 130));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel nameLabel = new JLabel("👤 Your Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameField.setBackground(new Color(30, 30, 30));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel carLabel = new JLabel("🚘 Select Car:");
        carLabel.setForeground(Color.WHITE);
        carLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        carDropdown = new JComboBox<>(cars.keySet().toArray(new String[0]));
        carDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        carDropdown.setBackground(new Color(30, 30, 30));
        carDropdown.setForeground(Color.WHITE);
        carDropdown.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel daysLabel = new JLabel("📅 Rental Days:");
        daysLabel.setForeground(Color.WHITE);
        daysLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        daysField = new JTextField();
        daysField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        daysField.setBackground(new Color(30, 30, 30));
        daysField.setForeground(Color.WHITE);
        daysField.setCaretColor(Color.WHITE);
        daysField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JButton rentButton = new JButton("Rent Now 🚀");
        rentButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rentButton.setBackground(new Color(50, 200, 100));
        rentButton.setForeground(Color.BLACK);
        rentButton.setFocusPainted(false);
        rentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rentButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        rentButton.addMouseListener(new HoverEffect(rentButton, new Color(60, 220, 120)));

        JButton clearButton = new JButton("Clear ❌");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        clearButton.setBackground(new Color(200, 70, 70));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        clearButton.addMouseListener(new HoverEffect(clearButton, new Color(220, 90, 90)));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(carLabel);
        formPanel.add(carDropdown);
        formPanel.add(daysLabel);
        formPanel.add(daysField);
        formPanel.add(rentButton);
        formPanel.add(clearButton);

        formWrapper.add(formPanel);

        // Output Area
        outputArea = new JTextArea(8, 40);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(new Color(200, 255, 200));
        outputArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "📄 Rental Summary", 0, 0, new Font("Segoe UI", Font.BOLD, 18), Color.WHITE));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
        backgroundPanel.add(scrollPane, BorderLayout.SOUTH);

        // Actions
        rentButton.addActionListener(e -> rentCar());
        clearButton.addActionListener(e -> clearForm());

        setVisible(true);
    }

    private void initializeCars() {
        cars.put("C001 - Toyota Camry", "Toyota Camry");
        cars.put("C002 - Honda Accord", "Honda Accord");
        cars.put("C003 - Mahindra Thar", "Mahindra Thar");

        carPrices.put("Toyota Camry", 100.0);
        carPrices.put("Honda Accord", 90.0);
        carPrices.put("Mahindra Thar", 150.0);
    }

    private void rentCar() {
        String name = nameField.getText().trim();
        String selected = (String) carDropdown.getSelectedItem();
        String carName = cars.get(selected);
        String daysText = daysField.getText().trim();

        if (name.isEmpty() || daysText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int days = Integer.parseInt(daysText);
            double pricePerDay = carPrices.get(carName);
            double total = days * pricePerDay;

            outputArea.setText("🎉 Booking Successful!\n\n"
                    + "👤 Name         : " + name + "\n"
                    + "🚘 Car Selected : " + carName + "\n"
                    + "📅 Days         : " + days + "\n"
                    + "💰 Price/Day    : $" + pricePerDay + "\n"
                    + "🧾 Total Amount : $" + total);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of days!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        daysField.setText("");
        carDropdown.setSelectedIndex(0);
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarRentalGUI::new);
    }

    // ⭐️ Button hover effect helper class
    private static class HoverEffect extends MouseAdapter {
        private final JButton button;
        private final Color hoverColor;
        private final Color originalColor;

        public HoverEffect(JButton button, Color hoverColor) {
            this.button = button;
            this.hoverColor = hoverColor;
            this.originalColor = button.getBackground();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(hoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(originalColor);
        }
    }
}
