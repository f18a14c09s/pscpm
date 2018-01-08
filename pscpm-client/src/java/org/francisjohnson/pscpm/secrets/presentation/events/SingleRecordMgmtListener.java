package org.francisjohnson.pscpm.secrets.presentation.events;

import java.awt.Component;

import java.io.Serializable;

import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.secrets.presentation.general.PSCPMPanel;


public abstract class SingleRecordMgmtListener<RecordType extends Serializable> implements PSCPMListener {
    private NavigationListener navigator;

    public SingleRecordMgmtListener() {
    }

    public SingleRecordMgmtListener(NavigationListener navigator) {
        setNavigator(navigator);
    }

    public abstract void savePressed(SingleRecordMgmtEvent<RecordType> evt);

    public void cancelPressed(SingleRecordMgmtEvent<RecordType> evt) {
        System.out.println((evt.getRecord() == null ? "" :
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
