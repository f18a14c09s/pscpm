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

import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;

public class LoginSucceededEvent extends NavigationEvent {
    private transient SecretsFacade session;

    public LoginSucceededEvent(Object source, SecretsFacade session) {
        super(source, PSCPMViewId.LOGIN_FORM, PSCPMOutcome.SUCCESS);
        setSession(session);
    }

    private void setSession(SecretsFacade session) {
        this.session = session;
    }

    public SecretsFacade getSession() {
        return session;
    }
}
