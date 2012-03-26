package com.kokakiwi.kintell.client;

import java.io.File;

import javax.swing.UIManager;

import com.kokakiwi.kintell.client.core.KintellClientCore;
import com.kokakiwi.kintell.client.net.Client;
import com.kokakiwi.kintell.client.plugins.ClientPluginsManager;
import com.kokakiwi.kintell.client.ui.MainWindow;
import com.kokakiwi.kintell.spec.console.ConsoleOutputManager;
import com.kokakiwi.kintell.spec.utils.Configuration;

public class KintellClient
{
    private final Configuration        configuration = new Configuration();
    
    private final KintellClientCore    core;
    private final MainWindow           window;
    private final ClientPluginsManager pluginsManager;
    private final Client               client;
    
    public KintellClient()
    {
        ConsoleOutputManager.register("client");
        
        // Loading configuration
        System.out.println("Loading configuration...");
        try
        {
            configuration.load(
                    KintellClient.class.getResourceAsStream("/config.yml"),
                    "yaml");
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        // Initialize components
        System.out.println("Initializing components...");
        
        core = new KintellClientCore(this);
        
        window = new MainWindow(this);
        
        pluginsManager = new ClientPluginsManager(this);
        pluginsManager.setPluginsDir(new File(configuration
                .getString("plugins.path")));
        
        client = new Client(this);
    }
    
    public void start()
    {
        System.out.println("Starting...");
        
        // Load plugins
        System.out.println("Loading plugins...");
        try
        {
            pluginsManager.loadPlugins();
            pluginsManager.enablePlugins();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        // Show window
        window.setVisible(true);
    }
    
    public void stop()
    {
        System.out.println("Stopping...");
        window.setVisible(false);
        client.stop();
        pluginsManager.disablePlugins();
        
        System.exit(0);
    }
    
    public Configuration getConfiguration()
    {
        return configuration;
    }
    
    public KintellClientCore getCore()
    {
        return core;
    }
    
    public MainWindow getWindow()
    {
        return window;
    }
    
    public ClientPluginsManager getPluginsManager()
    {
        return pluginsManager;
    }
    
    public Client getClient()
    {
        return client;
    }
    
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        final KintellClient main = new KintellClient();
        main.start();
    }
}
