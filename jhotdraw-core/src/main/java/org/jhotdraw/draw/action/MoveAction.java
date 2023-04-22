package org.jhotdraw.draw.action;

import java.awt.geom.*;
import java.util.HashSet;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.TransformEdit;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.undo.CompositeEdit;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Moves the selected figures by one unit.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class MoveAction extends AbstractSelectedAction {

  private static final long serialVersionUID = 1L;
  private static int dx;
  private static int dy;

  /** Creates a new instance. */
  public MoveAction(DrawingEditor editor, int dx, int dy) {
    super(editor);
    this.dx = dx;
    this.dy = dy;
    updateEnabledState();
  }

  /** Moves the selected figures by dx and dy. */
  protected void moveSelectedFigures(int dx, int dy) {
    CompositeEdit edit;
    AffineTransform tx = new AffineTransform();
    tx.translate(dx, dy);
    HashSet<Figure> transformedFigures = new HashSet<>();
    for (Figure f : getView().getSelectedFigures()) {
      if (f.isTransformable()) {
        transformedFigures.add(f);
        f.willChange();
        f.transform(tx);
        f.changed();
      }
    }
    fireUndoableEditHappened(new TransformEdit(transformedFigures, tx));
  }

  public static class MoveDirection extends MoveAction {

    private static final long serialVersionUID = 1L;

    public MoveDirection(DrawingEditor editor, int dx, int dy, String id) {
      super(editor, dx, dy);
      ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
      labels.configureAction(this, id);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
      moveSelectedFigures(dx, dy);
    }
  }

  public static class East extends MoveDirection {

    public static final String ID = "edit.moveEast";

    public East(DrawingEditor editor) {
      super(editor, 1, 0, ID);
    }
  }

  public static class West extends MoveDirection {

    public static final String ID = "edit.moveWest";

    public West(DrawingEditor editor) {
      super(editor, -1, 0, ID);
    }
  }

  public static class North extends MoveDirection {

    public static final String ID = "edit.moveNorth";

    public North(DrawingEditor editor) {
      super(editor, 0, -1, ID);
    }
  }

  public static class South extends MoveDirection {

    public static final String ID = "edit.moveSouth";

    public South(DrawingEditor editor) {
      super(editor, 0, 1, ID);
    }
  }
}
