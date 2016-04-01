/*
* Copyright (c) 2005, Graph Builder
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* * Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
*
* * Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* * Neither the name of Graph Builder nor the names of its contributors may be
* used to endorse or promote products derived from this software without
* specific prior written permission.

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.graphbuilder.curve;

import com.graphbuilder.math.*;

/**
<p>Exception thrown if the parsing of a control-string fails.  A ControlStringParseException
contains a description, and may contain any of the following information:

<ul>
<li>An index or index range of the substring that caused the problem.</li>
<li>An ExpressionParseException that was thrown by the ExpressionTree.parse(String) method.</li>
</ul>

<p>Otherwise the index values will be -1 if they are unassigned and the ExpressionParseException
will be null.

@see GroupIterator
*/
public class ControlStringParseException extends RuntimeException {

	private String descrip = null;
	private int fromIndex = -1;
	private int toIndex = -1;
	private ExpressionParseException epe = null;

	/**
	Constructor with only a description.
	*/
	public ControlStringParseException(String descrip) {
		this.descrip = descrip;
	}

	/**
	Constructor with a description and index value.  The index value is assigned to both the
	fromIndex and the toIndex.
	*/
	public ControlStringParseException(String descrip, int index) {
		this.descrip = descrip;
		fromIndex = index;
		toIndex = index;
	}

	/**
	Constructor with a description and index range.
	*/
	public ControlStringParseException(String descrip, int fromIndex, int toIndex) {
		this.descrip = descrip;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	/**
	Constructor with a description, index range and ExpressionParseException.
	*/
	public ControlStringParseException(String descrip, int fromIndex, int toIndex, ExpressionParseException epe) {
		this.descrip = descrip;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
		this.epe = epe;
	}

	/**
	Returns the index location in the control-string that marks the start of the problem or -1 if
	not available.
	*/
	public int getFromIndex() {
		return fromIndex;
	}

	/**
	Returns the index location in the control-string that marks the end of the problem or -1 if not
	available.
	*/
	public int getToIndex() {
		return toIndex;
	}

	/**
	Returns the description of the problem.
	*/
	public String getDescription() {
		return descrip;
	}

	/**
	Returns an ExpressionParseException if an expression in the control-string could not be
	parsed correctly or null if this was not the problem.
	*/
	public ExpressionParseException getExpressionParseException() {
		return epe;
	}

	/**
	Returns a nicely formatted string of this exception.
	*/
	public String toString() {
		String e = "";
		if (epe != null)
			e = "\n" + epe.toString();

		if (fromIndex == -1 && toIndex == -1)
			return descrip + e;

		if (fromIndex == toIndex)
			return descrip + " : [" + toIndex + "]" + e;

		return descrip + " : [" + fromIndex + ", " + toIndex + "]" + e;
	}
}
