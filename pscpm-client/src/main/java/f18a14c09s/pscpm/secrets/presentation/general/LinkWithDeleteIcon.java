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
package f18a14c09s.pscpm.secrets.presentation.general;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import static f18a14c09s.pscpm.secrets.presentation.general.Hyperlink.HyperlinkListener;


public class LinkWithDeleteIcon extends JPanel {
    private Hyperlink hyperlink = new Hyperlink();
    private Hyperlink deleteIcon =
        new Hyperlink(new ImageIcon(getClass().getResource(
                "/jlfgr/toolbarButtonGraphics/general/Delete16.gif")));
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
