/**
 * 
 */
package org.mnode.touchbase.im.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.action.ActionManager;
import org.mnode.base.views.icon.IconSet;

/**
 * @author Ben
 *
 */
public class CallContact extends AbstractContactAction {

    /**
     * 
     */
    private static final long serialVersionUID = -8083697845143686830L;

    private static final ImageIcon ICON = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/call.png"));

    /**
     * 
     */
    public CallContact(String id) {
        super("Call", id, ICON);
//        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_DOWN_MASK));
        ActionManager.getInstance().addAction(this);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
