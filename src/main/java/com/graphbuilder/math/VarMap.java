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
<p>VarMap maps a name to a value.  A VarMap is used in the eval method of an Expression object.
This class can be used as the default variable-map.

<p>During the evaluation of an expression, if a variable is not supported then a RuntimeException is thrown.
Case sensitivity can only be specified in the constructor (for consistency).  When case sensitivity is false,
the String.equalsIgnoreCase method is used.  When case sensitivity is true, the String.equals method is used.
By default, case sensitivity is true.
*/
public class VarMap {

	private boolean caseSensitive = true;
	private String[] name = new String[2];
	private double[] value = new double[2];
	private int numVars = 0;

	public VarMap() {
		this(true);
	}

	public VarMap(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	Returns the value associated with the specified variable name.

	@throws RuntimeException If a matching variable name cannot be found.
	*/
	public double getValue(String varName) {
		for (int i = 0; i < numVars; i++)
			if (caseSensitive && name[i].equals(varName) || !caseSensitive && name[i].equalsIgnoreCase(varName))
				return value[i];

		throw new RuntimeException("variable value has not been set: " + varName);
	}

	/**
	Assigns the value to the specified variable name.

	@throws IllegalArgumentException If the variable name is null.
	*/	
	public void setValue(String varName, double val) {
		if (varName == null)
			throw new IllegalArgumentException("varName cannot be null");

		for (int i = 0; i < numVars; i++) {
			if (caseSensitive && name[i].equals(varName) || !caseSensitive && name[i].equalsIgnoreCase(varName)) {
				value[i] = val;
				return;
			}
		}

		if (numVars == name.length) {
			String[] tmp1 = new String[2 * numVars];
			double[] tmp2 = new double[tmp1.length];

			for (int i = 0; i < numVars; i++) {
				tmp1[i] = name[i];
				tmp2[i] = value[i];
			}

			name = tmp1;
			value = tmp2;
		}

		name[numVars] = varName;
		value[numVars] = val;
		numVars++;
	}

	/**
	Returns true if the case of the variable names is considered.
	*/
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	Returns an array of exact length of the variable names stored in this map.
	*/
	public String[] getVariableNames() {
		String[] arr = new String[numVars];

		for (int i = 0; i < arr.length; i++)
			arr[i] = name[i];

		return arr;
	}

	/**
	Returns an array of exact length of the values stored in this map.  The returned
	array corresponds to the order of the names returned by getVariableNames.
	*/
	public double[] getValues() {
		double[] arr = new double[numVars];

		for (int i = 0; i < arr.length; i++)
			arr[i] = value[i];

		return arr;
	}

	/**
	Removes the variable-name from the map. Does nothing if the variable-name is not found.
	*/
	public void remove(String varName) {
		for (int i = 0; i < numVars; i++) {
			if (caseSensitive && name[i].equals(varName) || !caseSensitive && name[i].equalsIgnoreCase(varName)) {
				for (int j = i + 1; j < numVars; j++) {
					name[j - 1] = name[j];
					value[j - 1] = value[j];
				}
				numVars--;
				name[numVars] = null;
				value[numVars] = 0;
				break;
			}
		}
	}
}
