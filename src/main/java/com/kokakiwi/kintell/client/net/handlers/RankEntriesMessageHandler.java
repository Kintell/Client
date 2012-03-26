package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.dashboard.GuiDashboard;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.RankEntriesMessage;

public class RankEntriesMessageHandler extends
        MessageHandler<RankEntriesMessage>
{
    private final Client client;
    
    public RankEntriesMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            RankEntriesMessage msg)
    {
        final Gui gui = client.getMain().getWindow().getCurrentGui();
        if (gui instanceof GuiDashboard)
        {
            ((GuiDashboard) gui).openRankEntriesWindow(msg);
        }
        
        return true;
    }
    
}
