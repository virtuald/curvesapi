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
<p>General implementation of the Non-Uniform Rational B-spline or NURB-Spline.  The main advantage
of the NURB-Spline over the B-Spline is the ability to represent conic sections.  To do this, a curve
with degree of 2 is used.  Figure 1 contains examples of conic arcs.

<p><center><img align="center" src="doc-files/nurbs1.gif"/></center>

<p>NURB-Splines can also be used to generate circles as shown in figure 2.

<p><center><img align="center" src="doc-files/nurbs2.gif"/></center>

<p>As seen in the figures, every control-point has an associated weight value.  The weight-values control
how much relative pull each control-point has on the curve.  If the weight-value is 0, then the associated
point will have no affect on the curve.  If a point has an associated weight of 0, but the curve is expected
to pass through that point, then it is likely that the result of evaluation will be the origin.  All weights
must be >= 0.
*/
public class NURBSpline extends BSpline {

	private static final ThreadLocal<SharedData> SHARED_DATA = new ThreadLocal<SharedData>(){
		protected SharedData initialValue() {
			return new SharedData();
		}
	};
	private final SharedData sharedData = SHARED_DATA.get();

	private static class SharedData {
		private double[] nw = new double[0]; // (required length >= numPts)
		private double[] weight = new double[0]; // (required length >= numPts)
	}

	private ValueVector weightVector = new ValueVector(new double[] { 1, 1, 1, 1 }, 4);
	private boolean useWeightVector = true;

	public NURBSpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
	}

	protected void eval(double[] p) {
		int dim = p.length - 1;
		double t = p[dim];
		double sum2 = 0;

		int numPts = gi.getGroupSize();

		for (int i = 0; i < numPts; i++) {
			sharedData.nw[i] = N(t, i) * sharedData.weight[i];
			sum2 += sharedData.nw[i];
		}

		if (sum2 == 0) sum2 = 1;

		for (int i = 0; i < dim; i++) {
			double sum1 = 0;
			gi.set(0,0);

			for (int j = 0; j < numPts; j++)
				sum1 += sharedData.nw[j] * cp.getPoint(gi.next()).getLocation()[i];

			p[i] = sum1 / sum2;
		}
	}

	/**
	Returns the weight-vector.

	@see #setWeightVector(ValueVector)
	*/
	public ValueVector getWeightVector() {
		return weightVector;
	}

	/**
	Sets the weight-vector.

	@see #getWeightVector()
	@throws IllegalArgumentException If the value-vector is null.
	*/
	public void setWeightVector(ValueVector v) {
		if (v == null)
			throw new IllegalArgumentException("Weight-vector cannot be null.");

		weightVector = v;
	}

	/**
	Returns the value of the useWeightVector flag.  The default value is true.

	@see #setUseWeightVector(boolean)
	*/
	public boolean getUseWeightVector() {
		return useWeightVector;
	}

	/**
	Sets the value of the useWeightVector flag.  If the flag is true, then the internal weightVector
	will be used.  If the flag is false, then all weights will be assumed to be 1.

	@see #getUseWeightVector()
	*/
	public void setUseWeightVector(boolean b) {
		useWeightVector = b;
	}

	/**
	The requirements of the appendTo method include the requirements of the BSpline appendTo method, plus
	a couple more.  The additional requirements only apply if the useWeightVector flag is true.  If so, then
	the weight-vector must have size equal to the group-size of the GroupIterator and all weights must have a
	value >= 0.  This method returns quietly if these requirements are not met.

	@see com.graphbuilder.curve.BSpline#appendTo(MultiPath)
	*/
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints()))
			throw new IllegalArgumentException("Group iterator not in range");
		int numPts = gi.getGroupSize();

		if (sharedData.nw.length < numPts) {
			sharedData.nw = new double[2 * numPts];
			sharedData.weight = new double[2 * numPts];
		}

		if (useWeightVector) {
			if (weightVector.size() != numPts)
				throw new IllegalArgumentException("weightVector.size(" + weightVector.size() + ") != group iterator size(" + numPts + ")");

			for (int i = 0; i < numPts; i++) {
				sharedData.weight[i] = weightVector.get(i);
				if (sharedData.weight[i] < 0)
					throw new IllegalArgumentException("Negative weight not allowed");
			}
		}
		else {
			for (int i = 0; i < numPts; i++)
				sharedData.weight[i] = 1;
		}

		super.appendTo(mp);
	}

	public void resetMemory() {
		super.resetMemory();
		if (sharedData.nw.length > 0) {
			sharedData.nw = new double[0];
			sharedData.weight = new double[0];
		}
	}
}
