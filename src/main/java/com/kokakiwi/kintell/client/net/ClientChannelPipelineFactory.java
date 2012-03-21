package com.kokakiwi.kintell.client.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.kokakiwi.kintell.spec.net.CodecFrameDecoder;

public class ClientChannelPipelineFactory implements ChannelPipelineFactory
{
    private final Client client;
    
    public ClientChannelPipelineFactory(Client client)
    {
        this.client = client;
    }
    
    public ChannelPipeline getPipeline() throws Exception
    {
        ChannelPipeline pipeline = Channels.pipeline();
        
        pipeline.addLast("framer", new CodecFrameDecoder());
        
        pipeline.addLast("decoder", new ClientDecoder(client));
        pipeline.addLast("encoder", new ClientEncoder(client));
        
        pipeline.addLast("handler", new ClientHandler(client));
        
        return pipeline;
    }
    
}
