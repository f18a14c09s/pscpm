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

import java.io.Serializable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public abstract class PSCPMTableColumnMetadata<BeanClass, PropertyClass> implements Serializable {
    private String columnName;
    private Number minWidth;
    private Number preferredWidth;
    private Number maxWidth;
    private Number fixedWidth;
    private Class<PropertyClass> propertyClass;
    private boolean editable = false;
    private transient TableCellRenderer renderer;
    private transient TableCellEditor editor;

    public PSCPMTableColumnMetadata() {
    }

    public PSCPMTableColumnMetadata(String columnName, Number minWidth,
                                    Number preferredWidth, Number maxWidth,
                                    Class<PropertyClass> propertyClass) {
        setColumnName(columnName);
        setMinWidth(minWidth);
        setPreferredWidth(preferredWidth);
        setMaxWidth(maxWidth);
        setPropertyClass(propertyClass);
    }

    public PSCPMTableColumnMetadata(String columnName, Number fixedWidth,
                                    Class<PropertyClass> propertyClass) {
        setColumnName(columnName);
        setFixedWidth(fixedWidth);
        setPropertyClass(propertyClass);
    }

    public PSCPMTableColumnMetadata(String columnName, Number fixedWidth,
                                    Class<PropertyClass> propertyClass,
                                    boolean editable) {
        setColumnName(columnName);
        setFixedWidth(fixedWidth);
        setPropertyClass(propertyClass);
        setEditable(editable);
    }

    public PSCPMTableColumnMetadata(String columnName, Number fixedWidth,
                                    Class<PropertyClass> propertyClass,
                                    boolean editable,
                                    TableCellRenderer renderer,
                                    TableCellEditor editor) {
        setColumnName(columnName);
        setFixedWidth(fixedWidth);
        setPropertyClass(propertyClass);
        setEditable(editable);
        setRenderer(renderer);
        setEditor(editor);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Number getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Number minWidth) {
        this.minWidth = minWidth;
    }

    public Number getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Number maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setPropertyClass(Class<PropertyClass> propertyClass) {
        this.propertyClass = propertyClass;
    }

    public Class<PropertyClass> getPropertyClass() {
        return propertyClass;
    }

    public abstract PropertyClass getValue(BeanClass bean);

    private void setFixedWidth(Number fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public Number getFixedWidth() {
        return fixedWidth;
    }

    private void setPreferredWidth(Number preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public Number getPreferredWidth() {
        return preferredWidth;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    private void setRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    public TableCellRenderer getRenderer() {
        return renderer;
    }

    private void setEditor(TableCellEditor editor) {
        this.editor = editor;
    }

    public TableCellEditor getEditor() {
        return editor;
    }
}
