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
package f18a14c09s.pscpm.secrets.presentation.events;

import f18a14c09s.pscpm.general.data.BreadAction;

public enum PSCPMOutcome {
    DONE,
    SUCCESS,
    FAILURE,
    BROWSE_SERVERS(BreadAction.BROWSE, "Server Secret Browser"),
    ADD_SERVER(BreadAction.ADD, "Server Secret Add"),
    // Feel free to change to EDIT.  Doing so will imply that the default mode
    // is to edit the server.
    MANAGE_SERVER(BreadAction.READ, "Server Secret Mgmt"),
    ADD_ACCOUNT(BreadAction.ADD, "User Account Add"),
    // Feel free to change to EDIT.  Doing so will imply that the default mode
    // is to edit the account.
    MANAGE_ACCOUNT(BreadAction.READ, "User Account Mgmt"),
    LOGOUT;
    private BreadAction action;
    private String featureName;

    private PSCPMOutcome() {
    }

    private PSCPMOutcome(BreadAction action) {
        setAction(action);
    }

    private PSCPMOutcome(BreadAction action, String functionalityDisplayName) {
        setAction(action);
        setFeatureName(functionalityDisplayName);
    }

    private void setAction(BreadAction action) {
        this.action = action;
    }

    public BreadAction getAction() {
        return action;
    }

    private void setFeatureName(String functionalityDisplayName) {
        this.featureName = functionalityDisplayName;
    }

    public String getFeatureName() {
        return featureName;
    }
}
