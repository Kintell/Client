package com.kokakiwi.kintell.client.ui.dashboard;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import com.kokakiwi.kintell.client.ui.editor.EditorPane;

import java.awt.*;
import java.awt.event.*;

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
        this.pane = dashboard.getTabs();
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
            int size = 17;
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
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1)
            {
                Component selected = pane.getComponentAt(i);
                
                if (selected instanceof EditorPane)
                {
                    EditorPane editor = (EditorPane) selected;
                    dashboard.getOpened()
                            .put(editor.getProgram().getId(), null);
                }
                
                pane.remove(i);
            }
        }
        
        // we don't want to update UI for this button
        public void updateUI()
        {
        }
        
        // paint the cross
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Stroke stroke = g2.getStroke();
            // shift the image for pressed buttons
            if (!getModel().isPressed())
            {
                g2.translate(-1, -1);
            }
            g2.setStroke(new BasicStroke(2));
            g.setColor(Color.BLACK);
            int delta = 6;
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
                                                               public void mouseEntered(
                                                                       MouseEvent e)
                                                               {
                                                                   Component component = e
                                                                           .getComponent();
                                                                   if (component instanceof AbstractButton)
                                                                   {
                                                                       AbstractButton button = (AbstractButton) component;
                                                                       button.setBorderPainted(true);
                                                                   }
                                                               }
                                                               
                                                               public void mouseExited(
                                                                       MouseEvent e)
                                                               {
                                                                   Component component = e
                                                                           .getComponent();
                                                                   if (component instanceof AbstractButton)
                                                                   {
                                                                       AbstractButton button = (AbstractButton) component;
                                                                       button.setBorderPainted(false);
                                                                   }
                                                               }
                                                           };
}
