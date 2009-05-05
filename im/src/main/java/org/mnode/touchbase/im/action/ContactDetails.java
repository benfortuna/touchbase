/**
 * 
 */
package org.mnode.touchbase.im.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.action.ActionManager;
import org.mnode.touchbase.im.ContactDetailsPane;
import org.mnode.touchbase.im.VCardCache;
import org.mnode.touchbase.im.XMPPConnectionManager;

/**
 * @author Ben
 *
 */
public class ContactDetails extends AbstractContactAction {

    private static final ImageIcon ICON = new ImageIcon(ContactDetails.class.getResource("/icons/info.png"));

    private Frame parent;
    
    private XMPPConnectionManager connectionManager;
    
    private VCardCache cardCache;

    /**
     * 
     */
    public ContactDetails(String id) {
        super("View Details", id, ICON);
//        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_DOWN_MASK));
        ActionManager.getInstance().addAction(this);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JXDialog dialog = new JXDialog(parent, new ContactDetailsPane(connectionManager.getConnection(getContact()), getContact(), cardCache));
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * @return the parent
     */
    public final Frame getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public final void setParent(Frame parent) {
        this.parent = parent;
    }

	/**
	 * @param cardCache the cardCache to set
	 */
	public void setCardCache(VCardCache cardCache) {
		this.cardCache = cardCache;
	}

	/**
	 * @param connectionManager the connectionManager to set
	 */
	public void setConnectionManager(XMPPConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

}
