/*
 * 01/06/2009
 *
 * MarkupTagComletion.java - A completion representing a tag in markup, such
 * as HTML or XML.
 * Copyright (C) 2009 Robert Futrell
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

import java.util.ArrayList;
import java.util.List;

import org.fife.ui.autocomplete.ParameterizedCompletion.Parameter;


/**
 * A completion representing a tag in markup, such as HTML or XML.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MarkupTagCompletion extends AbstractCompletion {

	private String name;
	private String desc;
	private String definedIn;

	/**
	 * Attributes of the tag.
	 */
	private List attrs;


	/**
	 * Constructor.
	 *
	 * @param provider The parent provider instance.
	 * @param name The name of the tag.
	 */
	public MarkupTagCompletion(CompletionProvider provider, String name) {
		super(provider);
		this.name = name;
	}


	/**
	 * Adds HTML describing the attributes of this tag to a buffer.
	 *
	 * @param sb The buffer to append to.
	 */
	protected void addAttributes(StringBuffer sb) {

		// TODO: Localize me.

		int attrCount = getAttributeCount();
		if (attrCount>0) {
			sb.append("<b>Attributes:</b><br>");
			sb.append("<center><table width='90%'><tr><td>");
			for (int i=0; i<attrCount; i++) {
				Parameter attr = getAttribute(i);
				sb.append("&nbsp;&nbsp;&nbsp;<b>");
				sb.append(attr.getName()!=null ? attr.getName() :
							attr.getType());
				sb.append("</b>&nbsp;");
				String desc = attr.getDescription();
				if (desc!=null) {
					sb.append(desc);
				}
				sb.append("<br>");
			}
			sb.append("</td></tr></table></center><br><br>");
		}

	}


	protected void addDefinitionString(StringBuffer sb) {
		sb.append("<html><b>").append(name).append("</b>");
	}


	/**
	 * Returns all attributes of this tag.
	 *
	 * @return A list of {@link ParameterizedCompletion.Parameter}s.
	 * @see #getAttribute(int)
	 * @see #getAttributeCount()
	 */
	public List getAttributes() {
		return attrs;
	}


	/**
	 * Returns the specified {@link ParameterizedCompletion.Parameter}.
	 *
	 * @param index The index of the attribute to retrieve.
	 * @return The attribute.
	 * @see #getAttributeCount()
	 */
	public Parameter getAttribute(int index) {
		return (Parameter)attrs.get(index);
	}


	/**
	 * Returns the number of attributes of this tag.
	 *
	 * @return The number of attributes of this tag.
	 * @see #getAttribute(int)
	 */
	public int getAttributeCount() {
		return attrs==null ? 0 : attrs.size();
	}


	/**
	 * Returns where this variable is defined.
	 *
	 * @return Where this variable is defined.
	 * @see #setDefinedIn(String)
	 */
	public String getDefinedIn() {
		return definedIn;
	}


	/**
	 * Returns a short description of this variable.  This should be an
	 * HTML snippet.
	 *
	 * @return A short description of this variable.  This may be
	 *         <code>null</code>.
	 * @see #setDescription(String)
	 */
	public String getDescription() {
		return desc;
	}


	/**
	 * Returns the name of this tag.
	 *
	 * @return The name of this tag.
	 */
	public String getName() {
		return name;
	}


	/**
	 * {@inheritDoc}
	 */
	public String getReplacementText() {
		return getName();
	}


	/**
	 * {@inheritDoc}
	 */
	public String getSummary() {
		StringBuffer sb = new StringBuffer();
		addDefinitionString(sb);
		possiblyAddDescription(sb);
		addAttributes(sb);
		possiblyAddDefinedIn(sb);
		return sb.toString();
	}


	/**
	 * Adds some HTML describing where this variable is defined, if this
	 * information is known.
	 *
	 * @param sb The buffer to append to.
	 */
	protected void possiblyAddDefinedIn(StringBuffer sb) {
		if (definedIn!=null) {
			sb.append("<hr>Defined in:"); // TODO: Localize me
			sb.append(" <em>").append(definedIn).append("</em>");
		}
	}


	/**
	 * Adds the description text as HTML to a buffer, if a description is
	 * defined.
	 *
	 * @param sb The buffer to append to.
	 */
	protected void possiblyAddDescription(StringBuffer sb) {
		if (desc!=null) {
			sb.append("<hr><br>");
			sb.append(desc);
			sb.append("<br><br><br>");
		}
	}


	/**
	 * Sets where this variable is defined.
	 *
	 * @param definedIn Where this variable is defined.
	 * @see #getDefinedIn()
	 */
	public void setDefinedIn(String definedIn) {
		this.definedIn = definedIn;
	}


	/**
	 * Sets the short description of this tag.  This should be an
	 * HTML snippet.
	 *
	 * @param desc A short description of this tag.  This may be
	 *        <code>null</code>.
	 * @see #getDescription()
	 */
	public void setDescription(String desc) {
		this.desc = desc;
	}


	/**
	 * Sets the attributes of this tag.
	 *
	 * @param attrs The attributes.
	 * @see #getAttribute(int)
	 * @see #getAttributeCount()
	 */
	public void setAttributes(List attrs) {
		// Deep copy so parsing can re-use its array.
		this.attrs = new ArrayList(attrs);
	}


}