package org.mnode.touchbase.im;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class VCardCache {

	private static final Log LOG = LogFactory.getLog(VCardCache.class);
	
	private Cache cache;
	
	public VCardCache(Cache cache) {
		this.cache = cache;
	}
	
	public VCard getVCard(XMPPConnection connection, String user) {
		Element cardElement = cache.get(user);
		if (cardElement == null) {
			try {
				VCard card = new VCard();
				card.load(connection, user);
				cardElement = new Element(user, card);
				cache.put(cardElement);
			} catch (XMPPException e) {
				LOG.info("[" + user + "]: vCard not available");
				return null;
			}
		}
		return (VCard) cardElement.getObjectValue();
	}
}
