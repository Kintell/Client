package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.StopMessage;

public class StopMessageHandler extends MessageHandler<StopMessage>
{
    private final Client client;
    
    public StopMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            StopMessage msg)
    {
        if (client.getMain().getWindow().getFrame() != null)
        {
            client.getMain().getWindow().getFrame().getBoard().stop();
            client.getMain().getWindow().getFrame().setVisible(false);
        }
        
        return true;
    }
    
}
