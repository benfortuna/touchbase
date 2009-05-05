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
     * @param store
     */
    public GroupsListModel(XMPPConnectionManager store) {
        this.store = store;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
     */
    public void addListDataListener(ListDataListener arg0) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return store.getGroups().get(index);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return store.getGroups().size();
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
     */
    public void removeListDataListener(ListDataListener arg0) {
        // TODO Auto-generated method stub

    }

}
