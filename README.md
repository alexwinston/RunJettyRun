RunJettyRun: Jetty Eclipse Run Configuration Plugin
===================================================

This new version of RunJettyRun is forked from the orginal work done at http://code.google.com/p/run-jetty-run/. Unfortunately the old version has the limitation of only allowing configuration from the plugin dialog and only supporting Jetty6. This new version now supports the ability to supply a jetty.xml configuration file in addition to supporting both Jetty6, Jetty7 and Jetty8.

The one caveat is that support for JSP 2.2 in Jetty8 has not yet been included, it only supports 2.1.  Once the various Maven dependencies are finalized for Jetty8 and JSP then version 2.2 will be included.

This version has been tested on Eclipse 3.5 - 3.6. Feedback on whether this works on older versions of Eclipse is encouraged.

Features
---------
- Rebuilt to use Maven2 and eliminated external dependencies
- Easily select between embedded versions of Jetty6, Jetty7 and Jetty8
- Supply jetty.xml file via configuration dialog
- New update site hosted on GitHub

Update Site
------------
- [http://alexwinston.github.com/RunJettyRun/update](http://alexwinston.github.com/RunJettyRun/update)

Quick Start
------------
Simply use Eclipse's update manager (Help -> Install New Software... -> Add... to add a remote site and point it to http://alexwinston.github.com/RunJettyRun/update. Click finish, select the plugin (just one choice) and finish again.

Open "Run Configurations" and add a new "Jetty Webapp". Select between Jetty6 and Jetty7 and then click "Browse" to locate a jetty.xml file on the filesystem or simply type the name of the jetty.xml file for the current project. By default the plugin will look in the project root directory if a path is not specified.

Building
--------
RunJettyRun uses Maven to build the plugin.  Running "mvn package" from the root should build the bootstrap and feature projects and subsequently build the plugin and copy the jar to the site/update/plugin directory.

Example Jetty 6 jetty.xml File
------------------------------
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://jetty.mortbay.org/configure.dtd">

<!-- =============================================================== -->
<!-- Configure the Jetty Server                                      -->
<!-- =============================================================== -->
<Configure id="Server" class="org.mortbay.jetty.Server">

	<!-- =========================================================== -->
	<!-- Server Thread Pool                                          -->
	<!-- =========================================================== -->
	<Set name="ThreadPool">
		<New class="org.mortbay.thread.BoundedThreadPool">
			<Set name="minThreads">10</Set>
			<Set name="maxThreads">100</Set>
		</New>
	</Set>

	<!-- =========================================================== -->
	<!-- Set connectors                                              -->
	<!-- =========================================================== -->
	<Set name="connectors">
		<Array type="org.mortbay.jetty.Connector">
			<Item>
				<New class="org.mortbay.jetty.nio.SelectChannelConnector">
					<Set name="host">localhost</Set>
					<Set name="port">8080</Set>
					<Set name="maxIdleTime">30000</Set>
					<Set name="Acceptors">10</Set>
				</New>
			</Item>
		</Array>
	</Set>

	<!-- =========================================================== -->
	<!-- Set handlers                                                -->
	<!-- =========================================================== -->
	<Set name="handlers">
		<Array type="org.mortbay.jetty.Handler">

			<!-- ======================================================= -->
			<!-- Configure a web application with web.xml           -->
			<!-- ======================================================= -->
			<Item>
				<New id="testWebAppContext" class="org.mortbay.jetty.webapp.WebAppContext">
					<Set name="contextPath">/</Set>
					<Set name="war">src/main/webapp</Set>
				</New>
			</Item>
		</Array>
	</Set>

	<!-- =========================================================== -->
	<!-- extra options                                               -->
	<!-- =========================================================== -->
	<Set name="stopAtShutdown">true</Set>

</Configure>

Example Jetty 7 and 8 jetty.xml
-------------------------------
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://jetty.mortbay.org/configure.dtd">

<!-- =============================================================== -->
<!-- Configure the Jetty Server -->
<!-- =============================================================== -->
<Configure id="Server" class="org.eclipse.jetty.server.Server">

	<!-- =========================================================== -->
	<!-- Server Thread Pool -->
	<!-- =========================================================== -->
	<Set name="ThreadPool">
		<New class="org.eclipse.jetty.util.thread.QueuedThreadPool">
			<Set name="minThreads">10</Set>
			<Set name="maxThreads">100</Set>
		</New>
	</Set>

	<!-- =========================================================== -->
	<!-- Set connectors -->
	<!-- =========================================================== -->
	<Set name="connectors">
		<Array type="org.eclipse.jetty.server.Connector">
			<Item>
				<New class="org.eclipse.jetty.server.nio.SelectChannelConnector">
					<Set name="host">localhost</Set>
					<Set name="port">8080</Set>
					<Set name="maxIdleTime">30000</Set>
					<Set name="Acceptors">10</Set>
				</New>
			</Item>
		</Array>
	</Set>

	<!-- =========================================================== -->
	<!-- Set handlers -->
	<!-- =========================================================== -->
	<Set name="handler">
		<New id="Handlers" class="org.eclipse.jetty.server.handler.HandlerCollection">
			<Set name="handlers">
				<Array type="org.eclipse.jetty.server.Handler">

					<!-- ======================================================= -->
					<!-- Configure a web application with web.xml -->
					<!-- ======================================================= -->
					<Item>
						<New class="org.eclipse.jetty.webapp.WebAppContext">
							<Set name="contextPath">/</Set>
							<Set name="war">src/main/webapp</Set>
						</New>
					</Item>
				</Array>
			</Set>
		</New>
	</Set>

	<!-- =========================================================== -->
	<!-- extra options -->
	<!-- =========================================================== -->
	<Set name="stopAtShutdown">true</Set>

</Configure>
