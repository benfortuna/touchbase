/*
 * This file is part of Touchbase.
 *
 * Created: [30/07/2008]
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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.KeyStroke;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;
import org.mnode.touchbase.im.ChatFrame;

/**
 * @author Ben
 *
 */
public class CloseChatView extends AbstractActionExt implements WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = 9064200905079604544L;

    private ChatFrame chatFrame;
    
    /**
     * @param id the action identifier
     */
    public CloseChatView(String id) {
        super("Close", id);
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
//        this.chatFrame = chatFrame;
        setEnabled(false);
        ActionManager.getInstance().addAction(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        closeFrame();
    }
    
    /**
     * 
     */
    private void closeFrame() {
//        chatFrame.getView().closeAllTabs();
        chatFrame.setVisible(false);
    }

    /**
     * @return the chatFrame
     */
    public final ChatFrame getChatFrame() {
        return chatFrame;
    }

    /**
     * @param chatFrame the chatFrame to set
     */
    public final void setChatFrame(ChatFrame chatFrame) {
        if (this.chatFrame != null) {
            this.chatFrame.removeWindowListener(this);
        }

        this.chatFrame = chatFrame;
        if (chatFrame != null) {
            chatFrame.addWindowListener(this);
            setEnabled(true);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowClosing(WindowEvent e) {
        closeFrame();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowActivated(WindowEvent e) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowClosed(WindowEvent e) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowIconified(WindowEvent e) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void windowOpened(WindowEvent e) {
    }
}
