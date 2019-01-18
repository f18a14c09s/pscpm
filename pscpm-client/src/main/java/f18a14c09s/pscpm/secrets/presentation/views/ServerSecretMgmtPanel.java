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


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import f18a14c09s.pscpm.general.data.BreadAction;
import f18a14c09s.pscpm.secrets.data.UserAccount.Type;
import f18a14c09s.pscpm.secrets.data.SecretsException;
import f18a14c09s.pscpm.secrets.business.SecretsFacade;
import f18a14c09s.pscpm.secrets.data.UserAccount;
import f18a14c09s.pscpm.secrets.data.ServerSecret;
import f18a14c09s.pscpm.secrets.presentation.general.EnhancedPasswordField;
import f18a14c09s.pscpm.secrets.presentation.general.FormField;
import f18a14c09s.pscpm.secrets.presentation.general.Hyperlink;
import f18a14c09s.pscpm.secrets.presentation.general.Hyperlink.HyperlinkListener;
import f18a14c09s.pscpm.secrets.presentation.general.LinkWithDeleteIcon;
import f18a14c09s.pscpm.secrets.presentation.general.MyPanelCollection;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMTable;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMTableColumnMetadata;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMTableModel;
import f18a14c09s.pscpm.secrets.presentation.general.SecretKeyComboBox;
import f18a14c09s.pscpm.secrets.presentation.general.SingleRecordManagementPanel;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtEvent;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtListener;
import f18a14c09s.pscpm.secrets.presentation.events.SingleRecordMgmtNavEvent;
import f18a14c09s.pscpm.security.data.UserSecretKey;


public class ServerSecretMgmtPanel extends SingleRecordManagementPanel<ServerSecret> {
    private static final long serialVersionUID = 1;
    private static final BreadAction DEFAULT_ACTION = BreadAction.READ;
    private transient List<FormField<ServerSecret, ?>> fields;
    private ServerUserAccountMgmtPanel userAcctMgmtView;
    private PSCPMTable<UserAccount> adminUserTable;
    private PSCPMTable<UserAccount> appUserTable;

    public ServerSecretMgmtPanel() {
        this(DEFAULT_ACTION);
    }

    public ServerSecretMgmtPanel(BreadAction action) {
        this(action, new ServerSecret());
    }

    public ServerSecretMgmtPanel(ServerSecret server) {
        this(DEFAULT_ACTION, server);
    }

    public ServerSecretMgmtPanel(BreadAction action,
                                        ServerSecret server) {
        super(action, server);
        try {
            jbInit();
        } catch (Exception e) {
            handleException(getViewId().getDisplayName() + " Unavailable",
                            "Unable to initialize the " +
                            getViewId().getDisplayName() + " feature.", e);
        }
    }

    protected void jbInit() throws Exception {
        super.jbInit();
        setUserAcctMgmtView(new ServerUserAccountMgmtPanel(BreadAction.READ,
                                                            new UserAccount()));
        layoutAdditionalSections();
        getEditButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setAction(BreadAction.EDIT);
                    refreshLayout();
                }
            });
    }

    private void refreshLayout() {
        layoutMainContainer();
        layoutAdditionalSections();
        revalidate();
        repaint();
    }

    @Override
    protected void initFormFields() {
        setFields(Arrays.asList(new FormField<ServerSecret, JTextField>("Name",
                                                                        new Dimension(120,
                                                                                      20),
                                                                        new Dimension(400,
                                                                                      25),
                                                                        new JTextField(),
                                                                        false) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setText(c.getData().getName());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.getData().setName(getComponent().getText());
                    }

                },
new FormField<ServerSecret, JTextField>("Environment", new Dimension(120, 20),
                                        new Dimension(400, 25),
                                        new JTextField(), false) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setText(c.getData().getEnvironment());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.getData().setEnvironment(getComponent().getText());
                    }
                },
new FormField<ServerSecret, JTextField>("OS Version", new Dimension(120, 20),
                                        new Dimension(400, 25),
                                        new JTextField(), false) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setText(c.getData().getOsVersion());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.getData().setOsVersion(getComponent().getText());
                    }
                },
new FormField<ServerSecret, JTextField>("IP Address", new Dimension(120, 20),
                                        new Dimension(400, 25),
                                        new JTextField(), false) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setText(c.getData().getIpAddress());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.getData().setIpAddress(getComponent().getText());
                    }
                },
new FormField<ServerSecret, JTextArea>("Description", new Dimension(120, 20),
                                       new Dimension(400, 100),
                                       new JTextArea(10, 30), true) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setText(c.getData().getDescription());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.getData().setDescription(getComponent().getText());
                    }
                },
new FormField<ServerSecret, SecretKeyComboBox>("Encryption Key",
                                               new Dimension(120, 20),
                                               new Dimension(400, 25),
                                               new SecretKeyComboBox(getAvailableSecretKeys()),
                                               false) {

                    @Override
                    public void syncToField(ServerSecret c) {
                        getComponent().setSelectedItem(c.getSecretKey());
                    }

                    @Override
                    public void syncFromField(ServerSecret c) {
                        c.setSecretKey((UserSecretKey)getComponent().getSelectedItem());
                    }
                }));

        super.initFormFields();
    }

    private void resetSaveListener() {
        ActionListener[] lsnrs = getSaveButton().getActionListeners();
        if (lsnrs != null) {
            for (ActionListener lsnr : lsnrs) {
                getSaveButton().removeActionListener(lsnr);
            }
        }
    }

    private void layoutAdditionalSections() {
        getNavContainer().add(getUserAcctMgmtView(), PSCPMViewId.ACCOUNT_MGMT_FORM.name());
        getMainContainer().add(createAdminUserSection());
        getMainContainer().add(Box.createRigidArea(new Dimension(1, 20)));
        getMainContainer().add(createAppUserSection());
        getMainContainer().add(Box.createVerticalGlue());
    }

    private Container createAppUserSection() {
        MyPanelCollection retval = new MyPanelCollection();
        retval.setTable(createAppUserTable());
        //
        JButton userAddButton = new JButton("Add");
        userAddButton.setEnabled(!isReadOnly());
        userAddButton.addActionListener(new UserAddButtonListener(UserAccount.Type.APPLICATION));
        retval.getHeader().add(userAddButton);
        retval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                         1),
                                                          "Application Users:"));
        //
        return retval;
    }

    private Container createAdminUserSection() {
        MyPanelCollection retval = new MyPanelCollection();
        retval.setTable(createAdminUserTable());
        //
        JButton userAddButton = new JButton("Add");
        userAddButton.setEnabled(!isReadOnly());
        userAddButton.addActionListener(new UserAddButtonListener(UserAccount.Type.ADMINISTRATIVE));
        retval.getHeader().add(userAddButton);
        retval.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                         1),
                                                          "Administrative Users:"));
        //
        return retval;
    }

    private void setFields(List<FormField<ServerSecret, ?>> fields) {
        this.fields = fields;
    }

    @Override
    protected List<FormField<ServerSecret, ?>> getFields() {
        return fields;
    }

    protected PSCPMTable<UserAccount> createAdminUserTable() {
        List<UserAccount> users =
            getRecord() == null ? new ArrayList<UserAccount>() :
            getRecord().getData().getAdminCredentials();
        debug(getClass().getName(), "Creating admin user table.",
              (users == null ? 0 : users.size()) + " users specified.");
        setAdminUserTable(new PSCPMTable<UserAccount>(new PSCPMTableModel<UserAccount>(Arrays.asList(new PSCPMTableColumnMetadata<UserAccount, UserAccount>("User",
                                                                                                                                                        100,
                                                                                                                                                        UserAccount.class,
                                                                                                                                                        true,
                                                                                                                                                        new UserAccountCellRenderer(),
                                                                                                                                                        new UserAccountCellEditor()) {

                            @Override
                            public UserAccount getValue(UserAccount bean) {
                                return bean;
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, char[]>("OS Password", 300,
                                                 char[].class, true,
                                                 new PasswordEditorRenderer(),
                                                 new PasswordEditorRenderer()) {
                            @Override
                            public char[] getValue(UserAccount bean) {
                                return bean.getOsPassword();
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, String>("Comments", null,
                                                 String.class) {
                            @Override
                            public String getValue(UserAccount bean) {
                                return bean == null ? null :
                                       bean.getComments();
                            }
                        }), users), null, null));
        return getAdminUserTable();
    }

    protected PSCPMTable<UserAccount> createAppUserTable() {
        List<UserAccount> users =
            getRecord() == null ? new ArrayList<UserAccount>() :
            getRecord().getData().getAppCredentials();
        debug(getClass().getName(), "Creating application user table.",
              (users == null ? 0 : users.size()) + " users specified.");
        setAppUserTable(new PSCPMTable<UserAccount>(new PSCPMTableModel<UserAccount>(Arrays.asList(new PSCPMTableColumnMetadata<UserAccount, UserAccount>("User",
                                                                                                                                                      100,
                                                                                                                                                      UserAccount.class,
                                                                                                                                                      true,
                                                                                                                                                      new UserAccountCellRenderer(),
                                                                                                                                                      new UserAccountCellEditor()) {

                            @Override
                            public UserAccount getValue(UserAccount bean) {
                                return bean;
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, char[]>("OS Password", 300,
                                                 char[].class, true,
                                                 new PasswordEditorRenderer(),
                                                 new PasswordEditorRenderer()) {
                            @Override
                            public char[] getValue(UserAccount bean) {
                                return bean.getOsPassword();
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, char[]>("DB Password", 300,
                                                 char[].class, true,
                                                 new PasswordEditorRenderer(),
                                                 new PasswordEditorRenderer()) {
                            @Override
                            public char[] getValue(UserAccount bean) {
                                return bean.getDbPassword();
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, String>("Database", 200,
                                                 String.class) {
                            @Override
                            public String getValue(UserAccount bean) {
                                return bean == null ? null :
                                       bean.getDatabase();
                            }
                        },
new PSCPMTableColumnMetadata<UserAccount, String>("Comments", null,
                                                 String.class) {
                            @Override
                            public String getValue(UserAccount bean) {
                                return bean == null ? null :
                                       bean.getComments();
                            }
                        }), users), null, null));
        return getAppUserTable();
    }

    public List<UserSecretKey> getAvailableSecretKeys() {
        if (getSession() == null) {
            debug("User's encrypted keys are unavailable.",
                  "This can happen internally until the user has logged in.");
        } else {
            try {
                return getSession().findMySecretKeys();
            } catch (SecretsException e) {
                handleException("User's encrypted keys are unavailable.", e);
            }
        }
        return new ArrayList<UserSecretKey>();
    }

    private void saveServerIfEditing() {
        if (getAction() == BreadAction.EDIT) {
            try {
                getSession().save(getRecord());
            } catch (SecretsException e) {
                handleException("Save Failed",
                                "System failed to save the server secret with the added/updated user account.",
                                e);
            }
        }
    }

    @Override
    public String getEntityName() {
        return "Server";
    }

    @Override
    public PSCPMViewId getViewId() {
        return getAction() == BreadAction.ADD ? PSCPMViewId.NEW_SERVER_FORM : PSCPMViewId.SERVER_MGMT_FORM;
    }

    private void setUserAcctMgmtView(ServerUserAccountMgmtPanel credentialManagerView) {
        this.userAcctMgmtView = credentialManagerView;
    }

    private ServerUserAccountMgmtPanel getUserAcctMgmtView() {
        return userAcctMgmtView;
    }

    private void setAdminUserTable(PSCPMTable<UserAccount> adminUserTable) {
        this.adminUserTable = adminUserTable;
    }

    private PSCPMTable<UserAccount> getAdminUserTable() {
        return adminUserTable;
    }

    private void setAppUserTable(PSCPMTable<UserAccount> appUserTable) {
        this.appUserTable = appUserTable;
    }

    private PSCPMTable<UserAccount> getAppUserTable() {
        return appUserTable;
    }

    private final class PasswordEditorRenderer extends AbstractCellEditor implements TableCellEditor,
                                                                                     TableCellRenderer,
                                                                                     CellEditorListener {
        private ServerSecretMgmtPanel outer =
            ServerSecretMgmtPanel.this;
        // This is not specifically for rendering.  It's for when the user is
        // "reading" the parent record.  When the user changes the mode from read
        // to edit, and there is a desire to allow editing inline in the table,
        // then a separate variable/instance should be used or forReadOnly's
        // "action" property should be overridden/set to BreadAction.EDIT or ADD.
        private EnhancedPasswordField forReadOnly =
            new EnhancedPasswordField() {

            @Override
            public BreadAction getAction() {
                return BreadAction.READ;
            }

            @Override
            public SecretsFacade getSession() {
                return outer.getSession();
            }
        };

        public PasswordEditorRenderer() {
            super.addCellEditorListener(this);
        }

        @Override
        public Object getCellEditorValue() {
            return getForReadOnly().getPassword();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            getForReadOnly().setPassword((char[])value);
            return getForReadOnly();
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            getForReadOnly().setPassword((char[])value);
            return getForReadOnly();
        }

        public void editingStopped(ChangeEvent e) {
            ServerSecretMgmtPanel.this.debug(getClass().getName(),
                                                    "editingStopped(ChangeEvent).",
                                                    "Now hiding the password.");
            getForReadOnly().hidePassword();
        }

        public void editingCanceled(ChangeEvent e) {
            ServerSecretMgmtPanel.this.debug(getClass().getName(),
                                                    "editingCanceled(ChangeEvent).",
                                                    "Now hiding the password.");
            getForReadOnly().hidePassword();
        }

        private void setForReadOnly(EnhancedPasswordField forReadOnly) {
            this.forReadOnly = forReadOnly;
        }

        private EnhancedPasswordField getForReadOnly() {
            return forReadOnly;
        }
    }

    private final class UserAccountCellRenderer implements TableCellRenderer {
        // This is not specifically for editing.  It's for when the user is
        // editing the parent record.  When the user changes the mode from read
        // to edit, then forReadOnly should be used.
        private LinkWithDeleteIcon forEditable;
        // This is not specifically for rendering.  It's for when the user is
        // reading the parent record.  When the user changes the mode from read
        // to edit, then forEditable should be used.
        private Hyperlink forReadOnly;
        private ServerSecretMgmtPanel outer =
            ServerSecretMgmtPanel.this;

        public UserAccountCellRenderer() {
            setForEditable(new LinkWithDeleteIcon("", true));
            setForReadOnly(new Hyperlink(""));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            setRowInfo((UserAccount)value, table, row);
            return outer.isReadOnly() ? getForReadOnly() : getForEditable();
        }

        private void setRowInfo(UserAccount userAccount, JTable table,
                                int rowId) {
            //            if (outer.isReadOnly()) {
            getForReadOnly().setText(userAccount.getUserId());
            //            } else {
            getForEditable().setText(userAccount.getUserId());
            //            }
        }

        private void setForEditable(LinkWithDeleteIcon field) {
            this.forEditable = field;
        }

        private LinkWithDeleteIcon getForEditable() {
            return forEditable;
        }

        private void setForReadOnly(Hyperlink forRendering) {
            this.forReadOnly = forRendering;
        }

        private Hyperlink getForReadOnly() {
            return forReadOnly;
        }
    }

    private final class UserIdTableCell extends LinkWithDeleteIcon {
        private ServerSecretMgmtPanel outer =
            ServerSecretMgmtPanel.this;
        private UserAccount userAccount;
        private JTable table;

        public UserIdTableCell(UserAccount userAccount, JTable table,
                               boolean editable) {
            super(userAccount == null ? "" : userAccount.getUserId(),
                  editable);
            setUserAccount(userAccount);
            setTable(table);
            if (editable) {
                addActivationListener(new Hyperlink.HyperlinkListener() {
                        public void linkActivated(InputEvent evt) {
                            outer.getUserAcctMgmtView().init(outer.getSession(),
                                                             new SingleRecordMgmtNavEvent<UserAccount>(new UserAccountSaveListener(),
                                                                                                      getUserAccount(), BreadAction.READ,
                                                                                                      true));
                            outer.nav(PSCPMViewId.ACCOUNT_MGMT_FORM.name());
                        }
                    });
                addDeletionListener(new Hyperlink.HyperlinkListener() {
                        public void linkActivated(InputEvent e) {
                            outer.getRecord().getServer().removeCredential(getUserAccount());
                            // This applies to the editing of a server.  It does not
                            // to adding a new server, because the changes will
                            // persist upon creation of the server.
                            outer.saveServerIfEditing();
                            outer.debug("User account credentials deleted.");
//                            if (getTable() != null &&
//                                getTable().getModel() instanceof
//                                PSCPMTableModel) {
//                                PSCPMTableModel<Credential> model =
//                                    (PSCPMTableModel<Credential>)getTable().getModel();
////                                int rowId =
//                                    model.removeRow(getUserAccount());
//                                //                                model.fireTableDataChanged();
////                                model.fireTableRowsDeleted(rowId, rowId);
//                                //                                getTable().revalidate();
//                                //                                getTable().repaint();
//                            }
                            outer.debug("Refreshing the " +
                                        getUserAccount().getType() +
                                        " User table after " + BreadAction.DELETE +
                                        " operation.");
                            outer.refreshLayout();
                        }
                    });
            } else {
                addActivationListener(new Hyperlink.HyperlinkListener() {
                        public void linkActivated(InputEvent evt) {
                            outer.debug("Read-only of user account requested.");
                            outer.getUserAcctMgmtView().init(outer.getSession(),
                                                             new SingleRecordMgmtNavEvent<UserAccount>(new UserAccountSaveListener(),
                                                                                                      getUserAccount(), BreadAction.READ,
                                                                                                      false));
                            outer.nav(PSCPMViewId.ACCOUNT_MGMT_FORM.name());
                        }
                    });
            }
        }

        private void setUserAccount(UserAccount userAccount) {
            this.userAccount = userAccount;
        }

        private UserAccount getUserAccount() {
            return userAccount;
        }

        private void setTable(JTable table) {
            this.table = table;
        }

        private JTable getTable() {
            return table;
        }
    }

    private final class UserAccountCellEditor extends AbstractCellEditor implements TableCellEditor,
                                                                                    CellEditorListener {
        private UserAccount userAccount;
        private int rowId;
        private JTable table;
        private ServerSecretMgmtPanel outer =
            ServerSecretMgmtPanel.this;

        public UserAccountCellEditor() {
            super.addCellEditorListener(this);
        }

        @Override
        public Object getCellEditorValue() {
            return getUserAccount();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            setRowInfo((UserAccount)value, table, row);
            return new UserIdTableCell((UserAccount)value, table,
                                       !outer.isReadOnly());
        }

        private void setRowInfo(UserAccount userAccount, JTable table,
                                int rowId) {
            setTable(table);
            setRowId(rowId);
            setUserAccount(userAccount);
        }

        private void setUserAccount(UserAccount userAccount) {
            this.userAccount = userAccount;
        }

        private UserAccount getUserAccount() {
            return userAccount;
        }

        public void editingStopped(ChangeEvent e) {
            //            AddReadEditServerSecretPanel.this.debug(getClass().getName(),
            //                                                    "editingStopped(ChangeEvent).",
            //                                                    "Now hiding the password.");
        }

        public void editingCanceled(ChangeEvent e) {
            //            AddReadEditServerSecretPanel.this.debug(getClass().getName(),
            //                                                    "editingCanceled(ChangeEvent).",
            //                                                    "Now hiding the password.");
        }

        private void setTable(JTable table) {
            this.table = table;
        }

        private JTable getTable() {
            return table;
        }

        private void setRowId(int rowId) {
            this.rowId = rowId;
        }

        private int getRowId() {
            return rowId;
        }
    }


    private final class UserAccountSaveListener extends SingleRecordMgmtListener<UserAccount> {
        private ServerSecretMgmtPanel outer =
            ServerSecretMgmtPanel.this;

        public UserAccountSaveListener() {
            super();
        }

        public void savePressed(SingleRecordMgmtEvent<UserAccount> evt) {
            if (evt.getAction() == BreadAction.ADD) {
                // Add the new user to the list of users.  Not persisted here.
                outer.getRecord().getServer().addCredential(evt.getRecord());
                PSCPMTable<UserAccount> table = null;
                switch (evt.getRecord().getType()) {
                case ADMINISTRATIVE:
                    {
                        table = getAdminUserTable();
                        break;
                    }
                case APPLICATION:
                    {
                        table = getAppUserTable();
                        break;
                    }
                default:
                    {
                        outer.debug("Unexpected user type:",
                                    "Type: " + evt.getRecord().getType(),
                                    "The corresponding user UI table might now be out of date.");
                        break;
                    }
                }
                if (table != null &&
                    table.getModel() instanceof PSCPMTableModel) {
                    outer.debug("Refreshing the " + evt.getRecord().getType() +
                                " User table after " + evt.getAction() +
                                " operation.");
                    PSCPMTableModel<UserAccount> model =
                        (PSCPMTableModel<UserAccount>)table.getModel();
                    model.addRow(evt.getRecord());
                    model.fireTableDataChanged();
                }
            }
            outer.saveServerIfEditing();
            outer.debug("User account credential " +
                        evt.getAction().getLabel() + " action succeeded.");
            outer.nav(SingleRecordManagementPanel.MAIN_VIEW_ID);
        }

        public void cancelPressed(SingleRecordMgmtEvent<UserAccount> evt) {
            outer.debug((evt.getRecord() == null ? "" :
                         evt.getRecord().getClass().getSimpleName()) + " " +
                        (evt.getAction()) + " cancelled.");
            outer.nav(SingleRecordManagementPanel.MAIN_VIEW_ID);
        }

    }

    private final class UserAddButtonListener implements ActionListener {
        private UserAccount.Type type;

        public UserAddButtonListener(UserAccount.Type type) {
            setType(type);
        }

        public void actionPerformed(ActionEvent e) {
            UserAccount newUser = new UserAccount();
            newUser.setType(getType());
            ServerSecretMgmtPanel outer =
                ServerSecretMgmtPanel.this;
            outer.getUserAcctMgmtView().init(getSession(),
                                             new SingleRecordMgmtNavEvent<UserAccount>(new UserAccountSaveListener(),
                                                                                      newUser, BreadAction.ADD,
                                                                                      true));
            outer.nav(PSCPMViewId.ACCOUNT_MGMT_FORM.name());
        }

        private void setType(UserAccount.Type type) {
            this.type = type;
        }

        private UserAccount.Type getType() {
            return type;
        }
    }
}
