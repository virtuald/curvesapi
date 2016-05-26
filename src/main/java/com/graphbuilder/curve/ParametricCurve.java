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

/**
Curves that extend the ParametricCurve class are continuous and can use the
BinaryCurveApproximationAlgorithm class to generate a sequence of points that
approximate the curve.  Note: Approximate means a finite set of points that
are <i>on</i> the curve, <u>not</u> close to the curve.

@see com.graphbuilder.curve.BinaryCurveApproximationAlgorithm
@see com.graphbuilder.curve.Curve
*/
public abstract class ParametricCurve extends Curve {

	public ParametricCurve(ControlPath cp, GroupIterator gp) {
		super(cp, gp);
	}

	/**
	The eval method evaluates a point on a curve given a parametric value "t".  The parametric
	value "t" is stored in the last index location of the specified double array.  <i>This value
	should not be changed</i>.  The dimension of the point to evaluate is p.length - 1.  The result
	of the evaluation is placed in index locations 0 .. p.length - 2 (inclusive).

	The eval method should remain protected except for those curves that do no need any
	preparation to be done in the appendTo method.
	*/
	protected abstract void eval(double[] p);

	/**
	The sample limit specifies how many additional subdivisions are done to ensure that there
	are no missed pieces of the curve.  The sample limit must be >= 0.
	*/
	public abstract int getSampleLimit();
}
