/*
 * 12/11/2010
 *
 * ParameterizedCompletionChoicesWindow.java - A list of likely choices for a
 * parameter.
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.autocomplete;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;


/**
 * A small popup window offering a list of likely choices for a parameter
 * when the user has code-completed a parameterized completion.  For example,
 * if they have just code-completed the C function "<code>fprintf</code>",
 * when entering the file name, this popup might display all local variables
 * of type "<code>char *</code>".
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ParameterizedCompletionChoicesWindow extends JWindow {

	/**
	 * The parent AutoCompletion instance.
	 */
	private AutoCompletion ac;

	/**
	 * The list of completion choices.
	 */
	private JList list;

	/**
	 * The currently displayed completion choices.
	 */
	private DefaultListModel model;

	/**
	 * A list of lists of choices for each parameter.
	 */
	private List choicesListList;

	/**
	 * The scroll pane containing the list.
	 */
	private JScrollPane sp;

	/**
	 * Comparator used to sort completions by their relevance before sorting
	 * them lexicographically.
	 */
	private static final Comparator sortByRelevanceComparator =
								new SortByRelevanceComparator();


	/**
	 * Constructor.
	 *
	 * @param parent The parent window (hosting the text component).
	 * @param ac The auto-completion instance.
	 * @param tip The parent parameter description tool tip.
	 */
	public ParameterizedCompletionChoicesWindow(Window parent,
						AutoCompletion ac,
						final ParameterizedCompletionDescriptionToolTip tip) {

		super(parent);
		this.ac = ac;
		ComponentOrientation o = ac.getTextComponentOrientation();

		model = new DefaultListModel();
		list = new JList(model);
		if (ac.getParamChoicesRenderer()!=null) {
			list.setCellRenderer(ac.getParamChoicesRenderer());
		}
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					tip.insertSelectedChoice();
				}
			}
		});
		sp = new JScrollPane(list);

		setContentPane(sp);
		applyComponentOrientation(o);
		setFocusableWindowState(false);

		// Give apps a chance to decorate us with drop shadows, etc.
		PopupWindowDecorator decorator = PopupWindowDecorator.get();
		if (decorator!=null) {
			decorator.decorate(this);
		}

	}


	/**
	 * Returns the selected value.
	 *
	 * @return The selected value, or <code>null</code> if nothing is
	 *         selected.
	 */
	public String getSelectedChoice() {
		Completion c = (Completion)list.getSelectedValue();
		return c==null ? null : c.toString();
	}


	/**
	 * Changes the selected index.
	 *
	 * @param amount The amount by which to change the selected index.
	 */
	public void incSelection(int amount) {
		int selection = list.getSelectedIndex();
		selection += amount;
		if (selection<0) {
			// Account for nothing selected yet
			selection = model.getSize()-1;//+= model.getSize();
		}
		else {
			selection %= model.getSize();
		}
		list.setSelectedIndex(selection);
		list.ensureIndexIsVisible(selection);
	}


	/**
	 * Initializes this window to offer suggestions for the parameters of
	 * a specific completion.
	 *
	 * @param pc The completion whose parameters we should offer suggestions
	 *        for.
	 */
	public void initialize(ParameterizedCompletion pc) {

		CompletionProvider provider = pc.getProvider();
		ParameterChoicesProvider pcp = provider.getParameterChoicesProvider();
		if (pcp==null) {
			choicesListList = null;
			return;
		}

		int paramCount = pc.getParamCount();
		choicesListList = new ArrayList(paramCount);
		JTextComponent tc = ac.getTextComponent();

		for (int i=0; i<paramCount; i++) {
			ParameterizedCompletion.Parameter param = pc.getParam(i);
			List choices = pcp.getParameterChoices(tc, param);
			choicesListList.add(choices);
		}

	}


	/**
	 * Sets the location of this window relative to the given rectangle.
	 *
	 * @param r The visual position of the caret (in screen coordinates).
	 */
	public void setLocationRelativeTo(Rectangle r) {

		// Multi-monitor support - make sure the completion window (and
		// description window, if applicable) both fit in the same window in
		// a multi-monitor environment.  To do this, we decide which monitor
		// the rectangle "r" is in, and use that one (just pick top-left corner
		// as the defining point).
		Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);
		//Dimension screenSize = tooltip.getToolkit().getScreenSize();

		// Try putting our stuff "below" the caret first.
		int y = r.y + r.height + 5;

		// Get x-coordinate of completions.  Try to align left edge with the
		// caret first.
		int x = r.x;
		if (x<screenBounds.x) {
			x = screenBounds.x;
		}
		else if (x+getWidth()>screenBounds.x+screenBounds.width) { // completions don't fit
			x = screenBounds.x + screenBounds.width - getWidth();
		}

		setLocation(x, y);

	}


	/**
	 * Displays the choices for the specified parameter matching the given
	 * text.
	 *
	 * @param param The index of the parameter the caret is currently in.
	 *        This may be <code>-1</code> if not in a parameter (i.e., on
	 *        the comma between parameters).
	 * @param prefix Text in the parameter before the dot.  This may
	 *        be <code>null</code> to represent the empty string.
	 */
	public void setParameter(int param, String prefix) {

		model.clear();
		List temp = new ArrayList();

		if (choicesListList!=null && param>=0 && param<choicesListList.size()) {

			List choices = (List)choicesListList.get(param);
			if (choices!=null) {
				for (Iterator i=choices.iterator(); i.hasNext(); ) {
					Completion c = (Completion)i.next();
					String choice = c.getReplacementText();
					if (prefix==null || Util.startsWithIgnoreCase(choice, prefix)) {
						temp.add(c);
					}
				}
			}

			// Sort completions appropriately.
			Comparator c = null;
			if (/*sortByRelevance*/true) {
				c = sortByRelevanceComparator;
			}
			Collections.sort(temp, c);
			for (int i=0; i<temp.size(); i++) {
				model.addElement(temp.get(i));
			}

			int visibleRowCount = Math.min(model.size(), 10);
			list.setVisibleRowCount(visibleRowCount);

			// Toggle visibility, if necessary.
			if (visibleRowCount==0 && isVisible()) {
				setVisible(false);
			}
			else if (visibleRowCount>0) {
				Dimension size = getPreferredSize();
				if (size.width<150) {
					setSize(150, size.height);
				}
				else {
					pack();
				}
				// Make sure nothing is ever obscured by vertical scroll bar.
				if (sp.getVerticalScrollBar()!=null &&
						sp.getVerticalScrollBar().isVisible()) {
					size = getSize();
					int w = size.width + sp.getVerticalScrollBar().getWidth()+5;
					setSize(w, size.height);
				}
				list.setSelectedIndex(0);
				list.ensureIndexIsVisible(0);
				if (!isVisible()) {
					setVisible(true);
				}
			}

		}

	}


	/**
	 * Toggles the visibility of this popup window.
	 *
	 * @param visible Whether this window should be visible.
	 */
	public void setVisible(boolean visible) {
		if (visible!=isVisible()) {
			// i.e. if no possibilities matched what's been typed
			if (visible && list.getVisibleRowCount()==0) {
				return;
			}
			super.setVisible(visible);
		}
	}


	/**
	 * Updates the <tt>LookAndFeel</tt> of this window.
	 */
	public void updateUI() {
		SwingUtilities.updateComponentTreeUI(this);
	}


}