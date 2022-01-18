curvesapi
=========

Implementation of various mathematical curves that define themselves over
a set of control points. The API is written in Java. The curves supported
are: Bezier, B-Spline, Cardinal Spline, Catmull-Rom Spline, Lagrange,
Natural Cubic Spline, and NURBS.

About this project
------------------

This is a mavenized version of http://sourceforge.net/projects/curves . This
is not quite a fork -- as I do not intend to change/improve the project.
However, bugfixes and reasonably scoped improvements will be accepted.


List of changes
---------------

* Version has been bumped to 1.07
* The javadocs have been removed from this distribution
* The build scripts have been removed and pom.xml has been added to support
  usage with maven
* No longer using JDK internals to compute path intersection, copied
  Crossing.java from Apache Harmony instead
* The 'appendTo' method now raises IllegalArgumentException with useful error
  messages on invalid arguments instead of swallowing the error
* A PointFactory has been added to aid in creating points
* Point2d now extends Point
* Uses standard maven source layout
* Added simple test for ShapeMultiPath

Licenses
--------

The original project used a BSD license, and remains so.

com.graphbuilder.org.apache.harmony.awt.gl.Crossing is from the Apache
Harmony project and is released under the Apache 2.0 license.
