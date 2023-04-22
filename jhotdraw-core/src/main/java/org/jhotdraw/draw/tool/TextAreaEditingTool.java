/*
 * @(#)TextAreaEditingTool.java
 *
 * Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.*;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * A tool to edit existing figures that implement the TextHolderFigure interface, such as
 * TextAreaFigure.
 *
 * <p>To edit an existing text figure using the TextAreaEditingTool, the user does the following
 * mouse gesture on a DrawingView:
 *
 * <ol>
 *   <li>Press the mouse button over a Figure on the DrawingView.
 * </ol>
 *
 * <p>The TextAreaEditingTool then uses Figure.findFigureInside to find a Figure that implements the
 * TextHolderFigure interface and that is editable. Then it overlays a text area over the drawing
 * where the user can enter the text for the Figure. <hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure} interface define together
 * the contracts of a smaller framework inside of the JHotDraw framework for structured drawing
 * editors.<br>
 * Contract: {@link TextHolderFigure}, {@link TextCreationTool}, {@link TextAreaCreationTool},
 * {@link TextEditingTool}, {@link TextAreaEditingTool}, {@link FloatingText}, {@link
 * FloatingTextArea}. <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @see TextHolderFigure
 * @see FloatingText
 */
public class TextAreaEditingTool extends AbstractTool implements ActionListener {

  private static final long serialVersionUID = 1L;
  private FloatingText text;
  private TextHolderFigure typingTarget;

  /** Creates a new instance. */
  public TextAreaEditingTool(TextHolderFigure typingTarget) {
    this.typingTarget = typingTarget;
  }

  @Override
  public void deactivate(DrawingEditor editor) {
    endEdit();
    super.deactivate(editor);
  }

  /**
   * Creates a new figure at the mouse location. If editing is in progress, this finishes editing.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    if (typingTarget != null) {
      beginEdit(typingTarget);
      updateCursor(getView(), e.getPoint());
    }
  }

  @Override
  public void draw(Graphics2D g) {}

  protected void beginEdit(TextHolderFigure textHolder) {
    if (text == null) {
      text = new FloatingText();
      // textArea.addActionListener(this);
    }
    if (textHolder != typingTarget && typingTarget != null) {
      endEdit();
    }
    text.createOverlayArea(getView(), textHolder);
    text.setBoundsArea(getFieldBounds(textHolder), textHolder.getText());
    text.requestFocusArea();
    typingTarget = textHolder;
  }

  private Rectangle2D.Double getFieldBounds(TextHolderFigure figure) {
    Rectangle2D.Double r = figure.getDrawingArea();
    Insets2D.Double insets = figure.getInsets();
    insets.subtractTo(r);
    // FIXME - Find a way to determine the parameters for grow.
    // r.grow(1,2);
    // r.width += 16;
    r.x -= 1;
    r.y -= 2;
    r.width += 18;
    r.height += 4;
    return r;
  }

  protected void endEdit() {
    if (typingTarget != null) {
      typingTarget.willChange();
      final TextHolderFigure editedFigure = typingTarget;
      final String oldText = typingTarget.getText();
      final String newText = text.getTextArea();
      typingTarget.willChange();
      if (newText.length() > 0) {
        typingTarget.setText(newText);
      } else {
        typingTarget.setText("");
      }
      typingTarget.changed();
      UndoableEdit edit =
              new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getPresentationName() {
                  ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                  return labels.getString("attribute.text.text");
                }

                @Override
                public void undo() {
                  super.undo();
                  editedFigure.willChange();
                  editedFigure.setText(oldText);
                  editedFigure.changed();
                }

                @Override
                public void redo() {
                  super.redo();
                  editedFigure.willChange();
                  editedFigure.setText(newText);
                  editedFigure.changed();
                }
              };
      getDrawing().fireUndoableEditHappened(edit);
      typingTarget.changed();
      typingTarget = null;
      text.endOverlayArea();
    }
    //         view().checkDamage();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    endEdit();
    fireToolDone();
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
