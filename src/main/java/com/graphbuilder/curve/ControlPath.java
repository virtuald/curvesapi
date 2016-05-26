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

import com.graphbuilder.struc.Bag;

/**
<p>A ControlPath is a container of Point objects and Curve objects.  The control-path
uses arrays to store the points and curves.  Methods that accept objects will throw an
IllegalArgumentException if those objects are null.  Methods that accept index values
will throw an IllegalArgumentException if those index values are out of range.
*/
public class ControlPath {

	private Bag curveBag = new Bag();
	private Bag pointBag = new Bag();

	/**
	Creates a control-path.
	*/
	public ControlPath() {}

	/**
	Adds a curve to the curve array at index location numCurves.

	@throws IllegalArgumentException If the specified curve is null.
	*/
	public void addCurve(Curve c) {
		if (c == null)
			throw new IllegalArgumentException("Curve cannot be null.");

		curveBag.add(c);
	}

	/**
	Adds a point to the point array at index location numPoints.

	@throws IllegalArgumentException If the specified point is null.
	*/
	public void addPoint(Point p) {
		if (p == null)
			throw new IllegalArgumentException("Point cannot be null.");

		pointBag.add(p);
	}

	/**
	Inserts a curve at the specified index in the curve array.

	@throws IllegalArgumentException If the specified curve is null.
	*/
	public void insertCurve(Curve c, int index) {
		if (c == null)
			throw new IllegalArgumentException("Curve cannot be null.");

		curveBag.insert(c, index);
	}

	/**
	Inserts a point at the specified index in the point array.

	@throws IllegalArgumentException If the specified point is null.
	*/
	public void insertPoint(Point p, int index) {
		if (p == null)
			throw new IllegalArgumentException("Point cannot be null.");

		pointBag.insert(p, index);
	}

	/**
	Sets a curve at the specified index in the curve array, returning the curve
	that was at that index.

	@throws IllegalArgumentException If the specified curve is null.
	*/
	public Curve setCurve(Curve c, int index) {
		if (c == null)
			throw new IllegalArgumentException("Curve cannot be null.");

		return (Curve) curveBag.set(c, index);
	}

	/**
	Sets a point at the specified index in the point array, returning the point
	that was at that index.

	@throws IllegalArgumentException If the specified point is null.
	*/
	public Point setPoint(Point p, int index) {
		if (p == null)
			throw new IllegalArgumentException("Point cannot be null.");

		return (Point) pointBag.set(p, index);
	}

	/**
	Returns the curve at the specified index.
	*/
	public Curve getCurve(int index) {
		return (Curve) curveBag.get(index);
	}

	/**
	Returns the point at the specified index.
	*/
	public Point getPoint(int index) {
		return (Point) pointBag.get(index);
	}

	/**
	Returns the number of curves in the control-path.
	*/
	public int numCurves() {
		return curveBag.size();
	}

	/**
	Returns the number of points in the control-path.
	*/
	public int numPoints() {
		return pointBag.size();
	}

	/**
	Removes the first occurrence of the specified curve from the curve array.
	*/
	public void removeCurve(Curve c) {
		curveBag.remove(c);
	}

	/**
	Removes the first occurrence of the specified point from the point array.
	*/
	public void removePoint(Point p) {
		pointBag.remove(p);
	}

	/**
	Removes the curve at the specified index location.
	*/
	public void removeCurve(int index) {
		curveBag.remove(index);
	}

	/**
	Removes the point at the specified index location.
	*/
	public void removePoint(int index) {
		pointBag.remove(index);
	}

	/**
	Checks that the curve array has the specified capacity, otherwise the capacity of the
	curve array is increased to be the maximum between twice the current capacity or
	the specified capacity.
	*/
	public void ensureCurveCapacity(int capacity) {
		curveBag.ensureCapacity(capacity);
	}

	/**
	Checks that the point array has the specified capacity, otherwise the capacity of the
	point array is increased to be the maximum between twice the current capacity or
	the specified capacity.
	*/
	public void ensurePointCapacity(int capacity) {
		pointBag.ensureCapacity(capacity);
	}

	/**
	Creates a new curve array of exact size, copying the curves from the old array into the
	new one.
	*/
	public void trimCurveArray() {
		curveBag.trimArray();
	}

	/**
	Creates a new point array of exact size, copying the points from the old array into the
	new one.
	*/
	public void trimPointArray() {
		pointBag.trimArray();
	}
}
