package com.kokakiwi.kintell.client.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.kokakiwi.kintell.spec.net.codec.MessageCodec;
import com.kokakiwi.kintell.spec.net.msg.Message;
import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.DynamicDataBuffer;

public class ClientDecoder extends OneToOneDecoder
{
    private final Client client;
    
    public ClientDecoder(Client client)
    {
        super();
        this.client = client;
    }
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel,
            Object msg) throws Exception
    {
        if (!(msg instanceof ChannelBuffer))
        {
            return msg;
        }
        
        DataBuffer buf = new DynamicDataBuffer();
        buf.setReadableBytes(((ChannelBuffer) msg).array());
        
        Object decoded = msg;
        
        byte opcode = buf.readByte();
        MessageCodec<Message> codec = client.getCodec().getCodec(opcode);
        
        if (codec != null)
        {
            decoded = codec.decode(buf);
        }
        
        return decoded;
    }
    
}
