package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;

public class ServerAddEvent extends SingleRecordMgmtEvent<ServerSecret> {
    public ServerAddEvent(Object source, ServerSecret record) {
        super(source, BreadAction.ADD, record);
    }
}
