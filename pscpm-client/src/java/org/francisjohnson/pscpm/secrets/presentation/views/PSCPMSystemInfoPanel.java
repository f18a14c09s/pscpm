package org.francisjohnson.pscpm.secrets.presentation.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class PSCPMSystemInfoPanel extends JPanel {
    private JLabel labelTitle = new JLabel();
//    private JLabel labelAuthor = new JLabel();
    private JLabel labelCopyright = new JLabel();
//    private JLabel labelCompany = new JLabel();
    private GridBagLayout layoutMain = new GridBagLayout();
    private Border border = BorderFactory.createEtchedBorder();

    public PSCPMSystemInfoPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout( layoutMain );
        this.setBorder( border );
        labelTitle.setText( "PKI-Enabled Smart Card Password Manager (PSCPM)" );
//        labelAuthor.setText( "Author: " );
        labelCopyright.setText( "© 2016 Francis Johnson (business@francisjohnson.org)" );
//        labelCompany.setText( "Company" );
        this.add( labelTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 0, 15), 0, 0) );
//        this.add( labelAuthor, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 15), 0, 0) );
        this.add( labelCopyright, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 15), 0, 0) );
//        this.add( labelCompany, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 5, 15), 0, 0) );
    }
}
