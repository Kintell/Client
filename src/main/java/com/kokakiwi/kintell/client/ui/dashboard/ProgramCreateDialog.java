package com.kokakiwi.kintell.client.ui.dashboard;

import javax.swing.JDialog;

import com.kokakiwi.kintell.client.core.Machine;
import com.kokakiwi.kintell.client.core.Program;
import com.kokakiwi.kintell.spec.net.msg.CreateProgramMessage;
import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ProgramCreateDialog extends JDialog
{
    private static final long                           serialVersionUID = 4264679620135171287L;
    private JTextField                                  id;
    private JTextField                                  name;
    private JComboBox<Machine>                          machines;
    private JComboBox<WorkspaceInitMessage.ContentType> language;
    
    public ProgramCreateDialog(final GuiDashboard dashboard)
    {
        super(dashboard.getWindow(), "Create a program");
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        panel.setLayout(gbl_panel);
        
        JLabel lblMachine = new JLabel("Machine :");
        GridBagConstraints gbc_lblMachine = new GridBagConstraints();
        gbc_lblMachine.anchor = GridBagConstraints.EAST;
        gbc_lblMachine.insets = new Insets(0, 0, 5, 5);
        gbc_lblMachine.gridx = 0;
        gbc_lblMachine.gridy = 0;
        panel.add(lblMachine, gbc_lblMachine);
        
        machines = new JComboBox<Machine>(dashboard.getWindow().getMain()
                .getCore().getMachines().getMachines().values()
                .toArray(new Machine[0]));
        GridBagConstraints gbc_machines = new GridBagConstraints();
        gbc_machines.insets = new Insets(0, 0, 5, 0);
        gbc_machines.fill = GridBagConstraints.HORIZONTAL;
        gbc_machines.gridx = 1;
        gbc_machines.gridy = 0;
        panel.add(machines, gbc_machines);
        
        JButton buttonCreate = new JButton("Cr\u00E9er");
        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                valid(dashboard);
            }
        });
        
        JLabel lblLangage = new JLabel("Langage :");
        GridBagConstraints gbc_lblLangage = new GridBagConstraints();
        gbc_lblLangage.anchor = GridBagConstraints.EAST;
        gbc_lblLangage.insets = new Insets(0, 0, 5, 5);
        gbc_lblLangage.gridx = 0;
        gbc_lblLangage.gridy = 1;
        panel.add(lblLangage, gbc_lblLangage);
        
        language = new JComboBox<WorkspaceInitMessage.ContentType>(dashboard
                .getWindow().getMain().getCore().getContentTypes().values()
                .toArray(new WorkspaceInitMessage.ContentType[0]));
        GridBagConstraints gbc_language = new GridBagConstraints();
        gbc_language.insets = new Insets(0, 0, 5, 0);
        gbc_language.fill = GridBagConstraints.HORIZONTAL;
        gbc_language.gridx = 1;
        gbc_language.gridy = 1;
        panel.add(language, gbc_language);
        
        JLabel lblId = new JLabel("ID :");
        GridBagConstraints gbc_lblId = new GridBagConstraints();
        gbc_lblId.anchor = GridBagConstraints.EAST;
        gbc_lblId.insets = new Insets(0, 0, 5, 5);
        gbc_lblId.gridx = 0;
        gbc_lblId.gridy = 2;
        panel.add(lblId, gbc_lblId);
        
        id = new JTextField();
        GridBagConstraints gbc_id = new GridBagConstraints();
        gbc_id.insets = new Insets(0, 0, 5, 0);
        gbc_id.fill = GridBagConstraints.HORIZONTAL;
        gbc_id.gridx = 1;
        gbc_id.gridy = 2;
        panel.add(id, gbc_id);
        id.setColumns(10);
        
        JLabel lblName = new JLabel("Nom :");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.EAST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 3;
        panel.add(lblName, gbc_lblName);
        
        name = new JTextField();
        GridBagConstraints gbc_name = new GridBagConstraints();
        gbc_name.insets = new Insets(0, 0, 5, 0);
        gbc_name.fill = GridBagConstraints.HORIZONTAL;
        gbc_name.gridx = 1;
        gbc_name.gridy = 3;
        panel.add(name, gbc_name);
        name.setColumns(10);
        GridBagConstraints gbc_buttonCreate = new GridBagConstraints();
        gbc_buttonCreate.gridx = 1;
        gbc_buttonCreate.gridy = 4;
        panel.add(buttonCreate, gbc_buttonCreate);
        
        KeyListener keyListener = new KeyAdapter() {
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
        name.addKeyListener(keyListener);
        
        pack();
        setLocationRelativeTo(dashboard);
    }
    
    public void valid(GuiDashboard dashboard)
    {
        if (id.getText().isEmpty() || name.getText().isEmpty())
        {
            return;
        }
        Machine selected = machines.getItemAt(machines.getSelectedIndex());
        
        if (selected.getProgram(id.getText()) != null)
        {
            return;
        }
        
        Program program = selected.createProgram(id.getText(), name.getText(),
                language.getItemAt(language.getSelectedIndex()));
        
        CreateProgramMessage msg = new CreateProgramMessage();
        msg.setMachine(selected.getId());
        msg.setId(program.getId());
        msg.setName(program.getName());
        msg.setContentType(program.getContentType());
        dashboard.getWindow().getMain().getClient().getChannel().write(msg);
        
        dashboard.getTree().setModel(
                dashboard.getWindow().getMain().getCore().getMachines()
                        .getTreeModel());
        setVisible(false);
    }
    
    public JTextField getId()
    {
        return id;
    }
    
    public JTextField getNameTextField()
    {
        return name;
    }
    
    public JComboBox<Machine> getMachines()
    {
        return machines;
    }
    
    public JComboBox<WorkspaceInitMessage.ContentType> getLanguage()
    {
        return language;
    }
}
