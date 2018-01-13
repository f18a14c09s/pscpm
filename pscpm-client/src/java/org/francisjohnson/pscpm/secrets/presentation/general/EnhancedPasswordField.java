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
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.data.Server;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.secrets.presentation.PSCPMClientMain;
import org.francisjohnson.pscpm.secrets.services.FileAndObjectIOAdapter;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.services.javacrypto.DataDecryptionAdapter;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;


public abstract class EnhancedPasswordField extends PSCPMPanel {
    private static final Dimension DEFAULT_DIMENSION = new Dimension(400, 25);
    private static final Dimension SHOW_HIDE_BTN_WIDTH = new Dimension(70, 25);
    private static final String DEFAULT_FAKE_PASSWORD = "*****";
    private static final char DEFAULT_ECHO_CHARACTER =
        new JPasswordField().getEchoChar();
    private JPasswordField passwordField =
        new JPasswordField(DEFAULT_FAKE_PASSWORD);
    private PSCPMButton clipboardBtn = new PSCPMButton("Copy");
    private PSCPMButton generatorBtn = new PSCPMButton("Gen");
    private PSCPMButton showButton = new PSCPMButton("Show");
    private PSCPMButton hideBtn = new PSCPMButton("Hide");
    private JPanel showHidePanel = new JPanel();
    private transient ActionListener revealer = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            showPassword();
        }
    };

    private transient ActionListener hider = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            hidePassword();
        }
    };

    private transient ActionListener generator = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            EnhancedPasswordField outer = EnhancedPasswordField.this;
            if (outer.getSession() == null) {
                handleError("Session Invalid",
                            "Cannot generate a password because the session object, which generates passwords, is invalid.");
            } else {
                setPassword(outer.getSession().generatePassword());
            }
        }
    };

    private transient ActionListener copier = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            char[] password = getPassword();
            if (password != null && password.length >= 1) {
                // TODO: Is there a more secure way to do this?  Is the clipboard inherently insecure anyway?
                StringSelection selec =
                    new StringSelection(new String(password));
                try {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selec,
                                                                                 selec);
                } catch (Exception f) {
                    handleException("Clipboard Unavailable",
                                    "Failed to generate a password to the clipboard.",
                                    f);
                }
                // Clearing the password actually messes up the JPasswordField.
                //                Arrays.fill(password, '*');
            }
        }
    };

    public EnhancedPasswordField() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            // Forced an "unexpected error" message because we don't need to
            // tell the user specifically about this class.
            handleException(e);
        }
    }

    public static void main(String... args) {
        SecretsFacade facade = null;
        Server server = null;
        try {
            //            facade =
            //                    SecretsFacade.startSession(WebLogicRMIPrototype.DEFAULT_RMI_PROVIDER_URL,
            //                                               PublicKeyIdentityStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
            facade =
                    new SecretsFacade(IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
            List<ServerSecret> secrets = FileAndObjectIOAdapter.loadAllByClass(ServerSecret.class);
            for (PublicKeyEncryptedSecretKey tempKey : FileAndObjectIOAdapter.loadAllByClass(PublicKeyEncryptedSecretKey.class)) {
                if (tempKey.getSecretKey().getId().equals(secrets.get(0).getSecretKey().getId())) {
                    server = DataDecryptionAdapter.decrypt(secrets.get(0), tempKey);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        JPanel panel = new JPanel();
        class MyPasswordField extends EnhancedPasswordField {
            private transient SecretsFacade secrets;

            public MyPasswordField(SecretsFacade secrets) {
                setSecrets(secrets);
            }

            @Override
            public BreadAction getAction() {
                return BreadAction.EDIT;
            }

            public void setSecrets(SecretsFacade secrets) {
                this.secrets = secrets;
            }

            public SecretsFacade getSession() {
                return null;
            }
        }
        EnhancedPasswordField field = new MyPasswordField(facade);
        field.setPassword(server.getAppCredentials().get(0).getOsPassword());
        panel.add(field);
        PSCPMClientMain.main(panel);
    }

    private void jbInit() throws Exception {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setSize(new Dimension(DEFAULT_DIMENSION));
        this.setPreferredSize(new Dimension(DEFAULT_DIMENSION));
        this.setMinimumSize(new Dimension(DEFAULT_DIMENSION));
        hideBtn.setPreferredSize(new Dimension(SHOW_HIDE_BTN_WIDTH));
        hideBtn.setMaximumSize(new Dimension(SHOW_HIDE_BTN_WIDTH));
        showButton.setPreferredSize(new Dimension(SHOW_HIDE_BTN_WIDTH));
        showButton.setMaximumSize(new Dimension(SHOW_HIDE_BTN_WIDTH));
        //        generatorBtn.setPreferredSize(new Dimension(GEN_BTN_WIDTH));
        //        generatorBtn.setMaximumSize(new Dimension(GEN_BTN_WIDTH));
        //        clipboardBtn.setPreferredSize(new Dimension(COPY_BTN_WIDTH));
        //        clipboardBtn.setMaximumSize(new Dimension(COPY_BTN_WIDTH));
        generatorBtn.addActionListener(generator);
        showHidePanel.setLayout(new BoxLayout(showHidePanel,
                                              BoxLayout.LINE_AXIS));
        getClipboardBtn().addActionListener(getCopier());
        getHideBtn().addActionListener(getHider());
        getShowButton().addActionListener(getRevealer());
        getShowHidePanel().add(getShowButton());
        actionChanged();
    }

    private void passwordChanged() {
        if (showHidePanel.isAncestorOf(hideBtn)) {
            showPassword();
        } else {
            hidePassword();
        }
    }

    private void actionChanged() {
        this.removeAll();
        this.addIgnoreNull(passwordField, null);
        this.addIgnoreNull(showHidePanel, null);
        this.addIgnoreNull(clipboardBtn, null);
        if (!isReadOnly()) {
            this.addIgnoreNull(generatorBtn, null);
        }
        if (passwordField != null) {
            passwordField.setEditable(!isReadOnly());
        }
    }

    private void addIgnoreNull(Component child, Object constraints) {
        if (child != null) {
            this.add(child, constraints);
        }
    }

    public void hidePassword() {
        char[] password = getPasswordField().getPassword();
        if (password != null) {
            // JPasswordField internally represents the password as char[], so
            // if you fill the array, WYSIWYG.
            //            Arrays.fill(password, '*');
        }
        //        passwordField.setText(DEFAULT_FAKE_PASSWORD);
        getPasswordField().setEchoChar(DEFAULT_ECHO_CHARACTER);
        getShowHidePanel().removeAll();
        getShowHidePanel().add(getShowButton());
        this.revalidate();
    }

    private void showPassword() {
        getPasswordField().setEchoChar((char)0);
        getShowHidePanel().removeAll();
        getShowHidePanel().add(getHideBtn());
        this.revalidate();
    }

    public abstract BreadAction getAction();

    private void setRevealer(ActionListener revealer) {
        this.revealer = revealer;
    }

    private ActionListener getRevealer() {
        return revealer;
    }

    private void setHider(ActionListener hider) {
        this.hider = hider;
    }

    private ActionListener getHider() {
        return hider;
    }

    private boolean isReadOnly() {
        return getAction() != BreadAction.ADD &&
            getAction() != BreadAction.EDIT;
    }

    @Override
    public void revalidate() {
        //        System.out.println(this.getClass().getSimpleName()+".  Revalidate called.");
        actionChanged();
        super.revalidate();
    }

    public char[] getPassword() {
        return getPasswordField().getPassword();
    }

    public void setPassword(char[] password) {
        // TODO: Not sure why JPasswordField has no setPassword(char[]).
        getPasswordField().setText(password == null ? null :
                                   new String(password));
        if (password != null) {
            // In theory this should be safe regardless of JPasswordField's
            // behavior, since it has no awareness of this char[]; in practice,
            // however, it does have an effect.
            // TODO: Determine if the PSCPM code reuses this password array
            // after filling it.
            //            Arrays.fill(password, '*');
        }
        passwordChanged();
    }

    private void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    private JPasswordField getPasswordField() {
        return passwordField;
    }

    private void setClipboardBtn(PSCPMButton clipboardBtn) {
        this.clipboardBtn = clipboardBtn;
    }

    private PSCPMButton getClipboardBtn() {
        return clipboardBtn;
    }

    private void setGeneratorBtn(PSCPMButton generatorBtn) {
        this.generatorBtn = generatorBtn;
    }

    private PSCPMButton getGeneratorBtn() {
        return generatorBtn;
    }

    private void setShowHidePanel(JPanel showHidePanel) {
        this.showHidePanel = showHidePanel;
    }

    private JPanel getShowHidePanel() {
        return showHidePanel;
    }

    private void setShowButton(PSCPMButton showButton) {
        this.showButton = showButton;
    }

    private PSCPMButton getShowButton() {
        return showButton;
    }

    private void setHideBtn(PSCPMButton hideBtn) {
        this.hideBtn = hideBtn;
    }

    private PSCPMButton getHideBtn() {
        return hideBtn;
    }

    private void setGenerator(ActionListener generator) {
        this.generator = generator;
    }

    private ActionListener getGenerator() {
        return generator;
    }

    private void setCopier(ActionListener copier) {
        this.copier = copier;
    }

    private ActionListener getCopier() {
        return copier;
    }

    /**
     * It's both interesting and helpful that Java allows (1) this abstract
     * class's superclass to be concrete and (2) the concrete superclass to have
     * a concrete version of this abstract method.  Abstractness is allowed to
     * override concreteness, in other words.  Very helpful in this case!
     * @return
     */
    public abstract SecretsFacade getSession();
}
