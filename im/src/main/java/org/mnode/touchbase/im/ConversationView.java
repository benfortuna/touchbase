/**
 * 
 */
package org.mnode.touchbase.im;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Date;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateManager;
import org.jivesoftware.smackx.packet.VCard;
import org.mnode.base.views.AbstractView;
import org.mnode.base.views.icon.IconSet;
import org.mnode.base.xmpp.ChatContext;

/**
 * @author fortuna
 *
 */
public class ConversationView extends AbstractView {

    private static final Log LOG = LogFactory.getLog(ConversationView.class);
    
//    private static final ImageIcon ICON = new ImageIcon(ChatView.class.getResource("/icons/liquidicity/conversation.png"));

    private static final Icon DEFAULT_AVATAR = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/avatar.png"));

    private JXList messageList;
    
    private JTextArea messageText;

    private Chat chat;
    
    private Clip messageAlert;
    
    private DefaultListModel model;
    
    private VCardCache vcardCache;
    
	/**
	 * @param id
	 * @param title
	 */
	public ConversationView(final ChatContext context, final Chat chat, VCardCache vcardCache) {
		super("conversation", StringUtils.parseBareAddress(chat.getParticipant()), null);

		this.vcardCache = vcardCache;
		
		model = new DefaultListModel();
		
		messageList = new JXList(model);
		messageList.setBorder(null);
		messageList.setCellRenderer(new ConversationListCellRenderer(context.getConnection().getRoster()));
//        messageList.setAutoscrolls(true);
		
		JScrollPane messageScroller = new JScrollPane(messageList);
		
		final String emptyMessage = "Type message..";
		messageText = new JTextArea(emptyMessage, 4, 0);
		messageText.setWrapStyleWord(true);
        messageText.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		messageText.setForeground(Color.LIGHT_GRAY);
		messageText.setToolTipText("Press <Enter> to send");
		messageText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (emptyMessage.equals(messageText.getText())) {
					messageText.setText(null);
					messageText.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if (messageText.getText().length() == 0) {
					messageText.setText(emptyMessage);
					messageText.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		messageText.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
		            if (e.getModifiers() == 0 && org.apache.commons.lang.StringUtils.isNotEmpty(messageText.getText())) {
	                    Message message = new Message();
	                    message.setBody(messageText.getText());
	                    model.addElement(message);
	                    messageList.ensureIndexIsVisible(model.getSize() - 1);
	                    messageText.setText(null);
	                    try {
	                        chat.sendMessage(message);
	                    } catch (XMPPException e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
		            }
                    e.consume();
		        }
		    }
		});
		messageText.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        updateChatState();
		    }
		    @Override
		    public void insertUpdate(DocumentEvent e) {
                updateChatState();
		    }
		    @Override
		    public void removeUpdate(DocumentEvent e) {
                updateChatState();
		    }
		    private void updateChatState() {
                if (org.apache.commons.lang.StringUtils.isEmpty(messageText.getText())) {
                    try {
                        ChatStateManager.getInstance(context.getConnection()).setCurrentState(ChatState.inactive, chat);
                    } catch (XMPPException e1) {
                        LOG.warn("Error updating chat state", e1);
                    }
                }
                else if (!emptyMessage.equals(messageText.getText())) {
                    try {
                        ChatStateManager.getInstance(context.getConnection()).setCurrentState(ChatState.composing, chat);
                    } catch (XMPPException e1) {
                        LOG.warn("Error updating chat state", e1);
                    }
                }
		    }
		});
		
//		final XmppContact contact = (XmppContact) contactStore.getContact(StringUtils.parseBareAddress(chat.getParticipant()));
		final Presence initialPresence = context.getConnection().getRoster().getPresence(StringUtils.parseBareAddress(chat.getParticipant()));
//		JXPanel infoPane = new JXPanel();
		final JXLabel infoLabel = new JXLabel(initialPresence.getStatus(), JXLabel.CENTER);
		Icon icon = null;
		VCard card = vcardCache.getVCard(context.getConnection(), chat.getParticipant());
		if (card != null && card.getAvatar() != null) {
		    icon = new ImageIcon(card.getAvatar());
		}
		else {
	        icon = DEFAULT_AVATAR;
		}
        infoLabel.setIcon(icon);
        infoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        infoLabel.setHorizontalTextPosition(JLabel.CENTER);
        infoLabel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight() + 50));
		infoLabel.setVerticalAlignment(JLabel.TOP);
        infoLabel.setLineWrap(true);
//        infoPane.add(infoLabel);
		
        context.getConnection().getRoster().addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void entriesDeleted(Collection<String> arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void entriesUpdated(Collection<String> arg0) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void presenceChanged(Presence presence) {
                if (initialPresence.getFrom().equals(presence.getFrom())) {
                    infoLabel.setText(presence.getStatus());
                    infoLabel.repaint();
                }
            }
        });
		JXPanel messageBox = new JXPanel(new BorderLayout());
		messageBox.add(messageScroller, BorderLayout.CENTER);
        messageBox.add(infoLabel, BorderLayout.EAST);
		messageBox.add(messageText, BorderLayout.SOUTH);
		add(messageBox, BorderLayout.CENTER);

		try {
			messageAlert = AudioSystem.getClip();
//			messageAlert.open(new AudioInputStream(getClass().getResourceAsStream("/sounds/ping.wav"),
//					new AudioFormat()))
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		chat = connection.getChatManager().createChat(contact.getEmailAddress(), new MessageListener() {
		    @Override
		    public void processMessage(Chat chat, Message message) {
		        LOG.info("Received message: " + message.getType());
		        if (message.getBody() != null) {
                    model.addElement(message);
                    setStatusMessage("Last message received at: " + new Date());
		        }
		    }
		});
		*/
//		chat.addMessageListener(this);
	}
    
    public void addMessage(Message message) {
        LOG.info("Received message: " + message.getType());
        if (message.getBody() != null) {
            model.addElement(message);
            messageList.ensureIndexIsVisible(model.getSize() - 1);
            setStatusMessage("Last message received at: " + new Date());
        }
    }

}
