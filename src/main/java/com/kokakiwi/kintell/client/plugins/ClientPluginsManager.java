package com.kokakiwi.kintell.client.plugins;

import java.lang.reflect.Constructor;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.spec.plugin.PluginDescriptionFile;
import com.kokakiwi.kintell.spec.plugin.PluginLoader;
import com.kokakiwi.kintell.spec.plugin.PluginsManager;

public class ClientPluginsManager extends PluginsManager<ClientPlugin>
{
    private final KintellClient main;
    
    public ClientPluginsManager(KintellClient main)
    {
        this.main = main;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ClientPlugin createPlugin(PluginDescriptionFile pdf,
            PluginLoader loader)
    {
        ClientPlugin plugin = null;
        
        try
        {
            final String pluginMainClassName = pdf.getMain();
            final Class<? extends ClientPlugin> pluginClass = (Class<? extends ClientPlugin>) loader
                    .loadClass(pluginMainClassName);
            final Constructor<? extends ClientPlugin> constructor = pluginClass
                    .getConstructor();
            plugin = constructor.newInstance();
            plugin.setMain(main);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        return plugin;
    }
    
}
