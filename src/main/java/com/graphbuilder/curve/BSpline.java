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
<p>General non-rational B-Spline implementation where the degree can be specified.

<p>For the B-Spline, there are 3 types of knot-vectors, uniform clamped, uniform unclamped,
and non-uniform.  A uniform knot-vector means that the knots are equally spaced.  A clamped
knot-vector means that the first k-knots and last k-knots are repeated, where k is the degree + 1.
Non-uniform means that the knot-values have no specific properties.  For all 3 types, the
knot-values must be non-decreasing.

<p>Here are some examples of uniform clamped knot vectors for degree 3:

<pre>
number of control points = 4: [0, 0, 0, 0, 1, 1, 1, 1]
number of control points = 7: [0, 0, 0, 0, 0.25, 0.5, 0.75, 1, 1, 1, 1]
</pre>

<p>The following is a figure of a B-Spline generated using a uniform clamped knot vector:

<p><center><img align="center" src="doc-files/bspline1.gif"/></center>

<p>Here are some examples of uniform unclamped knot vectors for degree 3:

<pre>
number of control points = 4: [0, 0.14, 0.29, 0.43, 0.57, 0.71, 0.86, 1] (about)
number of control points = 7: [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1]
</pre>

<p>The following is a figure of a B-Spline generated using a uniform unclamped knot vector:

<p><center><img align="center" src="doc-files/bspline2.gif"/></center>

<p>Note: Although the knot-values in the examples are between 0 and 1, this is not a requirement.

<p>When the knot-vector is uniform clamped, the default interval is [0, 1].  When the knot-vector
is uniform unclamped, the default interval is [grad * degree, 1 - grad * degree], where grad is the
gradient or the knot-span.  Specifying the knotVectorType as UNIFORM_CLAMPED or UNIFORM_UNCLAMPED
means that the internal knot-vector will not be used.

<p>Note: The computation required is O(2^degree) or exponential.  Increasing the degree by 1 means
that twice as many computations are done.
*/
public class BSpline extends ParametricCurve {

	public static final int UNIFORM_CLAMPED = 0;
	public static final int UNIFORM_UNCLAMPED = 1;
	public static final int NON_UNIFORM = 2;
	
	private static final ThreadLocal<SharedData> SHARED_DATA = new ThreadLocal<SharedData>(){
		protected SharedData initialValue() {
			return new SharedData();
		}
	};
	private final SharedData sharedData = SHARED_DATA.get();

	private static class SharedData {
		private int[] a = new int[0]; // counter used for the a-function values (required length >= degree)
		private int[] c = new int[0]; // counter used for bit patterns (required length >= degree)
		private double[] knot = new double[0]; // (required length >= numPts + degree)
	}

	private ValueVector knotVector = new ValueVector(new double[] { 0, 0, 0, 0, 1, 1, 1, 1 }, 8);
	private double t_min = 0.0;
	private double t_max = 1.0;
	private int sampleLimit = 1;
	private int degree = 4; // the internal degree variable is always 1 plus the specified degree
	private int knotVectorType = UNIFORM_CLAMPED;
	private boolean useDefaultInterval = true;

	public BSpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
	}

	protected void eval(double[] p) {
		int dim = p.length - 1;
		double t = p[dim];
		int numPts = gi.getGroupSize();
		gi.set(0,0);

		for (int i = 0; i < numPts; i++) {
			double w = N(t, i);
			//double w = N(t, i, degree);

			double[] loc = cp.getPoint(gi.next()).getLocation();

			for (int j = 0; j < dim; j++)
				p[j] += (loc[j] * w); //pt[i][j] * w);
		}
	}

	/**
	Specifies the interval that the curve should define itself on.  The default interval is [0.0, 1.0].
	When the knot-vector type is one of UNIFORM_CLAMPED or UNIFORM_UNCLAMPED and the useDefaultInterval
	flag is true, then these values will not be used.

	@throws IllegalArgumentException If t_min > t_max.
	@see #t_min()
	@see #t_max()
	*/
	public void setInterval(double t_min, double t_max) {
		if (t_min > t_max)
			throw new IllegalArgumentException("t_min <= t_max required.");

		this.t_min = t_min;
		this.t_max = t_max;
	}

	/**
	Returns the starting interval value.

	@see #setInterval(double, double)
	@see #t_max()
	*/
	public double t_min() {
		return t_min;
	}

	/**
	Returns the finishing interval value.

	@see #setInterval(double, double)
	@see #t_min()
	*/
	public double t_max() {
		return t_max;
	}

	public int getSampleLimit() {
		return sampleLimit;
	}

	/**
	Sets the sample-limit.  For more information on the sample-limit, see the
	BinaryCurveApproximationAlgorithm class.  The default sample-limit is 1.

	@throws IllegalArgumentException If sample-limit < 0.
	@see com.graphbuilder.curve.BinaryCurveApproximationAlgorithm
	@see #getSampleLimit()
	*/
	public void setSampleLimit(int limit) {
		if (limit < 0)
			throw new IllegalArgumentException("Sample-limit >= 0 required.");

		sampleLimit = limit;
	}

	/**
	Returns the degree of the curve.

	@see #setDegree(int)
	*/
	public int getDegree() {
		return degree - 1;
	}

	/**
	Sets the degree of the curve.  The degree specifies how many controls points have influence
	when computing a single point on the curve.  Specifically, degree + 1 control points are used.
	The degree must be greater than 0.  A degree of 1 is linear, 2 is quadratic, 3 is cubic, etc.
	Warning: Increasing the degree by 1 doubles the number of computations required.  The default
	degree is 3 (cubic).

	@see #getDegree()
	@throws IllegalArgumentException If degree <= 0.
	*/
	public void setDegree(int d) {
		if (d <= 0)
			throw new IllegalArgumentException("Degree > 0 required.");

		degree = d + 1;
	}

	/**
	Returns the knot-vector for this curve.

	@see #setKnotVector(ValueVector)
	*/
	public ValueVector getKnotVector() {
		return knotVector;
	}

	/**
	Sets the knot-vector for this curve.  When the knot-vector type is one of UNIFORM_CLAMPED or
	UNIFORM_UNCLAMPED then the values in the knot-vector will not be used.

	@see #getKnotVector()
	@throws IllegalArgumentException If the value-vector is null.
	*/
	public void setKnotVector(ValueVector v) {
		if (v == null)
			throw new IllegalArgumentException("Knot-vector cannot be null.");
		knotVector = v;
	}

	/**
	Returns the value of the useDefaultInterval flag.

	@see #setUseDefaultInterval(boolean)
	*/
	public boolean getUseDefaultInterval() {
		return useDefaultInterval;
	}

	/**
	Sets the value of the useDefaultInterval flag.  When the knot-vector type is one of UNIFORM_CLAMPED or
	UNIFORM_UNCLAMPED and the useDefaultInterval flag is true, then default values will be computed for
	t_min and t_max.  Otherwise t_min and t_max are used as the interval.

	@see #getUseDefaultInterval()
	*/
	public void setUseDefaultInterval(boolean b) {
		useDefaultInterval = b;
	}

	/**
	Returns the type of knot-vector to use.

	@see #setKnotVectorType(int)
	*/
	public int getKnotVectorType() {
		return knotVectorType;
	}

	/**
	Sets the type of knot-vector to use.  There are 3 types, UNIFORM_CLAMPED, UNIFORM_UNCLAMPED and NON_UNIFORM.
	NON_UNIFORM can be thought of as user specified.  UNIFORM_CLAMPED and UNIFORM_UNCLAMPED are standard
	knot-vectors for the B-Spline.

	@see #getKnotVectorType()
	@throws IllegalArgumentException If the knot-vector type is unknown.
	*/
	public void setKnotVectorType(int type) {
		if (type < 0 || type > 2)
			throw new IllegalArgumentException("Unknown knot-vector type.");

		knotVectorType = type;
	}

	/**
	There are two types of requirements for this curve, common requirements and requirements that depend on the
	knotVectorType.  The common requirements are that the group-iterator must be in range and the number of
	points (group size) must be greater than the degree.  If the knot-vector type is NON_UNIFORM (user specified)
	then there are additional requirements, otherwise there are no additional requirements.

	The additional requirements when the knotVectorType is NON_UNIFORM are that the internal-knot vector must have
	an exact size of degree + numPts + 1, where degree is specified by the setDegree method and numPts is the
	group size.  Also, the knot-vector values must be non-decreasing.

	If any of these requirements are not met, then IllegalArgumentException is thrown
	*/
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints()))
			throw new IllegalArgumentException("Group iterator not in range");

		int numPts = gi.getGroupSize();
		int f = numPts - degree;
		if (f < 0)
			throw new IllegalArgumentException("group iterator size - degree < 0");

		int x = numPts + degree;

		if (sharedData.knot.length < x)
			sharedData.knot = new double[2 * x];

		double t1 = t_min;
		double t2 = t_max;

		if (knotVectorType == NON_UNIFORM) {
			if (knotVector.size() != x)
				throw new IllegalArgumentException("knotVector.size(" + knotVector.size() + ") != " + x);

			sharedData.knot[0] = knotVector.get(0);

			for (int i = 1; i < x; i++) {
				sharedData.knot[i] = knotVector.get(i);
				if (sharedData.knot[i] < sharedData.knot[i-1])
					throw new IllegalArgumentException("Knot not in sorted order! (knot[" + i + "] < knot[" + i + "-1])");
			}
		}
		else if (knotVectorType == UNIFORM_UNCLAMPED) {

			double grad = 1.0 / (x - 1);

			for (int i = 0; i < x; i++)
				sharedData.knot[i] = i * grad;

			if (useDefaultInterval) {
				t1 = (degree - 1) * grad;
				t2 = 1.0 - (degree - 1) * grad;
			}
		}
		else if (knotVectorType == UNIFORM_CLAMPED) {
			double grad = 1.0 / (f + 1);

			for (int i = 0; i < degree; i++)
				sharedData.knot[i] = 0;

			int j = degree;
			for (int i = 1; i <= f; i++)
				sharedData.knot[j++] = i * grad;

			for (int i = j; i < x; i++)
				sharedData.knot[i] = 1.0;

			if (useDefaultInterval) {
				t1 = 0.0;
				t2 = 1.0;
			}
		}

		if (sharedData.a.length < degree) {
			sharedData.a = new int[2 * degree];
			sharedData.c = new int[2 * degree];
		}

		double[] p = new double[mp.getDimension() + 1];
		p[mp.getDimension()] = t1;
		eval(p);

		if (connect)
			mp.lineTo(p);
		else
			mp.moveTo(p);

		BinaryCurveApproximationAlgorithm.genPts(this, t1, t2, mp);
	}

	/**
	Non-recursive implementation of the N-function.
	*/
	protected double N(double t, int i) {

		double d = 0;

		for (int j = 0; j < degree; j++) {
			double t1 = sharedData.knot[i+j];
			double t2 = sharedData.knot[i+j+1];

			if (t >= t1 && t <= t2 && t1 != t2) {

				int dm2 = degree - 2;

				for (int k = degree - j - 1; k >= 0; k--)
					sharedData.a[k] = 0;

				if (j > 0) {
					for (int k = 0; k < j; k++)
						sharedData.c[k] = k;
					sharedData.c[j] = Integer.MAX_VALUE;
				}
				else {
					sharedData.c[0] = dm2;
					sharedData.c[1] = degree;
				}

				int z = 0;

				while (true) {
					if (sharedData.c[z] < sharedData.c[z+1] - 1) {
						double e = 1.0;
						int bc = 0;
						int y = dm2 - j;
						int p = j - 1;

						for (int m = dm2, n = degree; m >= 0; m--, n--) {
							if (p >= 0 && sharedData.c[p] == m) {
								int w = i + bc;
								double kd = sharedData.knot[w+n];
								e *= (kd - t) / (kd - sharedData.knot[w+1]);
								bc++;
								p--;
							}
							else {
								int w = i + sharedData.a[y];
								double kw = sharedData.knot[w];
								e *= (t - kw) / (sharedData.knot[w+n-1] - kw);
								y--;
							}
						}

						// this code updates the a-counter
						if (j > 0) {
							int g = 0;
							boolean reset = false;

							while (true) {
								sharedData.a[g]++;

								if (sharedData.a[g] > j) {
									g++;
									reset = true;
								}
								else {
									if (reset) {
										for (int h = g - 1; h >= 0; h--)
											sharedData.a[h] = sharedData.a[g];
									}
									break;
								}
							}
						}

						d += e;

						// this code updates the bit-counter
						sharedData.c[z]++;
						if (sharedData.c[z] > dm2) break;

						for (int k = 0; k < z; k++)
							sharedData.c[k] = k;
						z = 0;
					}
					else {
						z++;
					}
				}

				break; // required to prevent spikes
			}
		}

		return d;
	}

	/*
	The recursive implementation of the N-function (not used) is below.  In addition to being
	slower, the recursive implementation of the N-function has another problem which relates to
	the base case.  Note: the reason the recursive implementation is slower is because there
	are a lot of repetitive calculations.

	Some definitions of the N-function give the base case as t >= knot[i] && t < knot[i+1] or
	t > knot[i] && t <= knot[i+1].  To see why this is a problem, consider evaluating t on the
	range [0, 1] with the Bezier Curve knot vector [0,0,0,...,1,1,1].  Then, trying to evaluate
	t == 1 or t == 0 won't work.  Changing the base case to the one below (with equal signs on
	both comparisons) leads to a problem known as spikes.  A curve will have spikes	at places
	where t falls on a knot because when equality is used on both comparisons, the value of t
	will have 2 regions of influence.
	*/
	/*private double N(double t, int i, int k) {
		if (k == 1) {
			if (t >= knot[i] && t <= knot[i+1] && knot[i] != knot[i+1]) return 1.0;
			return 0.0;
		}

		double n1 = N(t, i, k-1);
		double n2 = N(t, i+1, k-1);

		double a = 0.0;
		double b = 0.0;

		if (n1 != 0) a = (t - knot[i]) / (knot[i+k-1] - knot[i]);
		if (n2 != 0) b = (knot[i+k] - t) / (knot[i+k] - knot[i+1]);

		return a * n1 + b * n2;
	}*/

	public void resetMemory() {
		if (sharedData.a.length > 0) {
			sharedData.a = new int[0];
			sharedData.c = new int[0];
		}

		if (sharedData.knot.length > 0)
			sharedData.knot = new double[0];
	}
}
