package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.francisjohnson.pscpm.general.services.MapUtil;


public class Hyperlink extends JLabel {
    public static interface HyperlinkListener {
        void linkActivated(InputEvent evt);
    }

    private final class CombinationMouseKeyListener implements MouseListener,
                                                               KeyListener {
        private HyperlinkListener activation;

        public CombinationMouseKeyListener(HyperlinkListener actionListener) {
            setActivation(actionListener);
        }

        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (getActivation() != null) {
                    getActivation().linkActivated(e);
                }
            }
        }

        private void setActivation(HyperlinkListener actionListener) {
            this.activation = actionListener;
        }

        private HyperlinkListener getActivation() {
            return activation;
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (getActivation() != null) {
                    getActivation().linkActivated(e);
                }
            }
        }
    }

    public Hyperlink() {
        super();
        init();
    }

    public Hyperlink(String string) {
        super(string);
        init();
    }

    public Hyperlink(Icon image) {
        super(image);
        init();
    }

    private void init() {
        if (getFont() != null) {
            setFont(getFont().deriveFont(MapUtil.asMap(MapUtil.entry(TextAttribute.FOREGROUND,
                                                                     Color.BLUE), MapUtil.entry(TextAttribute.UNDERLINE,
                                                                     TextAttribute.UNDERLINE_ON))));
        }
        // TODO: Huh?
//        addActivationListener(null);
    }

    public void addActivationListener(HyperlinkListener lsnr) {
        CombinationMouseKeyListener myListener =
            new CombinationMouseKeyListener(lsnr);
        super.addMouseListener(myListener);
        super.addKeyListener(myListener);
    }
}
