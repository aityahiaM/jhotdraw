/*
 * @(#)OutputFormat.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.io;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;

/**
 * An <em>output format</em> implements a strategy for writing a {@link Drawing} using a specific
 * format into an {@code OutputStream}, an {@code URI} or a {@code Transferable}.
 *
 * <p>Typically a format can be identified by a Mime type or by a file extension. To identify the
 * format used by a file, an appropriate {@code FileFilter} for a javax.swing.JFileChooser component
 * can be requested from {@code OutputFormat}.
 *
 * <p>This interface intentionally contains many identical operations like InputFormat to make it
 * easy, to write classes that implement both interfaces.
 *
 * <p><hr> <b>Design Patterns</b>
 *
 * <p><em>Strategy</em><br>
 * {@code OutputFormat} encapsulates a strategy for writing drawings to output streams.<br>
 * Strategy: {@link OutputFormat}; Context: {@link Drawing}. <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface OutputFormat {

  /**
   * Return a FileFilter that can be used to identify files which can be stored with this output
   * format. Typically, each output format has its own recognizable file extension.
   *
   * @return FileFilter to be used with a javax.swing.JFileChooser
   */
  public javax.swing.filechooser.FileFilter getFileFilter();

  /**
   * Returns the file extension for the output format. The file extension should be appended to a
   * file name when storing a Drawing with the specified file format.
   */
  public String getFileExtension();

  /**
   * Writes a Drawing into an URI.
   *
   * @param uri The uri.
   * @param drawing The drawing.
   */
  public void write(URI uri, Drawing drawing) throws IOException;

  /**
   * Writes a Drawing into an output stream.
   *
   * @param out The output stream.
   * @param drawing The drawing.
   */
  public void write(OutputStream out, Drawing drawing) throws IOException;

  /**
   * Creates a Transferable for the specified list of Figures.
   *
   * @param drawing The drawing.
   * @param figures A list of figures of the drawing.
   * @param scaleFactor The factor to be used, when the Transferable creates an image with a fixed
   *     size from the figures.
   * @return The Transferable.
   */
  public Transferable createTransferable(Drawing drawing, List<Figure> figures, double scaleFactor)
      throws IOException;
}
