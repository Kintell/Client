package com.kokakiwi.kintell.client.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.Message;

public class ClientHandler extends SimpleChannelUpstreamHandler
{
    private final Client client;
    
    public ClientHandler(Client client)
    {
        super();
        this.client = client;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception
    {
        final Object message = e.getMessage();
        if (message instanceof Message)
        {
            final Message msg = (Message) message;
            final Class<Message> clazz = (Class<Message>) msg.getClass();
            final MessageHandler<Message> handler = client.getCodec()
                    .getHandler(clazz);
            if (handler != null)
            {
                final PacketExecutor executor = new PacketExecutor(handler,
                        ctx, e);
                client.getExecutor().execute(executor);
                return;
            }
        }
        
        super.messageReceived(ctx, e);
    }
    
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
            ChannelStateEvent e) throws Exception
    {
        System.out.println("Connection is off!");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception
    {
        e.getCause().printStackTrace();
    }
}
