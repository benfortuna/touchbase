package org.mnode.touchbase.im.integration;

import org.jdesktop.swingx.JXFrame;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.mnode.touchbase.im.ChatFrame;
import org.mnode.touchbase.im.ContactsView;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.platform.Platforms;

public class ChatFrameTest extends AbstractConfigurableBundleCreatorTests {
	
	public void testGetChatFrame() throws XMPPException {

		ServiceTracker contactsViewTracker = new ServiceTracker(bundleContext, ContactsView.class.getName(), null);
		contactsViewTracker.open();
		ContactsView contactsView = (ContactsView) contactsViewTracker.getService();
		assertNotNull(contactsView);
		JXFrame contactsFrame = new JXFrame("Contacts");
		contactsFrame.add(contactsView);
		contactsFrame.setVisible(true);

		ServiceTracker chatFrameTracker = new ServiceTracker(bundleContext, ChatFrame.class.getName(), null);
		chatFrameTracker.open();
		ChatFrame chatFrame = (ChatFrame) chatFrameTracker.getService();
		assertNotNull(chatFrame);
		chatFrame.setVisible(true);
		
		final XMPPConnection connection = new XMPPConnection("basepatterns.org");
		connection.connect();
		connection.login("test", "!password");
		
		connection.disconnect();
	}
	
	@Override
	protected final String[] getTestBundlesNames() {
		return new String[] { "org.mnode.touchbase, org.mnode.touchbase.im, 0.0.1-SNAPSHOT",
				"org.mnode.base, org.mnode.base.xmpp, 0.0.1-SNAPSHOT",
				"org.mnode.base, org.mnode.base.views, 0.0.1-SNAPSHOT",
				"org.mnode.base, org.mnode.base.cache, 0.0.1-SNAPSHOT",
				"org.springframework, spring-context-support, 2.5.5",
				"net.sourceforge.cglib, com.springsource.net.sf.cglib, 2.1.3"
			};
	}

	@Override
	protected String getPlatformName() {
		return Platforms.FELIX;
	}
}