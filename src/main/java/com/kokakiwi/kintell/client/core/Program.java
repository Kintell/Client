package com.kokakiwi.kintell.client.core;

import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;

public class Program
{
    private final Machine                    owner;
    private final String                     id;
    private String                           name;
    private String                           content = "";
    
    private WorkspaceInitMessage.ContentType contentType;
    
    public Program(Machine owner, String id)
    {
        this(owner, id, id);
    }
    
    public Program(Machine owner, String id, String name)
    {
        this.owner = owner;
        this.id = id;
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Machine getOwner()
    {
        return owner;
    }
    
    public String getId()
    {
        return id;
    }
    
    public WorkspaceInitMessage.ContentType getContentType()
    {
        return contentType;
    }
    
    public void setContentType(WorkspaceInitMessage.ContentType contentType)
    {
        this.contentType = contentType;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String loadContent()
    {
        return content;
    }
    
    @Override
    public String toString()
    {
        return new StringBuilder().append(name).append(" [").append(id)
                .append(']').toString();
    }
}
