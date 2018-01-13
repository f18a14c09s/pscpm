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
package org.francisjohnson.pscpm.secrets.presentation.general;

import java.awt.Dimension;

import java.io.Serializable;

import javax.swing.JComponent;


public abstract class FormField<BeanClass extends Object, ComponentClass extends JComponent> implements Serializable {
    private static final long serialVersionUID = 1;
    private String label;
    private Dimension labelMinimumPreferredSize;
    private Dimension fieldMinimumPreferredSize;
    private ComponentClass component;
    private boolean scrollingNeeded = false;

    public FormField(String label, Dimension labelMinimumPreferredSize,
                     Dimension fieldMinimumPreferredSize,
                     ComponentClass component, boolean scrollingNeeded) {
        setLabel(label);
        setLabelMinimumPreferredSize(labelMinimumPreferredSize);
        setFieldMinimumPreferredSize(fieldMinimumPreferredSize);
        setComponent(component);
        setScrollingNeeded(scrollingNeeded);
    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    private void setLabelMinimumPreferredSize(Dimension labelMinimumPreferredSize) {
        this.labelMinimumPreferredSize = labelMinimumPreferredSize;
    }

    public Dimension getLabelMinimumPreferredSize() {
        return labelMinimumPreferredSize;
    }

    private void setFieldMinimumPreferredSize(Dimension fieldMinimumPreferredSize) {
        this.fieldMinimumPreferredSize = fieldMinimumPreferredSize;
    }

    public Dimension getFieldMinimumPreferredSize() {
        return fieldMinimumPreferredSize;
    }

    private void setScrollingNeeded(boolean scrollingNeeded) {
        this.scrollingNeeded = scrollingNeeded;
    }

    public boolean isScrollingNeeded() {
        return scrollingNeeded;
    }

    private void setComponent(ComponentClass component) {
        this.component = component;
    }

    public ComponentClass getComponent() {
        return component;
    }

    public abstract void syncToField(BeanClass c);

    public abstract void syncFromField(BeanClass c);
}
