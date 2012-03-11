package com.kokakiwi.kintell.client.ui;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

public abstract class Gui extends JPanel
{
    private static final long serialVersionUID = 5600481075150526655L;
    
    public abstract void fillMenuBar(JMenuBar bar);
}
