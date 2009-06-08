package org.mnode.touchbase.im;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * A cache for vCard instances.
 * 
 * @author fortuna
 *
 */
public class VCardCache {

    private static final Log LOG = LogFactory.getLog(VCardCache.class);

    private Cache cache;

    /**
     * @param cache an underlying ehcache instance
     */
    public VCardCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * @param connection an active XMPP connection to retrieve a vCard from
     * @param user the user to retrieve the vCard for
     * @return a vCard instance for the specified user
     */
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
