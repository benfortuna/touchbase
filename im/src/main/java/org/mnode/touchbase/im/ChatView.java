/*
 * This file is part of Touchbase.
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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;
import org.mnode.base.views.AbstractTabbedView;
import org.mnode.base.views.AbstractView;
import org.mnode.base.views.ViewFrame;
import org.mnode.base.views.ViewStatusBar;
import org.mnode.base.views.action.CloseAllTabs;
import org.mnode.base.views.action.CloseOtherTabs;
import org.mnode.base.views.action.CloseTab;
import org.mnode.base.views.action.ShowStatusBarAction;
import org.mnode.base.views.icon.AbstractIconSetFactory;
import org.mnode.base.views.icon.IconSet;
import org.mnode.base.views.icon.LiquidicityIconSetFactory;
import org.mnode.base.xmpp.ChatContextManager;
import org.mnode.touchbase.im.action.NewConnection;
import org.mnode.touchbase.im.action.OpenChatView;

/**
 * @author fortuna
 * 
 */
public class ChatView extends AbstractTabbedView<Chat, ConversationView> implements ChatManagerListener,
        ChatStateListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2272090138099396227L;

    private static final ImageIcon ICON = new ImageIcon(IconSet.class.getResource("/icons/liquidicity/chat.png"));

    private ChatContextManager contextManager;

    private VCardCache cardCache;

    /**
     * @param contextManager the context manage for active chats
     * @param cardCache the cache for vCard instances
     */
    public ChatView(final ChatContextManager contextManager, VCardCache cardCache) {
        super("chat", "Chat - Touchbase", ICON);

        this.contextManager = contextManager;
        this.cardCache = cardCache;

        /*
         * JXButton closeButton = new JXButton("Close"); JXButton sendButton = new JXButton("Send"); JXPanel buttonPane
         * = new JXPanel(new FlowLayout(FlowLayout.TRAILING)); buttonPane.add(closeButton); buttonPane.add(sendButton);
         * add(buttonPane, BorderLayout.SOUTH);
         */
        ActionContainerFactory acf = new ActionContainerFactory();
        IconSet icons = AbstractIconSetFactory.getInstance(LiquidicityIconSetFactory.class.getName())
                .getDefaultIconSet();

        // CloseChatView closeChatView = new CloseChatView(((OpenChatView)
        // ActionManager.getInstance().getAction("closeTab")).getChatFrame());
        // ActionManager.getInstance().addAction("closeChatView", closeChatView);

        // initialise status bar..
        ViewStatusBar statusBar = new ViewStatusBar(this);

        AbstractButton a1 = acf.createButton(ActionManager.getInstance().getAction("editStatusBusy"));
        a1.setSelectedIcon(icons.getSelectedIcon("busy"));
        a1.setRolloverIcon(icons.getRolloverIcon("busy"));
        a1.setMargin(null);
        a1.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        a1.setBorderPainted(false);
        a1.setContentAreaFilled(false);
        a1.setFocusPainted(false);
        a1.setOpaque(false);
        a1.setToolTipText("Busy");
        // availabilityPane.add(a1);

        AbstractButton a2 = acf.createButton(ActionManager.getInstance().getAction("editStatusInvisible"));
        a2.setSelectedIcon(icons.getSelectedIcon("invisible"));
        a2.setRolloverIcon(icons.getRolloverIcon("invisible"));
        a2.setMargin(null);
        a2.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        a2.setBorderPainted(false);
        a2.setContentAreaFilled(false);
        a2.setFocusPainted(false);
        a2.setOpaque(false);
        a2.setToolTipText("Invisible");
        // availabilityPane.add(a2);

        statusBar.add(a1, new JXStatusBar.Constraint(ResizeBehavior.FIXED));
        statusBar.add(a2, new JXStatusBar.Constraint(ResizeBehavior.FIXED));

        setStatusBar(statusBar);

        // ShowStatusBarAction viewStatusBar = new ShowStatusBarAction();
        ShowStatusBarAction viewStatusBar = (ShowStatusBarAction) ActionManager.getInstance().getAction(
                "chat.viewStatusBar");
        viewStatusBar.addStatusBar(statusBar);
        // ActionManager.getInstance().addAction(getId() + ".viewStatusBar", viewStatusBar);

        CloseTab<ChatView, Chat> closeTab = (CloseTab<ChatView, Chat>) ActionManager.getInstance().getAction(
                "chat.closeTab");
        // CloseTab<ChatView, XmppContact> closeTab = new CloseTab<ChatView, XmppContact>();
        closeTab.setView(this);
        // ActionManager.getInstance().addAction(getId() + ".closeTab", closeTab);

        CloseOtherTabs<ChatView, Chat> closeOtherTabs = (CloseOtherTabs<ChatView, Chat>) ActionManager.getInstance()
                .getAction("chat.closeOtherTabs");
        // CloseOtherTabs<ChatView, XmppContact> closeOtherTabs = new CloseOtherTabs<ChatView, XmppContact>();
        closeOtherTabs.setView(this);
        // ActionManager.getInstance().addAction(getId() + ".closeOtherTabs", closeOtherTabs);

        CloseAllTabs<ChatView, Chat> closeAllTabs = (CloseAllTabs<ChatView, Chat>) ActionManager.getInstance()
                .getAction("chat.closeAllTabs");
        // CloseAllTabs<ChatView, XmppContact> closeAllTabs = new CloseAllTabs<ChatView, XmppContact>();
        closeAllTabs.setView(this);
        // ActionManager.getInstance().addAction(getId() + ".closeAllTabs", closeAllTabs);

        NewConnection newConnection = (NewConnection) ActionManager.getInstance().getAction("newConnection");

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("newConnection")));
        fileMenu.addSeparator();
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction(getId() + ".closeTab")));
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction(getId() + ".closeOtherTabs")));
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction(getId() + ".closeAllTabs")));
        fileMenu.addSeparator();
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("call")));
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("sendFile")));
        fileMenu.addSeparator();
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("chatView.close")));
        fileMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("exit")));

        JMenu statusMenu = new JMenu("Status");
        statusMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("editStatusBusy")));
        statusMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("editStatusInvisible")));
        statusMenu.addSeparator();
        statusMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("editStatusMessage")));

        // ActionManager.getInstance().addAction("editPreferences", new EditPreferences());

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        editMenu.add(statusMenu);
        editMenu.addSeparator();
        editMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction("editPreferences")));

        JMenu viewMenu = new JMenu("View");
        viewMenu.add(acf.createMenuItem(ActionManager.getInstance().getAction(getId() + ".viewStatusBar")));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        // menuBar.add(new HelpMenu());

        setMenuBar(menuBar);

        // XMPPConnection.addConnectionCreationListener(this);
    }

    // @Override
    // public void connectionCreated(XMPPConnection connection) {
    // connection.getChatManager().addChatListener(this);
    // }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        // if (!createdLocally) {
        // XmppContact contact = (XmppContact)
        // contactStore.getContact(StringUtils.parseBareAddress(chat.getParticipant()));
        // chats.put(contact, chat);
        chat.addMessageListener(this);
        /*
         * List<XmppContact> contactList = new ArrayList<XmppContact>(); contactList.add(contact);
         * showView(contactList); ViewFrame<ChatView> chatFrame = ((OpenChatView)
         * ActionManager.getInstance().getAction("chat")).getChatFrame(); chatFrame.setVisible(true);
         */
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(Chat chat, ChatState state) {
        // XmppContact contact = (XmppContact)
        // contactStore.getContact(StringUtils.parseBareAddress(chat.getParticipant()));
        AbstractView view = getViews().get(chat);
        if (view != null) {
            if (ChatState.composing.equals(state)) {
                view.setStatusMessage(chat.getParticipant() + " is typing..");
            } else if (ChatState.gone.equals(state)) {
                view.setStatusMessage(chat.getParticipant() + " has left the conversation.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processMessage(Chat chat, Message message) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(message.getBody())) {
            // XmppContact contact = (XmppContact)
            // contactStore.getContact(StringUtils.parseBareAddress(chat.getParticipant()));
            // List<XmppContact> contactList = new ArrayList<XmppContact>();
            // contactList.add(contact);

            getView(chat).addMessage(message);
            showView(chat);

            ViewFrame<ChatView> chatFrame = ((OpenChatView) ActionManager.getInstance().getAction("chat"))
                    .getChatFrame();
            if (!chatFrame.isActive()) {
                chatFrame.setVisible(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConversationView createView(Chat chat) {
        // if (key.size() > 1) {
        // return new ForumView(key);
        // }

        // XmppContact contact = key.get(0);
        // Chat chat = contactStore.getChat(key);
        // if (chat == null) {
        // chat = contactStore.getConnection().getChatManager().createChat(contact.getEmailAddress(), this);
        // chats.put(contact, chat);
        // }
        return new ConversationView(contextManager.getContext(chat), chat, cardCache);
    }
}
