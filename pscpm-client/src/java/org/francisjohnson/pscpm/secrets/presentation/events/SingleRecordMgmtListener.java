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

import java.awt.Component;

import java.io.Serializable;
import java.util.logging.Logger;

import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.secrets.presentation.general.PSCPMPanel;


public abstract class SingleRecordMgmtListener<RecordType extends Serializable> implements PSCPMListener {
    private NavigationListener navigator;
    private final Logger _log=Logger.getLogger(getClass().getName());
    private Logger getLog(){return _log;}

    public SingleRecordMgmtListener() {
    }

    public SingleRecordMgmtListener(NavigationListener navigator) {
        setNavigator(navigator);
    }

    public abstract void savePressed(SingleRecordMgmtEvent<RecordType> evt);

    public void cancelPressed(SingleRecordMgmtEvent<RecordType> evt) {
        getLog().info((evt.getRecord() == null ? "" :
                            evt.getRecord().getClass().getSimpleName()) + " " +
                           (evt.getAction()) + " cancelled.");
        Component compon =
            compon = evt.getSource() instanceof Component ? (Component)evt.getSource() :
                     null;
        while (compon != null && !(compon instanceof PSCPMPanel)) {
            compon = compon.getParent();
        }
        getNavigator().navigate(new NavigationEvent(evt.getSource(),
                                                    compon instanceof
                                                    PSCPMPanel ?
                                                    ((PSCPMPanel)compon).getViewId() :
                                                    null, PSCPMOutcome.DONE));
    }

    void setNavigator(NavigationListener navigator) {
        this.navigator = navigator;
    }

    NavigationListener getNavigator() {
        return navigator;
    }
}
