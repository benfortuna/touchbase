/*
 * This file is part of Touchbase.
 *
 * Created: [19/08/2008]
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

package org.mnode.touchbase.im.action;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * @author fortuna
 *
 */
public abstract class AbstractStatusAction extends AbstractActionExt implements ConnectionCreationListener {

	private List<XMPPConnection> connections;

	/**
	 * @param name
	 * @param command
	 * @param icon
	 */
	public AbstractStatusAction(String name, String command, Icon icon) {
		super(name, command, icon);
		connections = new ArrayList<XMPPConnection>();
        setEnabled(false);
        ActionManager.getInstance().addAction(this);
        XMPPConnection.addConnectionCreationListener(this);
	}

	@Override
	public void connectionCreated(XMPPConnection connection) {
		this.connections.add(connection);
		setEnabled(!connections.isEmpty());
	}

	/**
	 * @return the connection
	 */
	public final List<XMPPConnection> getConnection() {
		return connections;
	}
}
