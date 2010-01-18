package org.mnode.touchbase.im.integration;

import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.mnode.base.commons.osgi.OsgiServiceLocator;
import org.mnode.base.commons.ServiceLocator;
import org.mnode.base.commons.ServiceNotAvailableException;
import org.mnode.touchbase.im.ChatFrame;
import org.mnode.touchbase.im.ContactsView;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.platform.Platforms;

public class ChatFrameIntegrationTest extends AbstractConfigurableBundleCreatorTests {

    public void testGetChatFrame() throws XMPPException, ServiceNotAvailableException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ServiceTracker contactsViewTracker = new ServiceTracker(bundleContext, ContactsView.class.getName(), null);
//        contactsViewTracker.open();
//        ContactsView contactsView = (ContactsView) contactsViewTracker.getService();
        
        ServiceLocator serviceLocator = new OsgiServiceLocator(bundleContext);
        
        ContactsView contactsView = serviceLocator.findService(ContactsView.class);
        assertNotNull(contactsView);
        JXFrame contactsFrame = new JXFrame("Contacts");
        contactsFrame.add(contactsView);
        contactsFrame.pack();
        contactsFrame.setVisible(true);

//        ServiceTracker chatFrameTracker = new ServiceTracker(bundleContext, ChatFrame.class.getName(), null);
//        chatFrameTracker.open();
//        ChatFrame chatFrame = (ChatFrame) chatFrameTracker.getService();
        ChatFrame chatFrame = serviceLocator.findService(ChatFrame.class);
        assertNotNull(chatFrame);
        chatFrame.setVisible(true);

        final XMPPConnection connection = new XMPPConnection("basepatterns.org");
        connection.connect();
        connection.login("test", "!password");

        connection.disconnect();
    }

    @Override
    protected final String[] getTestBundlesNames() {
        return new String[] { "org.mnode.touchbase, touchbase-im, 0.0.1-SNAPSHOT",
                "org.mnode.base, base-xmpp, 0.0.1-SNAPSHOT",
                "org.mnode.base, base-views, 0.0.1-SNAPSHOT",
                "org.mnode.base, base-cache, 0.0.1-SNAPSHOT",
                "org.mnode.base, base-commons, 0.0.1-SNAPSHOT",
                "org.springframework, spring-context-support, 2.5.5",
                "net.sourceforge.cglib, com.springsource.net.sf.cglib, 2.1.3",
                "commons-lang, commons-lang, 2.4" };
    }

    @Override
    protected String getPlatformName() {
        return Platforms.FELIX;
    }
}
