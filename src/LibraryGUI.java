import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class LibraryGUI extends JFrame {
    private BookDAO dao;
    private final Color PRIMARY_COLOR = new Color(30, 58, 138); // Deep navy blue
    private final Color SECONDARY_COLOR = new Color(241, 245, 249); // Light gray-blue
    private final Color ACCENT_COLOR = new Color(234, 88, 12); // Vibrant orange
    private final Font TITLE_FONT = new Font("Segoe UI Emoji", Font.BOLD, 34);
private final Font SUBTITLE_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 18);
private final Font BUTTON_FONT = new Font("Segoe UI Emoji", Font.BOLD, 16);
private final Font LABEL_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);
private final Font INPUT_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 15);


    public LibraryGUI() {
        // Initialize DB connection
        initializeDatabase();
        
        // Configure main window
        configureWindow();
        
        // Create UI components
        createHeader();
        createMainPanel();
        createFooter();
        
        setVisible(true);
    }

    private void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db", "root", "Ayushdddoracle5@");
            dao = new BookDAO(conn);
        } catch (SQLException e) {
            showErrorDialog("Database Connection Failed", "Unable to connect to database: " + e.getMessage());
            System.exit(1);
        }
    }

    private void configureWindow() {
        setTitle("Library Management System");
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SECONDARY_COLOR);
        setLayout(new BorderLayout());
    }

    private void createHeader() {
        JPanel headerPanel = new GradientPanel(PRIMARY_COLOR, new Color(15, 29, 69));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage your library collection with ease");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());
        
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Create card panel with modern shadow effect
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new RoundedBorder(15, new Color(229, 231, 235)));
        
        // Add buttons with icons and modern styling
        cardPanel.add(createActionButton("➕ ADD NEW BOOK", ACCENT_COLOR, e -> showAddBookDialog()));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(createActionButton("📖 VIEW BOOK COLLECTION", PRIMARY_COLOR, e -> showBooksTable()));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(createActionButton("📤 ISSUE BOOK TO MEMBER", new Color(22, 163, 74), e -> showIssueDialog()));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(createActionButton("📥 RETURN BOOK FROM MEMBER", new Color(220, 38, 38), e -> showReturnDialog()));
        
        mainPanel.add(cardPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border needed for the modern look
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(350, 55));
        button.setMaximumSize(new Dimension(350, 55));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        return button;
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(241, 245, 249));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));
        
        JLabel footerLabel = new JLabel("© 2023 Library Management System | Version 2.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerLabel.setForeground(new Color(100, 116, 139));
        
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void showAddBookDialog() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 15, 15);
        
        JLabel titleLabel = new JLabel("Add New Book to Collection");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        // Declare the text fields
        JTextField bookIdField = new JTextField(20);
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        
        // Add form fields
        addFormField(formPanel, gbc, "Book ID:", bookIdField);
        addFormField(formPanel, gbc, "Title:", titleField);
        addFormField(formPanel, gbc, "Author:", authorField);
        addFormField(formPanel, gbc, "Quantity:", quantityField);
        
        int option = JOptionPane.showOptionDialog(this, formPanel, "Add Book", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, 
            null, null, null);
            
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(bookIdField.getText());
                String title = titleField.getText();
                String author = authorField.getText();
                int qty = Integer.parseInt(quantityField.getText());
    
                dao.addBook(id, title, author, qty);
                showSuccessDialog("Book Added", "The book has been successfully added to the collection!");
            } catch (Exception ex) {
                showErrorDialog("Add Book Failed", "Error: " + ex.getMessage());
            }
        }
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(LABEL_FONT);
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        field.setFont(INPUT_FONT);
        if (field instanceof JTextField) {
            ((JTextField)field).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
        panel.add(field, gbc);
        
        gbc.gridy++;
    }

    private void showBooksTable() {
        try {
            String[] columns = {"ID", "Title", "Author", "Available Qty"};
            java.util.List<Book> books = dao.getAllBooks();
            String[][] data = new String[books.size()][4];

            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                data[i][0] = String.valueOf(b.getId());
                data[i][1] = b.getTitle();
                data[i][2] = b.getAuthor();
                data[i][3] = String.valueOf(b.getQuantity());
            }

            DefaultTableModel model = new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            table.setRowHeight(35);
            table.setSelectionBackground(new Color(219, 234, 254));
            table.setSelectionForeground(Color.BLACK);
            table.setGridColor(new Color(226, 232, 240));
            table.setShowGrid(true);
            table.setIntercellSpacing(new Dimension(0, 0));
            
            // Custom header renderer
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
            header.setBackground(PRIMARY_COLOR);
            header.setForeground(Color.WHITE);
            header.setReorderingAllowed(false);
            
            // Center align ID and Quantity columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(750, 400));
            
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            tablePanel.setBackground(Color.WHITE);
            tablePanel.add(scrollPane);
            
            JOptionPane.showMessageDialog(this, tablePanel, "Library Book Collection", 
                JOptionPane.PLAIN_MESSAGE, null);
        } catch (SQLException ex) {
            showErrorDialog("Load Books Failed", "Error loading books: " + ex.getMessage());
        }
    }

    class InputDialogComponents {
        JPanel panel;
        JTextField inputField;
    
        InputDialogComponents(JPanel panel, JTextField inputField) {
            this.panel = panel;
            this.inputField = inputField;
        }
    }

    private void showIssueDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Enter Book ID:"));
        JTextField bookIdField = new JTextField();
        panel.add(bookIdField);
    
        panel.add(new JLabel("Enter User Name:"));
        JTextField userNameField = new JTextField();
        panel.add(userNameField);
    
        int option = JOptionPane.showOptionDialog(this, panel, "Issue Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if (option == JOptionPane.OK_OPTION) {
            try {
                int bookId = Integer.parseInt(bookIdField.getText().trim());
                String userName = userNameField.getText().trim();
    
                if (userName.isEmpty()) {
                    showErrorDialog("Missing Input", "User name cannot be empty.");
                    return;
                }
    
                dao.issueBook(bookId, userName);
                showSuccessDialog("Book Issued", "Book has been successfully issued to member!");
            } catch (NumberFormatException nfe) {
                showErrorDialog("Invalid Input", "Book ID must be a number.");
            } catch (Exception ex) {
                showErrorDialog("Issue Book Failed", "Error: " + ex.getMessage());
            }
        }
    }
    

    private void showReturnDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        JTextField bookIdField = new JTextField();
        JTextField userNameField = new JTextField();
    
        panel.add(new JLabel("Enter Book ID to return:"));
        panel.add(bookIdField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Enter User Name:"));
        panel.add(userNameField);
    
        int option = JOptionPane.showOptionDialog(
            this, panel, "Return Book",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, null, null
        );
    
        if (option == JOptionPane.OK_OPTION) {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                String userName = userNameField.getText().trim();
    
                dao.returnBook(bookId, userName);
                showSuccessDialog("Book Returned", "Book has been successfully returned to library!");
            } catch (Exception ex) {
                showErrorDialog("Return Book Failed", "Error: " + ex.getMessage());
            }
        }
    }
    

    private InputDialogComponents createInputDialogPanel(String title, String prompt) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 15));
        inputPanel.setBackground(Color.WHITE);
        
        JLabel promptLabel = new JLabel(prompt);
        promptLabel.setFont(LABEL_FONT);
        inputPanel.add(promptLabel, BorderLayout.NORTH);
        
        JTextField inputField = new JTextField();
        inputField.setFont(INPUT_FONT);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return new InputDialogComponents(panel, inputField);
    }
    
    private void showSuccessDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, 
            "<html><div style='font-family:Segoe UI;font-size:15px;width:350px;'>" + message + "</div></html>", 
            title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, 
            "<html><div style='font-family:Segoe UI;font-size:15px;width:350px;'>" + message + "</div></html>", 
            title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Set custom UI defaults
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
                
                new LibraryGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Custom gradient panel for header
    class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;

        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            super.paintComponent(g);
        }
    }

    // Modern rounded border with shadow effect
    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color borderColor;
        
        public RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Paint shadow
            for (int i = 0; i < 5; i++) {
                g2.setColor(new Color(0, 0, 0, 10 - i*2));
                g2.fillRoundRect(x + i, y + i, width - i*2, height - i*2, radius, radius);
            }
            
            // Paint border
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+1, radius+1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius+1;
            return insets;
        }
    }
}