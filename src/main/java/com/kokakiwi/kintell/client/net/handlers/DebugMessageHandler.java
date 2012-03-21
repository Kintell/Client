package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.DebugMessage;

public class DebugMessageHandler extends MessageHandler<DebugMessage>
{
    private final Client client;
    
    public DebugMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            DebugMessage msg)
    {
        Gui gui = client.getMain().getWindow().getCurrentGui();
        if (gui instanceof GuiDashboard)
        {
            GuiDashboard dashboard = (GuiDashboard) gui;
            
            dashboard.getDebugArea()
                    .append(new StringBuilder(msg.getMessage()).append('\n')
                            .toString());
            
            return true;
        }
        
        return false;
    }
    
}
