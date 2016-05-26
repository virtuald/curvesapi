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

import java.awt.geom.*;

class ShapeMultiPathIterator implements PathIterator {

	private final int ai0;
	private final int ai1;
	private final AffineTransform at;
	private final ShapeMultiPath smp;
	private final int windingRule;
	private int n = 0;

	ShapeMultiPathIterator(ShapeMultiPath smp, AffineTransform at) {
		this.smp = smp;
		int[] bv = smp.getBasisVectors();
		ai0 = bv[0];
		ai1 = bv[1];
		this.at = at;
		windingRule = smp.getWindingRule();
	}

	public int getWindingRule() {
		return windingRule;
	}

	public boolean isDone() {
		return n >= smp.getNumPoints();
	}

	public void next() {
		n++;
	}

	public int currentSegment(float[] coords) {
		double[] p = smp.get(n);
		coords[0] = (float) p[ai0];
		coords[1] = (float) p[ai1];

		if (n > 0 && p == smp.get(0))
			return PathIterator.SEG_CLOSE;

		if (at != null)
			at.transform(coords, 0, coords, 0, 1);

		if (smp.getType(n) == MultiPath.MOVE_TO)
			return PathIterator.SEG_MOVETO;

		return PathIterator.SEG_LINETO;
	}

	public int currentSegment(double[] coords) {
		double[] p = smp.get(n);
		coords[0] = p[ai0];
		coords[1] = p[ai1];

		if (n > 0 && p == smp.get(0))
			return PathIterator.SEG_CLOSE;

		if (at != null)
			at.transform(coords, 0, coords, 0, 1);


		if (smp.getType(n) == MultiPath.MOVE_TO)
			return PathIterator.SEG_MOVETO;

		return PathIterator.SEG_LINETO;
	}
}
