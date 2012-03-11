package com.kokakiwi.kintell.client.ui;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GuiMainMenu extends Gui
{
    private static final long serialVersionUID = 1763750127076785091L;
    
    private final MainWindow  window;
    private final JTextField  pseudo;
    
    public GuiMainMenu(final MainWindow window)
    {
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        this.window = window;
        setMinimumSize(new Dimension(270, 80));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        
        JLabel lblPseudo = new JLabel("Pseudo :");
        GridBagConstraints gbc_lblPseudo = new GridBagConstraints();
        gbc_lblPseudo.insets = new Insets(0, 0, 5, 5);
        gbc_lblPseudo.anchor = GridBagConstraints.EAST;
        gbc_lblPseudo.gridx = 0;
        gbc_lblPseudo.gridy = 0;
        add(lblPseudo, gbc_lblPseudo);
        
        pseudo = new JTextField();
        GridBagConstraints gbc_pseudo = new GridBagConstraints();
        gbc_pseudo.insets = new Insets(0, 0, 5, 0);
        gbc_pseudo.fill = GridBagConstraints.HORIZONTAL;
        gbc_pseudo.gridx = 1;
        gbc_pseudo.gridy = 0;
        add(pseudo, gbc_pseudo);
        pseudo.setColumns(10);
        
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                valid();
            }
        });
        GridBagConstraints gbc_btnConnect = new GridBagConstraints();
        gbc_btnConnect.gridx = 1;
        gbc_btnConnect.gridy = 1;
        add(btnConnect, gbc_btnConnect);
        
        KeyListener keyListener = new KeyAdapter() {
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
    }
    
    public void valid()
    {
        if (!pseudo.getText().isEmpty()
                && window.getMain().getClient().connect(pseudo.getText()))
        {
            JDialog dialog = new JDialog(window,
                    "Chargement de l'espace de travail...");
            JLabel label = new JLabel("Chargement de l'espace de travail...");
            dialog.add(label);
            dialog.pack();
            dialog.validate();
            dialog.setLocationRelativeTo(window);
            dialog.setVisible(true);
            while (window.getMain().getCore().isWaiting())
            {
                
            }
            dialog.setVisible(false);
            window.displayGui(new GuiDashboard(window));
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
