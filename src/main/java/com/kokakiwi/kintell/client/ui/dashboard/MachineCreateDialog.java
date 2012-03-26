package com.kokakiwi.kintell.client.ui.dashboard;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.kokakiwi.kintell.spec.net.msg.CreateMachineMessage;

public class MachineCreateDialog extends JDialog
{
    private static final long serialVersionUID = 4264679620135171287L;
    
    private final JTextField  id;
    
    public MachineCreateDialog(final GuiDashboard dashboard)
    {
        super(dashboard.getWindow(), "Create a machine");
        
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        final JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(panel, BorderLayout.CENTER);
        final GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);
        
        final JLabel lblId = new JLabel("ID :");
        final GridBagConstraints gbc_lblId = new GridBagConstraints();
        gbc_lblId.insets = new Insets(0, 0, 5, 5);
        gbc_lblId.anchor = GridBagConstraints.EAST;
        gbc_lblId.gridx = 0;
        gbc_lblId.gridy = 0;
        panel.add(lblId, gbc_lblId);
        
        id = new JTextField();
        final GridBagConstraints gbc_id = new GridBagConstraints();
        gbc_id.insets = new Insets(0, 0, 5, 0);
        gbc_id.fill = GridBagConstraints.HORIZONTAL;
        gbc_id.gridx = 1;
        gbc_id.gridy = 0;
        panel.add(id, gbc_id);
        id.setColumns(10);
        
        final JButton btnCreate = new JButton("Create");
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                valid(dashboard);
            }
        });
        final GridBagConstraints gbc_btnCreate = new GridBagConstraints();
        gbc_btnCreate.gridx = 1;
        gbc_btnCreate.gridy = 1;
        panel.add(btnCreate, gbc_btnCreate);
        
        final KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r')
                {
                    valid(dashboard);
                }
            }
        };
        
        id.addKeyListener(keyListener);
        
        pack();
        setLocationRelativeTo(dashboard);
    }
    
    public void valid(GuiDashboard dashboard)
    {
        if (id.getText().isEmpty())
        {
            return;
        }
        if (dashboard.getWindow().getMain().getCore().getMachines()
                .getMachine(id.getText()) != null)
        {
            return;
        }
        
        final CreateMachineMessage msg = new CreateMachineMessage();
        msg.setId(id.getText());
        dashboard.getWindow().getMain().getClient().getChannel().write(msg);
        
        dashboard
                .getWindow()
                .getMain()
                .getCore()
                .getMachines()
                .addMachine(
                        dashboard.getWindow().getMain().getCore().getMachines()
                                .createMachine(id.getText()));
        dashboard.getTree().setModel(
                dashboard.getWindow().getMain().getCore().getMachines()
                        .getTreeModel());
        setVisible(false);
    }
}
