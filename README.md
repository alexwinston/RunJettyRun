RunJettyRun: Jetty Eclipse Run Configuration Plugin
===================================================

This new version of RunJettyRun is forked from the orginal work done at http://code.google.com/p/run-jetty-run/. Unfortunately the old version has the limitation of only allowing configuration from the plugin dialog. This new version now supports the ability to supply a jetty.xml configuration file in addition to some other small enhancements.

This version has only been tested on Eclipse 3.5.2. Feedback on whether this works on older versions of Eclipse is encouraged.

Features
---------
- Rebuilt to use Maven2 and eliminated external dependencies
- Supply jetty.xml file via configuration dialog
- New update site hosted on GitHub for this new fork

Quick Start
------------
Simply use Eclipse's update manager (Help -> Install New Software... -> Add... to add a remote site and point it to http://alexwinston.github.com/RunJettyRun/update. Click finish, select the plugin (just one choice) and finish again.

Open "Run Configurations" and add a new "Jetty Webapp". Click "Browse" to locate a jetty.xml file on the filesystem or simply type the name of the jett.xml file for the current project. By default the plugin will look in the project root directory if a path is not specified.

In order for this version of RunJettyRun to work correctly with "Maven Dependencies" the jetty.xml file must also set the classLoader in the WebAppContext similar to the example below.

	<!-- ================================================== -->
	<!-- Configure a web application with web.xml           -->
	<!-- ================================================== -->
	<Item>
		<New id="testWebAppContext" class="org.mortbay.jetty.webapp.WebAppContext">
			<Set name="contextPath">/MyHealth</Set>
			<Set name="war">src/main/webapp</Set>
			<Set name="classLoader">
				<New id="webAppClassloader" class="runjettyrun.ProjectClassLoader">
					<Arg>
						<Ref id="testWebAppContext" />
					</Arg>
				</New>
			</Set>
		</New>
	</Item>

The "classLoader" is now required to be injected into the WebAppContext in order to pickup the project classpath in Eclipse. I imagine there might be a better way to do this but it was the best I could do for this first version.  Any thoughts are welcome on how this could be improved.

* Take special note of the fact that the "Ref id" must match the "New id" for the WebAppContext.

Building
--------
RunJettyRun uses Maven to build the plugin.  Running "mvn package" from the root should build the bootstrap and feature projects and subsequently build the plugin and copy the jar to the site/update/plugin directory.
