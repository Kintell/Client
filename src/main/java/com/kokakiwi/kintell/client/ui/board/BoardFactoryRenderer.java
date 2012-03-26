package com.kokakiwi.kintell.client.ui.board;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.kokakiwi.kintell.client.core.board.Board;
import com.kokakiwi.kintell.client.core.board.BoardFactory;

public class BoardFactoryRenderer<T> extends BasicComboBoxRenderer
{
    private static final long serialVersionUID = 1796822512673481657L;
    
    @SuppressWarnings("unchecked")
    @Override
    public Component getListCellRendererComponent(
            @SuppressWarnings("rawtypes") JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        setFont(list.getFont());
        
        if (value instanceof Icon)
        {
            setIcon((Icon) value);
        }
        else if (value instanceof BoardFactory)
        {
            final BoardFactory<? extends Board> boardFactory = (BoardFactory<? extends Board>) value;
            setText(boardFactory.getName());
        }
        else
        {
            setText(value == null ? "" : value.toString());
        }
        return this;
    }
    
}
