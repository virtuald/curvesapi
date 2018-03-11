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
The CatmullRomSpline is equal to the CardinalSpline with the value of alpha fixed at 0.5.
The implementation for the CatmullRomSpline is about 10% faster then the CardinalSpline
implementation.

@see com.graphbuilder.curve.CardinalSpline
*/
public class CatmullRomSpline extends ParametricCurve {

	private static final ThreadLocal<SharedData> SHARED_DATA = new ThreadLocal<SharedData>(){
		protected SharedData initialValue() {
			return new SharedData();
		}
	};
	private final SharedData sharedData = SHARED_DATA.get();

	private static class SharedData {
		private double[][] pt = new double[4][];
	}

	public CatmullRomSpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
	}

	protected void eval(double[] p) {
		double t = p[p.length - 1];
		double t2 = t * t;
		double t3 = t2 * t;

		// Note: The 0.5 does NOT represent alpha. It is a result of the simplification.

		for (int i = 0; i < p.length - 1; i++) {
			p[i] = 0.5 * ((sharedData.pt[3][i] - sharedData.pt[0][i] + 3 * (sharedData.pt[1][i] - sharedData.pt[2][i])) * t3
				+ (2 * (sharedData.pt[0][i] + 2*sharedData.pt[2][i]) - 5*sharedData.pt[1][i] - sharedData.pt[3][i]) * t2
				+ (sharedData.pt[2][i] - sharedData.pt[0][i]) * t) + sharedData.pt[1][i];
		}
	}

	/**
	Returns a value of 1.
	*/
	public int getSampleLimit() {
		return 1;
	}

	/**
	The requirements for this curve are the group-iterator must be in-range and have a group size of at least 4.
	If these requirements are not met then this method returns quietly.
	*/
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints()))
			throw new IllegalArgumentException("Group iterator not in range");;
		if (gi.getGroupSize() < 4)
			throw new IllegalArgumentException("Group iterator size < 4");;

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
				if (!gi.hasNext()) {
					return;
				}
				sharedData.pt[i] = cp.getPoint(gi.next()).getLocation();
			}

			gi.set(index_i, count_j);
			gi.next();

			BinaryCurveApproximationAlgorithm.genPts(this, 0.0, 1.0, mp);
		}
	}
}
