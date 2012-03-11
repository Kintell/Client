package com.kokakiwi.kintell.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.kokakiwi.kintell.client.KintellClient;
import com.kokakiwi.kintell.client.ui.board.BoardFrame;

public class MainWindow extends JFrame
{
    private static final long   serialVersionUID = 3221402690977450756L;
    
    private final KintellClient main;
    private Gui                 currentGui       = null;
    private BoardFrame          frame            = null;
    
    public MainWindow(final KintellClient main) throws HeadlessException
    {
        super(main.getConfiguration().getString("window.title"));
        this.main = main;
        
        getContentPane().setLayout(new BorderLayout());
        
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e)
            {
                main.stop();
            }
        });
        
        displayGui(new GuiMainMenu(this));
        
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }
    
    public void displayGui(Gui gui)
    {
        currentGui = gui;
        
        getContentPane().removeAll();
        
        getContentPane().add(gui, BorderLayout.CENTER);
        
        JMenuBar bar = new JMenuBar();
        gui.fillMenuBar(bar);
        fillMenuBar(bar);
        setJMenuBar(bar);
        
        validate();
    }
    
    private void fillMenuBar(JMenuBar bar)
    {
        JMenu help = new JMenu("Help");
        
        Action about = new AbstractAction("About") {
            private static final long serialVersionUID = 8601855836914622132L;
            
            public void actionPerformed(ActionEvent e)
            {
                AboutDialog dialog = new AboutDialog();
                dialog.setLocationRelativeTo(MainWindow.this);
                dialog.setVisible(true);
            }
        };
        help.add(about);
        
        bar.add(help);
    }
    
    public KintellClient getMain()
    {
        return main;
    }
    
    public Gui getCurrentGui()
    {
        return currentGui;
    }
    
    public BoardFrame getFrame()
    {
        return frame;
    }
    
    public void setFrame(BoardFrame frame)
    {
        this.frame = frame;
    }
}
