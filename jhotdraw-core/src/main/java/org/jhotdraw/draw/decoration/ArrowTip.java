/*
 * @(#)ArrowTip.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.decoration;

import java.awt.geom.*;
import org.jhotdraw.draw.figure.Figure;

/**
 * A {@link LineDecoration} which can draws an arrow tip.
 *
 * <p>The shape of the arrow can be controlled with three parameters:
 *
 * <ul>
 *   <li>angle - the angle in radians at which the two legs of the arrow meet.
 *   <li>outer radius - the length of the two legs of the arrow.
 *   <li>inner radius - the distance from the tip of the arrow to the point where its end meets the
 *       line.
 * </ul>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ArrowTip extends AbstractLineDecoration {

  private static final long serialVersionUID = 1L;
  /** Pointiness of arrow. */
  private double angle;

  private double outerRadius;
  private double innerRadius;

  public ArrowTip() {
    this(0.35, 12, 11.3);
  }

  /** Constructs an arrow tip with the specified angle and outer and inner radius. */
  public ArrowTip(double angle, double outerRadius, double innerRadius) {
    this(angle, outerRadius, innerRadius, true, false, true);
  }

  /** Constructs an arrow tip with the specified parameters. */
  public ArrowTip(
      double angle,
      double outerRadius,
      double innerRadius,
      boolean isFilled,
      boolean isStroked,
      boolean isSolid) {
    super(isFilled, isStroked, isSolid);
    this.angle = angle;
    this.outerRadius = outerRadius;
    this.innerRadius = innerRadius;
  }

  @Override
  protected Path2D.Double getDecoratorPath(Figure f) {
    // FIXME - This should take the stroke join an the outer radius into
    // account to compute the offset properly.
    double offset = (isStroked()) ? 1 : 0;
    Path2D.Double path = new Path2D.Double();
    path.moveTo((outerRadius * Math.sin(-angle)), (offset + outerRadius * Math.cos(-angle)));
    path.lineTo(0, offset);
    path.lineTo((outerRadius * Math.sin(angle)), (offset + outerRadius * Math.cos(angle)));
    if (innerRadius != 0) {
      path.lineTo(0, (innerRadius + offset));
      path.closePath();
    }
    return path;
  }

  @Override
  protected double getDecoratorPathRadius(Figure f) {
    double offset = (isStroked()) ? 0.5 : -0.1;
    return innerRadius + offset;
  }

  public double getAngle() {
    return angle;
  }

  public double getInnerRadius() {
    return innerRadius;
  }

  public double getOuterRadius() {
    return outerRadius;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public void setOuterRadius(double outerRadius) {
    this.outerRadius = outerRadius;
  }

  public void setInnerRadius(double innerRadius) {
    this.innerRadius = innerRadius;
  }
}
