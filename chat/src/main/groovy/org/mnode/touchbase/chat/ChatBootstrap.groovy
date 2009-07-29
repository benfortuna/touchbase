/**
 * This file is part of Base Modules.
 *
 * Copyright (c) 2009, Ben Fortuna [fortuna@micronode.com]
 *
 * Base Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Base Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Base Modules.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.touchbase.chat

import org.osgi.framework.ServiceEvent
import org.osgi.framework.ServiceListener
import javax.swing.JFrameimport org.osgi.framework.BundleContext

/**
 * @author fortuna
 *
 */
public class ChatBootstrap implements ServiceListener{
    
    def BundleContext context
    
    public ChatBootstrap(BundleContext context) {
        this.context = context
    }
    
    /**
     * {@inheritDoc}
     */
    public void serviceChanged(ServiceEvent e){
         JFrame chatFrame = (JFrame) context.getService(e.getServiceReference())
         chatFrame.setVisible(true)
    }
    
}
