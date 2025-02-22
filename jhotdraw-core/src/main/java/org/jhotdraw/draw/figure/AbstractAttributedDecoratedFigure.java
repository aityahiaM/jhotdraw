/*
 * @(#)AbstractAttributedDecoratedFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.*;
import java.awt.geom.*;
import org.jhotdraw.geom.Insets2D;

/**
 * This abstract class can be extended to implement a {@link DecoratedFigure} which has an attribute
 * set.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractAttributedDecoratedFigure extends AbstractAttributedFigure
    implements DecoratedFigure {

  private static final long serialVersionUID = 1L;
  private Figure decorator;

  @Override
  public final void draw(Graphics2D g) {
    if (decorator != null) {
      drawDecorator(g);
    }
    drawFigure(g);
  }

  protected void drawFigure(Graphics2D g) {
    super.draw(g);
  }

  protected void drawDecorator(Graphics2D g) {
    updateDecoratorBounds();
    decorator.draw(g);
  }

  @Override
  public final Rectangle2D.Double getDrawingArea() {
    Rectangle2D.Double r = getFigureDrawingArea();
    if (decorator != null) {
      updateDecoratorBounds();
      r.add(decorator.getDrawingArea());
    }
    return r;
  }

  protected Rectangle2D.Double getFigureDrawingArea() {
    return super.getDrawingArea();
  }

  @Override
  public void setDecorator(Figure newValue) {
    willChange();
    decorator = newValue;
    if (decorator != null) {
      decorator.setBounds(getStartPoint(), getEndPoint());
    }
    changed();
  }

  @Override
  public Figure getDecorator() {
    return decorator;
  }

  protected void updateDecoratorBounds() {
    if (decorator != null) {
      Point2D.Double sp = getStartPoint();
      Point2D.Double ep = getEndPoint();
      Insets2D.Double decoratorInsets = attr().get(DECORATOR_INSETS);
      sp.x -= decoratorInsets.left;
      sp.y -= decoratorInsets.top;
      ep.x += decoratorInsets.right;
      ep.y += decoratorInsets.bottom;
      decorator.setBounds(sp, ep);
    }
  }

  @Override
  public final boolean contains(Point2D.Double p, double scaleDenominator) {
    if (decorator != null) {
      updateDecoratorBounds();
      if (decorator.contains(p, scaleDenominator)) {
        return true;
      }
    }
    return figureContains(p);
  }

  protected abstract boolean figureContains(Point2D.Double p);

  @Override
  public AbstractAttributedDecoratedFigure clone() {
    AbstractAttributedDecoratedFigure that = (AbstractAttributedDecoratedFigure) super.clone();
    if (this.decorator != null) {
      that.decorator = this.decorator.clone();
    }
    return that;
  }
}
