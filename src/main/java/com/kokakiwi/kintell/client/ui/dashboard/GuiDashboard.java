package com.kokakiwi.kintell.client.ui.dashboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
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
import com.kokakiwi.kintell.client.ui.board.RankEntriesDialog;
import com.kokakiwi.kintell.client.ui.editor.EditorPane;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;
import com.kokakiwi.kintell.spec.net.msg.RankEntriesMessage;

public class GuiDashboard extends Gui
{
    private static final long          serialVersionUID = 7005308840219874402L;
    private final JTree                tree;
    private final JTabbedPane          tabs;
    
    private final Map<String, Program> opened           = Maps.newLinkedHashMap();
    
    private final MainWindow           window;
    private final JTextArea            debugArea;
    
    public GuiDashboard(final MainWindow window)
    {
        super();
        this.window = window;
        
        setLayout(new BorderLayout(0, 0));
        
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setContinuousLayout(true);
        splitPane.setPreferredSize(new Dimension(800, 600));
        add(splitPane, BorderLayout.CENTER);
        
        final JScrollPane scrollPane = new JScrollPane();
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
                        final TreePath path = tree.getSelectionPath();
                        if (path.getPathCount() >= 3)
                        {
                            final Program program = (Program) ((DefaultMutableTreeNode) path
                                    .getPathComponent(2)).getUserObject();
                            
                            final Program isOpened = opened.get(program.getId());
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
        
        final JPanel mainPanel = new JPanel();
        splitPane.setRightComponent(mainPanel);
        mainPanel.setLayout(new BorderLayout(0, 0));
        
        final JSplitPane mainSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setContinuousLayout(true);
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);
        
        final JPanel panel = new JPanel();
        mainSplitPane.setLeftComponent(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        tabs = new JTabbedPane(SwingConstants.TOP);
        tabs.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e)
            {
                selectedTab(tabs.getSelectedIndex());
            }
        });
        panel.add(tabs, BorderLayout.CENTER);
        
        final JScrollPane debugAreaScroll = new JScrollPane();
        debugAreaScroll
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        debugAreaScroll.setMinimumSize(new Dimension(
                debugAreaScroll.getWidth(), 100));
        
        debugArea = new JTextArea(5, 50);
        debugArea.setEditable(false);
        debugAreaScroll.setViewportView(debugArea);
        
        mainSplitPane.setRightComponent(debugAreaScroll);
        mainSplitPane.setDividerLocation(420);
        
        setMinimumSize(new Dimension(800, 600));
    }
    
    @Override
    public void fillMenuBar(JMenuBar bar)
    {
        final JMenu file = new JMenu("File");
        
        final JMenu create = new JMenu("Nouveau");
        
        final JMenuItem newMachine = new JMenuItem(
                new AbstractAction("Machine") {
                    private static final long serialVersionUID = 8601855836914622132L;
                    
                    public void actionPerformed(ActionEvent e)
                    {
                        final MachineCreateDialog dialog = new MachineCreateDialog(
                                GuiDashboard.this);
                        dialog.setVisible(true);
                    }
                });
        create.add(newMachine);
        
        final JMenuItem newProgram = new JMenuItem(new AbstractAction(
                "Programme") {
            private static final long serialVersionUID = -8815638955911075235L;
            
            public void actionPerformed(ActionEvent e)
            {
                if (window.getMain().getCore().getMachines().getMachines()
                        .size() > 0)
                {
                    final ProgramCreateDialog dialog = new ProgramCreateDialog(
                            GuiDashboard.this);
                    dialog.setVisible(true);
                    dialog.getId().requestFocus();
                }
            }
        });
        create.add(newProgram);
        
        file.add(create);
        
        final JMenuItem launch = new JMenuItem(new AbstractAction(
                "Lancer un match") {
            private static final long serialVersionUID = 8835698270712743644L;
            
            public void actionPerformed(ActionEvent e)
            {
                window.getStatusBar().setText(
                        "Chargement des autres programmes...");
                
                final ProgramsListMessage msg = new ProgramsListMessage();
                window.getMain().getClient().sendMessage(msg);
            }
        });
        
        file.add(launch);
        
        final JMenuItem ranking = new JMenuItem(
                new AbstractAction("Classement") {
                    private static final long serialVersionUID = 5251804134125998998L;
                    
                    public void actionPerformed(ActionEvent e)
                    {
                        window.getStatusBar().setText(
                                "Chargement du classement...");
                        
                        final RankEntriesMessage msg = new RankEntriesMessage();
                        window.getMain().getClient().sendMessage(msg);
                    }
                });
        
        file.add(ranking);
        
        bar.add(file);
    }
    
    public void openLaunchWindow()
    {
        window.getStatusBar().setText(null);
        
        final LaunchBoardDialog dialog = new LaunchBoardDialog(this);
        dialog.setVisible(true);
    }
    
    public void openRankEntriesWindow(RankEntriesMessage msg)
    {
        window.getStatusBar().setText(null);
        
        final RankEntriesDialog dialog = new RankEntriesDialog(this, msg);
        dialog.setVisible(true);
    }
    
    public void open(Program program)
    {
        final EditorPane editor = new EditorPane(program);
        tabs.addTab(program.getName(), editor);
    }
    
    public void selectedTab(int index)
    {
        for (int i = 0; i < tabs.getTabCount(); i++)
        {
            tabs.setTabComponentAt(i, new ButtonTabComponent(
                    tabs.getTitleAt(i), this, i == index));
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
    
    public JTextArea getDebugArea()
    {
        return debugArea;
    }
    
}
