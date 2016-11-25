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
PascalsTriangle can be used for O(1) lookup of the nCr function.
*/
public final class PascalsTriangle {
	
	private static final ThreadLocal<SharedData> SHARED_DATA = new ThreadLocal<SharedData>(){
		protected SharedData initialValue() {
			return new SharedData();
		}
	};
	private final SharedData sharedData = SHARED_DATA.get();
	
	private static class SharedData {
		private double[][] pt = new double[][] { {1} };
	}

	public PascalsTriangle() {}
	
	/**
	The nCr function returns the number of ways r things can be chosen from a set of size n.
	Mathematically, it is defined as: n! / (r! * (n - r)!)
	Although the result is always a whole number, double precision is used because
	the maximum value a double can represent is larger than long.  Thus, large returned
	values will only be an approximation of the actual value.  If the result exceeds the
	capabilities of double precision then the result can be checked using Double.isInfinite(...).
	For example: System.out.println(PascalsTriangle.nCr(1030, 515)); // outputs: Infinity
	If the value of n or r is less than 0 or the value of r is greater than n then 0 is
	returned.
	*/
	public  double nCr(int n, int r) {
		if (n < 0 || r < 0 || r > n) return 0;

		if (n >= sharedData.pt.length) {
			int d = 2 * sharedData.pt.length;
			double[][] pt2 = null;
			if (n > d)
				pt2 = new double[n + 1][];
			else
				pt2 = new double[d + 1][];

			for (int i = 0; i < sharedData.pt.length; i++)
				pt2[i] = sharedData.pt[i];

			for (int i = sharedData.pt.length; i < pt2.length; i++) {
				pt2[i] = new double[(i / 2) + 1];

				pt2[i][0] = 1;

				for (int j = 1; j < pt2[i].length; j++) {
					double x = pt2[i-1][j-1];
					if (j < pt2[i-1].length)
						x = x + pt2[i-1][j];
					else
						x = 2 * x;

					pt2[i][j] = x;
				}
			}
			sharedData.pt = pt2;
		}

		if (2 * r > n)
			r = n - r;

		return sharedData.pt[n][r];
	}

	/**
	Resets the internal array to the initial state to free up memory.
	*/
	public  void reset() {
		sharedData.pt = new double[][] { {1} };
	}
}
