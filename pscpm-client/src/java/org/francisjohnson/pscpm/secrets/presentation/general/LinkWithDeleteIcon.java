package org.francisjohnson.pscpm.secrets.presentation.general;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import static org.francisjohnson.pscpm.secrets.presentation.general.Hyperlink.HyperlinkListener;


public class LinkWithDeleteIcon extends JPanel {
    private Hyperlink hyperlink = new Hyperlink();
    private Hyperlink deleteIcon =
        new Hyperlink(new ImageIcon(getClass().getResource("/jlfgr/toolbarButtonGraphics/general/Delete16.gif")));
    private boolean editable;

    public LinkWithDeleteIcon(String label, boolean editable) {
        if (label != null) {
            getHyperlink().setText(label);
        }
        setEditable(editable);
        jbInit();
    }

    private void jbInit() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        add(getHyperlink());
        if (isEditable()) {
            add(getDeleteIcon());
        }
        add(Box.createHorizontalGlue());
    }

    private void setHyperlink(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }

    private Hyperlink getHyperlink() {
        return hyperlink;
    }

    private void setDeleteIcon(Hyperlink deleteIcon) {
        this.deleteIcon = deleteIcon;
    }

    private Hyperlink getDeleteIcon() {
        return deleteIcon;
    }

    public void addActivationListener(HyperlinkListener lsnr) {
        getHyperlink().addActivationListener(lsnr);
    }

    public void addDeletionListener(HyperlinkListener lsnr) {
        getDeleteIcon().addActivationListener(lsnr);
    }

    public void setText(String text) {
        getHyperlink().setText(text);
    }

    private void setEditable(boolean editable) {
        this.editable = editable;
        refreshLayout();
    }

    private boolean isEditable() {
        return editable;
    }
}
