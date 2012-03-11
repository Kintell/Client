package com.kokakiwi.kintell.client.core.board;

import com.kokakiwi.kintell.spec.net.msg.BoardMessage;

public interface Listener
{
    public void messageReceived(BoardMessage msg);
}
