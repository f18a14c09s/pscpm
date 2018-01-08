package org.francisjohnson.pscpm.secrets.presentation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import javax.swing.UIManager;

import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMMainContentPanel;
import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMMainJFrame;


public class PSCPMClientMain {
    public PSCPMClientMain(Component demoChild) {
        JFrame frame = new PSCPMMainJFrame(demoChild);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Entry point testing/prototyping other views.
     * @param contentComponent
     */
    public static void main(Component contentComponent) {
        new PSCPMClientMain(contentComponent);
    }

    /**
     * Entry point to the application.
     * @param args
     */
    public static void main(String... args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        main(new PSCPMMainContentPanel());
    }
}
