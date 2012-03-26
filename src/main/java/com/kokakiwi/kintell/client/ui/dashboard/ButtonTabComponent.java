package com.kokakiwi.kintell.client.ui.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import com.kokakiwi.kintell.client.ui.editor.EditorPane;

/**
 * Component to be used as as tabComponent; Contains a JLabel to show the text
 * and a JButton to close the tab it belongs to
 */
public class ButtonTabComponent extends JPanel
{
    private static final long  serialVersionUID = 2278503824587713416L;
    private final GuiDashboard dashboard;
    private final JTabbedPane  pane;
    private final JLabel       label;
    private final JButton      button           = new TabButton();
    
    public ButtonTabComponent(String title, GuiDashboard dashboard,
            boolean selected)
    {
        // unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.dashboard = dashboard;
        pane = dashboard.getTabs();
        setOpaque(false);
        label = new JLabel(title);
        
        add(label);
        if (selected)
        {
            // add more space between the label and the button
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            add(button);
        }
        // add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
    
    private class TabButton extends JButton implements ActionListener
    {
        private static final long serialVersionUID = -7227192187160405839L;
        
        public TabButton()
        {
            final int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            // Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            // Make it transparent
            setContentAreaFilled(false);
            // No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            // Making nice rollover effect
            // we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            // Close the proper tab by clicking the button
            addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            final int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1)
            {
                final Component selected = pane.getComponentAt(i);
                
                if (selected instanceof EditorPane)
                {
                    final EditorPane editor = (EditorPane) selected;
                    dashboard.getOpened()
                            .put(editor.getProgram().getId(), null);
                }
                
                pane.remove(i);
            }
        }
        
        // we don't want to update UI for this button
        @Override
        public void updateUI()
        {
        }
        
        // paint the cross
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D) g;
            final Stroke stroke = g2.getStroke();
            // shift the image for pressed buttons
            if (!getModel().isPressed())
            {
                g2.translate(-1, -1);
            }
            g2.setStroke(new BasicStroke(2));
            g.setColor(Color.BLACK);
            final int delta = 6;
            g.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
                    - delta - 1);
            g.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
                    - delta - 1);
            // leave the graphics unchanged
            if (!getModel().isPressed())
            {
                g.translate(1, 1);
            }
            g2.setStroke(stroke);
        }
    }
    
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
                                                               @Override
                                                               public void mouseEntered(
                                                                       MouseEvent e)
                                                               {
                                                                   final Component component = e
                                                                           .getComponent();
                                                                   if (component instanceof AbstractButton)
                                                                   {
                                                                       final AbstractButton button = (AbstractButton) component;
                                                                       button.setBorderPainted(true);
                                                                   }
                                                               }
                                                               
                                                               @Override
                                                               public void mouseExited(
                                                                       MouseEvent e)
                                                               {
                                                                   final Component component = e
                                                                           .getComponent();
                                                                   if (component instanceof AbstractButton)
                                                                   {
                                                                       final AbstractButton button = (AbstractButton) component;
                                                                       button.setBorderPainted(false);
                                                                   }
                                                               }
                                                           };
}
