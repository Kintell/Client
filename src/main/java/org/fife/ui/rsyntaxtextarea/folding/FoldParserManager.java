/*
 * 10/08/2011
 *
 * FoldParserManager.java - Used by RSTA to determine what fold parser to use
 * for each language it supports.
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

import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


/**
 * Manages fold parsers.  Instances of <code>RSyntaxTextArea</code> call into
 * this class to retrieve fold parsers for whatever language they're editing.
 * Folks implementing custom languages can add a {@link FoldParser}
 * implementation for their language to this manager and it will be used by
 * RSTA.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FoldParserManager implements SyntaxConstants {

	/**
	 * Map from syntax styles to fold parsers.
	 */
	private Map foldParserMap;

	private static final FoldParserManager INSTANCE = new FoldParserManager();


	/**
	 * Private constructor to prevent instantiation.
	 */
	private FoldParserManager() {
		foldParserMap = createFoldParserMap();
	}


	/**
	 * Adds a mapping from a syntax style to a fold parser.  The parser
	 * specified will be shared among all RSTA instances editing that language,
	 * so it should be stateless (which should not be difficult for a fold
	 * parser).  You can also override the fold parser for built-in languages,
	 * such as <code>SYNTAX_STYLE_JAVA</code>, with your own parser
	 * implementations.
	 *
	 * @param syntaxStyle The syntax style.
	 * @param parser The parser.
	 * @see SyntaxConstants
	 */
	public void addFoldParserMapping(String syntaxStyle, FoldParser parser) {
		foldParserMap.put(syntaxStyle, parser);
	}


	/**
	 * Creates the syntax style-to-fold parser mapping for built-in languages.
	 * @return
	 */
	private Map createFoldParserMap() {

		Map map = new HashMap();

		map.put(SYNTAX_STYLE_C,					new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_CPLUSPLUS,			new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_CSHARP,			new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_CSS,				new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_GROOVY,			new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_JAVA,				new CurlyFoldParser(true, true));
		map.put(SYNTAX_STYLE_JAVASCRIPT,		new CurlyFoldParser(true, true));
		map.put(SYNTAX_STYLE_MXML,				new XmlFoldParser());
		map.put(SYNTAX_STYLE_PERL,				new CurlyFoldParser(true, false));
		map.put(SYNTAX_STYLE_XML,				new XmlFoldParser());

		return map;

	}


	/**
	 * Returns the singleton instance of this class.
	 *
	 * @return The singleton instance.
	 */
	public static FoldParserManager get() {
		return INSTANCE;
	}


	/**
	 * Returns a fold parser to use for an editor highlighting code of a
	 * specific language.
	 *
	 * @param syntaxStyle A value from {@link SyntaxConstants}, such as
	 *        <code>SYNTAX_STYLE_JAVA</code>.
	 * @return A fold parser to use, or <code>null</code> if none is registered
	 *         for the language.
	 */
	public FoldParser getFoldParser(String syntaxStyle) {
		return (FoldParser)foldParserMap.get(syntaxStyle);
	}


}