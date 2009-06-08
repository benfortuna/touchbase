/**
 * 
 */
package org.mnode.touchbase.im.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.action.ActionManager;
import org.mnode.base.views.icon.IconSet;
import org.mnode.touchbase.im.ChatFrame;

/**
 * @author Ben
 * 
 */
public class OpenChatView extends AbstractContactAction {

    /**
     * 
     */
    private static final long serialVersionUID = 164530061435239088L;

    private static final ImageIcon ICON = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/chat.png"));

    private ChatFrame chatFrame;

    /**
     * @param id the action identifier
     */
    public OpenChatView(String id) {
        super("Chat", id, ICON);
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
                | InputEvent.SHIFT_DOWN_MASK));
        /*
         * chatFrame.addWindowListener(new WindowAdapter() {
         * @Override public void windowClosing(WindowEvent e) { chatView.closeAllTabs(); chatFrame.setVisible(false); }
         * }); chatFrame.addKeyListener(new KeyAdapter() {
         * @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
         * chatView.closeAllTabs(); chatFrame.setVisible(false); } } });
         */
        ActionManager.getInstance().addAction(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // chatFrame.openChat((XmppContact) getContact());
    }

    /**
     * @return the chatFrame
     */
    public final ChatFrame getChatFrame() {
        return chatFrame;
    }

    /**
     * @param chatFrame
     *            the chatFrame to set
     */
    public final void setChatFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
        setEnabled(chatFrame != null && super.isEnabled());
    }

}
