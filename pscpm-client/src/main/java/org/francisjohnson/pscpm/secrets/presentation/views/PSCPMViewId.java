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
package org.francisjohnson.pscpm.secrets.presentation.views;

public enum PSCPMViewId {
    WILDCARD("* (any view)"),
    LOGIN_FORM("Login Form"),
    SERVER_BROWSER("Server Secret Browser"),
    NEW_SERVER_FORM("New Server Secret Form"),
    SERVER_MGMT_FORM("Server Secret Mgmt Form"),
    NEW_ACCOUNT_FORM("New User Account Form"),
    ACCOUNT_MGMT_FORM("User Account Mgmt Form");
    private String displayName;

    private PSCPMViewId(String displayName) {
        setDisplayName(displayName);
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
