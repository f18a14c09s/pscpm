package org.francisjohnson.pscpm.secrets.presentation.prototyping;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.francisjohnson.pscpm.secrets.presentation.general.MyPanelCollection;
import org.francisjohnson.pscpm.secrets.presentation.PSCPMClientMain;
import org.francisjohnson.pscpm.secrets.presentation.general.PSCPMPanel;


public class IncompleteServerSecretMgmtPanelAlternative extends PSCPMPanel {
    public static final String MAIN_CONTENT_VIEW_ID = "MAIN_CONTENT";
    private JScrollPane mainScrollPane;
    private CardLayout nav;
    private JPanel navContainer;
    private Box mainVerticalBox;
    private Box formBody;
    private Box formFooter;
    private MyPanelCollection adminUserSection;
    private MyPanelCollection appUserSection;

    public IncompleteServerSecretMgmtPanelAlternative() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        IncompleteServerSecretMgmtPanelAlternative test = new IncompleteServerSecretMgmtPanelAlternative();
        PSCPMClientMain.main(test);
    }

    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        setMainScrollPane(new JScrollPane());
        setNav(new CardLayout());
        setNavContainer(new JPanel(getNav()));
        setMainVerticalBox(Box.createVerticalBox());
        setFormBody(Box.createVerticalBox());
        setFormFooter(Box.createHorizontalBox());
        setAdminUserSection(new MyPanelCollection());
        setAppUserSection(new MyPanelCollection());
        //
        add(getMainScrollPane());
        getMainScrollPane().getViewport().add(getNavContainer());
        getNav().addLayoutComponent(getMainVerticalBox(),
                                    MAIN_CONTENT_VIEW_ID);
        getNavContainer().add(getMainVerticalBox());
        getMainVerticalBox().add(Box.createRigidArea(new Dimension(1, 20)));
        //        getMainVerticalBox().add(getFormBody());
        getMainVerticalBox().add(createTestForm());
        getMainVerticalBox().add(getFormFooter());
        getMainVerticalBox().add(Box.createRigidArea(new Dimension(1, 10)));
        getMainVerticalBox().add(getAdminUserSection());
        getMainVerticalBox().add(Box.createRigidArea(new Dimension(1, 10)));
        getMainVerticalBox().add(getAppUserSection());
        getMainVerticalBox().add(Box.createVerticalGlue());
        //
        getMainVerticalBox().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                                       1),
                                                                        "Read Server Secret:"));
        getAdminUserSection().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                                        1),
                                                                         "Administrative Users:"));
        getAppUserSection().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                                      1),
                                                                       "Application Users:"));
        //
        getNav().show(getNavContainer(), MAIN_CONTENT_VIEW_ID);
    }

    private Component createTestForm() {
        JPanel testingVertBoxBorderLayoutVertBox =
            new JPanel(new BorderLayout());
        Box retval = Box.createVerticalBox();
        List<String> labels =
            Arrays.asList("First Name", "Last Name", "Email Address",
                          "Address Line 1", "Address Line 2", "City",
                          "State or Province", "Postal Code", "Country");
        List<JLabel> labelComponents = new ArrayList<JLabel>();
        int labelMaxWidth = 0;
        for (String fieldName : labels) {
            Box fieldRow = Box.createHorizontalBox();
            //
            fieldRow.add(Box.createRigidArea(new Dimension(40, 1)));
            //
            JLabel fieldLabel = new JLabel(fieldName + ":");
            fieldLabel.setHorizontalAlignment(JLabel.RIGHT);
            fieldLabel.setVerticalAlignment(JLabel.TOP);
            fieldRow.add(fieldLabel);
            labelComponents.add(fieldLabel);
            labelMaxWidth =
                    Math.max(Math.max(Math.max(labelMaxWidth, fieldLabel.getWidth()),
                                      fieldLabel.getPreferredSize().width),
                             fieldLabel.getPreferredSize().width);
            //
            fieldRow.add(Box.createRigidArea(new Dimension(10, 1)));
            //
            JTextField inputField = new JTextField();
            fieldRow.add(inputField);
            //
            fieldRow.add(Box.createRigidArea(new Dimension(100, 1)));
            retval.add(fieldRow);
        }
        for (JLabel label : labelComponents) {
            Dimension dim = null;
            dim = label.getSize();
            dim.width = labelMaxWidth;
            label.setSize(dim);
            dim = label.getPreferredSize();
            dim.width = labelMaxWidth;
            label.setPreferredSize(dim);
        }
        testingVertBoxBorderLayoutVertBox.add(retval, BorderLayout.CENTER);
        testingVertBoxBorderLayoutVertBox.setMaximumSize(new Dimension(testingVertBoxBorderLayoutVertBox.getMaximumSize().width,testingVertBoxBorderLayoutVertBox.getPreferredSize().height));
        return testingVertBoxBorderLayoutVertBox;
        //        return retval;
    }

    private void setMainScrollPane(JScrollPane mainScrollPane) {
        this.mainScrollPane = mainScrollPane;
    }

    private JScrollPane getMainScrollPane() {
        return mainScrollPane;
    }

    private void setNavContainer(JPanel navContainer) {
        this.navContainer = navContainer;
    }

    private JPanel getNavContainer() {
        return navContainer;
    }

    private void setNav(CardLayout nav) {
        this.nav = nav;
    }

    private CardLayout getNav() {
        return nav;
    }

    private void setMainVerticalBox(Box mainVerticalBox) {
        this.mainVerticalBox = mainVerticalBox;
    }

    private Box getMainVerticalBox() {
        return mainVerticalBox;
    }

    private void setFormBody(Box formBody) {
        this.formBody = formBody;
    }

    private Box getFormBody() {
        return formBody;
    }

    private void setFormFooter(Box formFooter) {
        this.formFooter = formFooter;
    }

    private Box getFormFooter() {
        return formFooter;
    }

    private void setAdminUserSection(MyPanelCollection adminUserSection) {
        this.adminUserSection = adminUserSection;
    }

    private MyPanelCollection getAdminUserSection() {
        return adminUserSection;
    }

    private void setAppUserSection(MyPanelCollection appUserSection) {
        this.appUserSection = appUserSection;
    }

    private MyPanelCollection getAppUserSection() {
        return appUserSection;
    }
}
