package org.jhotdraw.draw.figure;

import static org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE;
import static org.jhotdraw.draw.AttributeKeys.TEXT;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.geom.Geom;

public class TextFigureShape extends AbstractAttributedDecoratedFigure {
    protected Point2D.Double origin = new Point2D.Double();
    // cache of the TextFigure's layout
    protected transient TextLayout textLayout;

    // SHAPE AND BOUNDS
    @Override
    public void transform(AffineTransform tx) {
        tx.transform(origin, origin);
    }

    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        origin = new Point2D.Double(anchor.x, anchor.y);
    }

    @Override
    public boolean figureContains(Point2D.Double p) {
        if (getBounds().contains(p)) {
            return true;
        }
        return false;
    }

    protected TextLayout getTextLayout() {
        if (textLayout == null) {
            String text = getText();
            if (text == null || text.length() == 0) {
                text = " ";
            }
            FontRenderContext frc = getFontRenderContext();
            HashMap<TextAttribute, Object> textAttributes = new HashMap<>();
            textAttributes.put(TextAttribute.FONT, getFont());
            if (attr().get(FONT_UNDERLINE)) {
                textAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            }
            textLayout = new TextLayout(text, textAttributes, frc);
        }
        return textLayout;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        TextLayout layout = getTextLayout();
        Rectangle2D.Double r =
                new Rectangle2D.Double(
                        origin.x, origin.y, layout.getAdvance(), layout.getAscent() + layout.getDescent());
        return r;
    }

    public double getBaseline() {
        TextLayout layout = getTextLayout();
        return origin.y + layout.getAscent() - getBounds().y;
    }

    /** Gets the drawing area without taking the decorator into account. */
    @Override
    protected Rectangle2D.Double getFigureDrawingArea() {
        if (getText() == null) {
            return getBounds();
        } else {
            TextLayout layout = getTextLayout();
            Rectangle2D.Double r =
                    new Rectangle2D.Double(origin.x, origin.y, layout.getAdvance(), layout.getAscent());
            Rectangle2D lBounds = layout.getBounds();
            if (!lBounds.isEmpty() && !Double.isNaN(lBounds.getX())) {
                r.add(
                        new Rectangle2D.Double(
                                lBounds.getX() + origin.x,
                                (lBounds.getY() + origin.y + layout.getAscent()),
                                lBounds.getWidth(),
                                lBounds.getHeight()));
            }
            // grow by two pixels to take antialiasing into account
            Geom.grow(r, 2d, 2d);
            return r;
        }
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        Point2D.Double p = (Point2D.Double) geometry;
        origin.x = p.x;
        origin.y = p.y;
    }

    @Override
    public Object getTransformRestoreData() {
        return origin.clone();
    }

    /** Gets the text shown by the text figure. */
    public String getText() {
        return attr().get(TEXT);
    }

    public Font getFont() {
        return AttributeKeys.getFont(this);
    }
}

