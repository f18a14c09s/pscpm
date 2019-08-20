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

import java.awt.Dimension;

import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.data.UserAccount;
import org.francisjohnson.pscpm.secrets.presentation.general.EnhancedPasswordField;
import org.francisjohnson.pscpm.secrets.presentation.general.FormField;
import org.francisjohnson.pscpm.secrets.presentation.general.SingleRecordManagementPanel;

// TODO: Backup the record so that the original record does not change if the
// users cancels edits.
public class ServerUserAccountMgmtPanel extends SingleRecordManagementPanel<UserAccount> {
    private static final long serialVersionUID = 1;

    private class MyPasswordField extends EnhancedPasswordField {
        private ServerUserAccountMgmtPanel outer =
            ServerUserAccountMgmtPanel.this;

        @Override
        public BreadAction getAction() {
            return ServerUserAccountMgmtPanel.this.getAction();
        }

        @Override
        public SecretsFacade getSession() {
            return outer.getSession();
        }
    }
    //    private transient SecretsFacade secrets;
    private List<FormField<UserAccount, ?>> fields =
        Arrays.asList(new FormField<UserAccount, JTextField>("User",
                                                             new Dimension(120,
                                                                           20),
                                                             new Dimension(400,
                                                                           25),
                                                             new JTextField(),
                                                             false) {

            @Override
            public void syncToField(UserAccount c) {
                getComponent().setText(c.getUserId());
            }

            @Override
            public void syncFromField(UserAccount c) {
                c.setUserId(getComponent().getText());
            }

        },
new FormField<UserAccount, ServerUserAccountMgmtPanel.MyPasswordField>("OS Password",
                                                                       new Dimension(120,
                                                                                     20),
                                                                       new Dimension(400,
                                                                                     25),
                                                                       new MyPasswordField(),
                                                                       false) {
            @Override
            public void syncToField(UserAccount c) {
                getComponent().setPassword(c.getOsPassword());
            }

            @Override
            public void syncFromField(UserAccount c) {
                c.setOsPassword(getComponent().getPassword());
            }
        },
new FormField<UserAccount, ServerUserAccountMgmtPanel.MyPasswordField>("DB Password",
                                                                       new Dimension(120,
                                                                                     20),
                                                                       new Dimension(400,
                                                                                     25),
                                                                       new MyPasswordField(),
                                                                       false) {

            @Override
            public void syncToField(UserAccount c) {
                getComponent().setPassword(c.getDbPassword());
            }

            @Override
            public void syncFromField(UserAccount c) {
                c.setDbPassword(getComponent().getPassword());
            }
        },
new FormField<UserAccount, JTextField>("Database", new Dimension(120, 20),
                                       new Dimension(400, 25),
                                       new JTextField(), false) {

            @Override
            public void syncToField(UserAccount c) {
                getComponent().setText(c.getDatabase());
            }

            @Override
            public void syncFromField(UserAccount c) {
                c.setDatabase(getComponent().getText());
            }
        },
new FormField<UserAccount, JTextArea>("Comments", new Dimension(120, 20),
                                      new Dimension(400, 100),
                                      new JTextArea(10, 30), true) {

            @Override
            public void syncToField(UserAccount c) {
                getComponent().setText(c.getComments());
            }

            @Override
            public void syncFromField(UserAccount c) {
                c.setComments(getComponent().getText());
            }
        });

    public ServerUserAccountMgmtPanel() {
        this(BreadAction.READ, new UserAccount());
    }

    public ServerUserAccountMgmtPanel(BreadAction action) {
        this(action, new UserAccount());
    }

    public ServerUserAccountMgmtPanel(BreadAction action,
                                      UserAccount credential) {
        super(action, credential);
        try {
            jbInit();
        } catch (Exception e) {
            handleException(getViewId().getDisplayName() + " Unavailable",
                            "Unable to initialize the " +
                            getViewId().getDisplayName() + " feature.", e);
        }
    }

    private void setFields(List<FormField<UserAccount, ?>> fields) {
        this.fields = fields;
    }

    @Override
    protected List<FormField<UserAccount, ?>> getFields() {
        return fields;
    }

    @Override
    public String getEntityName() {
        return "User Account";
    }

    @Override
    public PSCPMViewId getViewId() {
        return getAction() == BreadAction.ADD ? PSCPMViewId.NEW_SERVER_FORM :
               PSCPMViewId.SERVER_MGMT_FORM;
    }
}
