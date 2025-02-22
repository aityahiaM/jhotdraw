/* @(#)package-info.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
/**
 * Defines a framework for structured drawing editors and provides default implementations.
 *
 * <h3>Package Contents</h3>
 *
 * <p>All key contracts of the framework are defined by Java interfaces. For each interface exists
 * an abstract class, which implements the event handling contract of the interface. And finally,
 * there is at least one default implementation of each interface.
 *
 * <p>The key interfaces for the representation of a drawing are:
 *
 * <ul>
 *   <li>{@link org.jhotdraw.draw.Drawing}
 *   <li>{@link org.jhotdraw.draw.Figure}
 * </ul>
 *
 * <p>The key interface for displaying a drawing on screen is:
 *
 * <ul>
 *   <li>{@link org.jhotdraw.draw.DrawingView}
 * </ul>
 *
 * <p>The key interfaces for editing a drawing are:
 *
 * <ul>
 *   <li>{@link org.jhotdraw.draw.DrawingEditor}
 *   <li>{@link org.jhotdraw.draw.tool.Tool} (in sub-package "tool")
 *   <li>{@link org.jhotdraw.draw.handle.Handle} (in sub-package "handle")
 * </ul>
 *
 * <p>
 */
package org.jhotdraw.draw;
