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
package org.francisjohnson.pscpm.secrets.presentation.views;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.general.PSCPMPanel;


public class PSCPMMainJFrame extends JFrame {
    private transient SecretsFacade session;

    private BorderLayout layoutMain = new BorderLayout();
    private JMenuBar menuBar1 = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JLabel statusBar = new JLabel();

    public PSCPMMainJFrame(Component contentComponent) {
        try {
            jbInit(contentComponent);
        } catch (Exception e) {
            PSCPMPanel.handleException(this, "PSCPM Main UI Unavailable",
                                       "Unable to initialize the system's main UI.",
                                       null, e);
        }
    }

    private PSCPMMainJFrame() {
    }

    void jbInit(Component contentComponent) throws Exception {
        setJMenuBar(getMenuBar1());
        getContentPane().setLayout(getLayoutMain());
        setSize(new Dimension(1024, 768));
        setTitle("PSCPM");
        getMenuFile().setText("File");
        getMenuFileExit().setText("Exit");
        getMenuFileExit().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    fileExit_ActionPerformed(ae);
                }
            });
        getMenuHelp().setText("Help");
        getMenuHelpAbout().setText("About");
        getMenuHelpAbout().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    helpAbout_ActionPerformed(ae);
                }
            });
        getStatusBar().setText("");
        getMenuFile().add(getMenuFileExit());
        getMenuBar1().add(getMenuFile());
        getMenuHelp().add(getMenuHelpAbout());
        getMenuBar1().add(getMenuHelp());
        getContentPane().add(getStatusBar(), BorderLayout.SOUTH);
        getContentPane().add(contentComponent, BorderLayout.CENTER);
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, new PSCPMSystemInfoPanel(),
                                      "About", JOptionPane.PLAIN_MESSAGE);
    }

    void setSession(SecretsFacade session) {
        this.session = session;
    }

    SecretsFacade getSession() {
        return session;
    }

    private void setLayoutMain(BorderLayout layoutMain) {
        this.layoutMain = layoutMain;
    }

    private BorderLayout getLayoutMain() {
        return layoutMain;
    }

    private void setMenuBar1(JMenuBar menuBar) {
        this.menuBar1 = menuBar;
    }

    private JMenuBar getMenuBar1() {
        return menuBar1;
    }

    private void setMenuFile(JMenu menuFile) {
        this.menuFile = menuFile;
    }

    private JMenu getMenuFile() {
        return menuFile;
    }

    private void setMenuFileExit(JMenuItem menuFileExit) {
        this.menuFileExit = menuFileExit;
    }

    private JMenuItem getMenuFileExit() {
        return menuFileExit;
    }

    private void setMenuHelp(JMenu menuHelp) {
        this.menuHelp = menuHelp;
    }

    private JMenu getMenuHelp() {
        return menuHelp;
    }

    private void setMenuHelpAbout(JMenuItem menuHelpAbout) {
        this.menuHelpAbout = menuHelpAbout;
    }

    private JMenuItem getMenuHelpAbout() {
        return menuHelpAbout;
    }

    private void setStatusBar(JLabel statusBar) {
        this.statusBar = statusBar;
    }

    private JLabel getStatusBar() {
        return statusBar;
    }
}
