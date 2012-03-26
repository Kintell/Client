package com.kokakiwi.kintell.client.ui.board;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;
import com.kokakiwi.kintell.spec.net.msg.RankEntriesMessage;

public class RankEntriesDialog extends JDialog
{
    private static final long                              serialVersionUID = -1008556738964128566L;
    private final JComboBox<BoardFactory<? extends Board>> boards;
    private final JTable                                   table;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RankEntriesDialog(GuiDashboard dashboard,
            final RankEntriesMessage msg)
    {
        super(dashboard.getWindow(), "Classement");
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        final JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        
        final JLabel lblTableau = new JLabel("Tableau :");
        panel.add(lblTableau);
        
        final DefaultComboBoxModel<BoardFactory<? extends Board>> model = new DefaultComboBoxModel<BoardFactory<? extends Board>>();
        
        for (final BoardFactory<? extends Board> boardFactory : dashboard
                .getWindow().getMain().getCore().getBoardFactories().values())
        {
            model.addElement(boardFactory);
        }
        
        boards = new JComboBox<BoardFactory<? extends Board>>(model);
        boards.setRenderer(new BoardFactoryRenderer());
        
        panel.add(boards);
        
        table = new JTable(new RanksTableModel(this, msg));
        
        getContentPane().add(table, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public BoardFactory<? extends Board> getSelected()
    {
        return boards.getItemAt(boards.getSelectedIndex());
    }
    
    public static class RanksTableModel extends AbstractTableModel
    {
        private static final long        serialVersionUID = -1880201667154762347L;
        private final RankEntriesMessage msg;
        private final RankEntriesDialog  dialog;
        
        public RanksTableModel(RankEntriesDialog dialog, RankEntriesMessage msg)
        {
            this.msg = msg;
            this.dialog = dialog;
        }
        
        public int getRowCount()
        {
            return msg.getRanks().get(dialog.getSelected().getId()).size();
        }
        
        public int getColumnCount()
        {
            return 2;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            final RankEntriesMessage.Rank rank = msg.getRanks()
                    .get(dialog.getSelected().getId()).get(rowIndex);
            
            Object o = null;
            
            switch (columnIndex)
            {
                case 0:
                    o = rank.getProgram();
                    break;
                
                case 1:
                    o = rank.getPoints();
                    break;
            }
            
            return o;
        }
        
    }
}
