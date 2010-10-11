/*
 * $Id: Plugin.java 39 2009-05-03 22:38:57Z james.synge@gmail.com $
 * $HeadURL: http://run-jetty-run.googlecode.com/svn/trunk/plugin/src/runjettyrun/Plugin.java $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package runjettyrun;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author hillenius, Alex Winston
 */
public class Plugin extends AbstractUIPlugin {
	/** The icon for the RunJettyRunWebApp launch type */
	private static final String JETTY_ICON_PATH = "/icons/jetty.gif";

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "run_jetty_run";

	private static final String JETTY_ICON = PLUGIN_ID + ".jettyIcon";

	/** Configuration attribute for the full class name of the bootstrap class. */
	public static final String BOOTSTRAP_CLASS_NAME = "runjettyrun.Bootstrap";

	/** Configuration attributes for jetty version and xml. */
	public static final String ATTR_JETTY_VERSION = Plugin.PLUGIN_ID + ".JETTY_VERSION_ATTR";
	public static final String ATTR_JETTY6 = "Jetty 6";
	public static final String ATTR_JETTY7 = "Jetty 7";
	public static final String ATTR_JETTY8 = "Jetty 8";
	public static final String ATTR_JETTY_XML = Plugin.PLUGIN_ID + ".JETTY_XML_ATTR";

	/** Used to calculate the jars to include. */
	public static final String ANT_VERSION = "1.6.5";
	public static final String SERVLET_VERSION = "2.5";
	public static final String SERVLET_3_VERSION = "3.0.20100224";
	public static final String JSP_VERSION = "2.1";
	public static final String JETTY6_VERSION = "6.1.14";
	public static final String JETTY7_VERSION = "7.0.1.v20091125"; //"7.1.0.v20100505";
	public static final String JETTY8_VERSION = "8.0.0.M1";

	// The shared instance
	private static Plugin plugin;

	public Plugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Plugin getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {

		URL imageURL = getBundle().getEntry(JETTY_ICON_PATH);
		if (imageURL != null) {
			ImageDescriptor descriptor = ImageDescriptor
					.createFromURL(imageURL);
			reg.put(JETTY_ICON, descriptor);
		} else {
			logError("resource " + JETTY_ICON_PATH + " was not found");
		}
	}

	public static Image getJettyIcon() {
		return plugin.getImageRegistry().get(JETTY_ICON);
	}

	static public void logError(Exception e) {
		ILog log = plugin.getLog();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		String msg = stringWriter.getBuffer().toString();
		Status status = new Status(IStatus.ERROR, getDefault().getBundle()
				.getSymbolicName(), IStatus.ERROR, msg, null);
		log.log(status);
	}

	static public void logError(String msg) {
		ILog log = plugin.getLog();
		Status status = new Status(IStatus.ERROR, getDefault().getBundle()
				.getSymbolicName(), IStatus.ERROR, msg + "\n", null);
		log.log(status);
	}
}
