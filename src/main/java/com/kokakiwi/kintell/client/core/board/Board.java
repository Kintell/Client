package com.kokakiwi.kintell.client.core.board;

import java.awt.Component;
import java.util.List;

import com.kokakiwi.kintell.client.core.KintellClientCore;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;

public abstract class Board
{
    protected final KintellClientCore                 core;
    protected final List<ProgramsListMessage.Program> programs;
    protected final int                               id;
    
    public Board(KintellClientCore core,
            List<ProgramsListMessage.Program> programs, int id)
    {
        this.core = core;
        this.programs = programs;
        this.id = id;
    }
    
    public KintellClientCore getCore()
    {
        return core;
    }
    
    public abstract Component getComponent();
    
    public abstract void start();
    
    public abstract void stop();
}
