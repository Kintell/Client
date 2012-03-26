package com.kokakiwi.kintell.client.net.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;
import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.ui.board.BoardFrame;
import com.kokakiwi.kintell.spec.net.MessageHandler;
import com.kokakiwi.kintell.spec.net.msg.LaunchMessage;

public class LaunchMessageHandler extends MessageHandler<LaunchMessage>
{
    private final Client client;
    
    public LaunchMessageHandler(Client client)
    {
        this.client = client;
    }
    
    @Override
    public boolean handle(ChannelHandlerContext ctx, MessageEvent e,
            LaunchMessage msg)
    {
        final BoardFactory<? extends Board> boardFactory = client.getMain()
                .getCore().getBoardFactory(msg.getBoard());
        
        final BoardFrame frame = new BoardFrame(client.getMain(), boardFactory,
                msg.getPrograms(), msg.getId());
        client.getMain().getWindow().setFrame(frame);
        frame.setVisible(true);
        
        return true;
    }
    
}
