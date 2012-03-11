package com.kokakiwi.kintell.client.plugins;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.core.KintellClientCore;
import com.kokakiwi.kintell.spec.plugin.Plugin;

public abstract class ClientPlugin extends Plugin
{
    protected KintellClient client;
    
    public KintellClient getClient()
    {
        return client;
    }
    
    public void setMain(KintellClient client)
    {
        this.client = client;
    }
    
    public KintellClientCore getCore()
    {
        return client.getCore();
    }
    
}
