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
