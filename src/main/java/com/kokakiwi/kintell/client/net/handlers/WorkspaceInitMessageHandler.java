package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.core.Machine;
import com.kokakiwi.kintell.client.core.Program;
import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.GuiMainMenu;
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
        for (final WorkspaceInitMessage.ContentType contentType : msg
                .getContentTypes())
        {
            client.getMain().getCore().getContentTypes()
                    .put(contentType.getId(), contentType);
        }
        
        for (final WorkspaceInitMessage.Machine machine : msg.getMachines())
        {
            final Machine m = client.getMain().getCore().getMachines()
                    .createMachine(machine.getId());
            for (final WorkspaceInitMessage.Program program : machine
                    .getPrograms())
            {
                for (final WorkspaceInitMessage.ContentType contentType : client
                        .getMain().getCore().getContentTypes().values())
                {
                    if (contentType.getContentType().equals(
                            program.getContentType()))
                    {
                        final Program p = m.createProgram(program.getId(),
                                program.getName(), contentType);
                        p.setContent(program.getSource());
                    }
                }
            }
            client.getMain().getCore().getMachines().addMachine(m);
        }
        client.getMain().getCore().setConnectionResult(true);
        
        final Gui gui = client.getMain().getWindow().getCurrentGui();
        if (gui instanceof GuiMainMenu)
        {
            ((GuiMainMenu) gui).next();
        }
        
        return true;
    }
    
}
