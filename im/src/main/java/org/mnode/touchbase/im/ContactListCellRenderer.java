/**
 * 
 */
package org.mnode.touchbase.im;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.mnode.base.views.icon.IconSet;

/**
 * @author Ben
 * 
 */
public class ContactListCellRenderer extends JXLabel implements ListCellRenderer {

    // private static final Icon AVAILABLE_ICON = new
    // ImageIcon(ContactListCellRenderer.class.getResource("/icons/babelfish.png"));

    // private static final Icon UNAVAILABLE_ICON = new
    // ImageIcon(ContactListCellRenderer.class.getResource("/icons/babelfish_disabled.png"));
    // private static final Icon UNAVAILABLE_ICON = new
    // ImageIcon(ContactListCellRenderer.class.getResource("/icons/free_icon.png"));

    private static final URL AVAILABLE_BACKGROUND_URL = ContactListCellRenderer.class
            .getResource("/backgrounds/available.png");

    private static final URL IDLE_BACKGROUND_URL = ContactListCellRenderer.class.getResource("/backgrounds/idle.png");

    private static final URL BUSY_BACKGROUND_URL = ContactListCellRenderer.class.getResource("/backgrounds/busy.png");

    private static final URL UNAVAILABLE_BACKGROUND_URL = ContactListCellRenderer.class
            .getResource("/backgrounds/unavailable.png");

    private static final URL SELECTED_BACKGROUND_URL = ContactListCellRenderer.class
            .getResource("/backgrounds/selected.png");

    private static final Icon GROUP_ICON = new ImageIcon(IconSet.class.getResource("/icons/crystal/yast_kuser.png"));

    // private boolean gradientPaint;

    private ImagePainter<JXLabel> availablePainter;

    private Painter<JXLabel> unavailablePainter;

    private Painter<JXLabel> selectedPainter;

    private Painter<JXLabel> idlePainter;

    private Painter<JXLabel> busyPainter;

    private XMPPConnectionManager connectionManager;

    /**
     * @param connectionManager manager for active XMPP connections
     */
    public ContactListCellRenderer(XMPPConnectionManager connectionManager) {
        // super(new JXLabel(), new DefaultPainter<JXLabel>(GradientPaint(0, 0, Color.YELLOW, 20, 20, Color.BLUE));
        this.connectionManager = connectionManager;
        // setOpaque(true);
        // setBackgroundPainter(new GlossPainter<JXLabel>()); //new GradientPaint(0, 0, Color.YELLOW, 20, 20,
        // Color.ORANGE)));
        // setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setPreferredSize(new Dimension(75, 30));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        try {
            availablePainter = new ImagePainter<JXLabel>(AVAILABLE_BACKGROUND_URL, HorizontalAlignment.LEFT,
                    VerticalAlignment.CENTER);
            // ((ImagePainter<JXLabel>) availablePainter).setHorizontalRepeat(true);
            // availablePainter.setScaleType(ScaleType.InsideFit);
            // availablePainter.setScaleToFit(true);
            // availablePainter.setFillHorizontal(true);
            availablePainter.setHorizontalRepeat(true);
            availablePainter.setPaintStretched(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            unavailablePainter = new ImagePainter<JXLabel>(UNAVAILABLE_BACKGROUND_URL, HorizontalAlignment.LEFT,
                    VerticalAlignment.CENTER);
            // ((ImagePainter<JXLabel>) unavailablePainter).setHorizontalRepeat(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            selectedPainter = new ImagePainter<JXLabel>(SELECTED_BACKGROUND_URL, HorizontalAlignment.LEFT,
                    VerticalAlignment.CENTER);
            // ((ImagePainter<JXLabel>) unavailablePainter).setHorizontalRepeat(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            idlePainter = new ImagePainter<JXLabel>(IDLE_BACKGROUND_URL, HorizontalAlignment.LEFT,
                    VerticalAlignment.CENTER);
            // ((ImagePainter<JXLabel>) idlePainter).setHorizontalRepeat(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            busyPainter = new ImagePainter<JXLabel>(BUSY_BACKGROUND_URL, HorizontalAlignment.LEFT,
                    VerticalAlignment.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        setHorizontalTextPosition(JLabel.CENTER);
        setForeground(list.getForeground());
        setFont(list.getFont());

        if (isSelected) {
            // setBackground(list.getSelectionBackground());
            // setOpaque(true);
            setBackgroundPainter(selectedPainter);
            // gradientPaint = false;
        }
        /*
         * else { setBackground(list.getBackground()); setOpaque(false); // gradientPaint = true; }
         */

        if (value instanceof RosterGroup) {
            RosterGroup group = (RosterGroup) value;
            setText(group.getName());
            setToolTipText(null);
            setIcon(GROUP_ICON);
        } else if (value instanceof RosterEntry) {
            RosterEntry contact = (RosterEntry) value;
            if (isAvailable(contact)) {
                // setFont(list.getFont().deriveFont(Font.BOLD));
                // setIcon(AVAILABLE_ICON);
                if (!isSelected) {
                    // setBackgroundPainter(new MattePainter<JXLabel>(new GradientPaint(0, 0, Color.BLACK, 0, 10,
                    // Color.GREEN)));
                    setBackgroundPainter(availablePainter);
                }
            } else {
                // setFont(list.getFont().deriveFont(Font.PLAIN));
                // setIcon(UNAVAILABLE_ICON);
                if (!isSelected) {
                    // setBackgroundPainter(new MattePainter<JXLabel>(new GradientPaint(0, 0, Color.BLACK, 0, 10,
                    // Color.GRAY)));
                    setBackgroundPainter(unavailablePainter);
                }
            }

            if (isIdle(contact)) {
                // setForeground(Color.LIGHT_GRAY);
                if (!isSelected) {
                    // setBackgroundPainter(new MattePainter<JXLabel>(new GradientPaint(0, 0, Color.BLACK, 0, 10,
                    // Color.ORANGE)));
                    setBackgroundPainter(idlePainter);
                }
            }
            // else {
            // setForeground(list.getForeground());
            // }

            if (isBusy(contact)) {
                if (!isSelected) {
                    setBackgroundPainter(busyPainter);
                }
            }

            if (StringUtils.isNotEmpty(contact.getName())) {
                setText(contact.getName());
            } else {
                setText(StringUtils.abbreviate(contact.getUser(), 20));
            }
            // disabled tooltips..
            // setToolTipText(contact.getEmailAddress());
            // setIcon(contact.getAvatar());
            setIcon(null);
        }

        return this;
    }

    /*
     * @Override protected void paintComponent(Graphics g) { if (gradientPaint) { Graphics2D g2 = (Graphics2D)
     * g.create(); // TexturePaint texture = new TexturePaint(new BufferedImage()) GradientPaint gradient = new
     * GradientPaint(0, 0, new Color(30, 30, 30), 0, 10, getBackground().brighter()); g2.setPaint(gradient);
     * g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose(); } super.paintComponent(g); }
     */

    private Presence getPresence(RosterEntry contact) {
        XMPPConnection connection = connectionManager.getConnection(contact.getUser());
        if (connection != null) {
            return connection.getRoster().getPresence(contact.getUser());
        }
        return null;
    }

    /**
     * Indicates whether the specified user is available.
     * @param contact a roster entry to check for availability
     * @return true if the user is available, otherwise false
     */
    public boolean isAvailable(RosterEntry contact) {
        Presence presence = getPresence(contact);
        return presence != null && Type.available.equals(presence.getType());
    }

    /**
     * Indicates whether the specified user is idle.
     * @param contact a roster entry to check for idleness
     * @return true if the user is idle, otherwise false
     */
    public boolean isIdle(RosterEntry contact) {
        Presence presence = getPresence(contact);
        return presence != null && Mode.away.equals(presence.getMode());
    }

    /**
     * Indicates whether the specified user is busy.
     * @param contact a roster entry to check for business
     * @return true if the user is busy, otherwise false
     */
    public boolean isBusy(RosterEntry contact) {
        Presence presence = getPresence(contact);
        return presence != null && Mode.dnd.equals(presence.getMode());
    }
}
