package com.kokakiwi.kintell.client.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.net.handlers.*;
import com.kokakiwi.kintell.spec.net.msg.*;
import com.kokakiwi.kintell.spec.net.CodecResolver;

public class Client
{
    private final KintellClient   main;
    
    private final ClientBootstrap bootstrap;
    
    private Channel               channel  = null;
    
    private final CodecResolver   codec    = new CodecResolver();
    
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    public Client(KintellClient main)
    {
        this.main = main;
        
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ClientChannelPipelineFactory(this));
        
        // Register basic handlers
        codec.registerHandler(WorkspaceInitMessage.class,
                new WorkspaceInitMessageHandler(this));
        codec.registerHandler(BoardMessage.class, new BoardMessageHandler(this));
        codec.registerHandler(ProgramsListMessage.class,
                new ProgramsListMessageHandler(this));
        codec.registerHandler(LaunchMessage.class, new LaunchMessageHandler(
                this));
        codec.registerHandler(DebugMessage.class, new DebugMessageHandler(this));
        codec.registerHandler(WrongPasswordMessage.class,
                new WrongPasswordMessageHandler(this));
    }
    
    public boolean connect(String pseudo, String password)
    {
        boolean result = false;
        
        System.out.println("Trying to connect to "
                + main.getConfiguration().getString("server.host") + ":"
                + main.getConfiguration().getInteger("server.port"));
        
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(main
                .getConfiguration().getString("server.host"), main
                .getConfiguration().getInteger("server.port")));
        try
        {
            future.await(30000L);
            if (future.isSuccess())
            {
                channel = future.getChannel();
                result = true;
                
                ConnectMessage msg = new ConnectMessage();
                msg.setPseudo(pseudo);
                msg.setPassword(password);
                channel.write(msg);
                
                main.getCore().setWaiting(true);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public boolean disconnect()
    {
        boolean result = false;
        
        if (channel != null)
        {
            ChannelFuture future = channel.close();
            future.awaitUninterruptibly();
            
            if (future.isSuccess())
            {
                result = true;
            }
        }
        
        return result;
    }
    
    public void stop()
    {
        disconnect();
        
        bootstrap.releaseExternalResources();
    }
    
    public boolean sendMessage(Message msg)
    {
        boolean result = false;
        if (channel != null && channel.isWritable())
        {
            ChannelFuture future = channel.write(msg);
            try
            {
                future.await(30000L);
                if (future.isSuccess())
                {
                    result = true;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public KintellClient getMain()
    {
        return main;
    }
    
    public ClientBootstrap getBootstrap()
    {
        return bootstrap;
    }
    
    public Channel getChannel()
    {
        return channel;
    }
    
    public CodecResolver getCodec()
    {
        return codec;
    }
    
    public ExecutorService getExecutor()
    {
        return executor;
    }
}
