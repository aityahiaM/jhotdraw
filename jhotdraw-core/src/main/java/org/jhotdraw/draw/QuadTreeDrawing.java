/*
 * @(#)QuadTreeDrawing.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.geom.QuadTree;
import org.jhotdraw.util.*;

/**
 * An implementation of {@link Drawing} which uses a {@link org.jhotdraw.geom.QuadTree} to provide a
 * good responsiveness for drawings which contain many figures.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class QuadTreeDrawing extends AbstractDrawing {

  private static final long serialVersionUID = 1L;
  private QuadTree<Figure> quadTree = new QuadTree<>();
  private boolean needsSorting = false;

  @Override
  public int indexOf(Figure figure) {
    return children.indexOf(figure);
  }

  @Override
  public void basicAdd(int index, Figure figure) {
    super.basicAdd(index, figure);
    quadTree.add(figure, figure.getDrawingArea());
    needsSorting = true;
  }

  @Override
  public Figure basicRemoveChild(int index) {
    Figure figure = getChild(index);
    quadTree.remove(figure);
    needsSorting = true;
    super.basicRemoveChild(index);
    return figure;
  }

  @Override
  public void draw(Graphics2D g) {
    Rectangle2D clipBounds = g.getClipBounds();
    if (clipBounds != null) {
      draw(g, sort(quadTree.findIntersects(clipBounds)));
    } else {
      draw(g, children);
    }
  }

  /** Implementation note: Sorting can not be done for orphaned children. */
  @Override
  public List<Figure> sort(Collection<? extends Figure> c) {
    // ensureSorted();
    ArrayList<Figure> sorted = new ArrayList<>(c);
    Collections.sort(sorted, Comparator.comparing(Figure::getLayer));
    //    for (Figure f : children) {
    //      if (c.contains(f)) {
    //        sorted.add(f);
    //      }
    //    }
    return sorted;
  }

  public void draw(Graphics2D g, Collection<Figure> c) {
    // double factor = AttributeKeys.getScaleFactorFromGraphics(g);
    for (Figure f : c) {
      if (f.isVisible()) {
        f.draw(g);
        //        if (isDebugMode()) {
        //          Graphics2D g2 = (Graphics2D) g.create();
        //          try {
        //            g2.setStroke(new BasicStroke(0));
        //            g2.setColor(Color.BLUE);
        //            Rectangle2D.Double rect = f.getDrawingArea(factor);
        //            g2.draw(rect);
        //          } finally {
        //            g2.dispose();
        //          }
        //        }
      }
    }
  }

  public java.util.List<Figure> getChildren(Rectangle2D.Double bounds) {
    return new LinkedList<>(quadTree.findInside(bounds));
  }

  @Override
  public java.util.List<Figure> getChildren() {
    return Collections.unmodifiableList(children);
  }

  @Override
  public Figure findFigureInside(Point2D.Double p) {
    Collection<Figure> c = quadTree.findContains(p);
    for (Figure f : getFiguresFrontToBack(c)) {
      if (c.contains(f) && f.contains(p)) {
        return f.findFigureInside(p);
      }
    }
    return null;
  }

  /** Returns an iterator to iterate in Z-order front to back over the children. */
  @Override
  public List<Figure> getFiguresFrontToBack() {
    ensureSorted();
    return new ReversedList<>(children);
  }

  protected List<Figure> getFiguresFrontToBack(Collection<Figure> smallCollection) {
    List<Figure> list = new ArrayList<>(smallCollection);
    Collections.sort(list, Comparator.comparing(Figure::getLayer).reversed());
    return list;
  }

  @Override
  public Figure findFigure(Point2D.Double p) {
    Collection<Figure> c = quadTree.findContains(p);
    switch (c.size()) {
      case 0:
        return null;
      case 1:
        Figure f = c.iterator().next();
        return (f.contains(p)) ? f : null;
      default:
        for (Figure f2 : getFiguresFrontToBack(c)) {
          if (f2.contains(p)) {
            return f2;
          }
        }
        return null;
    }
  }

  @Override
  public Figure findFigureExcept(Point2D.Double p, Figure ignore) {
    Collection<Figure> c = quadTree.findContains(p);
    switch (c.size()) {
      case 0:
        return null;
      case 1:
        Figure f = c.iterator().next();
        return (f == ignore || !f.contains(p)) ? null : f;
      default:
        for (Figure f2 : getFiguresFrontToBack(c)) {
          if (f2 != ignore && f2.contains(p)) {
            return f2;
          }
        }
        return null;
    }
  }

  @Override
  public Figure findFigureExcept(Point2D.Double p, Collection<? extends Figure> ignore) {
    Collection<Figure> c = quadTree.findContains(p);
    switch (c.size()) {
      case 0:
        return null;
      case 1:
        Figure f = c.iterator().next();
        return (!ignore.contains(f) || !f.contains(p)) ? null : f;
      default:
        for (Figure f2 : getFiguresFrontToBack(c)) {
          if (!ignore.contains(f2) && f2.contains(p)) {
            return f2;
          }
        }
        return null;
    }
  }

  @Override
  public Figure findFigureBehind(Point2D.Double p, Figure figure) {
    boolean isBehind = false;
    for (Figure f : getFiguresFrontToBack()) {
      if (isBehind) {
        if (f.isVisible() && f.contains(p)) {
          return f;
        }
      } else {
        isBehind = figure == f;
      }
    }
    return null;
  }

  @Override
  public Figure findFigureBehind(Point2D.Double p, Collection<? extends Figure> children) {
    int inFrontOf = children.size();
    for (Figure f : getFiguresFrontToBack()) {
      if (inFrontOf == 0) {
        if (f.isVisible() && f.contains(p)) {
          return f;
        }
      } else {
        if (children.contains(f)) {
          inFrontOf--;
        }
      }
    }
    return null;
  }

  @Override
  public java.util.List<Figure> findFigures(Rectangle2D.Double r) {
    LinkedList<Figure> c = new LinkedList<>(quadTree.findIntersects(r));
    switch (c.size()) {
      case 0:
        // fall through
      case 1:
        return c;
      default:
        return getFiguresFrontToBack(c);
    }
  }

  @Override
  public java.util.List<Figure> findFiguresWithin(Rectangle2D.Double bounds) {
    LinkedList<Figure> contained = new LinkedList<>();
    for (Figure f : children) {
      Rectangle2D.Double r = f.getBounds();
      if (f.attr().get(TRANSFORM) != null) {
        Rectangle2D rt = f.attr().get(TRANSFORM).createTransformedShape(r).getBounds2D();
        r =
            (rt instanceof Rectangle2D.Double)
                ? (Rectangle2D.Double) rt
                : new Rectangle2D.Double(rt.getX(), rt.getY(), rt.getWidth(), rt.getHeight());
      }
      if (f.isVisible() && Geom.contains(bounds, r)) {
        contained.add(f);
      }
    }
    return contained;
  }

  @Override
  public void bringToFront(Figure figure) {
    if (children.remove(figure)) {
      children.add(figure);
      needsSorting = true;
      fireDrawingChanged(figure.getDrawingArea());
    }
  }

  @Override
  public void sendToBack(Figure figure) {
    if (children.remove(figure)) {
      children.add(0, figure);
      needsSorting = true;
      fireDrawingChanged(figure.getDrawingArea());
    }
  }

  //  @Override
  //  public boolean contains(Figure f) {
  //    return children.contains(f);
  //  }

  /** Ensures that the children are sorted in z-order sequence. */
  private void ensureSorted() {
    if (needsSorting) {
      Collections.sort(children, Comparator.comparing(Figure::getLayer));
      needsSorting = false;
    }
  }

  @Override
  public QuadTreeDrawing clone() {
    QuadTreeDrawing that = (QuadTreeDrawing) super.clone();
    that.quadTree = new QuadTree<>();
    for (Figure f : getChildren()) {
      quadTree.add(f, f.getDrawingArea());
    }
    return that;
  }

  @Override
  protected EventHandler createEventHandler() {
    return new QuadTreeEventHandler();
  }

  @Override
  public Figure findFigure(Point2D.Double p, double scaleDenominator) {
    double tolerance = 10 / 2 / scaleDenominator;
    Rectangle2D.Double rect =
        new Rectangle2D.Double(p.x - tolerance, p.y - tolerance, 2 * tolerance, 2 * tolerance);
    for (Figure figure : findFigures(rect)) {
      if (figure.isVisible() && figure.contains(p, scaleDenominator)) {
        return figure;
      }
    }
    return null;
  }

  @Override
  public Figure findFigureBehind(Point2D.Double p, double scaleDenominator, Figure behindFigure) {
    double tolerance = 10 / 2 / scaleDenominator;
    Rectangle2D.Double rect =
        new Rectangle2D.Double(p.x - tolerance, p.y - tolerance, 2 * tolerance, 2 * tolerance);
    boolean check = false;
    for (Figure figure : findFigures(rect)) {
      if (check && figure.isVisible() && figure.contains(p, scaleDenominator)) {
        return figure;
      } else if (figure == behindFigure) {
        check = true;
      }
    }
    return null;
  }

  /** Handles all figure events fired by Figures contained in the Drawing. */
  protected class QuadTreeEventHandler extends AbstractDrawing.EventHandler {

    private static final long serialVersionUID = 1L;

    @Override
    public void figureChanged(FigureEvent e) {
      if (!isChanging()) {
        quadTree.remove(e.getFigure());
        quadTree.add(e.getFigure(), e.getFigure().getDrawingArea());
        needsSorting = true;
        invalidate();
        fireDrawingChanged(e.getInvalidatedArea());
      }
    }
  }

}
