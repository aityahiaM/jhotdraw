/*
 * @(#)FontChooserMain.java
 *
 * Copyright (c) 2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.font;

import javax.swing.*;
import org.jhotdraw.gui.JFontChooser;

/**
 * FontChooserMain.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FontChooserMain extends javax.swing.JPanel {

  private static final long serialVersionUID = 1L;

  /** Creates new form FontChooserMain */
  public FontChooserMain() {
    initComponents();
    add(new JFontChooser());
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable t) {
              // allow empty
            }
            JFrame f = new JFrame("FontChooser");
            f.add(new FontChooserMain());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setVisible(true);
          }
        });
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    setLayout(new java.awt.BorderLayout());
  } // </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
