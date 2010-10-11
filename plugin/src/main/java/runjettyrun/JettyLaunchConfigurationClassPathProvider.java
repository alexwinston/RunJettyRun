/*
 * $Id: JettyLaunchConfigurationClassPathProvider.java 39 2009-05-03 22:38:57Z james.synge@gmail.com $
 * $HeadURL: http://run-jetty-run.googlecode.com/svn/trunk/plugin/src/runjettyrun/JettyLaunchConfigurationClassPathProvider.java $
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.StandardClasspathProvider;
import org.osgi.framework.Bundle;

public class JettyLaunchConfigurationClassPathProvider extends
    StandardClasspathProvider {

  public JettyLaunchConfigurationClassPathProvider() {
  }

  @Override
  public IRuntimeClasspathEntry[] computeUnresolvedClasspath(
      ILaunchConfiguration configuration) throws CoreException {
    IRuntimeClasspathEntry[] classpath = super
        .computeUnresolvedClasspath(configuration);
    boolean useDefault = configuration.getAttribute(
        IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
    if (useDefault) {
      //classpath = filterWebInfLibs(classpath, configuration);
      classpath = addJettyAndBootstrap(classpath, configuration);

    } else {
      // recover persisted classpath
      return recoverRuntimePath(configuration,
          IJavaLaunchConfigurationConstants.ATTR_CLASSPATH);
    }
    return classpath;
  }

  private IRuntimeClasspathEntry[] addJettyAndBootstrap(
      IRuntimeClasspathEntry[] existing, ILaunchConfiguration config) throws CoreException {

    List<IRuntimeClasspathEntry> entries = new ArrayList<IRuntimeClasspathEntry>();
    entries.addAll(Arrays.asList(existing));
    Bundle bundle = Plugin.getDefault().getBundle();
    URL installUrl = bundle.getEntry("/");

    String version = config.getAttribute(Plugin.ATTR_JETTY_VERSION, Plugin.ATTR_JETTY6);
    System.out.println("ATTR_JETTY_VERSION " + version);
    
    addRelativeArchiveEntry(entries, installUrl, "ant-"
	        + Plugin.ANT_VERSION);
    addRelativeArchiveEntry(entries, installUrl, "jsp-api-"
	        + Plugin.JSP_VERSION + "-" + Plugin.JETTY6_VERSION);
	addRelativeArchiveEntry(entries, installUrl, "jsp-"
	        + Plugin.JSP_VERSION + "-" + Plugin.JETTY6_VERSION);
	addRelativeArchiveEntry(entries, installUrl, "jetty-util-"
		    + Plugin.JETTY6_VERSION);
	    
    // Common archives for Jetty 6 and Jetty 7
    if (Plugin.ATTR_JETTY6.equals(version) || Plugin.ATTR_JETTY7.equals(version)) {
	    addRelativeArchiveEntry(entries, installUrl, "servlet-api-"
	        + Plugin.SERVLET_VERSION + "-" + Plugin.JETTY6_VERSION);
    }
    
    // Archives specific to each version of Jetty
    if (Plugin.ATTR_JETTY6.equals(version)) {
	    addRelativeArchiveEntry(entries, installUrl, "runjettyrun-bootstrap-jetty6");
	    addRelativeArchiveEntry(entries, installUrl, "jetty-"
	        + Plugin.JETTY6_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-management-"
	        + Plugin.JETTY6_VERSION);
    } else if (Plugin.ATTR_JETTY7.equals(version)) {
	    addRelativeArchiveEntry(entries, installUrl, "runjettyrun-bootstrap-jetty7");
	    addRelativeArchiveEntry(entries, installUrl, "jetty-jmx-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-xml-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-webapp-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-util-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-servlet-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-security-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-server-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-continuation-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-http-"
	        + Plugin.JETTY7_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-io-"
	        + Plugin.JETTY7_VERSION);
    } else if (Plugin.ATTR_JETTY8.equals(version)) {
    	addRelativeArchiveEntry(entries, installUrl, "runjettyrun-bootstrap-jetty8");
    	addRelativeArchiveEntry(entries, installUrl, "servlet-api-"
    	        + Plugin.SERVLET_3_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-jmx-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-xml-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-webapp-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-util-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-servlet-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-security-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-server-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-continuation-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-http-"
	        + Plugin.JETTY8_VERSION);
	    addRelativeArchiveEntry(entries, installUrl, "jetty-io-"
	        + Plugin.JETTY8_VERSION);
    } else {
    	System.out.println("ATTR_JETTY_VERSION " + version + " is invalid");
    }

    return entries.toArray(new IRuntimeClasspathEntry[entries.size()]);
  }

  private void addRelativeArchiveEntry(List<IRuntimeClasspathEntry> entries,
      URL installUrl, String libJarName) {

    try {
      String relativePath = "lib/" + libJarName + ".jar";
      URL bundleUrl = new URL(installUrl, relativePath);
      addArchiveEntry(entries, bundleUrl);
      return;
    } catch (MalformedURLException e) {
      Plugin.logError(e);
      return;
    }
  }

  private void addArchiveEntry(List<IRuntimeClasspathEntry> entries,
      URL bundleUrl) {

    try {
      URL fileUrl = FileLocator.toFileURL(bundleUrl);
      IRuntimeClasspathEntry rcpe = JavaRuntime
          .newArchiveRuntimeClasspathEntry(new Path(fileUrl.getFile()));
      entries.add(rcpe);
      return;
    } catch (IOException e) {
      Plugin.logError(e);
      return;
    }
  }

  /*
   * James Synge: overriding so that I can block the inclusion of external
   * libraries that should be found in WEB-INF/lib, and shouldn't be on the
   * JVM's class path.
   * 
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jdt.launching.StandardClasspathProvider#resolveClasspath(org
   * .eclipse.jdt.launching.IRuntimeClasspathEntry[],
   * org.eclipse.debug.core.ILaunchConfiguration)
   */
  @Override
  public IRuntimeClasspathEntry[] resolveClasspath(
      IRuntimeClasspathEntry[] entries, ILaunchConfiguration configuration)
      throws CoreException {

    Set<IRuntimeClasspathEntry> all = new LinkedHashSet<IRuntimeClasspathEntry>(
        entries.length);
    for (int i = 0; i < entries.length; i++) {
      IRuntimeClasspathEntry entry = entries[i];
      IResource resource = entry.getResource();
      if (resource instanceof IProject) {
        continue;
      }

      IRuntimeClasspathEntry[] resolved = JavaRuntime
          .resolveRuntimeClasspathEntry(entry, configuration);
      all.addAll(Arrays.asList(resolved));
    }

    IRuntimeClasspathEntry[] resolvedClasspath = all
        .toArray(new IRuntimeClasspathEntry[0]);

    return resolvedClasspath;
  }
}
