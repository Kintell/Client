/*
 * 10/23/2011
 *
 * XmlFoldParser.java - Fold parser for XML.
 * Copyright (C) 2011 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.folding;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;


/**
 * Fold parser for XML.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlFoldParser implements FoldParser {

	private static final char[] MARKUP_CLOSING_TAG_START = { '<', '/' };
	private static final char[] MARKUP_SHORT_TAG_END = { '/', '>' };
	private static final char[] MLC_END = { '-', '-', '>' };


	/**
	 * {@inheritDoc}
	 */
	public List getFolds(RSyntaxTextArea textArea) {

		List folds = new ArrayList();

		Fold currentFold = null;
		int lineCount = textArea.getLineCount();
		boolean inMLC = false;
		int mlcStart = 0;

		try {

			for (int line=0; line<lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {

					if (t.isComment()) {

						// Continuing an MLC from a previous line
						if (inMLC) {
							// Found the end of the MLC starting on a previous line...
							if (t.endsWith(MLC_END)) {
								int mlcEnd = t.offset + t.textCount - 1;
								if (currentFold==null) {
									currentFold = new Fold(FoldType.COMMENT, textArea, mlcStart);
									currentFold.setEndOffset(mlcEnd);
									folds.add(currentFold);
									currentFold = null;
								}
								else {
									currentFold = currentFold.createChild(FoldType.COMMENT, mlcStart);
									currentFold.setEndOffset(mlcEnd);
									currentFold = currentFold.getParent();
								}
								inMLC = false;
								mlcStart = 0;
							}
							// Otherwise, this MLC is continuing on to yet
							// another line.
						}

						else {
							// If we're an MLC that ends on a later line...
							if (t.type==Token.COMMENT_MULTILINE) {
								inMLC = true;
								mlcStart = t.offset;
							}
						}

					}

					else if (t.type==Token.MARKUP_TAG_DELIMITER && t.isSingleChar('<')) {
						if (currentFold==null) {
							currentFold = new Fold(FoldType.CODE, textArea, t.offset);
							folds.add(currentFold);
						}
						else {
							currentFold = currentFold.createChild(FoldType.CODE, t.offset);
						}
					}

					else if (t.is(Token.MARKUP_TAG_DELIMITER, MARKUP_SHORT_TAG_END)) {
						if (currentFold!=null) {
							Fold parentFold = currentFold.getParent();
							currentFold.removeFromParent();
							currentFold = parentFold;
						}
					}

					else if (t.is(Token.MARKUP_TAG_DELIMITER, MARKUP_CLOSING_TAG_START)) {
						if (currentFold!=null) {
							currentFold.setEndOffset(t.offset);
							Fold parentFold = currentFold.getParent();
							// Don't add fold markers for single-line blocks
							if (currentFold.isOnSingleLine()) {
								currentFold.removeFromParent();
							}
							currentFold = parentFold;
						}
					}

					t = t.getNextToken();

				}

			}

		} catch (BadLocationException ble) { // Should never happen
			ble.printStackTrace();
		}

		return folds;
	
	}


}