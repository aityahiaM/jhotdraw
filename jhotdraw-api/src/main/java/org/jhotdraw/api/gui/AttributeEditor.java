/*
 * @(#)AttributeEditor.java
 *
 * Copyright (c) 2007-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.api.gui;

import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Interface for a field or any other kind of editor which can be used to edit an attribute of the
 * selected {@code Figure}s in a {@code DrawingView}.
 *
 * <p>The {@code AttributeEditor} can be attached to a single {@code DrawingView} or to the whole
 * {@code DrawingEditor} by means of an {@code AttributeFieldHandler}.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface AttributeEditor<T> {

  public static final String ATTRIBUTE_VALUE_PROPERTY = "attributeValue";
  public static final String MULTIPLE_VALUES_PROPERTY = "multipleValues";

  /** Returns the JComponent of the attribute field. */
  public JComponent getComponent();

  /**
   * Sets the attribute value. This is a bound property.
   *
   * @param newValue
   */
  public void setAttributeValue(T newValue);

  /** Gets the attribute value. */
  public T getAttributeValue();

  /**
   * This method is called, if the figures of the attribute field have multiple values.
   *
   * @param newValue
   */
  public void setMultipleValues(boolean newValue);

  /** This method returns the value of the multipleValues property. */
  public boolean isMultipleValues();

  /** Returns true if the field is currently adjusting the value. */
  public boolean getValueIsAdjusting();

  /**
   * Adds a property change listener.
   *
   * @param l
   */
  public void addPropertyChangeListener(PropertyChangeListener l);

  /**
   * Removes a property change listener.
   *
   * @param l
   */
  public void removePropertyChangeListener(PropertyChangeListener l);
}
