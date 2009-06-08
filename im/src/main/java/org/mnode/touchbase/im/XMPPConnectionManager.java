package org.mnode.touchbase.im;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * A manager for active XMPP connections.
 * 
 * @author fortuna
 *
 */
public class XMPPConnectionManager implements ConnectionCreationListener {

    private List<XMPPConnection> connections;

    private List<RosterListener> rosterListeners;

    /**
     * 
     */
    public XMPPConnectionManager() {
        connections = new ArrayList<XMPPConnection>();
        rosterListeners = new ArrayList<RosterListener>();
        XMPPConnection.addConnectionCreationListener(this);
    }

    /**
     * @return the connections
     */
    public List<XMPPConnection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * @param userId a user available via the returned XMPP connection
     * @return an XMPP connection from which the specified user is available, otherwise null
     */
    public XMPPConnection getConnection(String userId) {
        for (XMPPConnection connection : connections) {
            if (connection.getRoster().contains(userId)) {
                return connection;
            }
        }
        return null;
    }

    /**
     * @return a list of roster entries for all known users
     */
    public List<RosterEntry> getUsers() {
        List<RosterEntry> users = new ArrayList<RosterEntry>();
        for (XMPPConnection connection : connections) {
            if (connection.getRoster() != null) {
                users.addAll(connection.getRoster().getEntries());
            }
        }
        return Collections.unmodifiableList(users);
    }

    /**
     * @return a list of known roster groups
     */
    public List<RosterGroup> getGroups() {
        List<RosterGroup> groups = new ArrayList<RosterGroup>();
        for (XMPPConnection connection : connections) {
            if (connection.getRoster() != null) {
                groups.addAll(connection.getRoster().getGroups());
            }
        }
        return Collections.unmodifiableList(groups);
    }

    /**
     * Adds a listener to roster events.
     * @param listener a listener to add
     */
    public void addRosterListener(RosterListener listener) {
        rosterListeners.add(listener);
        for (XMPPConnection connection : connections) {
            if (connection.getRoster() != null) {
                connection.getRoster().addRosterListener(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionCreated(XMPPConnection connection) {
        connections.add(connection);
        if (connection.getRoster() != null) {
            for (RosterListener listener : rosterListeners) {
                connection.getRoster().addRosterListener(listener);
            }
        }
    }
}
