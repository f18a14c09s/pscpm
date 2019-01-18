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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import f18a14c09s.pscpm.security.data.UserSecretKey;

public class SecretKeyComboBox extends JComboBox<UserSecretKey> {

    private final Logger _log = Logger.getLogger(getClass().getName());

    private Logger getLog() {
        return _log;
    }
    private final List<UserSecretKey> data = new ArrayList<UserSecretKey>();

    public final class MyModelImpl implements ComboBoxModel<UserSecretKey> {

        private Number selectedId;
        private final List<ListDataListener> dataListeners
                = new ArrayList<ListDataListener>();

        @Override
        public void setSelectedItem(Object selectedItem) {
            setSelectedId(selectedItem == null ? null
                    : ((UserSecretKey) selectedItem).getId());
        }

        public Object getSelectedItem() {
            if (getData() != null && getSelectedId() != null) {
                for (UserSecretKey key : getData()) {
                    if (key.getId().equals(getSelectedId())) {
                        return key;
                    }
                }
            }
            return null;
        }

        public int getSize() {
            SecretKeyComboBox outer = SecretKeyComboBox.this;
            return outer.getData() == null ? 0 : outer.getData().size();
        }

        public UserSecretKey getElementAt(int index) {
            return getData().get(index);
        }

        public void addListDataListener(ListDataListener lsnr) {
            getLog().info(getClass().getSimpleName()
                    + ".  addListDataListener(...).");
            if (lsnr != null) {
                getDataListeners().add(lsnr);
            }
        }

        public void removeListDataListener(ListDataListener lsnr) {
            getLog().info(getClass().getSimpleName()
                    + ".  removeListDataListener(...).");
            if (lsnr != null) {
                getDataListeners().remove(lsnr);
            }
        }

        private void setSelectedId(Number selectedId) {
            this.selectedId = selectedId;
        }

        private Number getSelectedId() {
            return selectedId;
        }

        private List<ListDataListener> getDataListeners() {
            return dataListeners;
        }

        public void notifyDataListeners() {
            if (getData() != null) {
                ListDataEvent evt
                        = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0,
                                getData().size() - 1);
                for (ListDataListener lsnr : getDataListeners()) {
                    lsnr.contentsChanged(evt);
                }
            }
        }
    }
    private transient final MyModelImpl myModel = new MyModelImpl();

    public SecretKeyComboBox() {
        super();
        //        initRenderer();
    }

    public SecretKeyComboBox(List<UserSecretKey> data) {
        super();
        setData(data);
        setModel(getMyModel());
        //        initRenderer();
    }

    //    private void initRenderer() {
    //        setRenderer(new ListCellRenderer() {
    //                public Component getListCellRendererComponent(JList list,
    //                                                              Object value,
    //                                                              int index,
    //                                                              boolean isSelected,
    //                                                              boolean cellHasFocus) {
    //                    return new JLabel(value == null ? "" :
    //                                      ((UserSecretKey)value).getAlias());
    //                }
    //            });
    //    }
    public void setData(List<UserSecretKey> data) {
        getData().clear();
        if (data != null) {
            getData().addAll(data);
        }
        getMyModel().notifyDataListeners();
    }

    private List<UserSecretKey> getData() {
        return data;
    }

    private MyModelImpl getMyModel() {
        return myModel;
    }
}
