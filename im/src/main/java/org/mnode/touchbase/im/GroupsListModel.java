/**
 * 
 */
package org.mnode.touchbase.im;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author Ben
 *
 */
public class GroupsListModel implements ListModel {
    
    private XMPPConnectionManager store;

    /**
     * @param store a manager for active XMPP connections
     */
    public GroupsListModel(XMPPConnectionManager store) {
        this.store = store;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addListDataListener(ListDataListener arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public Object getElementAt(int index) {
        return store.getGroups().get(index);
    }

    /**
     * {@inheritDoc}
     */
    public int getSize() {
        return store.getGroups().size();
    }

    /**
     * {@inheritDoc}
     */
    public void removeListDataListener(ListDataListener arg0) {
        // TODO Auto-generated method stub

    }

}
