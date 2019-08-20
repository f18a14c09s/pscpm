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
