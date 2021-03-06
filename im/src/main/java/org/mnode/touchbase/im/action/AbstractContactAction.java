/*
 * This file is part of Touchbase.
 *
 * Created: [10/08/2008]
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

import javax.swing.Icon;

import org.jdesktop.swingx.action.AbstractActionExt;

/**
 * @author Ben
 *
 */
public abstract class AbstractContactAction extends AbstractActionExt {
    
    private String contact;

    /**
     * @param name the action name
     * @param command the action identifier
     * @param icon the action icon
     */
    public AbstractContactAction(String name, String command, Icon icon) {
        super(name, command, icon);
        // assume no contacts selected..
        setEnabled(false);
    }

    /**
     * @return the contact
     */
    public final String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public final void setContact(String contact) {
        this.contact = contact;
        setEnabled(contact != null);
    }
}
