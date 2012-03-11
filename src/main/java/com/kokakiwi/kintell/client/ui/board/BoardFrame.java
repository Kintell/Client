package com.kokakiwi.kintell.client.ui.board;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JDialog;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;

public class BoardFrame extends JDialog
{
    private static final long                       serialVersionUID = 1635497237817889156L;
    
    private final KintellClient                     main;
    private final BoardFactory<? extends Board>     boardFactory;
    private final Board                             board;
    private final List<ProgramsListMessage.Program> programs;
    
    public BoardFrame(KintellClient main,
            BoardFactory<? extends Board> boardFactory,
            List<ProgramsListMessage.Program> programs, int id)
            throws HeadlessException
    {
        super(main.getWindow(), boardFactory.getName());
        this.main = main;
        this.boardFactory = boardFactory;
        board = boardFactory.createBoard(main.getCore(), programs, id);
        this.programs = programs;
        
        setLayout(new BorderLayout());
        
        Component component = board.getComponent();
        add(component, BorderLayout.CENTER);
        
        setSize(component.getSize());
        setMinimumSize(component.getSize());
        setMaximumSize(component.getSize());
        setResizable(false);
        
        setModalityType(ModalityType.DOCUMENT_MODAL);
        
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
