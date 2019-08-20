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
package f18a14c09s.pscpm.secrets.presentation.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.font.TextAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import f18a14c09s.pscpm.general.data.BreadAction;
import f18a14c09s.pscpm.general.services.MapUtil;
import f18a14c09s.pscpm.secrets.data.SecretsException;
import f18a14c09s.pscpm.secrets.data.ServerSecret;
import f18a14c09s.pscpm.secrets.presentation.general.Hyperlink;
import f18a14c09s.pscpm.secrets.presentation.general.LinkWithDeleteIcon;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMButton;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMPanel;
import f18a14c09s.pscpm.secrets.presentation.events.NavigationEvent;
import f18a14c09s.pscpm.secrets.presentation.events.PSCPMOutcome;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtEvent;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtListener;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtNavEvent;

public class ServerSecretsBrowserPanel extends PSCPMPanel {

    public static final int DEFAULT_NUMBER_PER_ROW = 4;
    private List<ServerSecret> data;
    private PSCPMButton logoutButton = new PSCPMButton("Logout");
    private PSCPMButton refreshServersButton = new PSCPMButton("Refresh");
    private PSCPMButton addServerButton = new PSCPMButton("Add Server");
    private PSCPMButton generatePasswordButton
            = new PSCPMButton("Generate Password to Clipboard");
    private Box headerBox = Box.createHorizontalBox();
    private Box verticalBox = Box.createVerticalBox();
    private ServerSecretMgmtPanel serverMgmtView;
    private CardLayout nav;
    private JPanel mainContent = new JPanel(new BorderLayout());

    public ServerSecretsBrowserPanel() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            handleException(getViewId().getDisplayName() + " Unavailable",
                    "Unable to initialize the "
                    + getViewId().getDisplayName() + " feature.", e);
        }
    }

    private void jbInit() throws Exception {
        setServerMgmtView(new ServerSecretMgmtPanel());
        setNav(new CardLayout());
        initButtonActions();
        getMainContent().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                1),
                "Browse Server Secrets:"));
        initLayout();
        refreshDataAndView();
    }

    private void initLayout() {
        removeAll();
        getMainContent().removeAll();
        getHeaderBox().removeAll();
        setLayout(getNav());
        // Level 1
        add(getMainContent(), PSCPMViewId.SERVER_BROWSER.name());
        add(getServerMgmtView(), PSCPMViewId.SERVER_MGMT_FORM.name());
        // Level 2
        getMainContent().add(getHeaderBox(), BorderLayout.PAGE_START);
        getMainContent().add(new JScrollPane(getVerticalBox()),
                BorderLayout.CENTER);
        // Level 3
//        String userInfo="Francis Johnson";
        String userInfo = (getSession() == null
                || getSession().getUser() == null
                || getSession().getUser().getUserId()
                == null
                || getSession().getUser().getUserId().trim().isEmpty()
                ? "Unknown"
                : getSession().getUser().getUserId().trim());
        getHeaderBox().add(new JLabel("Logged in as "
                + userInfo
                + "."));
        getHeaderBox().add(Box.createHorizontalGlue());
        getHeaderBox().add(getLogoutButton());
        getHeaderBox().add(getRefreshServersButton());
        getHeaderBox().add(getAddServerButton());
        getHeaderBox().add(getGeneratePasswordButton());
        nav(PSCPMViewId.SERVER_BROWSER.name());
    }

    private void nav(String viewId) {
        getNav().show(this, viewId);
    }

    /**
     * Used for reinitialization when this view is reused.
     */
    public void reinit() {
        try {
            jbInit();
        } catch (Exception e) {
            handleException("Server Browser Unavailable",
                    "Unable to initialize the server browser.", e);
        }
        revalidate();
    }

    private void setData(List<ServerSecret> data) {
        this.data = data;
    }

    private List<ServerSecret> getData() {
        return data;
    }

    private void setVerticalBox(Box verticalBox) {
        this.verticalBox = verticalBox;
    }

    private Box getVerticalBox() {
        return verticalBox;
    }

    private void setServerMgmtView(ServerSecretMgmtPanel serverMgmtView) {
        this.serverMgmtView = serverMgmtView;
    }

    private ServerSecretMgmtPanel getServerMgmtView() {
        return serverMgmtView;
    }

    private void setNav(CardLayout nav) {
        this.nav = nav;
    }

    private CardLayout getNav() {
        return nav;
    }

    private void setMainContent(JPanel mainContent) {
        this.mainContent = mainContent;
    }

    private JPanel getMainContent() {
        return mainContent;
    }

    private final class ServerSaveListener extends SingleRecordMgmtListener<ServerSecret> {

        public ServerSaveListener() {
            super(ServerSecretsBrowserPanel.this.getNavigator());
        }

        public void savePressed(SingleRecordMgmtEvent<ServerSecret> evt) {
            ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
            switch (evt.getAction()) {
                case ADD: {
                    try {
                        ServerSecret managedRecord
                                = outer.getSession().add(evt.getRecord());
                        outer.addLinkToView(managedRecord);
                        nav(PSCPMViewId.SERVER_BROWSER.name());
                    } catch (SecretsException e) {
                        outer.handleException("Add Failed",
                                "System failed to add the new server secret.",
                                e);
                    }
                    break;
                }
                case EDIT: {
                    try {
                        ServerSecret managedRecord
                                = outer.getSession().save(evt.getRecord());
                        getData().remove(evt.getRecord());
                        getData().add(managedRecord);
                        refreshServerLinks(groupDataByEnvironment(getData()));
                        nav(PSCPMViewId.SERVER_BROWSER.name());
                    } catch (SecretsException e) {
                        outer.handleException("Save Failed",
                                "System failed to save the server secret.",
                                e);
                    }
                    break;
                }
                default: {
                    outer.handleError("Unexpected Error",
                            "An unexpected error occurred.",
                            "The action is neither add nor edit: "
                            + evt.getAction() + ".");
                    break;
                }
            }
        }

        public void cancelPressed(SingleRecordMgmtEvent<ServerSecret> evt) {
            getLog().info((evt.getRecord() == null ? ""
                    : evt.getRecord().getClass().getSimpleName())
                    + " " + (evt.getAction()) + " cancelled.");
            nav(PSCPMViewId.SERVER_BROWSER.name());
        }
    }

    private void initButtonActions() {
        // Clear the action listeners
        for (Component compon : getHeaderBox().getComponents()) {
            if (compon instanceof PSCPMButton) {
                ((PSCPMButton) compon).removeAllActionListeners();
            }
        }
        getLogoutButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                // TODO: Here is an exception where you probably need to
                // rely externally for navigating away.
                outer.getNavigator().navigate(new NavigationEvent(e.getSource(),
                        outer.getViewId(), PSCPMOutcome.LOGOUT));
            }
        });
        getRefreshServersButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                try {
                    //                        outer.reinit();
                    outer.refreshDataAndView();
                } catch (Exception f) {
                    handleException("Error During Refresh",
                            "Unable to refresh server browser data.",
                            f);
                }
            }
        });
        getAddServerButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                //                    outer.getServerMgmtView().init(getSession(),
                //                                                   new SingleRecordMgmtNavEvent<ServerSecret>(e.getSource(),
                //                                                                                              outer.getViewId(),
                //                                                                                              PSCPMOutcome.ADD_SERVER,
                //                                                                                              new ServerSaveListener(),
                //                                                                                              new ServerSecret()));
                outer.getServerMgmtView().init(getSession(),
                        new SingleRecordMgmtNavEvent<ServerSecret>(new ServerSaveListener(),
                                new ServerSecret(), BreadAction.ADD,
                                true));
                outer.nav(PSCPMViewId.SERVER_MGMT_FORM.name());
            }
        });
        getGeneratePasswordButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                if (outer.getSession() == null) {
                    handleError("Session Invalid",
                            "Cannot generate a password because the session object, which generates passwords, is invalid.");
                } else {
                    StringSelection selec
                            = new StringSelection(new String(outer.getSession().generatePassword()));
                    try {
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selec,
                                selec);
                    } catch (Exception f) {
                        handleException("Clipboard Unavailable",
                                "Failed to generate a password to the clipboard.",
                                f);
                    }
                }
            }
        });
    }

    private void refreshDataAndView() throws SecretsException {
        Map<String, List<ServerSecret>> data = refreshData();
        refreshView(data);
    }

    private Map<String, List<ServerSecret>> refreshData() throws SecretsException {
        setData(getData() == null ? new ArrayList<ServerSecret>() : getData());
        getData().clear();
        if (getSession() != null) {
            setData(getSession().findAvailableServers());
        }
        return groupDataByEnvironment(getData());
    }

    private Map<String, List<ServerSecret>> groupDataByEnvironment(List<ServerSecret> rawData) {
        Map<String, List<ServerSecret>> retval
                = new TreeMap<String, List<ServerSecret>>(Collections.reverseOrder(new Comparator<String>() {
                    public int compare(String lhs, String rhs) {
                        if (lhs == rhs) {
                            return 0;
                        } else if (lhs == null && rhs != null) {
                            return 1;
                        } else if (lhs != null && rhs == null) {
                            return -1;
                        } else if (lhs.equals(rhs)) {
                            return 0;
                        }
                        String lhsLower = lhs == null ? "" : lhs.toLowerCase();
                        String rhsLower = rhs == null ? "" : rhs.toLowerCase();
                        return lhsLower.contains("prod")
                                ? rhsLower.contains("prod")
                                ? lhsLower.compareTo(rhsLower) : -1
                                : lhsLower.contains("test")
                                ? rhsLower.contains("prod") ? 1
                                : rhsLower.contains("test")
                                ? lhsLower.compareTo(rhsLower) : -1
                                : rhsLower.contains("prod")
                                || rhsLower.contains("test") ? 1
                                : lhsLower.compareTo(rhsLower);
                    }
                }));
        int nullServers = 0;
        for (ServerSecret server : rawData) {
            if (server == null) {
                // This indicates an error with add save, edit save, or finder method.
                nullServers++;
            } else {
                String env
                        = server.getData().getEnvironment() == null ? "UNKNOWN ENVIRONMENT"
                        : server.getData().getEnvironment();
                if (!retval.containsKey(env)) {
                    retval.put(env, new ArrayList<ServerSecret>());
                }
                List<ServerSecret> list = (List<ServerSecret>) retval.get(env);
                list.add(server);
            }
        }
        if (nullServers > 0) {
            handleError("Error While Traversing the Server Secrets",
                    "One or more (" + nullServers
                    + " total) server objects is/are null.");
        }
        return retval;
    }

    private void refreshView(Map<String, List<ServerSecret>> sourceData) {
        refreshServerLinks(sourceData);
    }

    private void refreshServerLinks(Map<String, List<ServerSecret>> sourceData) {
        getVerticalBox().removeAll();
        for (String env : sourceData.keySet()) {
            JPanel panel = new JPanel();
            JPanel panelWithSpacer = new JPanel();
            panel.setLayout(new GridLayout(0, DEFAULT_NUMBER_PER_ROW, 10, 10));
            for (final ServerSecret server : sourceData.get(env)) {
                JPanel linkPanel = new JPanel();
                linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                LinkWithDeleteIcon hyperlink
                        = new LinkWithDeleteIcon(server.getData().getName(), true);
                hyperlink.addActivationListener(new Hyperlink.HyperlinkListener() {
                    public void linkActivated(InputEvent evt) {
                        ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                        //                            outer.getServerMgmtView().init(getSession(),
                        //                                                           new SingleRecordMgmtNavEvent<ServerSecret>(evt.getSource(),
                        //                                                                                                      outer.getViewId(),
                        //                                                                                                      PSCPMOutcome.MANAGE_SERVER,
                        //                                                                                                      new ServerSaveListener(),
                        //                                                                                                      server));
                        outer.getServerMgmtView().init(getSession(),
                                new SingleRecordMgmtNavEvent<ServerSecret>(new ServerSaveListener(),
                                        server, BreadAction.READ,
                                        true));
                        outer.nav(PSCPMViewId.SERVER_MGMT_FORM.name());
                    }
                });
                hyperlink.addDeletionListener(new Hyperlink.HyperlinkListener() {
                    public void linkActivated(InputEvent e) {
                        ServerSecretsBrowserPanel outer = ServerSecretsBrowserPanel.this;
                        try {
                            outer.getSession().delete(server);
                        } catch (Exception f) {
                            outer.handleException("Deletion Failed",
                                    "Server secret deletion failed.",
                                    f);
                        }
                        // Refresh occurs anyway just in case the delete
                        // occurred (e.g. in the cache).
                        // Nevermind; too slow
                        outer.deleteLinkToView(server);
                    }
                });
                linkPanel.add(hyperlink);
                panel.add(linkPanel);
            }
            panelWithSpacer.setLayout(new BorderLayout());
            panelWithSpacer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,
                    2),
                    env,
                    TitledBorder.LEFT,
                    TitledBorder.CENTER,
                    new Font(Font.SANS_SERIF,
                            Font.BOLD,
                            16).deriveFont(MapUtil.asMap(MapUtil.entry(TextAttribute.FOREGROUND,
                            new Color(0,
                                    0,
                                    128))))));
            panelWithSpacer.add(panel, BorderLayout.CENTER);
            JPanel spacer = new JPanel();
            Dimension spacerHeight = new Dimension(1, 100);
            spacer.setMinimumSize(spacerHeight);
            spacer.setPreferredSize(spacerHeight);
            panelWithSpacer.add(spacer, BorderLayout.LINE_START);
            getVerticalBox().add(panelWithSpacer);
        }
        getVerticalBox().add(Box.createVerticalGlue());
        //
        getVerticalBox().revalidate();
        getVerticalBox().repaint();
    }

    private void deleteLinkToView(ServerSecret server) {
        getData().remove(server);
        refreshServerLinks(groupDataByEnvironment(getData()));
    }

    private void addLinkToView(ServerSecret server) {
        ((List<ServerSecret>) getData()).add(server);
        refreshServerLinks(groupDataByEnvironment(getData()));
    }

    private void setRefreshServersButton(PSCPMButton refreshServersButton) {
        this.refreshServersButton = refreshServersButton;
    }

    private PSCPMButton getRefreshServersButton() {
        return refreshServersButton;
    }

    private void setAddServerButton(PSCPMButton addServerButton) {
        this.addServerButton = addServerButton;
    }

    private PSCPMButton getAddServerButton() {
        return addServerButton;
    }

    private void setHeaderBox(Box headerBox) {
        this.headerBox = headerBox;
    }

    private Box getHeaderBox() {
        return headerBox;
    }

    private void setLogoutButton(PSCPMButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    private PSCPMButton getLogoutButton() {
        return logoutButton;
    }

    private void setGeneratePasswordButton(PSCPMButton generatePasswordButton) {
        this.generatePasswordButton = generatePasswordButton;
    }

    private PSCPMButton getGeneratePasswordButton() {
        return generatePasswordButton;
    }

    @Override
    public PSCPMViewId getViewId() {
        return PSCPMViewId.SERVER_BROWSER;
    }
}
