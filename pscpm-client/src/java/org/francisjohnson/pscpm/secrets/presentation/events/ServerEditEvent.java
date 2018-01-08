package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;

public class ServerEditEvent extends SingleRecordMgmtEvent<ServerSecret> {
    public ServerEditEvent(Object source, ServerSecret record) {
        super(source, BreadAction.EDIT, record);
    }
}
