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
