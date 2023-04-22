/*
 * @(#)FloatingTextField.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.text;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.event.FigureListenerAdapter;
import org.jhotdraw.draw.figure.TextHolderFigure;

/**
 * A <em>floating text field</em> that is used to edit a {@link TextHolderFigure}.
 *
 * <p>{@code FloatingTextField} requires a two step initialization: In a first step the overlay is
 * created and in a second step it can be positioned.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure} interface define together
 * the contracts of a smaller framework inside of the JHotDraw framework for structured drawing
 * editors.<br>
 * Contract: {@link TextHolderFigure}, {@link org.jhotdraw.draw.tool.TextCreationTool}, {@link
 * org.jhotdraw.draw.tool.TextAreaCreationTool}, {@link org.jhotdraw.draw.tool.TextEditingTool},
 * {@link org.jhotdraw.draw.tool.TextAreaEditingTool}, {@link FloatingText}, {@link
 * FloatingText}. <hr>
 *
 * @author Werner Randelshofer
 * @version $Id: FloatingTextField.java -1 $
 */
public class FloatingText {

    private TextHolderFigure editedFigure;
    private JTextField textField;
    private DrawingView view;
    private FigureListener figureHandlerField =
            new FigureListenerAdapter() {
                @Override
                public void attributeChanged(FigureEvent e) {
                    updateWidgetField();
                }
            };

    public FloatingText() {

        textField = new JTextField(20);
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        editScrollContainer =
                new JScrollPane(
                        textArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editScrollContainer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        editScrollContainer.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void requestFocusField() {
        textField.requestFocus();
    }

    /** Creates the overlay for the given Container using a specific font. */
    public void createOverlayField(DrawingView view, TextHolderFigure figure) {
        view.getComponent().add(textField, 0);
        textField.setText(figure.getText());
        textField.setColumns(figure.getTextColumns());
        textField.selectAll();
        textField.setVisible(true);
        editedFigure = figure;
        editedFigure.addFigureListener(figureHandlerField);
        this.view = view;
        updateWidgetField();
    }

    protected void updateWidgetField() {
        Font font = editedFigure.getFont();
        font =
                font.deriveFont(
                        font.getStyle(), (float) (editedFigure.getFontSize() * view.getScaleFactor()));
        textField.setFont(font);
        textField.setForeground(editedFigure.getTextColor());
        textField.setBackground(editedFigure.getFillColor());
        Rectangle2D.Double fDrawBounds = editedFigure.getBounds();
        Point2D.Double fDrawLoc = new Point2D.Double(fDrawBounds.getX(), fDrawBounds.getY());
        if (editedFigure.attr().get(TRANSFORM) != null) {
            editedFigure.attr().get(TRANSFORM).transform(fDrawLoc, fDrawLoc);
        }
        Point fViewLoc = view.drawingToView(fDrawLoc);
        Rectangle fViewBounds = view.drawingToView(fDrawBounds);
        fViewBounds.x = fViewLoc.x;
        fViewBounds.y = fViewLoc.y;
        Dimension tfDim = textField.getPreferredSize();
        Insets tfInsets = textField.getInsets();
        float fontBaseline = textField.getGraphics().getFontMetrics(font).getMaxAscent();
        double fBaseline = editedFigure.getBaseline() * view.getScaleFactor();
        textField.setBounds(
                fViewBounds.x - tfInsets.left,
                fViewBounds.y - tfInsets.top - (int) (fontBaseline - fBaseline),
                Math.max(fViewBounds.width + tfInsets.left + tfInsets.right, tfDim.width),
                Math.max(fViewBounds.height + tfInsets.top + tfInsets.bottom, tfDim.height));
    }

    public Insets getInsetsField() {
        return textField.getInsets();
    }

    /** Adds an action listener */
    public void addActionListenerField(ActionListener listener) {
        textField.addActionListener(listener);
    }

    /** Remove an action listener */
    public void removeActionListener(ActionListener listener) {
        textField.removeActionListener(listener);
    }

    /** Gets the text contents of the overlay. */
    public String getTextField() {
        return textField.getText();
    }

    /** Gets the preferred size of the overlay. */
    public Dimension getPreferredSizeField(int cols) {
        textField.setColumns(cols);
        return textField.getPreferredSize();
    }

    /** Removes the overlay. */
    public void endOverlayField() {
        view.getComponent().requestFocus();
        if (textField != null) {
            textField.setVisible(false);
            view.getComponent().remove(textField);
            Rectangle bounds = textField.getBounds();
            view.getComponent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if (editedFigure != null) {
            editedFigure.removeFigureListener(figureHandlerField);
            editedFigure = null;
        }
    }

    /** A scroll pane to allow for vertical scrolling while editing */
    protected JScrollPane editScrollContainer;
    /** The actual editor */
    protected JTextArea textArea;

    private FigureListener figureHandlerArea =
            new FigureListenerAdapter() {
                @Override
                public void attributeChanged(FigureEvent e) {
                    updateWidgetArea();
                }
            };

    /**
     * Creates the overlay within the given container.
     *
     * @param view the DrawingView
     */
    public void createOverlayArea(DrawingView view) {
        createOverlayArea(view, null);
    }

    public void requestFocusArea() {
        textArea.requestFocus();
    }

    /**
     * Creates the overlay for the given Container using a specific font.
     *
     * @param view the DrawingView
     * @param figure the figure holding the text
     */
    public void createOverlayArea(DrawingView view, TextHolderFigure figure) {
        view.getComponent().add(editScrollContainer, 0);
        editedFigure = figure;
        this.view = view;
        if (editedFigure != null) {
            editedFigure.addFigureListener(figureHandlerArea);
            updateWidgetArea();
        }
    }

    protected void updateWidgetArea() {
        Font f = editedFigure.getFont();
        // FIXME - Should scale with fractional value!
        f = f.deriveFont(f.getStyle(), (float) (editedFigure.getFontSize() * view.getScaleFactor()));
        textArea.setFont(f);
        textArea.setForeground(editedFigure.getTextColor());
        textArea.setBackground(editedFigure.getFillColor());
        //        textArea.setBounds(getFieldBounds(editedFigure));
    }

    /**
     * Positions and sizes the overlay.
     *
     * @param r the bounding Rectangle2D.Double for the overlay
     * @param text the text to edit
     */
    public void setBoundsArea(Rectangle2D.Double r, String text) {
        textArea.setText(text);
        editScrollContainer.setBounds(view.drawingToView(r));
        editScrollContainer.setVisible(true);
        textArea.setCaretPosition(0);
        textArea.requestFocus();
    }

    /**
     * Gets the text contents of the overlay.
     *
     * @return The text value
     */
    public String getTextArea() {
        return textArea.getText();
    }

    /**
     * Gets the preferred size of the overlay.
     *
     * @param cols Description of the Parameter
     * @return The preferredSize value
     */
    public Dimension getPreferredSizeArea(int cols) {
        return new Dimension(textArea.getWidth(), textArea.getHeight());
    }

    /** Removes the overlay. */
    public void endOverlayArea() {
        view.getComponent().requestFocus();
        if (editScrollContainer != null) {
            editScrollContainer.setVisible(false);
            view.getComponent().remove(editScrollContainer);
            Rectangle bounds = editScrollContainer.getBounds();
            view.getComponent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if (editedFigure != null) {
            editedFigure.removeFigureListener(figureHandlerArea);
            editedFigure = null;
        }
    }
}
