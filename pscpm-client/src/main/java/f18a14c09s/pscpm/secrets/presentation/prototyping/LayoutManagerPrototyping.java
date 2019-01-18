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
package f18a14c09s.pscpm.secrets.presentation.prototyping;

import java.awt.BorderLayout;
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

import f18a14c09s.pscpm.secrets.presentation.PSCPMClientMain;


public class LayoutManagerPrototyping extends JPanel {
    public LayoutManagerPrototyping() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        LayoutManagerPrototyping test = new LayoutManagerPrototyping();
        PSCPMClientMain.main(test);
    }

    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(createTestContainer(Color.BLUE, 200));
        verticalBox.add(createTestForm());
        verticalBox.add(createTestContainer(Color.GREEN, 100));
        verticalBox.add(createTestContainer(Color.MAGENTA, 400));
        verticalBox.add(createTestContainer(Color.ORANGE, 800));
        verticalBox.add(createTestContainer(Color.RED, 300));
        verticalBox.add(createTestContainer(Color.YELLOW, 50));
        verticalBox.add(Box.createVerticalGlue());
        scrollPane.getViewport().add(verticalBox);
    }

    private Component createTestContainer(Color borderColor, int height) {
        JPanel retval = new JPanel();
        Dimension heightOnly = new Dimension(0, height);
        //        heightOnly.height = height;
        retval.setSize(heightOnly);
        retval.setPreferredSize(heightOnly);
        retval.setBorder(BorderFactory.createDashedBorder(borderColor, 3, 7, 5,
                                                          false));
        return retval;
    }

    private Component createTestForm() {
        JPanel testingVertBoxBorderLayoutVertBox=new JPanel(new BorderLayout());
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
        testingVertBoxBorderLayoutVertBox.add(retval,BorderLayout.CENTER);
        return testingVertBoxBorderLayoutVertBox;
//        return retval;
    }
}
