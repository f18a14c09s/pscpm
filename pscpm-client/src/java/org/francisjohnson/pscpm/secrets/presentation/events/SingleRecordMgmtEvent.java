package org.francisjohnson.pscpm.secrets.presentation.events;

import java.io.Serializable;

import org.francisjohnson.pscpm.general.data.BreadAction;


public class SingleRecordMgmtEvent<RecordType extends Serializable> extends PSCPMEvent {
    private RecordType record;
    private BreadAction action;

    public SingleRecordMgmtEvent(Object object, BreadAction action,
                                 RecordType record) {
        super(object);
        setAction(action);
        setRecord(record);
    }

    public void setRecord(RecordType record) {
        this.record = record;
    }

    public RecordType getRecord() {
        return record;
    }

    public void setAction(BreadAction action) {
        this.action = action;
    }

    public BreadAction getAction() {
        return action;
    }
}
