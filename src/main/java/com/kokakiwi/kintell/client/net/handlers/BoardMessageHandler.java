package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.core.board.Listener;
import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.BoardMessage;

public class BoardMessageHandler extends MessageHandler<BoardMessage>
{
    private final Client client;
    
    public BoardMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            BoardMessage msg)
    {
        final int id = msg.getId();
        final Listener listener = client.getMain().getCore().getListener(id);
        if (listener != null)
        {
            listener.messageReceived(msg);
        }
        
        return true;
    }
    
}
