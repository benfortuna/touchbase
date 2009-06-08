/*
 * This file is part of Touchbase.
 *
 * Created: [04/08/2008]
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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Ben
 *
 */
public class ConversationListCellRenderer extends DefaultListCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = -6927561892966083003L;

    private Roster roster;
    
    /**
     * @param roster a roster from an active XMPP connection
     */
    public ConversationListCellRenderer(Roster roster) {
        this.roster = roster;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        Message message = (Message) value;
        
        RosterEntry participant = roster.getEntry(StringUtils.parseBareAddress(message.getFrom()));
        StringBuilder b = new StringBuilder();
        if (participant != null) {
            if (participant.getName() != null) {
                b.append(participant.getName());
            }
            else {
                b.append(participant.getUser());
            }
        }
        else {
            b.append(StringUtils.parseName(message.getFrom()));
        }
        b.append(": ");
        b.append(message.getBody());
        setText(b.toString());
        
        return this;
    }
}
