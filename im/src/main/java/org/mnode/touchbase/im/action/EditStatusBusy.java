/*
 * This file is part of Touchbase.
 *
 * Created: [31/07/2008]
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

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.mnode.base.views.icon.IconSet;

/**
 * @author Ben
 *
 */
public class EditStatusBusy extends AbstractStatusAction {

    /**
     * 
     */
    private static final long serialVersionUID = -9104287440527571122L;

    private static final ImageIcon ICON = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/busy.png"));
    
    /**
     * @param id the action identifier
     */
    public EditStatusBusy(String id) {
        super("Busy", id, ICON);
        setStateAction(true);
        setShortDescription("Busy");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Presence presence = new Presence(Type.available);
        if (isSelected()) {
            ActionManager.getInstance().setSelected("editStatusInvisible", false);
            presence.setMode(Mode.dnd);
        }
        else {
            presence.setMode(Mode.available);
        }
        for (XMPPConnection connection : getConnection()) {
            connection.sendPacket(presence);
        }
    }

}
