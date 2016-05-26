package com.graphbuilder.curve;

import org.junit.*;

import com.graphbuilder.geom.PointFactory;

import static org.junit.Assert.*;


/**
 * TODO: Needs more tests, these only verify that switching from sun.awt.geom to
 * the harmony implementation won't break things too badly...
 */
public class TestShapeMultiPath {

	@Test
	public void testContains() throws Exception {

		// TODO: test other types of shapes
		ControlPath cp = new ControlPath();
		cp.addPoint(PointFactory.create(10, 10));
		cp.addPoint(PointFactory.create(10, 200));
		cp.addPoint(PointFactory.create(290, 200));
		cp.addPoint(PointFactory.create(290, 10));
		Curve c = new BezierCurve(cp, new GroupIterator("0:n-1", cp.numPoints()));

		ShapeMultiPath smp = new ShapeMultiPath();
		c.appendTo(smp);
		
		// TODO: more nuanced points..
		assertFalse(smp.contains(0, 0));
		assertFalse(smp.contains(9, 9));
		assertFalse(smp.contains(30, 180));
		assertFalse(smp.contains(280, 180));
		
		assertTrue(smp.contains(11, 11));
		assertTrue(smp.contains(100, 100));
		assertTrue(smp.contains(289, 11));
	}
}
