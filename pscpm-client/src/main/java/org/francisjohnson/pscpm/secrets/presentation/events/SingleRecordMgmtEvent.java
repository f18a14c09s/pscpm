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
