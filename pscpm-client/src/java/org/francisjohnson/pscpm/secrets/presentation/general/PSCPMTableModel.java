package org.francisjohnson.pscpm.secrets.presentation.general;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class PSCPMTableModel<BeanClass extends Serializable> extends AbstractTableModel {
    private List<PSCPMTableColumnMetadata<BeanClass, ?>> columns =
        new ArrayList<PSCPMTableColumnMetadata<BeanClass, ?>>();
    private List<BeanClass> data = new ArrayList<BeanClass>();

    public PSCPMTableModel() {
    }

    public PSCPMTableModel(List<PSCPMTableColumnMetadata<BeanClass, ?>> columns,
                           List<BeanClass> data) {
        setColumns(columns);
        setData(data);
    }

    @Override
    public Object getValueAt(int rowNum, int colNum) {
        return rowNum > getData().size() || colNum > getColumns().size() ?
               null : getColumns().get(colNum).getValue(getData().get(rowNum));
    }

    @Override
    public int getRowCount() {
        return getData() == null ? 0 : getData().size();
    }

    @Override
    public int getColumnCount() {
        return getColumns().size();
    }

    @Override
    public Class<?> getColumnClass(int colNum) {
        return getColumns().get(colNum).getPropertyClass();
    }

    @Override
    public String getColumnName(int colNum) {
        return getColumns().get(colNum).getColumnName();
    }

    private void setData(List<BeanClass> data) {
        this.data = data;
    }

    private List<BeanClass> getData() {
        return data;
    }

    private void setColumns(List<PSCPMTableColumnMetadata<BeanClass, ?>> columns) {
        this.columns = columns;
    }

    public List<PSCPMTableColumnMetadata<BeanClass, ?>> getColumns() {
        return columns;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumns().get(columnIndex).isEditable();
    }

    public void addRow(BeanClass rowData) {
        getData().add(rowData);
    }

    public int removeRow(Object rowData) {
        int retval = getData().indexOf(rowData);
        getData().remove(rowData);
        return retval;
    }
}
