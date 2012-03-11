package com.kokakiwi.kintell.client.core.board;

import java.util.List;

import com.kokakiwi.kintell.client.core.KintellClientCore;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;

public interface BoardFactory<T extends Board>
{
    public String getId();
    
    public String getName();
    
    public T createBoard(KintellClientCore core,
            List<ProgramsListMessage.Program> programs, int id);
}
