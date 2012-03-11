package com.kokakiwi.kintell.client.core;

import java.util.Map;

import com.google.common.collect.Maps;
import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;

public class Machine
{
    private final Machines             parent;
    private final String               id;
    
    private final Map<String, Program> programs = Maps.newLinkedHashMap();
    
    public Machine(Machines parent, String id)
    {
        this.parent = parent;
        this.id = id;
    }
    
    public Machines getParent()
    {
        return parent;
    }
    
    public String getId()
    {
        return id;
    }
    
    public Map<String, Program> getPrograms()
    {
        return programs;
    }
    
    public Program getProgram(String id)
    {
        return programs.get(id);
    }
    
    public Program createProgram(String id,
            WorkspaceInitMessage.ContentType contentType)
    {
        return createProgram(id, id, contentType);
    }
    
    public Program createProgram(String id, String name,
            WorkspaceInitMessage.ContentType contentType)
    {
        Program program = null;
        
        if (!programs.containsKey(id))
        {
            program = new Program(this, id, name);
            program.setContentType(contentType);
            
            programs.put(id, program);
        }
        
        return program;
    }
    
    @Override
    public String toString()
    {
        return id;
    }
}
