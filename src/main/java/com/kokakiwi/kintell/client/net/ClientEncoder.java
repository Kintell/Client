package com.kokakiwi.kintell.client.net;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.kokakiwi.kintell.spec.net.codec.MessageCodec;
import com.kokakiwi.kintell.spec.net.msg.Message;
import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.DynamicDataBuffer;

public class ClientEncoder extends OneToOneEncoder
{
    private final Client client;
    
    public ClientEncoder(Client client)
    {
        super();
        this.client = client;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel,
            Object msg) throws Exception
    {
        if (!(msg instanceof Message))
        {
            return msg;
        }
        
        final Message message = (Message) msg;
        final MessageCodec<Message> codec = (MessageCodec<Message>) client
                .getCodec().getCodec(message.getClass());
        
        if (codec == null)
        {
            throw new NullPointerException("codec");
        }
        
        final DataBuffer buf = new DynamicDataBuffer();
        buf.writeByte(codec.getOpcode());
        codec.encode(buf, message);
        buf.copyWritedBytesToReadableBytes();
        
        return ChannelBuffers.copiedBuffer(buf.getReadableBytes());
    }
    
}
