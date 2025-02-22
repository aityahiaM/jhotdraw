/*
 * @(#)OpenApplicationAction.java
 *
 * Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.app.action.app;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jhotdraw.action.AbstractApplicationAction;
import org.jhotdraw.api.app.Application;

/**
 * Handles an open application request from Mac OS X (this action does nothing).
 *
 * <p>This action is called when {@code DefaultOSXApplication} receives an Open Application event
 * from the Mac OS X Finder or another Mac OS X application.
 *
 * <p>This action is automatically created by {@code DefaultOSXApplication} and put into the {@code
 * ApplicationModel} before {@link org.jhotdraw.app.ApplicationModel#initApplication} is called.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class OpenApplicationAction extends AbstractApplicationAction {

  private static final long serialVersionUID = 1L;
  public static final String ID = "application.openApplication";

  /** Creates a new instance. */
  public OpenApplicationAction(Application app) {
    super(app);
    putValue(Action.NAME, "OSX Open Application");
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
