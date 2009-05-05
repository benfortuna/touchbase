/*
 * This file is part of Touchbase.
 *
 * Created: [17/08/2008]
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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.packet.VCard;
import org.mnode.base.views.icon.IconSet;

/**
 * @author Ben
 *
 */
public class ContactDetailsPane extends JXPanel {

    private static final Icon DEFAULT_AVATAR = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/avatar.png"));
    
    private VCardCache cardCache;

    /**
     * 
     */
    public ContactDetailsPane(XMPPConnection connection, String user, VCardCache cache) {
    	VCard card = cache.getVCard(connection, user);
        String name = card.getFirstName();
        if (StringUtils.isEmpty(name)) {
            name = card.getEmailHome();
        }
        setName("About " + name);
        JXLabel nameLabel = null;
        if (card.getAvatar() != null) {
            nameLabel = new JXLabel(name, new ImageIcon(card.getAvatar()), JXLabel.TRAILING);
        }
        else {
            nameLabel = new JXLabel(name, DEFAULT_AVATAR, JXLabel.TRAILING);
        }
        nameLabel.setFont(nameLabel.getFont().deriveFont(18f));
        add(nameLabel);
    }

}
