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

import com.graphbuilder.struc.Bag;

/**
A node of an expression tree that represents a function.  A FuncNode can have 0 or more children.
*/
public class FuncNode extends TermNode {

	private Bag bag = new Bag(1);
	private double[] of = new double[1];

	public FuncNode(String name, boolean negate) {
		super(name, negate);
	}

	/**
	Adds the expression as a child.
	*/
	public void add(Expression x) {
		insert(x, bag.size());
	}

	/**
	Adds the expression as a child at the specified index.
	*/
	public void insert(Expression x, int i) {
		checkBeforeAccept(x);
		int oldCap = bag.getCapacity();
		bag.insert(x, i);
		int newCap = bag.getCapacity();

		if (oldCap != newCap)
			of = new double[newCap];

		x.parent = this;
	}

	/**
	Removes the specified expression as a child.  Does nothing if the expression was not a child.
	*/
	public void remove(Expression x) {
		int size = bag.size();
		bag.remove(x);
		if (size != bag.size())
			x.parent = null;
	}

	/**
	Returns the number of child expressions.
	*/
	public int numChildren() {
		return bag.size();
	}

	/**
	Returns the child expression at the specified index.
	*/
	public Expression child(int i) {
		return (Expression) bag.get(i);
	}

	/**
	Evaluates each of the children, storing the result in an internal double array.  The FuncMap is
	used to obtain a Function object based on the name of this FuncNode.  The function is passed
	the double array and returns a result.  If negate is true, the result is negated.  The result
	is then returned.  The numParam passed to the function is the number of children of this FuncNode.
	*/
	public double eval(VarMap v, FuncMap f) {
		int numParam = bag.size();

		for (int i = 0; i < numParam; i++)
			of[i] = child(i).eval(v, f);

		double result = f.getFunction(name, numParam).of(of, numParam);

		if (negate) result = -result;

		return result;
	}
}
