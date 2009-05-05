/**
 * 
 */
package org.mnode.touchbase.im;

import java.util.Collection;

import javax.swing.AbstractListModel;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

/**
 * @author Ben
 *
 */
public class ContactsListModel extends AbstractListModel {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3821681362487867025L;
    
    private XMPPConnectionManager store;
    
    /**
     * @param roster
     */
    public ContactsListModel(XMPPConnectionManager store) {
        this.store = store;

        store.addRosterListener(new RosterListener() {
        	@Override
        	public void entriesAdded(Collection<String> addresses) {
                fireContentsChanged(this, 0, getSize());
        	}
        	@Override
        	public void entriesDeleted(Collection<String> addresses) {
                fireContentsChanged(this, 0, getSize());
        	}
        	@Override
        	public void entriesUpdated(Collection<String> addresses) {
                fireContentsChanged(this, 0, getSize());
        	}
        	@Override
        	public void presenceChanged(Presence presence) {
                fireContentsChanged(this, 0, getSize());
        	}
        });
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return store.getUsers().get(index);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
//        return connection.getRoster().getEntryCount();
        return store.getUsers().size();
    }
}
