package com.kokakiwi.kintell.client.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.net.handlers.BoardMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.DebugMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.LaunchMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.ProgramsListMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.RankEntriesMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.WorkspaceInitMessageHandler;
import com.kokakiwi.kintell.client.net.handlers.WrongPasswordMessageHandler;
import com.kokakiwi.kintell.spec.net.CodecResolver;
import com.kokakiwi.kintell.spec.net.msg.BoardMessage;
import com.kokakiwi.kintell.spec.net.msg.ConnectMessage;
import com.kokakiwi.kintell.spec.net.msg.DebugMessage;
import com.kokakiwi.kintell.spec.net.msg.LaunchMessage;
import com.kokakiwi.kintell.spec.net.msg.Message;
import com.kokakiwi.kintell.spec.net.msg.ProgramsListMessage;
import com.kokakiwi.kintell.spec.net.msg.RankEntriesMessage;
import com.kokakiwi.kintell.spec.net.msg.WorkspaceInitMessage;
import com.kokakiwi.kintell.spec.net.msg.WrongPasswordMessage;

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
        codec.registerHandler(RankEntriesMessage.class,
                new RankEntriesMessageHandler(this));
    }
    
    public boolean connect(String pseudo, String password)
    {
        boolean result = false;
        
        System.out.println("Trying to connect to "
                + main.getConfiguration().getString("server.host") + ":"
                + main.getConfiguration().getInteger("server.port"));
        
        final ChannelFuture future = bootstrap.connect(new InetSocketAddress(
                main.getConfiguration().getString("server.host"), main
                        .getConfiguration().getInteger("server.port")));
        try
        {
            future.await(30000L);
            if (future.isSuccess())
            {
                channel = future.getChannel();
                result = true;
                
                final ConnectMessage msg = new ConnectMessage();
                msg.setPseudo(pseudo);
                msg.setPassword(password);
                sendMessage(msg);
            }
            else
            {
                System.err.println("Can't connect to server.");
                future.getCause().printStackTrace();
            }
        }
        catch (final InterruptedException e)
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
            final ChannelFuture future = channel.close();
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
            final ChannelFuture future = channel.write(msg);
            try
            {
                future.await(30000L);
                if (future.isSuccess())
                {
                    result = true;
                }
                else
                {
                    System.err.println("Error during sending packet.");
                    future.getCause().printStackTrace();
                }
            }
            catch (final InterruptedException e)
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
