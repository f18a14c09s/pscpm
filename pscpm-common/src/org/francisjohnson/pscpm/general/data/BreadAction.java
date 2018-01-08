package org.francisjohnson.pscpm.general.data;

public enum BreadAction {
    BROWSE("Browse"),
    READ("Read"),
    EDIT("Edit"),
    ADD("Add"),
    DELETE("Delete");
    private String label;

    private void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    private BreadAction(String label) {
        setLabel(label);
    }
}
