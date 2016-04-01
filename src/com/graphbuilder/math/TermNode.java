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

package com.graphbuilder.math;

/**
A node of an expression tree that represents a variable or a function.
*/
public abstract class TermNode extends Expression {

	protected String name = null;
	protected boolean negate = false;

	public TermNode(String name, boolean negate) {
		setName(name);
		setNegate(negate);
	}

	/**
	Returns true if the term should negate the result before returning it in the eval method.
	*/
	public boolean getNegate() {
		return negate;
	}

	public void setNegate(boolean b) {
		negate = b;
	}

	/**
	Returns the name of the term.
	*/
	public String getName() {
		return name;
	}

	/**
	Sets the name of the term.  Valid names must not begin with a digit or a decimal, and must not contain
	round brackets, operators, commas or whitespace.

	@throws IllegalArgumentException If the name is null or invalid.
	*/
	public void setName(String s) {
		if (s == null)
			throw new IllegalArgumentException("name cannot be null");

		if (!isValidName(s))
			throw new IllegalArgumentException("invalid name: " + s);

		name = s;
	}

	private static boolean isValidName(String s) {
		if (s.length() == 0) return false;

		char c = s.charAt(0);

		if (c >= '0' && c <= '9' || c == '.' || c == ',' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
			return false;

		for (int i = 1; i < s.length(); i++) {
			c = s.charAt(i);

			if (c == ',' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
				return false;
		}

		return true;
	}
}
