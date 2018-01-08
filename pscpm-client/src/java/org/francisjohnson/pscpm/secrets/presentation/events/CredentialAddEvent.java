package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.data.UserAccount;


public class CredentialAddEvent extends SingleRecordMgmtEvent<UserAccount> {
    public CredentialAddEvent(Object source, UserAccount record) {
        super(source, BreadAction.ADD, record);
    }
}
