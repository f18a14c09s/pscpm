package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.event.ActionListener;

import javax.swing.JButton;


public class PSCPMButton extends JButton {
    public PSCPMButton() {
        super();
    }

    public PSCPMButton(String text) {
        super(text);
    }

    public void removeAllActionListeners() {
        if (getActionListeners() != null) {
            for (ActionListener lsnr : getActionListeners()) {
                removeActionListener(lsnr);
            }
        }
    }
}
