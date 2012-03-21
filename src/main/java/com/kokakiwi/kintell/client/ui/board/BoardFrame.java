package com.kokakiwi.kintell.client.ui.board;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;
import com.kokakiwi.kintell.spec.net.msg.StopMessage;

public class BoardFrame extends JFrame
{
    private static final long                       serialVersionUID = 1635497237817889156L;
    
    private final KintellClient                     main;
    private final BoardFactory<? extends Board>     boardFactory;
    private final Board                             board;
    private final List<ProgramsListMessage.Program> programs;
    
    public BoardFrame(final KintellClient main,
            final BoardFactory<? extends Board> boardFactory,
            final List<ProgramsListMessage.Program> programs, final int id)
            throws HeadlessException
    {
        super(boardFactory.getName());
        this.main = main;
        this.boardFactory = boardFactory;
        board = boardFactory.createBoard(main.getCore(), programs, id);
        this.programs = programs;
        
        getContentPane().setLayout(new BorderLayout());
        
        Component component = board.getComponent();
        getContentPane().add(component, BorderLayout.CENTER);
        
        getContentPane().setSize(component.getSize());
        getContentPane().setMinimumSize(component.getSize());
        getContentPane().setMaximumSize(component.getSize());
        getContentPane().setPreferredSize(component.getSize());
        
        setResizable(false);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                StopMessage msg = new StopMessage();
                msg.setBoard(boardFactory.getId());
                msg.setId(id);
                main.getClient().sendMessage(msg);
                
                board.stop();
            }
        });
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public KintellClient getMain()
    {
        return main;
    }
    
    public BoardFactory<? extends Board> getBoardFactory()
    {
        return boardFactory;
    }
    
    public Board getBoard()
    {
        return board;
    }
    
    public List<ProgramsListMessage.Program> getPrograms()
    {
        return programs;
    }
}
