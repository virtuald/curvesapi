package com.graphbuilder.geom;

public class PointFactory {

	static class Point2D implements Point2d {
		
		double [] pts;
		
		public Point2D(double x, double y) {
			pts = new double[]{x, y};
		}
		
		@Override
		public double getX() {
			return pts[0];
		}

		@Override
		public double getY() {
			return pts[1];
		}

		@Override
		public void setLocation(double[] p) {
			pts[0] = p[0];
			pts[1] = p[1];
		}
		
		@Override
		public void setLocation(double x, double y) {
			pts[0] = x;
			pts[1] = y;
		}

		@Override
		public double[] getLocation() {
			return pts;
		}
	}
	
	public static Point2d create(double x, double y) {
		return new Point2D(x, y);
	}
	
	
}
