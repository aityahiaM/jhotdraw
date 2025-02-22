/*
 * @(#)PaletteColorChooserMainPanel.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette.colorchooser;

import java.awt.*;
import javax.swing.*;
import javax.swing.colorchooser.*;
import javax.swing.plaf.TabbedPaneUI;
import org.jhotdraw.gui.plaf.palette.PaletteTabbedPaneUI;

/**
 * The main panel of the color chooser UI.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class PaletteColorChooserMainPanel extends javax.swing.JPanel {

  private static final long serialVersionUID = 1L;
  /**
   * We store here the name of the last selected chooser. When the ColorChooserMainPanel is
   * recreated multiple times in the same applicatin, the application 'remembers' which panel the
   * user had opened before.
   */
  private static String lastSelectedChooserName = null;

  /** Creates new form. */
  public PaletteColorChooserMainPanel() {
    initComponents();
    setOpaque(false);
    tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    tabbedPane.setUI((TabbedPaneUI) PaletteTabbedPaneUI.createUI(tabbedPane));
    tabbedPane.putClientProperty("Palette.TabbedPane.paintContentBorder", false);
  }

  public void setPreviewPanel(JComponent c) {
    // there is no preview panel
  }

  public void addColorChooserPanel(final AbstractColorChooserPanel ccp) {
    final String displayName = ccp.getDisplayName();
    if (displayName == null) {
      // Return if we haven't initialized yet
      return;
    }
    JPanel centerView = new JPanel(new BorderLayout());
    centerView.add(ccp);
    tabbedPane.add(centerView, displayName);
  }

  public void removeAllColorChooserPanels() {
    tabbedPane.removeAll();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    toolBarButtonGroup = new javax.swing.ButtonGroup();
    tabbedPane = new javax.swing.JTabbedPane();
    setLayout(new java.awt.BorderLayout());
    tabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
    add(tabbedPane, java.awt.BorderLayout.CENTER);
  } // </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.ButtonGroup toolBarButtonGroup;
  // End of variables declaration//GEN-END:variables
}
