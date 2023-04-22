package org.jhotdraw.draw.figure;

import java.awt.geom.Point2D;

public class TextFigureConnecting extends TextFigureShape {
    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    @Override
    public void invalidate() {
        super.invalidate();
        textLayout = null;
    }

    @Override
    protected void validate() {
        super.validate();
        textLayout = null;
    }

    @Override
    public TextFigure clone() {
        TextFigure that = (TextFigure) super.clone();
        that.origin = (Point2D.Double) this.origin.clone();
        that.textLayout = null;
        return that;
    }

    public boolean isTextOverflow() {
        return false;
    }
}

