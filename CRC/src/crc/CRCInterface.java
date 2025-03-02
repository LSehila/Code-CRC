package crc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CRCInterface extends JFrame {

    private static final long serialVersionUID = 1L;
    
    // Composants pour la vue "Calculer CRC"
    private JTextField txtMessageCalc;
    private JTextField txtPolynomialCalc;
    private JComboBox<String> comboPolyCalc;
    private JTextArea txtResultCalc;
    
    // Composants pour la vue "Vérifier CRC"
    private JTextField txtMessageVerify;
    private JTextField txtPolynomialVerify;
    private JComboBox<String> comboPolyVerify;
    private JTextArea txtResultVerify;
    
    // Composants pour la vue "Calculer Polynôme"
    private JTextField txtTramePoly;
    private JTextArea txtResultPoly;
    
    // Nouvelle liste des polynômes connus
    private final String[] polyOptions = {
        "Custom",
        "CRC-12: 1100000001111",
        "CRC-16: 1100000000000101",
        "CRC-CCITT: 10001000000100001",
        "CRC-32: 100000100110000010001110110110111"
    };

    public CRCInterface() {
        setTitle("Calculateur et Vérificateur de CRC");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Définir une bordure globale et un fond personnalisé
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Utilisation d'un JTabbedPane avec customisation des onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(230, 230, 250));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Onglet Calculer CRC
        JPanel panelCalc = new JPanel(new BorderLayout(10, 10));
        panelCalc.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCalc.setBackground(Color.white);
        panelCalc.add(createCalcPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Calculer CRC", panelCalc);

        // Onglet Vérifier CRC
        JPanel panelVerify = new JPanel(new BorderLayout(10, 10));
        panelVerify.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelVerify.setBackground(Color.white);
        panelVerify.add(createVerifyPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Vérifier CRC", panelVerify);
        
        // Onglet Calculer Polynôme
        JPanel panelPoly = new JPanel(new BorderLayout(10, 10));
        panelPoly.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelPoly.setBackground(Color.white);
        panelPoly.add(createPolynomialPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Calculer Polynôme", panelPoly);

        contentPane.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCalcPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.white);
        
        // Panel des entrées organisé en 3 lignes
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        inputPanel.setBackground(Color.white);
        
        // Ligne 1 : Message binaire
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBackground(Color.white);
        JLabel lblMessage = new JLabel("Message binaire :");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessageCalc = new JTextField();
        txtMessageCalc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messagePanel.add(lblMessage, BorderLayout.WEST);
        messagePanel.add(txtMessageCalc, BorderLayout.CENTER);
        inputPanel.add(messagePanel);
        
        // Ligne 2 : Polynôme générateur
        JPanel polyPanel = new JPanel(new BorderLayout(5, 5));
        polyPanel.setBackground(Color.white);
        JLabel lblPolynomial = new JLabel("Polynôme générateur (binaire) :");
        lblPolynomial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPolynomialCalc = new JTextField();
        txtPolynomialCalc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPolyCalc = new JComboBox<>(polyOptions);
        comboPolyCalc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPolyCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) comboPolyCalc.getSelectedItem();
                if (selection.startsWith("Custom")) {
                    txtPolynomialCalc.setEnabled(true);
                    txtPolynomialCalc.setText("");
                } else {
                    String[] parts = selection.split(":");
                    if (parts.length > 1) {
                        String polyBinary = parts[1].trim();
                        txtPolynomialCalc.setText(polyBinary);
                        txtPolynomialCalc.setEnabled(false);
                    }
                }
            }
        });
        comboPolyCalc.setSelectedIndex(0);
        JPanel polySelectionPanel = new JPanel(new BorderLayout(5, 5));
        polySelectionPanel.setBackground(Color.white);
        polySelectionPanel.add(comboPolyCalc, BorderLayout.NORTH);
        polySelectionPanel.add(txtPolynomialCalc, BorderLayout.CENTER);
        
        polyPanel.add(lblPolynomial, BorderLayout.WEST);
        polyPanel.add(polySelectionPanel, BorderLayout.CENTER);
        inputPanel.add(polyPanel);
        
        // Ligne 3 : Bouton Calculer
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.white);
        JButton btnCalculate = new JButton("Calculer CRC");
        btnCalculate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalculate.setBackground(new Color(100, 149, 237));
        btnCalculate.setForeground(Color.white);
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCRC();
            }
        });
        btnPanel.add(btnCalculate);
        inputPanel.add(btnPanel);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Zone de résultat
        txtResultCalc = new JTextArea();
        txtResultCalc.setEditable(false);
        txtResultCalc.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultCalc.setBackground(new Color(248, 248, 255));
        txtResultCalc.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(txtResultCalc);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createVerifyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.white);
        
        // Panel des entrées organisé en 3 lignes
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        inputPanel.setBackground(Color.white);
        
        // Ligne 1 : Message avec CRC
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBackground(Color.white);
        JLabel lblMessage = new JLabel("Message avec CRC :");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessageVerify = new JTextField();
        txtMessageVerify.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messagePanel.add(lblMessage, BorderLayout.WEST);
        messagePanel.add(txtMessageVerify, BorderLayout.CENTER);
        inputPanel.add(messagePanel);
        
        // Ligne 2 : Polynôme générateur
        JPanel polyPanel = new JPanel(new BorderLayout(5, 5));
        polyPanel.setBackground(Color.white);
        JLabel lblPolynomial = new JLabel("Polynôme générateur (binaire) :");
        lblPolynomial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPolynomialVerify = new JTextField();
        txtPolynomialVerify.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPolyVerify = new JComboBox<>(polyOptions);
        comboPolyVerify.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPolyVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) comboPolyVerify.getSelectedItem();
                if (selection.startsWith("Custom")) {
                    txtPolynomialVerify.setEnabled(true);
                    txtPolynomialVerify.setText("");
                } else {
                    String[] parts = selection.split(":");
                    if (parts.length > 1) {
                        String polyBinary = parts[1].trim();
                        txtPolynomialVerify.setText(polyBinary);
                        txtPolynomialVerify.setEnabled(false);
                    }
                }
            }
        });
        comboPolyVerify.setSelectedIndex(0);
        JPanel polySelectionPanel = new JPanel(new BorderLayout(5, 5));
        polySelectionPanel.setBackground(Color.white);
        polySelectionPanel.add(comboPolyVerify, BorderLayout.NORTH);
        polySelectionPanel.add(txtPolynomialVerify, BorderLayout.CENTER);
        
        polyPanel.add(lblPolynomial, BorderLayout.WEST);
        polyPanel.add(polySelectionPanel, BorderLayout.CENTER);
        inputPanel.add(polyPanel);
        
        // Ligne 3 : Bouton Vérifier
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.white);
        JButton btnVerify = new JButton("Vérifier CRC");
        btnVerify.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerify.setBackground(new Color(60, 179, 113));
        btnVerify.setForeground(Color.white);
        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyCRC();
            }
        });
        btnPanel.add(btnVerify);
        inputPanel.add(btnPanel);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Zone de résultat
        txtResultVerify = new JTextArea();
        txtResultVerify.setEditable(false);
        txtResultVerify.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultVerify.setBackground(new Color(248, 248, 255));
        txtResultVerify.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(txtResultVerify);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createPolynomialPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.white);
        
        // Panel d'entrée
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.white);
        JLabel lblTrame = new JLabel("Trame binaire :");
        lblTrame.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTramePoly = new JTextField();
        txtTramePoly.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(lblTrame);
        inputPanel.add(txtTramePoly);
        
        // Bouton Calculer Polynôme
        JButton btnComputePoly = new JButton("Calculer Polynôme");
        btnComputePoly.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnComputePoly.setBackground(new Color(218, 112, 214));
        btnComputePoly.setForeground(Color.white);
        btnComputePoly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computePolynomial();
            }
        });
        inputPanel.add(new JLabel());  // espace vide
        inputPanel.add(btnComputePoly);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Zone de résultat
        txtResultPoly = new JTextArea();
        txtResultPoly.setEditable(false);
        txtResultPoly.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultPoly.setBackground(new Color(248, 248, 255));
        txtResultPoly.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(txtResultPoly);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Convertit une trame binaire en une expression polynomiale.
     * Par exemple, "10110" devient "x^4 + x^2 + x" et affiche également la forme binaire.
     */
    private void computePolynomial() {
        String trame = txtTramePoly.getText().trim();
        if (trame.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir une trame binaire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!estBinaire(trame)) {
            JOptionPane.showMessageDialog(this, "La trame doit être composée uniquement de 0 et de 1.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder polyBuilder = new StringBuilder();
        int n = trame.length();
        boolean firstTerm = true;
        for (int i = 0; i < n; i++) {
            if (trame.charAt(i) == '1') {
                int exponent = n - i - 1;
                if (!firstTerm) {
                    polyBuilder.append(" + ");
                }
                if (exponent == 0) {
                    polyBuilder.append("1");
                } else if (exponent == 1) {
                    polyBuilder.append("x");
                } else {
                    polyBuilder.append("x^").append(exponent);
                }
                firstTerm = false;
            }
        }
        if (firstTerm) {
            polyBuilder.append("0");
        }
        txtResultPoly.setText("Polynôme générateur (expression) : " + polyBuilder.toString()
                + "\nPolynôme générateur (binaire) : " + trame);
    }
    
    /**
     * Vérifie que la chaîne ne contient que 0 et 1.
     */
    private boolean estBinaire(String chaine) {
        return chaine.matches("[01]+");
    }

    private void calculateCRC() {
        String message = txtMessageCalc.getText().trim();
        String polynomialStr = txtPolynomialCalc.getText().trim();
        if (message.isEmpty() || polynomialStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!estBinaire(message) || !estBinaire(polynomialStr)) {
            JOptionPane.showMessageDialog(this, "Le message et le polynôme doivent être composés uniquement de 0 et de 1.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Integer> polynomial;
        try {
            polynomial = Arrays.stream(polynomialStr.split(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Le polynôme doit être une chaîne binaire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<List<Integer>> steps = CRCCalculator.calculerCRC(message, polynomial);
        List<Integer> finalStep = steps.get(steps.size() - 1);
        int crcLength = polynomial.size() - 1;
        List<Integer> crc = finalStep.subList(finalStep.size() - crcLength, finalStep.size());
        StringBuilder sb = new StringBuilder();
        sb.append("=== Calcul du CRC ===\n");
        sb.append("Message binaire : ").append(message).append("\n");
        sb.append("Polynôme générateur : ").append(polynomialStr).append("\n\n");
        sb.append("Étapes de division :\n");
        for (int i = 0; i < steps.size(); i++) {
            sb.append("Étape ").append(i + 1).append(" : ");
            for (Integer bit : steps.get(i)) {
                sb.append(bit);
            }
            sb.append("\n");
        }
        sb.append("\nCRC calculé : ");
        crc.forEach(sb::append);
        String messageTransmis = message + crc.stream().map(String::valueOf).collect(Collectors.joining(""));
        sb.append("\nMessage à transmettre : ").append(messageTransmis).append("\n");
        txtResultCalc.setText(sb.toString());
    }

    private void verifyCRC() {
        String messageWithCRC = txtMessageVerify.getText().trim();
        String polynomialStr = txtPolynomialVerify.getText().trim();
        if (messageWithCRC.isEmpty() || polynomialStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!estBinaire(messageWithCRC) || !estBinaire(polynomialStr)) {
            JOptionPane.showMessageDialog(this, "Le message et le polynôme doivent être composés uniquement de 0 et de 1.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Integer> polynomial;
        try {
            polynomial = Arrays.stream(polynomialStr.split(""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Le polynôme doit être une chaîne binaire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<List<Integer>> steps = CRCCalculator.verifierCRC(messageWithCRC, polynomial);
        StringBuilder sb = new StringBuilder();
        sb.append("=== Vérification du CRC ===\n");
        sb.append("Message avec CRC : ").append(messageWithCRC).append("\n");
        sb.append("Polynôme générateur : ").append(polynomialStr).append("\n\n");
        sb.append("Étapes de division :\n");
        for (int i = 0; i < steps.size(); i++) {
            sb.append("Étape ").append(i + 1).append(" : ");
            for (Integer bit : steps.get(i)) {
                sb.append(bit);
            }
            sb.append("\n");
        }
        List<Integer> finalStep = steps.get(steps.size() - 1);
        boolean isCorrect = finalStep.stream().allMatch(bit -> bit == 0);
        sb.append("\nRésultat : ");
        sb.append(isCorrect ? "Le message est correct, aucune erreur détectée." : "Le message contient une erreur !");
        txtResultVerify.setText(sb.toString());
    }

    public static void main(String[] args) {
        try {
            // Appliquer le LookAndFeel Nimbus pour un rendu moderne
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus LookAndFeel non disponible");
        }
        
        SwingUtilities.invokeLater(() -> {
            CRCInterface crcInterface = new CRCInterface();
            crcInterface.setVisible(true);
        });
    }
}
