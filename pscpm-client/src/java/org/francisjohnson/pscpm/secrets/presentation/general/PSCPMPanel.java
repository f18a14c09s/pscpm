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

import java.awt.Dimension;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;
import org.francisjohnson.pscpm.secrets.presentation.events.NavigationListener;


public class PSCPMPanel extends JPanel {
    private PSCPMViewId viewId;
    private transient SecretsFacade session;
    private transient NavigationListener navigator;

    public void setViewId(PSCPMViewId viewId) {
        this.viewId = viewId;
    }

    public PSCPMViewId getViewId() {
        return viewId;
    }

    public void setNavigator(NavigationListener navigator) {
        this.navigator = navigator;
    }

    public NavigationListener getNavigator() {
        return navigator;
    }

    public void setSession(SecretsFacade session) {
        this.session = session;
    }

    public SecretsFacade getSession() {
        return session;
    }

    private static void errorDriver(Component parentComponent,
                                    String dialogTitle,
                                    String userFriendlyMessage,
                                    String additionalInfo, Exception e) {
        // Phase 1
        additionalInfo =
                additionalInfo == null || additionalInfo.trim().isEmpty() ?
                "" : null;
        dialogTitle =
                dialogTitle == null || dialogTitle.trim().isEmpty() ? "Unexpected Error" :
                dialogTitle;
        // Phase 2
        userFriendlyMessage =
                userFriendlyMessage == null || userFriendlyMessage.trim().isEmpty() ?
                "An unexpected error occurred." +
                (additionalInfo == null && e == null ?
                 "No further information available" :
                 "See StdError (in the console) for more information.") :
                userFriendlyMessage;
        // Phase 3
        String logMessage =
            "User-friendly error message: " + userFriendlyMessage +
            (additionalInfo == null ?
             e == null ? "No further information available." : "" :
             "  Additional information: " + additionalInfo);
        // Phase 4
        (e == null ? new Exception(logMessage) :
         new Exception(logMessage, e)).printStackTrace();
        JOptionPane.showMessageDialog(parentComponent, userFriendlyMessage,
                                      dialogTitle, JOptionPane.ERROR_MESSAGE);
    }

    public static void handleException(Component parentComponent,
                                       String dialogTitle,
                                       String userFriendlyMessage,
                                       String additionalInfo, Exception e) {
        errorDriver(parentComponent, dialogTitle, userFriendlyMessage,
                    additionalInfo, e);
    }

    private void errorDriver(String dialogTitle, String userFriendlyMessage,
                             String additionalInfo, Exception e) {
        errorDriver(this, dialogTitle, userFriendlyMessage, additionalInfo, e);
    }

    public void handleException(Exception e) {
        errorDriver(null, null, null, e);
    }

    public void handleException(String userFriendlyMessage, Exception e) {
        errorDriver(null, userFriendlyMessage, null, e);
    }

    public void handleException(String dialogTitle, String userFriendlyMessage,
                                Exception e) {
        errorDriver(dialogTitle, userFriendlyMessage, null, e);
    }

    public void handleException(String dialogTitle, String userFriendlyMessage,
                                String additionalInfo, Exception e) {
        errorDriver(dialogTitle, userFriendlyMessage, additionalInfo, e);
    }

    public void handleError(String dialogTitle, String userFriendlyMessage,
                            String additionalInfo) {
        errorDriver(dialogTitle, userFriendlyMessage, additionalInfo, null);
    }

    public void handleError(String dialogTitle, String userFriendlyMessage) {
        errorDriver(dialogTitle, userFriendlyMessage, null, null);
    }

    public void informUser(String dialogTitle, String userFriendlyMessage) {
        JOptionPane.showMessageDialog(this, userFriendlyMessage, dialogTitle,
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    public void debug(String message, String... subMessages) {
        System.out.println(message);
        if (subMessages != null) {
            for (String msg : subMessages) {
                System.out.println("\t" + msg);
            }
        }
    }

    public static int max(int x1, int... xn) {
        int retval = x1;
        if (xn != null) {
            for (int i = 0; i < xn.length;
                 retval = Math.max(retval, xn[i]), i++)
                ;
        }
        return retval;
    }

    public static void normalizeLabelWidths(List<JLabel> labels) {
        int maxWidth = 0;
        if (labels != null) {
            for (JLabel label : labels) {
                maxWidth =
                        max(maxWidth, label.getWidth(), label.getPreferredSize().width,
                            label.getMinimumSize().width);
            }
            for (JLabel label : labels) {
                Dimension dim = null;
                dim = label.getSize();
                dim.width = maxWidth;
                label.setSize(dim);
                dim = label.getPreferredSize();
                dim.width = maxWidth;
                label.setPreferredSize(dim);
            }
        }
    }
}
