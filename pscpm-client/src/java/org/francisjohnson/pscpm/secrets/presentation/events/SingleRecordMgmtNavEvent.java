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
package org.francisjohnson.pscpm.secrets.presentation.events;

import java.io.Serializable;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;


public class SingleRecordMgmtNavEvent<RecordType extends Serializable>
//extends NavigationEvent
{
    private transient SingleRecordMgmtListener<RecordType> recordManager;
    private RecordType record;
    private boolean editable = false;
    private BreadAction action;

//    public SingleRecordMgmtNavEvent(Object source, PSCPMViewId fromViewId,
//                                    PSCPMOutcome fromOutcome,
//                                    SingleRecordMgmtListener<RecordType> recordManager) {
//        super(source, fromViewId, fromOutcome);
//        setRecordManager(recordManager);
//    }
//
//    public SingleRecordMgmtNavEvent(Object source, PSCPMViewId fromViewId,
//                                    PSCPMOutcome fromOutcome,
//                                    SingleRecordMgmtListener<RecordType> recordManager,
//                                    RecordType record) {
//        super(source, fromViewId, fromOutcome);
//        setRecordManager(recordManager);
//        setRecord(record);
//    }

    public SingleRecordMgmtNavEvent(SingleRecordMgmtListener<RecordType> recordManager,
                                    RecordType record,
                                    BreadAction action,
                                    boolean editable) {
//        super(source, fromViewId, fromOutcome);
        setRecordManager(recordManager);
        setRecord(record);
        setAction(action);
        setEditable(editable);
    }

    private void setRecordManager(SingleRecordMgmtListener<RecordType> recordManager) {
        this.recordManager = recordManager;
    }

    public SingleRecordMgmtListener<RecordType> getRecordManager() {
        return recordManager;
    }

    private void setRecord(RecordType record) {
        this.record = record;
    }

    public RecordType getRecord() {
        return record;
    }

    private void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    private void setAction(BreadAction action) {
        this.action = action;
    }

    public BreadAction getAction() {
        return action;
    }
}
