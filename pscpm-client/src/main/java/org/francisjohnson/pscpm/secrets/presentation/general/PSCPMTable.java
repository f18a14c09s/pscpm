/* 
 * Copyright (C) 2018 Francis Johnson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.Serializable;

import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class PSCPMTable<BeanClass extends Serializable> extends JTable {

    private final Logger _log = Logger.getLogger(getClass().getName());

    private Logger getLog() {
        return _log;
    }

    private final class MyMouseListenerImpl implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                boolean buttonPressed = pressButtonIfFound(e);
                if (!buttonPressed) {
                    clickHyperlinkIfFound(e);
                }
            }
        }

        private boolean pressButtonIfFound(MouseEvent e) {
            getLog().info(getClass().getName()
                    + ".  Primary mouse button clicked.");
            getLog().info("\tSource: "
                    + (e.getSource() == null ? null : "Class: "
                    + e.getSource().getClass().getName()));
            getLog().info("\tComponent: "
                    + (e.getComponent() == null ? null
                    : "Class: " + e.getComponent().getClass().getName()));
            AbstractButton uiButton
                    = (AbstractButton) (e.getSource() instanceof AbstractButton
                    ? e.getSource()
                    : e.getComponent() instanceof AbstractButton
                    ? e.getComponent() : null);
            if (uiButton != null) {
                getLog().severe("\tA UI button was listed.  Firing its action if there is one.");
                //                AbstractButton button = (AbstractButton)e.getSource();
                //                ActionListener[] actionListeners = button.getActionListeners();
                ActionListener[] actionListeners
                        = uiButton.getActionListeners();
                if (actionListeners != null) {
                    ActionEvent evt
                            = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED,
                                    //                    button.getActionCommand());
                                    uiButton.getActionCommand());
                    boolean retval = false;
                    for (ActionListener lsnr : actionListeners) {
                        lsnr.actionPerformed(evt);
                        retval = true;
                    }
                    return retval;
                }
            }
            getLog().severe("\tA UI button was listed as neither the source nor the component.");
            return false;
        }

        private boolean clickHyperlinkIfFound(MouseEvent e) {
            getLog().info(getClass().getName()
                    + ".  Primary mouse button clicked.");
            getLog().info("\tSource: "
                    + (e.getSource() == null ? null : "Class: "
                    + e.getSource().getClass().getName()));
            getLog().info("\tComponent: "
                    + (e.getComponent() == null ? null
                    : "Class: " + e.getComponent().getClass().getName()));
            JTable table
                    = (JTable) (e.getSource() instanceof JTable ? e.getSource()
                    : e.getComponent() instanceof JTable
                    ? e.getComponent() : null);
            Component location
                    = table == null ? null : table.findComponentAt(e.getLocationOnScreen());
            getLog().info("\tLocation: "
                    + (location == null ? null : "Class: "
                            + location.getClass().getName()));
            Hyperlink hyperlink
                    = (Hyperlink) (e.getSource() instanceof Hyperlink
                    ? e.getSource()
                    : e.getComponent() instanceof Hyperlink
                    ? e.getComponent()
                    : location instanceof Hyperlink ? location : null);
            if (hyperlink != null) {
                getLog().severe("\tA custom hyperlink was listed.  Firing its action if there is one.");
                MouseListener[] actionListeners
                        = hyperlink.getMouseListeners();
                if (actionListeners != null) {
                    for (MouseListener lsnr : actionListeners) {
                        lsnr.mouseClicked(e);
                    }
                }
            }
            getLog().severe("\tA custom hyperlink was listed as neither the source nor the component.");
            return false;
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    public PSCPMTable() {
        super();
    }

    public PSCPMTable(PSCPMTableModel<BeanClass> model,
            Map<Class<?>, TableCellRenderer> renderers,
            Map<Class<?>, TableCellEditor> editors) {
        super();
        super.setModel(model);
        if (super.getColumnModel() != null) {
            for (int i = 0; i < super.getColumnCount(); i++) {
                TableColumn col = super.getColumnModel().getColumn(i);
                PSCPMTableColumnMetadata<BeanClass, ?> colMeta
                        = model.getColumns().get(i);
                if (colMeta.getFixedWidth() != null) {
                    col.setResizable(false);
                    col.setMinWidth(colMeta.getFixedWidth().intValue());
                    col.setPreferredWidth(colMeta.getFixedWidth().intValue());
                    col.setMaxWidth(colMeta.getFixedWidth().intValue());
                } else {
                    if (colMeta.getMinWidth() != null) {
                        col.setMinWidth(colMeta.getMinWidth().intValue());
                    }
                    if (colMeta.getPreferredWidth() != null) {
                        col.setPreferredWidth(colMeta.getPreferredWidth().intValue());
                    }
                    if (colMeta.getMaxWidth() != null) {
                        col.setMaxWidth(colMeta.getMaxWidth().intValue());
                    }
                }
                if (colMeta.getRenderer() != null) {
                    col.setCellRenderer(colMeta.getRenderer());
                } else if (renderers != null
                        && colMeta.getPropertyClass() != null
                        && renderers.containsKey(colMeta.getPropertyClass())) {
                    col.setCellRenderer(renderers.get(colMeta.getPropertyClass()));
                }
                if (colMeta.getEditor() != null) {
                    col.setCellEditor(colMeta.getEditor());
                } else if (editors != null
                        && colMeta.getPropertyClass() != null
                        && editors.containsKey(colMeta.getPropertyClass())) {
                    col.setCellEditor(editors.get(colMeta.getPropertyClass()));
                }
            }
        }
        super.addMouseListener(new MyMouseListenerImpl());
    }
}
