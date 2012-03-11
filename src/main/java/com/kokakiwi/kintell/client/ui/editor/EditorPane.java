package com.kokakiwi.kintell.client.ui.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.kokakiwi.kintell.client.core.Program;
import com.kokakiwi.kintell.spec.net.msg.SendSourceMessage;

public class EditorPane extends JPanel
{
    private static final long     serialVersionUID = 5120374153159587746L;
    
    private final Program         program;
    
    private final RSyntaxTextArea textArea;
    private final RTextScrollPane textScrollPane;
    
    private final Action          saveAction;
    
    public EditorPane(final Program program)
    {
        super();
        this.program = program;
        
        saveAction = new AbstractAction("Save") {
            private static final long serialVersionUID = -6021941599632531142L;
            
            public void actionPerformed(ActionEvent e)
            {
                save();
            }
        };
        saveAction.setEnabled(false);
        
        setLayout(new BorderLayout());
        
        textArea = createTextArea();
        textArea.setText(program.loadContent());
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            
            public void removeUpdate(DocumentEvent e)
            {
                
            }
            
            public void insertUpdate(DocumentEvent e)
            {
                
            }
            
            public void changedUpdate(DocumentEvent e)
            {
                saveAction.setEnabled(true);
            }
        });
        textArea.getActionMap().put("save", new AbstractAction() {
            private static final long serialVersionUID = -235363459014234965L;
            
            public void actionPerformed(ActionEvent e)
            {
                save();
            }
        });
        textArea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
                "save");
        textScrollPane = new RTextScrollPane(textArea);
        add(textScrollPane, BorderLayout.CENTER);
        
        JToolBar toolBar = new JToolBar();
        fillToolBar(toolBar);
        add(toolBar, BorderLayout.NORTH);
    }
    
    private RSyntaxTextArea createTextArea()
    {
        RSyntaxTextArea area = new RSyntaxTextArea(25, 70);
        area.setSyntaxEditingStyle(program.getContentType().getContentType());
        area.setCaretPosition(0);
        area.requestFocusInWindow();
        area.setMarkOccurrences(true);
        area.setAntiAliasingEnabled(true);
        area.setCodeFoldingEnabled(true);
        area.setClearWhitespaceLinesEnabled(false);
        
        try
        {
            Theme theme = Theme.load(EditorPane.class
                    .getResourceAsStream("/eclipse.xml"));
            theme.apply(area);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return area;
    }
    
    public void save()
    {
        SendSourceMessage msg = new SendSourceMessage();
        msg.setMachine(program.getOwner().getId());
        msg.setProgram(program.getId());
        msg.setSource(textArea.getText());
        program.getOwner().getParent().getCore().getMain().getClient()
                .getChannel().write(msg);
        
        program.setContent(textArea.getText());
        
        saveAction.setEnabled(false);
    }
    
    public void fillToolBar(JToolBar bar)
    {
        bar.add(saveAction);
    }
    
    public RSyntaxTextArea getTextArea()
    {
        return textArea;
    }
    
    public RTextScrollPane getTextScrollPane()
    {
        return textScrollPane;
    }
    
    public Program getProgram()
    {
        return program;
    }
}
