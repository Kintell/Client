package com.kokakiwi.kintell.client.ui;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class AboutDialog extends JDialog
{
    public AboutDialog()
    {
        setTitle("A propos de Kintell");
        
        JTextPane txtpnKintellVBy = new JTextPane();
        txtpnKintellVBy.setEditable(false);
        txtpnKintellVBy.setContentType("text/html");
        txtpnKintellVBy
                .setText("<div style=\"font-family: 'Helvetica', Arial, sans-serif;\">\r\n\tKintell v0.1.0<br />\r\n\tBy <a href=\"http://kokaelkiwi.tk\">Koka El Kiwi</a>\r\n</div>");
        getContentPane().add(txtpnKintellVBy, BorderLayout.CENTER);
        
        Dimension size = new Dimension(450, 300);
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }
    
    private static final long serialVersionUID = 8073861866286176947L;
    
}
