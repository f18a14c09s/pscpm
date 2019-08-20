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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


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
