package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;

public class ProgramsListMessageHandler extends
        MessageHandler<ProgramsListMessage>
{
    private final Client client;
    
    public ProgramsListMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            ProgramsListMessage msg)
    {
        client.getMain().getCore().getOtherPrograms().clear();
        client.getMain().getCore().getOtherPrograms().addAll(msg.getPrograms());
        
        final Gui gui = client.getMain().getWindow().getCurrentGui();
        if (gui instanceof GuiDashboard)
        {
            ((GuiDashboard) gui).openLaunchWindow();
        }
        
        return true;
    }
    
}
