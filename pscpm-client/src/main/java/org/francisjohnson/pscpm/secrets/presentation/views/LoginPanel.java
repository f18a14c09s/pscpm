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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.francisjohnson.pscpm.general.services.ServiceFacade;

import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.PSCPMClientMain;
import org.francisjohnson.pscpm.secrets.presentation.events.LoginSucceededEvent;
import org.francisjohnson.pscpm.secrets.presentation.general.PSCPMPanel;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

public class LoginPanel extends PSCPMPanel {

    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel loginBox = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel loginHeaderBox = new JPanel();
    private JPanel loginFieldBox = new JPanel();
    private JPanel loginFooterBox = new JPanel();
    private JLabel loginBoxHeader = new JLabel();
    private GridLayout gridLayout1 = new GridLayout();
    private JPanel selDbPanel = new JPanel();
    private JPanel selCredPanel = new JPanel();
    private JLabel selDbLabel = new JLabel();
    private JLabel selCredLabel = new JLabel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JScrollPane selDbScroll = new JScrollPane();
    private JScrollPane selCredScroll = new JScrollPane();
    private JList<Object> selCredList = new JList<Object>();
    private JList<Object> selDbList = new JList<Object>();
    private JButton loginButton = new JButton();
    private FlowLayout flowLayout2 = new FlowLayout();

    public LoginPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            PSCPMPanel.handleException(this, "Login UI Unavailable",
                    "Unable to access the system's login user interface.",
                    null, e);
        }
    }

    private void jbInit() throws Exception {
        flowLayout1.setAlignment(FlowLayout.LEADING);
        selDbScroll.setPreferredSize(new Dimension(400, 100));
        selDbScroll.setSize(new Dimension(400, 80));
        selCredScroll.setPreferredSize(new Dimension(400, 100));
        selCredScroll.setSize(new Dimension(400, 80));
        loginButton.setText("Login");
        flowLayout2.setAlignment(2);
        this.setLayout(gridBagLayout1);
        loginBox.setLayout(borderLayout1);
        loginBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                1),
                "Login:"));
        loginHeaderBox.setLayout(flowLayout1);
        loginHeaderBox.setVisible(false);
        loginFieldBox.setLayout(gridLayout1);
        loginFooterBox.setLayout(flowLayout2);
        loginBoxHeader.setText("Login:");
        loginBoxHeader.setFont(new Font("Arial", 1, 14));
        gridLayout1.setColumns(1);
        gridLayout1.setRows(2);
        selDbLabel.setText("Select a Server:");
        selDbLabel.setFont(new Font("Arial", 0, 12));
        selDbLabel.setPreferredSize(new Dimension(120, 20));
        selDbLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dbListInit();
        loginHeaderBox.add(loginBoxHeader, null);
        loginBox.add(loginHeaderBox, BorderLayout.NORTH);
        selDbPanel.add(selDbLabel, null);
        selDbScroll.getViewport().add(selDbList, null);
        selDbPanel.add(selDbScroll, null);
        loginFieldBox.add(selDbPanel, null);
        selCredPanel.add(selCredLabel, null);
        selCredScroll.getViewport().add(selCredList, null);
        selCredPanel.add(selCredScroll, null);
        loginFieldBox.add(selCredPanel, null);
        loginBox.add(loginFieldBox, BorderLayout.CENTER);
        loginFooterBox.add(loginButton, null);
        loginBox.add(loginFooterBox, BorderLayout.SOUTH);
        this.add(loginBox,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
        selCredLabel.setText("Select a Credential:");
        selCredLabel.setFont(new Font("Arial", 0, 12));
        selCredLabel.setPreferredSize(new Dimension(120, 20));
        selCredLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        credListInit();
        loginButtonInit();
    }

    private void loginButtonInit() {
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String dbUrl
                            = selDbList == null || !(selDbList.getSelectedValue() instanceof String) ? null
                            : (String) selDbList.getSelectedValue();
                    UserCredential cred
                            = selCredList == null || !(selCredList.getSelectedValue() instanceof UserCredential) ? null
                            : (UserCredential) selCredList.getSelectedValue();
                    List<String> validationErrors
                            = new ArrayList<String>();
                    if (dbUrl == null || dbUrl.isEmpty()) {
                        validationErrors.add("Please specify a server URL.");
                    }
                    if (cred == null) {
                        validationErrors.add("Please specify a credential.");
                    }
                    if (!validationErrors.isEmpty()) {
                        handleError("Login Cannot Proceed",
                                "Error: " + validationErrors + ".");
                        return;
                    }
                    SecretsFacade secrets
                            = SecretsFacade.startSession(dbUrl, cred);
                    User me = secrets.getUser();
                    if (me == null) {
                        handleError("Login Failed", "Login failed.",
                                "The User object is null.");
                    } else {
                        if (secrets.isOffline()) {
                            handleError("Offline Mode",
                                    "The remote secrets and/or security service(s) is/are inaccessible."
                                    + "\n\nTherefore this client application is in offline mode."
                                    + "\n\nAny operations you initiate will occur against the local cache file only.");
                        }
                        debug("Login successful!");
                        getNavigator().navigate(new LoginSucceededEvent(e.getSource(),
                                secrets));
                    }
                } catch (Exception f) {
                    handleException("Login Failed", "Login failed.", f);
                }
            }
        });
    }

    private LoginPanel getLoginPanel() {
        return this;
    }

    private static <Compon extends Container> Compon traverse(Container cur,
            Class<Compon> clazz) {
        return cur == null ? null
                : clazz.isAssignableFrom(cur.getClass()) ? (Compon) cur
                : traverse(cur.getParent(), clazz);
    }

    private void dbListInit() {
        DefaultListModel<Object> model = new DefaultListModel<Object>();
        model.addElement(ServiceFacade.DEFAULT_SERVER_URL);
        selDbList.setModel(model);
        selDbList.setSelectedIndex(0);
    }

    private void credListInit() {
        DefaultListModel<Object> model = new DefaultListModel<Object>();
        try {
            model.addElement(IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        } catch (UnrecoverableKeyException e) {
            handleException(e);
        } catch (CertificateException e) {
            handleException(e);
        } catch (NoSuchAlgorithmException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        } catch (KeyStoreException e) {
            handleException(e);
        }
        selCredList.setModel(model);
        selCredList.setSelectedIndex(0);
    }

    public static void main(String... args) {
        PSCPMClientMain.main(new LoginPanel());
    }
}
