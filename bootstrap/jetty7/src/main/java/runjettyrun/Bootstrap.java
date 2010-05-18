/*
 * $Id: Bootstrap.java 55 2009-06-14 21:30:26Z james.synge@gmail.com $
 * $HeadURL: http://run-jetty-run.googlecode.com/svn/trunk/bootstrap/src/runjettyrun/Bootstrap.java $
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

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * Started up by the plugin's runner. Starts Jetty.
 * 
 * @author hillenius, jsynge
 * @author winston, alex
 */
public class Bootstrap {

  /**
   * Main function, starts the jetty server.
   * 
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String version = System.getProperty("rjrversion");
    String xml = System.getProperty("rjrxml");

    if (version == null)
      throw new IllegalStateException("VM Argument -Drjrversion must be supplied");
    if (xml == null)
      throw new IllegalStateException("VM Argument -Drjrxml must be supplied for the jetty.xml file");

    Server server = new Server();
    
    XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(xml));
    configuration.configure(server);

    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    server.getContainer().addEventListener(mBeanContainer);
    mBeanContainer.start();

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }
    
    return;
  }
}