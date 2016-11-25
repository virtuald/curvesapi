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
<p>The Cardinal-spline passes through the points in the control-path specified by the group-iterator.
However, the curve does not pass through the first or the last control-point, it begins at the
second control-point and ends at the second last control-point.

<p>There is a single parameter, alpha, that gives some control over the shape of the curve.  When
the value of alpha is 0.5 the curve becomes the CatmullRomSpline.  Figure 1 shows an example of a
CardinalSpline.

<p><center><img align="center" src="doc-files/cardinal1.gif"/></center>

@see com.graphbuilder.curve.CatmullRomSpline
*/
public class CardinalSpline extends ParametricCurve {

	private static final ThreadLocal<SharedData> SHARED_DATA = new ThreadLocal<SharedData>(){
		protected SharedData initialValue() {
			return new SharedData();
		}
	};
	private final SharedData sharedData = SHARED_DATA.get();

	private static class SharedData {
		private double[][] pt = new double[4][];
	}

	private double alpha = 0.5;

	public CardinalSpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
	}

	protected void eval(double[] p) {
		double t = p[p.length - 1];
		double t2 = t * t;
		double t3 = t2 * t;

		double a = 2 * t3 - 3 * t2 + 1;
		double b = -2 * t3 + 3 * t2;
		double c = alpha * (t3 - 2 * t2 + t);
		double d = alpha * (t3 - t2);

		for (int i = 0; i < p.length - 1; i++)
			p[i] = a * sharedData.pt[1][i] + b * sharedData.pt[2][i] + c * (sharedData.pt[2][i] - sharedData.pt[0][i]) + d * (sharedData.pt[3][i] - sharedData.pt[1][i]);
	}

	/**
	Returns the value of alpha.  The default value is 0.5.

	@see #setAlpha(double)
	*/
	public double getAlpha() {
		return alpha;
	}

	/**
	Sets the value of alpha.

	@see #getAlpha()
	*/
	public void setAlpha(double a) {
		alpha = a;
	}

	/**
	Returns a value of 1.
	*/
	public int getSampleLimit() {
		return 1;
	}

	/**
	The requirements for this curve are the group-iterator must be in-range and have a group size of at least 4.
	If these requirements are not met then this method raises IllegalArgumentException
	*/
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints()))
			throw new IllegalArgumentException("group iterator not in range");
		if (gi.getGroupSize() < 4)
			throw new IllegalArgumentException("more than 4 groups required");

		gi.set(0, 0);

		for (int i = 0; i < 4; i++)
			sharedData.pt[i] = cp.getPoint(gi.next()).getLocation();

		double[] d = new double[mp.getDimension() + 1];
		eval(d);

		if (connect)
			mp.lineTo(d);
		else
			mp.moveTo(d);

		gi.set(0, 0);

		while (true) {
			int index_i = gi.index_i();
			int count_j = gi.count_j();

			for (int i = 0; i < 4; i++) {
				if (!gi.hasNext())
					throw new IllegalArgumentException("Group iterator ended early");
				sharedData.pt[i] = cp.getPoint(gi.next()).getLocation();
			}

			gi.set(index_i, count_j);
			gi.next();

			BinaryCurveApproximationAlgorithm.genPts(this, 0.0, 1.0, mp);
		}
	}
}
