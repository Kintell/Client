package com.kokakiwi.kintell.client.ui.dashboard;

import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.google.common.collect.Maps;
import com.kokakiwi.kintell.client.core.Program;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.MainWindow;
import com.kokakiwi.kintell.client.ui.board.LaunchBoardDialog;
import com.kokakiwi.kintell.client.ui.editor.EditorPane;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;

public class GuiDashboard extends Gui
{
    private static final long          serialVersionUID = 7005308840219874402L;
    private JTree                      tree;
    private JTabbedPane                tabs;
    
    private final Map<String, Program> opened           = Maps.newLinkedHashMap();
    
    private final MainWindow           window;
    
    public GuiDashboard(final MainWindow window)
    {
        super();
        this.window = window;
        
        setLayout(new BorderLayout(0, 0));
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setContinuousLayout(true);
        splitPane.setPreferredSize(new Dimension(800, 600));
        add(splitPane, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(150, 100));
        splitPane.setLeftComponent(scrollPane);
        
        tree = new JTree(window.getMain().getCore().getMachines()
                .getTreeModel());
        tree.setCellRenderer(new DefaultTreeCellRenderer());
        tree.setBorder(new EmptyBorder(3, 3, 3, 3));
        tree.setVisibleRowCount(50);
        tree.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2
                        && e.getButton() == MouseEvent.BUTTON1)
                {
                    if (tree.getSelectionCount() > 0)
                    {
                        TreePath path = tree.getSelectionPath();
                        if (path.getPathCount() >= 3)
                        {
                            Program program = (Program) ((DefaultMutableTreeNode) path
                                    .getPathComponent(2)).getUserObject();
                            
                            Program isOpened = opened.get(program.getId());
                            if (isOpened == null)
                            {
                                opened.put(program.getId(), program);
                                open(program);
                            }
                        }
                    }
                }
            }
        });
        scrollPane.setViewportView(tree);
        
        JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e)
            {
                selectedTab(tabs.getSelectedIndex());
            }
        });
        panel.add(tabs, BorderLayout.CENTER);
        
        setMinimumSize(new Dimension(800, 600));
    }
    
    @Override
    public void fillMenuBar(JMenuBar bar)
    {
        JMenu file = new JMenu("File");
        
        JMenu create = new JMenu("New");
        
        JMenuItem newMachine = new JMenuItem(new AbstractAction("Machine") {
            private static final long serialVersionUID = 8601855836914622132L;
            
            public void actionPerformed(ActionEvent e)
            {
                MachineCreateDialog dialog = new MachineCreateDialog(
                        GuiDashboard.this);
                dialog.setVisible(true);
            }
        });
        create.add(newMachine);
        
        JMenuItem newProgram = new JMenuItem(new AbstractAction("Program") {
            private static final long serialVersionUID = -8815638955911075235L;
            
            public void actionPerformed(ActionEvent e)
            {
                if (window.getMain().getCore().getMachines().getMachines()
                        .size() > 0)
                {
                    ProgramCreateDialog dialog = new ProgramCreateDialog(
                            GuiDashboard.this);
                    dialog.setVisible(true);
                    dialog.getId().requestFocus();
                }
            }
        });
        create.add(newProgram);
        
        file.add(create);
        
        JMenuItem launch = new JMenuItem(new AbstractAction("Lancer un match") {
            private static final long serialVersionUID = 8835698270712743644L;
            
            public void actionPerformed(ActionEvent e)
            {
                window.getMain().getCore().setWaiting(true);
                
                ProgramsListMessage msg = new ProgramsListMessage();
                window.getMain().getClient().getChannel().write(msg);
                
                JDialog dialog1 = new JDialog(window,
                        "Chargement des autres programmes...");
                JLabel label = new JLabel("Chargement des autres programmes...");
                dialog1.add(label);
                dialog1.pack();
                dialog1.validate();
                dialog1.setLocationRelativeTo(window);
                dialog1.setVisible(true);
                
                while (window.getMain().getCore().isWaiting())
                {
                    
                }
                
                dialog1.setVisible(false);
                
                LaunchBoardDialog dialog = new LaunchBoardDialog(
                        GuiDashboard.this);
                dialog.setVisible(true);
            }
        });
        
        file.add(launch);
        
        bar.add(file);
    }
    
    public void open(Program program)
    {
        EditorPane editor = new EditorPane(program);
        tabs.addTab(program.getName(), editor);
    }
    
    public void selectedTab(int index)
    {
        for (int i = 0; i < tabs.getTabCount(); i++)
        {
            tabs.setTabComponentAt(i, new ButtonTabComponent(
                    tabs.getTitleAt(i), this, (i == index)));
        }
    }
    
    public MainWindow getWindow()
    {
        return window;
    }
    
    public JTree getTree()
    {
        return tree;
    }
    
    public JTabbedPane getTabs()
    {
        return tabs;
    }
    
    public Map<String, Program> getOpened()
    {
        return opened;
    }
    
}
