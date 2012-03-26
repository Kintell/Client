package com.kokakiwi.kintell.client.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;

public class GuiMainMenu extends Gui
{
    private static final long    serialVersionUID = 1763750127076785091L;
    
    private final MainWindow     window;
    private final JTextField     pseudo;
    private final JPasswordField password;
    
    public GuiMainMenu(final MainWindow window)
    {
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        this.window = window;
        setMinimumSize(new Dimension(270, 120));
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        setLayout(gridBagLayout);
        
        final JLabel lblPseudo = new JLabel("Pseudo :");
        final GridBagConstraints gbc_lblPseudo = new GridBagConstraints();
        gbc_lblPseudo.insets = new Insets(0, 0, 5, 5);
        gbc_lblPseudo.anchor = GridBagConstraints.EAST;
        gbc_lblPseudo.gridx = 0;
        gbc_lblPseudo.gridy = 0;
        add(lblPseudo, gbc_lblPseudo);
        
        pseudo = new JTextField();
        final GridBagConstraints gbc_pseudo = new GridBagConstraints();
        gbc_pseudo.insets = new Insets(0, 0, 5, 0);
        gbc_pseudo.fill = GridBagConstraints.HORIZONTAL;
        gbc_pseudo.gridx = 1;
        gbc_pseudo.gridy = 0;
        add(pseudo, gbc_pseudo);
        pseudo.setColumns(10);
        
        final JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                valid();
            }
        });
        
        final JLabel lblPassword = new JLabel("Password :");
        final GridBagConstraints gbc_lblPassword = new GridBagConstraints();
        gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
        gbc_lblPassword.anchor = GridBagConstraints.EAST;
        gbc_lblPassword.gridx = 0;
        gbc_lblPassword.gridy = 1;
        add(lblPassword, gbc_lblPassword);
        
        password = new JPasswordField();
        final GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 1;
        add(password, gbc_textField);
        password.setColumns(10);
        final GridBagConstraints gbc_btnConnect = new GridBagConstraints();
        gbc_btnConnect.gridx = 1;
        gbc_btnConnect.gridy = 2;
        add(btnConnect, gbc_btnConnect);
        
        final KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r')
                {
                    valid();
                }
            }
        };
        
        pseudo.addKeyListener(keyListener);
        password.addKeyListener(keyListener);
    }
    
    public void valid()
    {
        final String password = new String(this.password.getPassword());
        
        if (!pseudo.getText().isEmpty()
                && !password.isEmpty()
                && window.getMain().getClient()
                        .connect(pseudo.getText(), password))
        {
            window.getStatusBar().setText(
                    "Chargement de l'espace de travail...");
        }
    }
    
    public void next()
    {
        window.getStatusBar().setText(null);
        
        if (window.getMain().getCore().isConnectionResult())
        {
            window.displayGui(new GuiDashboard(window));
        }
        else
        {
            password.setText(null);
            
            final JDialog dialog2 = new JDialog(window, "Erreur");
            dialog2.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            final JLabel label2 = new JLabel("Mot de passe incorrect");
            label2.setBorder(new EmptyBorder(5, 5, 5, 5));
            dialog2.getContentPane().add(label2);
            dialog2.pack();
            dialog2.validate();
            dialog2.setResizable(false);
            dialog2.setLocationRelativeTo(window);
            dialog2.setVisible(true);
        }
    }
    
    @Override
    public void fillMenuBar(JMenuBar bar)
    {
        
    }
    
    public MainWindow getWindow()
    {
        return window;
    }
    
    public JTextField getPseudo()
    {
        return pseudo;
    }
    
}
