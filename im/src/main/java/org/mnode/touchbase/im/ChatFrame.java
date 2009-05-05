/*
 * This file is part of Touchbase.
 *
 * Created: [18/08/2008]
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

import org.mnode.base.views.ViewFrame;


/**
 * @author Ben
 *
 */
public class ChatFrame extends ViewFrame<ChatView> {

    /**
     * 
     */
    private static final long serialVersionUID = 6800220370010527297L;

    /**
     * @param view
     */
    public ChatFrame(ChatView view) {
        super(view);
    }
    
    /**
     * @param contact
     */
    /*
    public void openChat(XmppContact contact) {
        getView().showView(contact);
        setVisible(true);
    }
    */

}
