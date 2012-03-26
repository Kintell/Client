package com.kokakiwi.kintell.client.ui.board;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;
import com.kokakiwi.kintell.spec.net.msg.LaunchMessage;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage.Program;

public class LaunchBoardDialog extends JDialog
{
    private static final long                              serialVersionUID = -5996791042911210307L;
    
    private final GuiDashboard                             dashboard;
    
    private final JComboBox<BoardFactory<? extends Board>> boards;
    private final JList<ProgramsListMessage.Program>       programs;
    private final JList<ProgramsListMessage.Program>       selectedPrograms;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public LaunchBoardDialog(GuiDashboard dashboard)
    {
        super(dashboard.getWindow(), "Lancer un match");
        this.dashboard = dashboard;
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        final JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(panel, BorderLayout.NORTH);
        final GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);
        
        final JLabel lblBoard = new JLabel("Board :");
        final GridBagConstraints gbc_lblBoard = new GridBagConstraints();
        gbc_lblBoard.insets = new Insets(0, 0, 0, 5);
        gbc_lblBoard.anchor = GridBagConstraints.EAST;
        gbc_lblBoard.gridx = 0;
        gbc_lblBoard.gridy = 0;
        panel.add(lblBoard, gbc_lblBoard);
        
        final DefaultComboBoxModel<BoardFactory<? extends Board>> model = new DefaultComboBoxModel<BoardFactory<? extends Board>>();
        
        for (final BoardFactory<? extends Board> boardFactory : dashboard
                .getWindow().getMain().getCore().getBoardFactories().values())
        {
            model.addElement(boardFactory);
        }
        
        boards = new JComboBox<BoardFactory<? extends Board>>(model);
        boards.setRenderer(new BoardFactoryRenderer());
        final GridBagConstraints gbc_boards = new GridBagConstraints();
        gbc_boards.fill = GridBagConstraints.HORIZONTAL;
        gbc_boards.gridx = 1;
        gbc_boards.gridy = 0;
        panel.add(boards, gbc_boards);
        
        final JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        final JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.SOUTH);
        
        final JButton button = new JButton("<--");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                final ProgramsListMessage.Program program = selectedPrograms
                        .getSelectedValue();
                if (program != null)
                {
                    final int index = selectedPrograms.getSelectedIndex();
                    final DefaultComboBoxModel<ProgramsListMessage.Program> model = (DefaultComboBoxModel<Program>) selectedPrograms
                            .getModel();
                    model.removeElementAt(index);
                }
            }
        });
        panel_2.add(button);
        
        final JButton btnLaunch = new JButton("Lancer");
        btnLaunch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                launch();
            }
        });
        panel_2.add(btnLaunch);
        
        final JButton button_1 = new JButton("-->");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                addToRight();
            }
        });
        panel_2.add(button_1);
        
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setContinuousLayout(true);
        panel_1.add(splitPane, BorderLayout.CENTER);
        
        programs = new JList<ProgramsListMessage.Program>(dashboard.getWindow()
                .getMain().getCore().getOtherPrograms()
                .toArray(new ProgramsListMessage.Program[0]));
        programs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        programs.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 2)
                {
                    addToRight();
                }
            }
        });
        final JScrollPane scrollPane = new JScrollPane(programs);
        scrollPane.setMinimumSize(new Dimension(150, 100));
        splitPane.setLeftComponent(scrollPane);
        
        selectedPrograms = new JList<ProgramsListMessage.Program>(
                new DefaultComboBoxModel<ProgramsListMessage.Program>());
        selectedPrograms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane2 = new JScrollPane(selectedPrograms);
        splitPane.setRightComponent(scrollPane2);
        
        final Dimension size = new Dimension(320, 240);
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setResizable(false);
        
        setLocationRelativeTo(dashboard);
    }
    
    public void addToRight()
    {
        final ProgramsListMessage.Program program = programs.getSelectedValue();
        if (program != null)
        {
            final DefaultComboBoxModel<ProgramsListMessage.Program> model = (DefaultComboBoxModel<Program>) selectedPrograms
                    .getModel();
            model.addElement(program);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void launch()
    {
        final BoardFactory<? extends Board> boardFactory = (BoardFactory<? extends Board>) boards
                .getSelectedItem();
        
        final List<ProgramsListMessage.Program> selectedPrograms = new LinkedList<ProgramsListMessage.Program>();
        
        final int size = this.selectedPrograms.getModel().getSize();
        for (int i = 0; i < size; i++)
        {
            selectedPrograms.add(this.selectedPrograms.getModel().getElementAt(
                    i));
        }
        
        final LaunchMessage msg = new LaunchMessage();
        msg.setBoard(boardFactory.getId());
        msg.setPrograms(selectedPrograms);
        dashboard.getWindow().getMain().getClient().getChannel().write(msg);
        
        setVisible(false);
    }
}
