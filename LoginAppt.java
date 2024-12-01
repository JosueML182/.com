import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginAppt {

    // Variables globales
    private static String currentUser;
    private static Map<String, DefaultTableModel> userTables = new HashMap<>();
    private static JFrame mainFrame; // Variable global para la ventana principal

    public static void main(String[] args) {
        // Crear la ventana de login
        createLoginWindow();
    }

    // Método para crear la ventana de login
    private static void createLoginWindow() {
        // Crear la ventana principal
        JFrame frame = new JFrame("Sistema PGE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 250));

        // Título con relieve y sombra
        JLabel title = new JLabel("Sistema PGE");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(60, 63, 65));
        title.setBounds(150, 20, 200, 40);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title);

        // Efecto de sombra
        JLabel shadow = new JLabel("Sistema PGE");
        shadow.setFont(new Font("Arial", Font.BOLD, 28));
        shadow.setBounds(152, 22, 200, 40);
        shadow.setHorizontalAlignment(SwingConstants.CENTER);
        shadow.setForeground(new Color(150, 150, 150, 150)); // Sombra gris claro
        panel.add(shadow);

        // Logo en la esquina superior derecha
        ImageIcon logoIcon = new ImageIcon("cy-logot.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledLogoIcon);
        logoLabel.setBounds(380, 0, 90, 90);
        panel.add(logoLabel);

        // Etiqueta para "Usuario"
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(70, 100, 100, 30);
        panel.add(userLabel);

        JTextField userText = createRoundedTextField();
        userText.setBounds(170, 100, 250, 40);
        panel.add(userText);

        // Etiqueta para "Contraseña"
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setBounds(70, 160, 100, 30);
        panel.add(passLabel);

        JPasswordField passText = createRoundedPasswordField();
        passText.setBounds(170, 160, 250, 40);
        panel.add(passText);

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(200, 50, 50));
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setBounds(170, 210, 250, 20);
        errorLabel.setVisible(false);
        panel.add(errorLabel);

        JButton loginButton = createRoundedButton("Login");
        loginButton.setBounds(200, 260, 120, 40);
        loginButton.setBackground(new Color(85, 150, 240));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        loginButton.addActionListener(e -> attemptLogin(frame, userText, passText, errorLabel));

        // Listeners para "Enter" en campos de texto
        userText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passText.requestFocus();
                }
            }
        });

        passText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void attemptLogin(JFrame frame, JTextField userText, JPasswordField passText, JLabel errorLabel) {
        String username = userText.getText();
        String password = new String(passText.getPassword());

        if (validateLogin(username, password)) {
            errorLabel.setVisible(false);
            currentUser = username; // Establecer el usuario actual

            // Ocultar la ventana de login en lugar de cerrarla
            frame.setVisible(false);

            // Cargar la tabla del usuario si existe
            DefaultTableModel model = loadUserTable(username); // Cargar la tabla del usuario
            if (model == null) { // Si no existe, crear una nueva tabla
                model = new DefaultTableModel();
                model.addColumn("Nombre");
                model.addColumn("Edad");
                model.addColumn("Medicamento");
                model.addColumn("Dolor");
                model.addColumn("Fecha");
                model.addColumn("Hora");
                model.addColumn("Grupo");
            }
            userTables.put(username, model); // Guardar la tabla cargada

            // Mostrar la ventana principal
            showMainWindow();
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos.");
            errorLabel.setVisible(true);
        }
    }

    private static void showMainWindow() {
        // Si ya existe la ventana principal, solo la mostramos
        if (mainFrame == null) {
            mainFrame = new JFrame("Panel Principal");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(600, 500);
            mainFrame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(null);
            mainPanel.setBackground(new Color(240, 240, 245)); // Color más suave para el fondo

            // Título principal con estilo
            JLabel mainTitle = new JLabel("Bienvenido al Sistema PGE");
            mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
            mainTitle.setForeground(new Color(50, 50, 50)); // Color gris oscuro
            mainTitle.setBounds(80, 20, 450, 40);
            mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(mainTitle);

            // Mostrar nombre de usuario en la esquina superior izquierda
            JLabel userLabel = new JLabel("Usuario: " + currentUser);
            userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            userLabel.setForeground(new Color(50, 50, 50)); // Gris oscuro para el texto
            userLabel.setBounds(10, 10, 200, 30);
            mainPanel.add(userLabel);

            // Logo en la esquina superior derecha
            ImageIcon logoIcon = new ImageIcon("cy-logot.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setBounds(490, -5, 90, 90); // Cambiar la posición para la esquina superior derecha
            mainPanel.add(logoLabel);

            // Botón de cerrar sesión
            JButton logoutButton = createRoundedButton("Cerrar Sesión");
            logoutButton.setBounds(470, 400, 100, 30); // Posición en la esquina inferior derecha
            logoutButton.setBackground(new Color(255, 85, 85)); // Rojo para indicar alerta
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente más pequeña
            mainPanel.add(logoutButton);

            logoutButton.addActionListener(e -> {
                saveUserTable(currentUser);
                currentUser = null;
                userTables.clear();
                mainFrame.dispose();
                mainFrame = null; // Permitir una nueva instancia
                createLoginWindow();
            });

            // Tabla para registrar enfermedades
            DefaultTableModel model = userTables.get(currentUser); // Usar la tabla cargada para el usuario
            JTable table = new JTable(model);
            table.setRowHeight(30); // Ajustar la altura de las filas
            table.setFont(new Font("Arial", Font.PLAIN, 14)); // Cambiar el tipo de letra de la tabla

            // Centrar los datos en las celdas
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Centrar contenido
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Ajustar el ancho de las columnas
            table.getColumnModel().getColumn(0).setPreferredWidth(100); // Nombre
            table.getColumnModel().getColumn(1).setPreferredWidth(50); // Edad
            table.getColumnModel().getColumn(2).setPreferredWidth(120); // Medicamento
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Dolor
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Fecha
            table.getColumnModel().getColumn(5).setPreferredWidth(80); // Hora
            table.getColumnModel().getColumn(6).setPreferredWidth(80); // Grupo

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(30, 80, 540, 300);
            mainPanel.add(scrollPane);

            // Agregar un botón para agregar nuevas filas
            JButton addButton = createRoundedButton("Agregar");
            addButton.setBounds(30, 400, 120, 30);
            mainPanel.add(addButton);

            // Botón para borrar los datos de la tabla
            JButton deleteButton = createRoundedButton("Borrar Datos");
            deleteButton.setBounds(200, 400, 220, 30);
            deleteButton.setBackground(new Color(255, 85, 85)); // Rojo para indicar alerta
            deleteButton.setForeground(Color.WHITE);
            mainPanel.add(deleteButton);

            deleteButton.addActionListener(e -> {
                model.setRowCount(0); // Eliminar todas las filas de la tabla
            });

            addButton.addActionListener(e -> {
                // Crear el cuadro de diálogo de registro
                JDialog registerDialog = new JDialog(mainFrame, "Registrar Enfermedad", true);
                registerDialog.setSize(400, 400);
                registerDialog.setLocationRelativeTo(mainFrame);

                JPanel registerPanel = new JPanel();
                registerPanel.setLayout(null);
                registerPanel.setBackground(new Color(240, 240, 245));

                // Campos de texto con diseño redondeado y etiquetas
                JTextField nameField = createRoundedTextField();
                JTextField ageField = createRoundedTextField();
                JTextField medicationField = createRoundedTextField();
                JTextField painField = createRoundedTextField();
                JTextField groupField = createRoundedTextField(); // Campo adicional para el grupo

                nameField.setBounds(130, 40, 200, 30);
                ageField.setBounds(130, 80, 200, 30);
                medicationField.setBounds(130, 120, 200, 30);
                painField.setBounds(130, 160, 200, 30);
                groupField.setBounds(130, 200, 200, 30); // Ubicación del nuevo campo

                // Agregar etiquetas con estilos coherentes
                JLabel nameLabel = new JLabel("Nombre:");
                nameLabel.setBounds(40, 40, 80, 30);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                JLabel ageLabel = new JLabel("Edad:");
                ageLabel.setBounds(40, 80, 80, 30);
                ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                JLabel medicationLabel = new JLabel("Medicamento:");
                medicationLabel.setBounds(40, 120, 100, 30); // Ajusta la posición de la etiqueta si es necesario
                medicationLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                JLabel painLabel = new JLabel("Dolor:");
                painLabel.setBounds(40, 160, 80, 30);
                painLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                JLabel groupLabel = new JLabel("Grupo:");
                groupLabel.setBounds(40, 200, 80, 30);
                groupLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                // Agregar campos y etiquetas al panel
                registerPanel.add(nameLabel);
                registerPanel.add(nameField);
                registerPanel.add(ageLabel);
                registerPanel.add(ageField);
                registerPanel.add(medicationLabel);
                registerPanel.add(medicationField);
                registerPanel.add(painLabel);
                registerPanel.add(painField);
                registerPanel.add(groupLabel);
                registerPanel.add(groupField);

                // Botón para registrar
                JButton registerButton = createRoundedButton("Registrar");
                registerButton.setBounds(150, 250, 120, 40);
                registerButton.setBackground(new Color(34, 139, 34)); // Verde para acción positiva
                registerButton.setForeground(Color.WHITE);

                // Lógica de los KeyListeners para cambiar el foco con Enter
                nameField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            ageField.requestFocus();
                        }
                    }
                });

                ageField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            medicationField.requestFocus();
                        }
                    }
                });

                medicationField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            painField.requestFocus();
                        }
                    }
                });

                painField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            groupField.requestFocus();
                        }
                    }
                });

                groupField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            registerButton.doClick(); // Simula un clic en el botón de registrar
                        }
                    }
                });

                registerButton.addActionListener(evt -> {
                    // Validar campos no vacíos
                    if (nameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                            medicationField.getText().isEmpty() || painField.getText().isEmpty()
                            || groupField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(registerDialog, "Todos los campos deben estar llenos.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crear nueva fila para la tabla
                    Object[] newRow = {
                            nameField.getText(),
                            ageField.getText(),
                            medicationField.getText(),
                            painField.getText(),
                            new SimpleDateFormat("dd/MM/yyyy").format(new Date()), // Fecha actual
                            new SimpleDateFormat("HH:mm").format(new Date()), // Hora actual
                            groupField.getText()
                    };

                    model.addRow(newRow); // Agregar datos al modelo de la tabla
                    registerDialog.dispose();
                });

                registerPanel.add(registerButton); // Asegurar que se agregue al panel

                registerDialog.add(registerPanel);
                registerDialog.setVisible(true);
            });

            mainFrame.add(mainPanel);
            mainFrame.setVisible(true);
        } else {
            mainFrame.setVisible(true); // Solo mostrar si no está visible
        }
    }

    private static boolean validateLogin(String username, String password) {
        // Validar usuario y contraseña con combinaciones específicas
        return ("admin".equals(username) && "admin".equals(password)) ||
                ("236071".equals(username) && "236071".equals(password)) ||
                ("234081".equals(username) && "234081".equals(password));
    }

    // Método para crear un campo de texto redondeado
    private static JTextField createRoundedTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(new Color(255, 255, 255));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        textField.setCaretColor(new Color(80, 80, 80));
        textField.setFocusTraversalKeysEnabled(false);
        return textField;
    }

    // Método para crear un campo de contraseña redondeado
    private static JPasswordField createRoundedPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        passwordField.setCaretColor(new Color(80, 80, 80));
        return passwordField;
    }

    // Método para crear un botón redondeado
    private static JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        button.setBackground(new Color(0, 123, 255)); // Color de fondo azul
        button.setForeground(Color.WHITE);
        return button;
    }

    private static void saveUserTable(String username) {
        DefaultTableModel model = userTables.get(username);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(username + "_table.csv"))) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }
            writer.newLine();
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    writer.write(model.getValueAt(row, col) + ",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DefaultTableModel loadUserTable(String username) {
        DefaultTableModel model = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(username + "_table.csv"))) {
            model = new DefaultTableModel();
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (firstLine) {
                    for (String token : tokens) {
                        model.addColumn(token);
                    }
                    firstLine = false;
                } else {
                    model.addRow(tokens);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }
}