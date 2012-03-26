package com.kokakiwi.kintell.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatusBar extends JPanel
{
    private static final long serialVersionUID = 405774672160602943L;
    
    private final JLabel      label;
    
    public StatusBar()
    {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(10, 23));
        
        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()),
                BorderLayout.SOUTH);
        rightPanel.setOpaque(false);
        
        add(rightPanel, BorderLayout.EAST);
        
        label = new JLabel();
        label.setBorder(new EmptyBorder(0, 5, 0, 0));
        
        add(label, BorderLayout.WEST);
        
        setBackground(SystemColor.control);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);
        
        y = getHeight() - 3;
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y = getHeight() - 1;
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
        
    }
    
    public void setText(String text)
    {
        label.setText(text);
    }
    
}