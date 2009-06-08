package org.mnode.touchbase.im.action;

import java.awt.event.ActionEvent;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * An action for creating a new XMPP connection.
 * 
 * @author fortuna
 *
 */
public class NewConnection extends AbstractActionExt {

    /**
     * @param id
     *            action identifier
     */
    public NewConnection(String id) {
        super("New connection..", id);
        ActionManager.getInstance().addAction(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            final XMPPConnection connection = new XMPPConnection("basepatterns.org");
            connection.connect();
            connection.login("test", "!password");
        } catch (XMPPException ex) {
            ex.printStackTrace();
        }
    }

}
