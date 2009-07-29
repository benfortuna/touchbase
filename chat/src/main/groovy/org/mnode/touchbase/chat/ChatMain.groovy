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

import org.osgi.framework.BundleActivatorimport java.util.Propertiesimport java.util.Listimport org.apache.felix.main.AutoActivatorimport org.apache.felix.framework.Feliximport org.osgi.framework.BundleContext

/**
 * @author fortuna
 *
 */
public class ChatMain implements BundleActivator {

     void start(final BundleContext context) throws Exception {
         new ChatBootstrap(context)
     }

     void stop(BundleContext arg0) throws Exception {
         // TODO Auto-generated method stub
         
     }

    /**
     * @param args
     */
    public static void main(def args){
        Properties config = new Properties();
        config.load(ChatMain.class.getResourceAsStream("/config.properties"));

        List activators = new ArrayList();
        activators.add(new AutoActivator(config));
        activators.add(new ChatMain());
        
        Felix felix = new Felix(config, activators);
        felix.start();
    }
    
}
