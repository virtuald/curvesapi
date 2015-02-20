package com.graphbuilder.curve;

/**
<p>The cubic B-spline is defined by third order polynomial basis functions.  Each point on the curve
is locally controlled by 4 control-points.  In general, the curve does not pass through the control points,
only near.  The exceptions to this are the first and last control-points and if there are duplicate sequential
control-points.

<p>Relative to other curves, the cubic B-spline is computationally inexpensive, and easy to work with.
To create a closed cubic B-spline, use "0:n-1,0:2" as the control-string and set interpolateEndpoints
to false.  Figures 1, 2 & 3 show some examples of cubic B-splines.  See the appendTo method for more
information.

<p><center><img align="center" src="doc-files/cubicb1.gif"/></center>

<p><center><img align="center" src="doc-files/cubicb2.gif"/></center>

<p><center><img align="center" src="doc-files/cubicb3.gif"/></center>
*/
public class CubicBSpline extends ParametricCurve {

	private static final int FIRST_SECTION = 0;
	private static final int SECOND_SECTION = 1;
	private static final int MIDDLE_SECTION = 2;
	private static final int SECOND_LAST_SECTION = 3;
	private static final int LAST_SECTION = 4;

	private static double[][] pt = new double[4][];
	private static double[] b = new double[4];
	private static int currentSection = FIRST_SECTION;

	private boolean interpolateEndpoints = false;

	public CubicBSpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
	}

	protected void eval(double[] p) {
		double t = p[p.length - 1];

		if (currentSection == SECOND_LAST_SECTION || currentSection == LAST_SECTION)
			t = 1.0 - t;

		double t2 = t * t;
		double t3 = t2 * t;

		double u = 1 - t;
		double u3 = u * u * u;

		if (currentSection == FIRST_SECTION) {
			b[0] = u3;
			b[1] = 21 * t3 / 12 - 9 * t2 / 2 + 3 * t;
			b[2] = -11 * t3 / 12 + 3 * t2 / 2;
			b[3] = t3 / 6;
		}
		else if (currentSection == SECOND_SECTION) {
			b[0] = u3 / 4;
			b[1] = 7 * t3 / 12 - 5 * t2 / 4 + t / 4 + 7.0 / 12;
			b[2] = -t3 / 2 + t2 / 2 + t / 2 + 1.0 / 6;
			b[3] = t3 / 6;
		}
		else if (currentSection == MIDDLE_SECTION) {
			b[0] = u3 / 6;
			b[1] = t3 / 2 - t2 + 2.0 / 3;
			b[2] = (-t3 + t2 + t) / 2 + 1.0 / 6;
			b[3] = t3 / 6;
		}
		else if (currentSection == SECOND_LAST_SECTION) {
			b[0] = t3 / 6;
			b[1] = -t3 / 2 + t2 / 2 + t / 2 + 1.0 / 6;
			b[2] = 7 * t3 / 12 - 5 * t2 / 4 + t / 4 + 7.0 / 12;
			b[3] = u3 / 4;
		}
		else if (currentSection == LAST_SECTION) {
			b[0] = t3 / 6;
			b[1] = -11 * t3 / 12 + 3 * t2 / 2;
			b[2] = 21 * t3 / 12 - 9 * t2 / 2 + 3 * t;
			b[3] = u3;
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < p.length - 1; j++)
				p[j] = p[j] + pt[i][j] * b[i];
		}
	}

	/**
	Returns a value of 1.
	*/
	public int getSampleLimit() {
		return 1;
	}

	/**
	Sets the curve to start at the first control-point and end at the last control-point specified by
	the group-iterator.

	@see #getInterpolateEndpoints()
	*/
	public void setInterpolateEndpoints(boolean b) {
		interpolateEndpoints = b;
	}

	/**
	Returns the interpolateEndpoints value.  The default value is false.

	@see #setInterpolateEndpoints(boolean)
	*/
	public boolean getInterpolateEndpoints() {
		return interpolateEndpoints;
	}

	/**
	The requirements for this curve are the group-iterator must be in-range and have a group size of at least 4.
	If interpolateEndpoints is true, then the group size must be at least 7.  If these requirements are not met
	then this method returns quietly.
	*/
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints())) return;

		int n = gi.getGroupSize();

		if (n < 4) return;

		if (interpolateEndpoints) {
			if (n < 7) return;
			currentSection = FIRST_SECTION;
		}
		else
			currentSection = MIDDLE_SECTION;

		gi.set(0, 0);

		for (int i = 0; i < 4; i++)
			pt[i] = cp.getPoint(gi.next()).getLocation();

		double[] d = new double[mp.getDimension() + 1];
		eval(d);

		if (connect)
			mp.lineTo(d);
		else
			mp.moveTo(d);

		gi.set(0, 0);

		int j = 0;

		while (true) {
			int index_i = gi.index_i();
			int count_j = gi.count_j();

			for (int i = 0; i < 4; i++) {
				if (!gi.hasNext()) return;
				pt[i] = cp.getPoint(gi.next()).getLocation();
			}

			gi.set(index_i, count_j);
			gi.next();
			BinaryCurveApproximationAlgorithm.genPts(this, 0.0, 1.0, mp);

			if (interpolateEndpoints) {
				if (currentSection != MIDDLE_SECTION)
					currentSection++;

				if (currentSection == MIDDLE_SECTION && j == n - 6)
					currentSection++;

				j++;
			}
		}
	}
}