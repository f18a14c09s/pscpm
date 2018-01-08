package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.general.data.BreadAction;
import org.francisjohnson.pscpm.secrets.data.UserAccount;

public class CredentialEditEvent extends SingleRecordMgmtEvent<UserAccount> {
    public CredentialEditEvent(Object source, UserAccount record) {
        super(source, BreadAction.EDIT, record);
    }
}
