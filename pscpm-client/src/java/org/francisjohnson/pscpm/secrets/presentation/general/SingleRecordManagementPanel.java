package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.events.SingleRecordMgmtEvent;
import org.francisjohnson.pscpm.secrets.presentation.events.SingleRecordMgmtListener;
import org.francisjohnson.pscpm.secrets.presentation.events.SingleRecordMgmtNavEvent;


public class SingleRecordManagementPanel<RecordType extends Serializable> extends PSCPMPanel {
    private static final long serialVersionUID = 1;
    private static final BreadAction DEFAULT_ACTION = BreadAction.READ;

    /**
     * The content container's CardLayout displays this view's main content when
     * on calls show(MAIN_VIEW_ID).
     */
    public static final String MAIN_VIEW_ID = "MAIN_CONTENT";
    private RecordType record;
    private BreadAction action;
    private transient SingleRecordMgmtListener<RecordType> recordManager;
    private CardLayout nav = new CardLayout();
    private JPanel navContainer = new JPanel();
    private Box mainContainer = Box.createVerticalBox();
    private JPanel formPanel = new JPanel(new BorderLayout());
    private Box formBodyBox = Box.createVerticalBox();
    private Box editOrSaveButtonBox = Box.createHorizontalBox();
    private PSCPMButton editButton = new PSCPMButton("Edit");
    private PSCPMButton saveButton = new PSCPMButton("Save");
    private PSCPMButton cancelButton = new PSCPMButton("Cancel");
    /**
     * Indicates whether the user is allowed to attempt to edit the record.
     */
    private boolean editable = false;

    public SingleRecordManagementPanel() {
        this(DEFAULT_ACTION);
    }

    public SingleRecordManagementPanel(BreadAction action) {
        super();
        setAction(action);
    }

    public SingleRecordManagementPanel(RecordType record) {
        this(DEFAULT_ACTION, record);
    }

    public SingleRecordManagementPanel(BreadAction action, RecordType record) {
        super();
        setAction(action);
        setRecord(record);
        // You can't call jbInit() here because it potentially, indirectly
        // depends on variables from subclasses that haven't been initialized
        // yet.
        //        try {
        //            jbInit();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
    }

    protected void jbInit() throws Exception {
        addButtonListeners();
        // Setup UI fields
        initFormFields();
        layoutSections();
        syncBorder();
        syncView();
    }

    private void addButtonListeners() {
        for (PSCPMButton button :
             Arrays.asList(getSaveButton(), getCancelButton(),
                           getEditButton())) {
            button.removeAllActionListeners();
        }
        //
        getSaveButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SingleRecordManagementPanel<RecordType> outer =
                        SingleRecordManagementPanel.this;
                    outer.syncValuesFromFields();
                    outer.getRecordManager().savePressed(new SingleRecordMgmtEvent<RecordType>(e.getSource(),
                                                                                               outer.getAction(),
                                                                                               outer.getRecord()));
                }
            });
        getCancelButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SingleRecordManagementPanel<RecordType> outer =
                        SingleRecordManagementPanel.this;
                    outer.getRecordManager().cancelPressed(new SingleRecordMgmtEvent<RecordType>(e.getSource(),
                                                                                                 outer.getAction(),
                                                                                                 outer.getRecord()));
                }
            });
        getEditButton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setAction(BreadAction.EDIT);
                    syncView();
                }
            });
    }

    private void syncBorder() {
        getMainContainer().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,
                                                                                                     1),
                                                                      getAction().getLabel() +
                                                                      " " +
                                                                      getEntityName() +
                                                                      ":"));
    }

    protected void syncView() {
        Container container = getEditOrSaveButtonBox();
        container.removeAll();
        if (getAction() == BreadAction.READ) {
            container.add(getEditButton(), null);
        } else if (!isReadOnly()) {
            container.add(getSaveButton(), null);
        }
        //
        for (FormField<?, ?> field : getFields()) {
            field.getComponent().setEnabled(!isReadOnly());
        }
        syncBorder();
        //        revalidate();
        //        for (FormField<RecordType, ?> field : getFields()) {
        //            field.getComponent().revalidate();
        //        }
        //        invalidate();
        repaint();
        revalidate();
    }

    protected void layoutSections() {
        removeAll();
        getNavContainer().removeAll();
        getFormPanel().removeAll();
        setLayout(new BorderLayout());
        getNavContainer().setLayout(getNav());
        Box formFooterPanel = Box.createHorizontalBox();
        // Level 1 - so that this class is a regular panel rather than a more
        // specialized scroll-pane.
        add(getNavContainer(), BorderLayout.CENTER);
        // Level 2 - for the main scrollbar
        // Level 3 - for swapping out the content i.e. nav
        getNavContainer().add(getMainContainer(), MAIN_VIEW_ID);
        getNav().show(getNavContainer(), MAIN_VIEW_ID);
        // Level 4 - for allowing multiple children - not affected by nav
        layoutMainContainer();
        // Level 5 - layout of the form
        // Level 6 - footer and form body
        getFormPanel().add(getFormBodyBox(), BorderLayout.CENTER);
        getFormPanel().add(formFooterPanel, BorderLayout.SOUTH);
        // Level 7 - layout of the footer sub-components
        // Note that the form body's sub-components were populated as part of
        // initialization of the form fields.
        formFooterPanel.add(Box.createHorizontalGlue());
        if (isEditable()) {
            formFooterPanel.add(getEditOrSaveButtonBox());
        }
        formFooterPanel.add(getCancelButton());
        formFooterPanel.add(Box.createHorizontalGlue());
        //
        //
        //
        getFormPanel().setMaximumSize(new Dimension(999999,
                                                    getFormPanel().getPreferredSize().height));
        getFormBodyBox().setMaximumSize(new Dimension(999999,
                                                      getFormBodyBox().getPreferredSize().height));
    }

    protected void layoutMainContainer() {
        getMainContainer().removeAll();
        getMainContainer().add(Box.createRigidArea(new Dimension(1, 30)));
        getMainContainer().add(getFormPanel());
        getMainContainer().add(Box.createRigidArea(new Dimension(1, 30)));
    }

    protected void initFormFields() {
        getFormBodyBox().removeAll();
        List<JLabel> labels = new ArrayList<JLabel>(getFields().size());
        for (FormField<RecordType, ?> field : getFields()) {
            Box fieldRowBox = Box.createHorizontalBox();
            fieldRowBox.add(Box.createRigidArea(new Dimension(100, 1)));
            JLabel labelComponent = new JLabel(field.getLabel() + ":");
            labels.add(labelComponent);
            labelComponent.setFont(new Font("Arial", 0, 12));
            labelComponent.setHorizontalAlignment(SwingConstants.RIGHT);
            labelComponent.setVerticalAlignment(SwingConstants.TOP);
            fieldRowBox.add(labelComponent);
            fieldRowBox.add(Box.createRigidArea(new Dimension(10, 1)));
            JComponent fieldComponent = field.getComponent();
            fieldComponent.setEnabled(!isReadOnly());
            //            fieldRowBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            //            labelComponent.setPreferredSize(field.getLabelMinimumPreferredSize());
            //            labelComponent.setMinimumSize(field.getLabelMinimumPreferredSize());
            //            labelComponent.setAlignmentX(Component.RIGHT_ALIGNMENT);
            //            labelComponent.setAlignmentY(Component.TOP_ALIGNMENT);
            // Here the value is theoretically copied from the bean to the field.
            if (field.isScrollingNeeded()) {
                JScrollPane optionalScrollPane = new JScrollPane();
                //                optionalScrollPane.setPreferredSize(field.getFieldMinimumPreferredSize());
                //                optionalScrollPane.setMaximumSize(field.getFieldMinimumPreferredSize());
                optionalScrollPane.getViewport().add(fieldComponent);
                optionalScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
                fieldRowBox.add(optionalScrollPane);
            } else {
                //                fieldComponent.setPreferredSize(field.getFieldMinimumPreferredSize());
                //                fieldComponent.setMaximumSize(field.getFieldMinimumPreferredSize());
                fieldComponent.setAlignmentY(Component.TOP_ALIGNMENT);
                fieldRowBox.add(fieldComponent);
            }
            //            fieldRowBox.add(Box.createGlue());
            fieldRowBox.add(Box.createRigidArea(new Dimension(100, 1)));
            getFormBodyBox().add(fieldRowBox);
        }
        normalizeLabelWidths(labels);
        syncValuesToFields();
    }

    protected List<FormField<RecordType, ?>> getFields() {
        return new ArrayList<FormField<RecordType, ?>>();
    }

    private void syncValuesFromFields() {
        if (getFields() != null) {
            for (FormField<RecordType, ?> field : getFields()) {
                field.syncFromField(getRecord());
            }
        }
    }

    private void syncValuesToFields() {
        if (getFields() != null) {
            for (FormField<RecordType, ?> field : getFields()) {
                field.syncToField(getRecord());
            }
        }
    }

    protected boolean isReadOnly() {
        return getAction() != BreadAction.ADD &&
            getAction() != BreadAction.EDIT;
    }

    protected void setRecord(RecordType record) {
        this.record = record;
        syncValuesToFields();
    }

    protected RecordType getRecord() {
        return record;
    }

    protected void setSaveButton(PSCPMButton saveButton) {
        this.saveButton = saveButton;
    }

    protected PSCPMButton getSaveButton() {
        return saveButton;
    }

    protected void setEditButton(PSCPMButton editButton) {
        this.editButton = editButton;
    }

    protected PSCPMButton getEditButton() {
        return editButton;
    }

    protected void setCancelButton(PSCPMButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    protected PSCPMButton getCancelButton() {
        return cancelButton;
    }

    protected void setAction(BreadAction action) {
        this.action = action;
    }

    public BreadAction getAction() {
        return action;
    }

    protected void setEditOrSaveButtonBox(Box editOrSaveButtonBox) {
        this.editOrSaveButtonBox = editOrSaveButtonBox;
    }

    protected Box getEditOrSaveButtonBox() {
        return editOrSaveButtonBox;
    }

    protected void setFormPanel(JPanel formPanel) {
        this.formPanel = formPanel;
    }

    protected JPanel getFormPanel() {
        return formPanel;
    }

    protected void setFormBodyBox(Box formBodyBox) {
        this.formBodyBox = formBodyBox;
    }

    protected Box getFormBodyBox() {
        return formBodyBox;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    protected void setRecordManager(SingleRecordMgmtListener<RecordType> recordManager) {
        this.recordManager = recordManager;
    }

    protected SingleRecordMgmtListener<RecordType> getRecordManager() {
        return recordManager;
    }

    public void init(SecretsFacade session,
                     SingleRecordMgmtNavEvent<RecordType> evt) {
        setSession(session);
        setRecord(evt.getRecord());
        //        setAction(evt.getFromOutcome().getAction());
        setAction(evt.getAction());
        setRecordManager(evt.getRecordManager());
        setEditable(evt.isEditable());
        try {
            jbInit();
        } catch (Exception e) {
            //            if (evt.getFromOutcome() == null ||
            //                evt.getFromOutcome().getFeatureName() == null) {
            handleException(evt.getAction().getLabel() +
                            " of the desired record is unavailable.", e);
            //            } else {
            //                handleException("Feature Unavailable",
            //                                evt.getFromOutcome().getFeatureName() +
            //                                " feature unavailable.", e);
            //            }
        }
    }

    public String getEntityName() {
        return "";
    }

    protected void setMainContainer(Box mainGridBag) {
        this.mainContainer = mainGridBag;
    }

    protected Box getMainContainer() {
        return mainContainer;
    }

    private void setNav(CardLayout nav) {
        this.nav = nav;
    }

    private CardLayout getNav() {
        return nav;
    }

    protected void nav(String viewId) {
        getNav().show(getNavContainer(), viewId);
    }

    private void setNavContainer(JPanel navContainer) {
        this.navContainer = navContainer;
    }

    protected JPanel getNavContainer() {
        return navContainer;
    }

    private void setEditable(boolean editable) {
        this.editable = editable;
    }

    private boolean isEditable() {
        return editable;
    }
}
