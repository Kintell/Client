package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.core.Machine;
import com.kokakiwi.kintell.client.core.Program;
import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;

public class WorkspaceInitMessageHandler extends
        MessageHandler<WorkspaceInitMessage>
{
    private final Client client;
    
    public WorkspaceInitMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            WorkspaceInitMessage msg)
    {
        for (WorkspaceInitMessage.ContentType contentType : msg
                .getContentTypes())
        {
            client.getMain().getCore().getContentTypes()
                    .put(contentType.getId(), contentType);
        }
        
        for (WorkspaceInitMessage.Machine machine : msg.getMachines())
        {
            Machine m = client.getMain().getCore().getMachines()
                    .createMachine(machine.getId());
            for (WorkspaceInitMessage.Program program : machine.getPrograms())
            {
                for (WorkspaceInitMessage.ContentType contentType : client
                        .getMain().getCore().getContentTypes().values())
                {
                    if (contentType.getContentType().equals(
                            program.getContentType()))
                    {
                        Program p = m.createProgram(program.getId(),
                                program.getName(), contentType);
                        p.setContent(program.getSource());
                    }
                }
            }
            client.getMain().getCore().getMachines().addMachine(m);
        }
        
        client.getMain().getCore().setWaiting(false);
        
        return true;
    }
    
}
