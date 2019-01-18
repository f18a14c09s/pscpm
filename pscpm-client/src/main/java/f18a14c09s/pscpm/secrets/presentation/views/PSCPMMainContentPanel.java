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
package f18a14c09s.pscpm.secrets.presentation.views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import f18a14c09s.pscpm.secrets.business.SecretsFacade;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.ACCOUNT_MGMT_FORM;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.LOGIN_FORM;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.NEW_ACCOUNT_FORM;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.NEW_SERVER_FORM;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.SERVER_BROWSER;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.SERVER_MGMT_FORM;
import static f18a14c09s.pscpm.secrets.presentation.views.PSCPMViewId.WILDCARD;
import f18a14c09s.pscpm.secrets.presentation.PSCPMClientMain;
import f18a14c09s.pscpm.secrets.presentation.events.LoginSucceededEvent;
import f18a14c09s.pscpm.secrets.presentation.events.NavigationEvent;
import f18a14c09s.pscpm.secrets.presentation.events.NavigationListener;
import f18a14c09s.pscpm.secrets.presentation.events.PSCPMOutcome;
import f18a14c09s.pscpm.general.services.MapUtil;
import f18a14c09s.pscpm.general.data.Pair;
import f18a14c09s.pscpm.secrets.presentation.general.PSCPMPanel;


public class PSCPMMainContentPanel extends PSCPMPanel implements NavigationListener {
    private ServerSecretsBrowserPanel serverBrowserView;
    private LoginPanel loginView;
    private CardLayout navigationModel = new CardLayout();

    public PSCPMMainContentPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            handleException("PSCPM Main UI Unavailable",
                            "Unable to initialize the system's main UI.", e);
        }
    }

    public static void main(String... args) {
        PSCPMMainContentPanel test = new PSCPMMainContentPanel();
        PSCPMClientMain.main(test);
    }

    private void jbInit() throws Exception {
        setSize(new Dimension(800, 600));
        setLoginView(new LoginPanel());
        setServerBrowserView(new ServerSecretsBrowserPanel());
        initCardLayout();
        setNavigator(this);
        for (Component compon : getComponents()) {
            if (compon instanceof PSCPMPanel) {
                ((PSCPMPanel)compon).setNavigator(this);
            }
        }
        navigate(LOGIN_FORM);
    }

    private void initCardLayout() {
        setNavigationModel(new CardLayout());
        setLayout(getNavigationModel());
        add(getServerBrowserView(), SERVER_BROWSER.name());
        add(getLoginView(), LOGIN_FORM.name());
    }

    public void navigate(PSCPMViewId toViewId) {
        try {
            switch (toViewId) {
            case SERVER_BROWSER:
                {
                    getServerBrowserView().reinit();
                    break;
                }
            default:
                {
                    // It is normal to reach this block;
                    break;
                }
            }
        } catch (Exception e) {
            handleException("Navigation failed.", e);
        }
        debug("Navigating to " + toViewId.getDisplayName() + ".");
        getNavigationModel().show(this, toViewId.name());
    }

    private void initServerBrowser() {
        try {
            getServerBrowserView().reinit();
        } catch (Exception e) {
            handleException("Server Browser Unavailable",
                            "Unable to reinitialize the server browser.", e);
        }
    }

    private void propagateSession(SecretsFacade session) {
        List<String> result = new ArrayList<String>();
        for (Component compon : getComponents()) {
            if (compon instanceof PSCPMPanel) {
                ((PSCPMPanel)compon).setSession(session);
                result.add(compon.getClass().getName());
            }
        }
        debug("Propagated the session:",
              result.toArray(new String[result.size()]));
    }

    public static final Map<Pair<PSCPMViewId, PSCPMOutcome>, PSCPMViewId> NAVIGATION_CASES =
        Collections.unmodifiableMap(MapUtil.asMap(MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(WILDCARD, PSCPMOutcome.LOGOUT),
                                                                LOGIN_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(WILDCARD, PSCPMOutcome.BROWSE_SERVERS),
                                                                SERVER_BROWSER), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(WILDCARD, PSCPMOutcome.ADD_SERVER),
                                                                NEW_SERVER_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(LOGIN_FORM, PSCPMOutcome.SUCCESS),
                                                                SERVER_BROWSER), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(SERVER_BROWSER, PSCPMOutcome.MANAGE_SERVER),
                                                                SERVER_MGMT_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(NEW_SERVER_FORM, PSCPMOutcome.ADD_ACCOUNT),
                                                                NEW_ACCOUNT_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(NEW_SERVER_FORM, PSCPMOutcome.MANAGE_ACCOUNT),
                                                                ACCOUNT_MGMT_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(SERVER_MGMT_FORM, PSCPMOutcome.ADD_ACCOUNT),
                                                                NEW_ACCOUNT_FORM), MapUtil.entry(new Pair<PSCPMViewId, PSCPMOutcome>(SERVER_MGMT_FORM, PSCPMOutcome.MANAGE_ACCOUNT),
                                                                ACCOUNT_MGMT_FORM)));


    public void navigate(NavigationEvent evt) {
        PSCPMViewId fromViewId = evt.getFromViewId();
        PSCPMOutcome fromOutcome = evt.getFromOutcome();
        PSCPMViewId toViewId = null;
        for (PSCPMViewId viewId : Arrays.asList(fromViewId, WILDCARD)) {
            Pair<PSCPMViewId, PSCPMOutcome> navKey =
                new Pair<PSCPMViewId, PSCPMOutcome>(viewId,
                                                    evt.getFromOutcome());
            if (NAVIGATION_CASES.containsKey(navKey)) {
                toViewId = NAVIGATION_CASES.get(navKey);
                break;
            }
        }
        if (evt.getFromOutcome() == PSCPMOutcome.LOGOUT) {
            getSession().endSession();
            debug("Logged off.");
            propagateSession(null);
        } else if (evt instanceof LoginSucceededEvent &&
                   fromViewId == LOGIN_FORM &&
                   fromOutcome == PSCPMOutcome.SUCCESS) {
            LoginSucceededEvent success = (LoginSucceededEvent)evt;
            setSession(success.getSession());
            propagateSession(success.getSession());
        }
        if (toViewId != null) {
        } else if (fromViewId == NEW_ACCOUNT_FORM ||
                   fromViewId == ACCOUNT_MGMT_FORM) {
            if (fromOutcome == PSCPMOutcome.DONE) {
                throw new UnsupportedOperationException("It's ambiguous!");
            }
        }
        if (toViewId == null) {
            debug("Potential error:",
                  "The to-view-id is null.  This is an error in some cases.");
        } else {
            navigate(toViewId);
        }
    }

    private void setServerBrowserView(ServerSecretsBrowserPanel serverBrowserView) {
        this.serverBrowserView = serverBrowserView;
    }

    private ServerSecretsBrowserPanel getServerBrowserView() {
        return serverBrowserView;
    }

    private void setLoginView(LoginPanel loginView) {
        this.loginView = loginView;
    }

    private LoginPanel getLoginView() {
        return loginView;
    }

    private void setNavigationModel(CardLayout navigationModel) {
        this.navigationModel = navigationModel;
    }

    private CardLayout getNavigationModel() {
        return navigationModel;
    }
}
