/*
 * This file is part of Touchbase.
 *
 * Created: [13/08/2008]
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

import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jivesoftware.smack.RosterEntry;
import org.mnode.touchbase.im.action.AbstractContactAction;
import org.mnode.touchbase.im.action.CallContact;

/**
 * @author Ben
 *
 */
public class ListActionsUI extends AbstractLayerUI<JXList> {

    private List<Action> actions;
    
    private JXPanel actionPane;
    
    private int currentViewIndex;
    
    private Timer showActionsTimer;
    
    private TimerTask showActionsTask;
    
    /**
     * 
     */
    public ListActionsUI(List<Action> actions) {
        this.actions = actions;
        
        actionPane = new JXPanel(new FlowLayout(FlowLayout.TRAILING));
//        actionPane = Box.createHorizontalBox();
        actionPane.setOpaque(false);
        actionPane.setAlpha(0.5f);
        actionPane.setBorder(null);
        ActionContainerFactory acf = new ActionContainerFactory();
        for (Action action : actions) {
            AbstractButton b = acf.createButton(action);
            Actions.configureSmallButton(b, BorderFactory.createEmptyBorder(2, 2, 2, 2));
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    actionPane.setAlpha(1f);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    actionPane.setAlpha(0.5f);
                }
            });
            actionPane.add(b);
        }
//        actionPane.setSize(50, 30);
        actionPane.setVisible(false);
        currentViewIndex = -1;
        showActionsTimer = new Timer();
    }
    
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(null);
        l.getGlassPane().add(actionPane);
    }
    
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(new FlowLayout());
        l.getGlassPane().remove(actionPane);
    }
    
    @Override
    protected void processMouseEvent(MouseEvent e, JXLayer<JXList> l) {
        super.processMouseEvent(e, l);
        if (e.getID() == MouseEvent.MOUSE_EXITED && e.getSource().equals(l.getView())) {
            Point pointerLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(pointerLocation, l.getView());
            if (!l.getView().contains(pointerLocation)) {
                if (showActionsTask != null) {
                    showActionsTask.cancel();
                }
                actionPane.setVisible(false);
                currentViewIndex = -1;
            }
        }
    }
    
    @Override
    protected void processMouseMotionEvent(MouseEvent e, final JXLayer<JXList> l) {
        super.processMouseMotionEvent(e, l);

        if (e.getID() == MouseEvent.MOUSE_MOVED && e.getSource().equals(l.getView())) {
            final int viewIndex = l.getView().locationToIndex(e.getPoint());
            if (viewIndex >= 0 && viewIndex != currentViewIndex) {
                actionPane.setVisible(false);
                if (showActionsTask != null) {
                    showActionsTask.cancel();
                }
                showActionsTask = new TimerTask() {
                    @Override
                    public void run() {
                        for (Action action : actions) {
                            ((AbstractContactAction) action).setContact(((RosterEntry) l.getView().getElementAt(viewIndex)).getUser());
                        }
                        Rectangle cellBounds = l.getView().getCellBounds(viewIndex, viewIndex);
                        actionPane.setSize(cellBounds.width, cellBounds.height);
                        actionPane.setLocation(cellBounds.x, cellBounds.y);
                        actionPane.setVisible(true);
                    }
                };
                showActionsTimer.schedule(showActionsTask, 1000);
                currentViewIndex = viewIndex;
            }
        }

    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        JXList list = new JXList(new Object[] {"1", "2", "3", "4", "5"});
        
        List<Action> actions = new ArrayList<Action>();
        actions.add(new CallContact("contactList.call"));
        
        JXLayer<JXList> layer = new JXLayer<JXList>(list, new ListActionsUI(actions));
        
        JXFrame f = new JXFrame(ListActionsUI.class.getSimpleName());
        f.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        f.add(layer);
        f.pack();
        f.setVisible(true);
    }
}
