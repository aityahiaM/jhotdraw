/*
 * @(#)TextFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.FontSizeHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.MoveHandle;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.draw.tool.TextEditingTool;
import org.jhotdraw.draw.tool.Tool;
import org.jhotdraw.geom.Dimension2DDouble;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.util.*;

/**
 * A {@code TextHolderFigure} which holds a single line of text.
 *
 * <p>A DrawingEditor should provide the {@link org.jhotdraw.draw.tool.TextCreationTool} to create a
 * {@code TextFigure}.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TextFigure extends TextFigureConnecting implements TextHolderFigure {

  private static final long serialVersionUID = 1L;
  protected boolean editable = true;

  /** Creates a new instance. */
  public TextFigure() {
    this(
            ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels")
                    .getString("TextFigure.defaultText"));
  }

  public TextFigure(String text) {
    setText(text);
  }

  @Override
  public Dimension2DDouble getPreferredSize() {
    Rectangle2D.Double b = getBounds();
    return new Dimension2DDouble(b.width, b.height);
  }

  // ATTRIBUTES

  /**
   * Sets the text shown by the text figure. This is a convenience method for calling {@code
   * set(TEXT,newText)}.
   */
  @Override
  public void setText(String newText) {
    attr().set(TEXT, newText);
  }

  @Override
  public int getTextColumns() {
    // return (getText() == null) ? 4 : Math.max(getText().length(), 4);
    return 4;
  }

  /** Gets the number of characters used to expand tabs. */
  @Override
  public int getTabSize() {
    return 8;
  }

  @Override
  public TextHolderFigure getLabelFor() {
    return this;
  }

  @Override
  public Insets2D.Double getInsets() {
    return new Insets2D.Double();
  }

  @Override
  public Color getTextColor() {
    return attr().get(TEXT_COLOR);
  }

  @Override
  public Color getFillColor() {
    return attr().get(FILL_COLOR);
  }

  @Override
  public void setFontSize(float size) {
    attr().set(FONT_SIZE, new Double(size));
  }

  @Override
  public float getFontSize() {
    return attr().get(FONT_SIZE).floatValue();
  }

  // EDITING
  @Override
  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean b) {
    this.editable = b;
  }

  @Override
  public Collection<Handle> createHandles(int detailLevel) {
    LinkedList<Handle> handles = new LinkedList<>();
    switch (detailLevel) {
      case -1:
        handles.add(new BoundsOutlineHandle(this, false, true));
        break;
      case 0:
        handles.add(new BoundsOutlineHandle(this));
        handles.add(new MoveHandle(this, RelativeLocator.northWest()));
        handles.add(new MoveHandle(this, RelativeLocator.northEast()));
        handles.add(new MoveHandle(this, RelativeLocator.southWest()));
        handles.add(new MoveHandle(this, RelativeLocator.southEast()));
        handles.add(new FontSizeHandle(this));
        break;
    }
    return handles;
  }

  /**
   * Returns a specialized tool for the given coordinate.
   *
   * <p>Returns null, if no specialized tool is available.
   */
  @Override
  public Tool getTool(Point2D.Double p) {
    if (isEditable() && contains(p)) {
      TextEditingTool t = new TextEditingTool(this);
      return t;
    }
    return null;
  }
}
