package com.kokakiwi.kintell.client.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.client.core.board.Listener;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;
import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;

public class KintellClientCore
{
    private final KintellClient                                 main;
    
    private final Machines                                      machines;
    private final Map<String, WorkspaceInitMessage.ContentType> contentTypes     = Maps.newLinkedHashMap();
    private final List<ProgramsListMessage.Program>             otherPrograms    = new LinkedList<ProgramsListMessage.Program>();
    private final Map<String, BoardFactory<? extends Board>>    boardFactories   = Maps.newLinkedHashMap();
    private final Map<Integer, Listener>                        listeners        = Maps.newLinkedHashMap();
    
    private boolean                                             connectionResult = false;
    
    public KintellClientCore(KintellClient main)
    {
        this.main = main;
        machines = new Machines(this);
    }
    
    public KintellClient getMain()
    {
        return main;
    }
    
    public Map<String, WorkspaceInitMessage.ContentType> getContentTypes()
    {
        return contentTypes;
    }
    
    public Machines getMachines()
    {
        return machines;
    }
    
    public List<ProgramsListMessage.Program> getOtherPrograms()
    {
        return otherPrograms;
    }
    
    public Map<String, BoardFactory<? extends Board>> getBoardFactories()
    {
        return boardFactories;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Board, V extends BoardFactory<T>> V getBoardFactory(
            String id)
    {
        return (V) boardFactories.get(id);
    }
    
    public <T extends Board, V extends BoardFactory<T>> void registerBoardFactory(
            V boardFactory)
    {
        boardFactories.put(boardFactory.getId(), boardFactory);
    }
    
    public Map<Integer, Listener> getListeners()
    {
        return listeners;
    }
    
    public Listener getListener(int id)
    {
        return listeners.get(id);
    }
    
    public void registerListener(int id, Listener listener)
    {
        listeners.put(id, listener);
    }
    
    public boolean isConnectionResult()
    {
        return connectionResult;
    }
    
    public void setConnectionResult(boolean connectionResult)
    {
        this.connectionResult = connectionResult;
    }
}
