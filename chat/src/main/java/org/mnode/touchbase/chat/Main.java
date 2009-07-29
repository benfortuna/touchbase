package org.mnode.touchbase.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import org.apache.felix.framework.Felix;
import org.apache.felix.main.AutoActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class Main {

	public static void main(String[] args) throws BundleException, IOException {
		
//		Map<String, String> config = new HashMap<String, String>();
		/*
		config.put("felix.cache.profiledir", "felix_cache");
		config.put("felix.embedded.execution", "true");
		config.put(Constants.FRAMEWORK_SYSTEMPACKAGES, "javax.naming.directory, javax.swing, javax.net.ssl, javax.security.sasl, javax.security.auth.callback, javax.xml.parsers, org.w3c.dom, javax.net, javax.swing.text, javax.swing.border, javax.accessibility, javax.swing.tree, javax.security.auth.login, org.xml.sax, javax.swing.colorchooser, javax.imageio, javax.swing.filechooser, javax.swing.plaf, javax.swing.event, javax.swing.plaf.basic, javax.swing.text.html, javax.naming, javax.crypto");
		config.put(AutoActivator.AUTO_START_PROP,
				"file:/home/fortuna/.m2/repository/org/mnode/base/xmpp/0.0.1-SNAPSHOT/xmpp-0.0.1-SNAPSHOT.jar "
				+ "file:/home/fortuna/.m2/repository/org/mnode/base/views/0.0.1-SNAPSHOT/views-0.0.1-SNAPSHOT.jar "
				+ "file:/home/fortuna/.m2/repository/org/mnode/base/cache/0.0.1-SNAPSHOT/cache-0.0.1-SNAPSHOT.jar "
				+ "file:/home/fortuna/.m2/repository/org/mnode/touchbase/im/0.0.1-SNAPSHOT/im-0.0.1-SNAPSHOT.jar "
				+ "file:/home/fortuna/.m2/repository/org/slf4j/com.springsource.slf4j.org.apache.commons.logging/1.5.0/com.springsource.slf4j.org.apache.commons.logging-1.5.0.jar "
				+ "file:/home/fortuna/.m2/repository/org/slf4j/com.springsource.slf4j.api/1.5.0/com.springsource.slf4j.api-1.5.0.jar "
				+ "file:/home/fortuna/.m2/repository/org/slf4j/com.springsource.slf4j.log4j/1.5.0/com.springsource.slf4j.log4j-1.5.0.jar "
				+ "file:/home/fortuna/.m2/repository/org/springframework/osgi/log4j.osgi/1.2.15-SNAPSHOT/log4j.osgi-1.2.15-SNAPSHOT.jar");
				*/
		
		Properties config = new Properties();
		config.load(Main.class.getResourceAsStream("/config.properties"));

		List<BundleActivator> activators = new ArrayList<BundleActivator>();
		activators.add(new AutoActivator(config));
		activators.add(new BundleActivator() {
			@Override
			public void start(final BundleContext context) throws Exception {
//				ServiceTracker tracker = new ServiceTracker(arg0, JFrame.class.getName(), null);
//				tracker.open();
				
				context.addServiceListener(new ServiceListener() {
				
					@Override
					public void serviceChanged(ServiceEvent e) {
						JFrame chatFrame = (JFrame) context.getService(e.getServiceReference());
						chatFrame.setVisible(true);
					}
				}, "(objectClass=" + JFrame.class.getName() + ")");
			}
			@Override
			public void stop(BundleContext arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
		
		Felix felix = new Felix(config, activators);
		felix.start();
	}
}
