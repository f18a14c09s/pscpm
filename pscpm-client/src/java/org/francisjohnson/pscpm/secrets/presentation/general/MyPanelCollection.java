package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class MyPanelCollection extends JPanel {
    private Box header = new Box(BoxLayout.LINE_AXIS);

    public MyPanelCollection() {
        super(new BorderLayout());
        add(getHeader(), BorderLayout.PAGE_START);
    }

    private void setHeader(Box header) {
        this.header = header;
    }

    public Box getHeader() {
        return header;
    }

    public void setTable(JTable table) {
        if (table != null) {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(table);
            add(scrollPane, BorderLayout.CENTER);
//            add(table, BorderLayout.CENTER);
        }
    }
}
