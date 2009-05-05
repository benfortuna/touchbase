/*
 * This file is part of Touchbase.
 *
 * Created: [11/09/2008]
 *
 * Copyright (c) 2008, Ben Fortuna
 *
 * Touchbase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Touchbase is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Touchbase.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.touchbase.im;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.mnode.base.views.activity.ActivityEvent;
import org.mnode.base.views.activity.ActivityListener;
import org.mnode.base.views.activity.ActivityPoller;

/**
 * @author Ben
 *
 */
public class IdleTime implements ConnectionCreationListener {
    
    private ActivityPoller activityPoller;
    
    private List<XMPPConnection> connections;
    
    /**
     * 
     */
    public IdleTime() {
        activityPoller = new ActivityPoller(180000);
        activityPoller.addActivityListener(new ActivityListener() {
            @Override
            public void mouseActive(ActivityEvent e) {
                updatePresence(org.jivesoftware.smack.packet.Presence.Mode.available);
            }
            @Override
            public void mouseIdle(ActivityEvent e) {
                updatePresence(org.jivesoftware.smack.packet.Presence.Mode.away);
            }
            private void updatePresence(org.jivesoftware.smack.packet.Presence.Mode mode) {
                if (!ActionManager.getInstance().isSelected("editStatusBusy")
                        && !ActionManager.getInstance().isSelected("editStatusInvisible")) {
                    Presence presence = new Presence(Type.available);
                    presence.setMode(mode);
                    for (XMPPConnection connection : connections) {
                        if (connection.isConnected()) {
                            connection.sendPacket(presence);
                        }
                    }
                }
            }
        });
        activityPoller.start();
        
        connections = new ArrayList<XMPPConnection>();
        
        XMPPConnection.addConnectionCreationListener(this);
    }
    
    @Override
    public void connectionCreated(XMPPConnection connection) {
        connections.add(connection);
    }
}
