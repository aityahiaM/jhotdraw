/*
 * @(#)DnDTracker.java
 *
 * Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import java.awt.Container;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;

/**
 * This is a tracker which supports drag and drop of figures between drawing views and any other
 * component or application which support drag and drop.
 *
 * <p>DnDTracker can be used stand-alone or instead of {@code DragTracker} in the {@code
 * SelectionTool} or the {@code DelegationSelectionTool}.
 *
 * <p>To get a drag image using drag and drop, the drawing needs to provide an image output format.
 *
 * <p>Drag and Drop is about information moving, not images or objects. Its about moving a figure to
 * another application and that application understanding both its shape, color, attributes, and
 * everything about it - not necessarily how it looks.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DnDTracker extends AbstractTool implements DragTracker {

  private static final long serialVersionUID = 1L;
  protected Figure anchorFigure;
  /** The drag rectangle encompasses the bounds of all dragged figures. */
  protected Rectangle2D.Double dragRect;
  /**
   * The previousOrigin holds the origin of all dragged figures of the previous mouseDragged event.
   * This coordinate is constrained using the Constrainer of the DrawingView.
   */
  protected Point2D.Double previousOrigin;
  /** The anchorOrigin holds the origin of all dragged figures of the mousePressed event. */
  protected Point2D.Double anchorOrigin;
  /**
   * The previousPoint holds the location of the mouse of the previous mouseDragged event. This
   * coordinate is not constrained using the Constrainer of the DrawingView.
   */
  protected Point2D.Double previousPoint;
  /**
   * The anchorPoint holds the location of the mouse of the mousePressed event. This coordinate is
   * not constrained using the Constrainer of the DrawingView.
   */
  protected Point2D.Double anchorPoint;

  private boolean isDragging;

  public DnDTracker() {}

  public DnDTracker(Figure figure) {
    anchorFigure = figure;
  }

  @Override
  public void mouseMoved(MouseEvent evt) {
    updateCursor(editor.findView((Container) evt.getSource()), evt.getPoint());
  }

  @Override
  public void mousePressed(MouseEvent evt) {
    super.mousePressed(evt);
    DrawingView view = getView();
    if (evt.isShiftDown()) {
      view.setHandleDetailLevel(0);
      view.toggleSelection(anchorFigure);
      if (!view.isFigureSelected(anchorFigure)) {
        anchorFigure = null;
      }
    } else if (!view.isFigureSelected(anchorFigure)) {
      view.setHandleDetailLevel(0);
      view.clearSelection();
      view.addToSelection(anchorFigure);
    }
    if (!view.getSelectedFigures().isEmpty()) {
      dragRect = null;
      for (Figure f : view.getSelectedFigures()) {
        if (dragRect == null) {
          dragRect = f.getBounds();
        } else {
          dragRect.add(f.getBounds());
        }
      }
      anchorPoint = previousPoint = view.viewToDrawing(anchor);
      anchorOrigin = previousOrigin = new Point2D.Double(dragRect.x, dragRect.y);
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    DrawingView v = getView();
    Figure f = v.findFigure(e.getPoint());
    if (f != null) {
      if (!v.getSelectedFigures().contains(f)) {
        v.clearSelection();
        v.addToSelection(f);
      }
      v.getComponent()
          .getTransferHandler()
          .exportAsDrag(v.getComponent(), e, DnDConstants.ACTION_MOVE);
    }
    fireToolDone();
  }

  @Override
  public void mouseReleased(MouseEvent evt) {
    updateCursor(editor.findView((Container) evt.getSource()), evt.getPoint());
    fireToolDone();
  }

  @Override
  public void setDraggedFigure(Figure f) {
    anchorFigure = f;
  }
}
