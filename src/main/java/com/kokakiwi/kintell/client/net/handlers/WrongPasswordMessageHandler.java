package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.Gui;
import com.kokakiwi.kintell.client.ui.GuiMainMenu;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.WrongPasswordMessage;

public class WrongPasswordMessageHandler extends
        MessageHandler<WrongPasswordMessage>
{
    private final Client client;
    
    public WrongPasswordMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            WrongPasswordMessage msg)
    {
        client.getMain().getClient().disconnect();
        client.getMain().getCore().setConnectionResult(false);
        
        final Gui gui = client.getMain().getWindow().getCurrentGui();
        if (gui instanceof GuiMainMenu)
        {
            ((GuiMainMenu) gui).next();
        }
        
        return false;
    }
}
