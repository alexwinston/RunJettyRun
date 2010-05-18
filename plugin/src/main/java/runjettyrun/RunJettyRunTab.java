/*
 * $Id: RunJettyRunTab.java 39 2009-05-03 22:38:57Z james.synge@gmail.com $
 * $HeadURL: http://run-jetty-run.googlecode.com/svn/trunk/plugin/src/runjettyrun/RunJettyRunTab.java $
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

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Launch tab for the RunJettyRun plugin.
 * 
 * @author hillenius, James Synge, Alex Winston
 */
public class RunJettyRunTab extends JavaLaunchTab {

  private static abstract class ButtonListener implements SelectionListener {
    public void widgetDefaultSelected(SelectionEvent e) {
    }
  }

  private Combo fJettyVersionCombo;
  private Text fJettyXmlText;
  private Button fJettyXmlButton;

  /**
   * Construct.
   */
  public RunJettyRunTab() {
  }

  public void createControl(Composite parent) {
    Composite comp = new Composite(parent, SWT.NONE);
    comp.setFont(parent.getFont());

    GridData gd = new GridData(1);
    gd.horizontalSpan = GridData.FILL_BOTH;
    comp.setLayoutData(gd);

    GridLayout layout = new GridLayout(1, false);
    layout.verticalSpacing = 0;
    comp.setLayout(layout);

    createXmlEditor(comp);
    createVerticalSpacer(comp, 1);
    setControl(comp);

    return;
  }

  /**
   * Creates the widgets for specifying the jetty.xml file.
   * 
   * @param parent the parent composite
   */
  private void createXmlEditor(Composite parent) {
    Font font = parent.getFont();
    Group group = new Group(parent, SWT.NONE);
    group.setText("Jetty Xml Configuration File");
    GridData gd = createHFillGridData();
    group.setLayoutData(gd);
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    group.setLayout(layout);
    group.setFont(font);
    
    fJettyVersionCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
    fJettyVersionCombo.add(Plugin.ATTR_JETTY6);
    fJettyVersionCombo.add(Plugin.ATTR_JETTY7);
    fJettyVersionCombo.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
        	System.out.println(fJettyVersionCombo.getText());
        	updateLaunchConfigurationDialog();
        }
    });
    
    fJettyXmlText = new Text(group, SWT.SINGLE | SWT.BORDER);
    gd = createHFillGridData();
    fJettyXmlText.setLayoutData(gd);
    fJettyXmlText.setFont(font);
    fJettyXmlText.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        updateLaunchConfigurationDialog();
      }
    });
    
    fJettyXmlButton = createPushButton(group, "&Browse...", null);
    fJettyXmlButton.addSelectionListener(new ButtonListener() {
      public void widgetSelected(SelectionEvent e) {
        handleProjectButtonSelected();
      }
    });
  }

  private GridData createHFillGridData() {
    GridData gd = new GridData();
    gd.horizontalAlignment = SWT.FILL;
    gd.grabExcessHorizontalSpace = true;
    return gd;
  }
 
  @Override
  public Image getImage() {
    return Plugin.getJettyIcon();
  }

  @Override
  public String getMessage() {
    return "Create a configuration to launch a web application with Jetty.";
  }

  public String getName() {
    return "Jetty";
  }

  public void initializeFrom(ILaunchConfiguration configuration) {
    super.initializeFrom(configuration);
    try {
    	fJettyVersionCombo.select(
    			fJettyVersionCombo.indexOf(configuration.getAttribute(
    					Plugin.ATTR_JETTY_VERSION, Plugin.ATTR_JETTY6)));
    	fJettyXmlText.setText(configuration.getAttribute(Plugin.ATTR_JETTY_XML, ""));
    } catch (CoreException e) {
      Plugin.logError(e);
    }
  }

  public boolean isValid(ILaunchConfiguration config) {
    setErrorMessage(null);
    setMessage(null);

    String xmlFile = fJettyXmlText.getText().trim();
    if (xmlFile.length() == 0) {
        setErrorMessage("No Jetty Xml Configuration File selected");
        return false;
    }
    
    return true;
  }

  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(Plugin.ATTR_JETTY_VERSION, fJettyVersionCombo.getText());
    configuration.setAttribute(Plugin.ATTR_JETTY_XML, fJettyXmlText.getText());

    return;
  }

  public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {

    IJavaElement javaElement = getContext();
    if (javaElement != null) {
      initializeJavaProject(javaElement, configuration);
    } else {
      configuration.setAttribute(ATTR_PROJECT_NAME, "");
    }

    configuration.setAttribute(
        IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
        Plugin.BOOTSTRAP_CLASS_NAME);

    // set the class path provider so that Jetty and the bootstrap jar are
    // added to the run time class path. Value has to be the same as the one
    // defined for the extension point
    configuration.setAttribute(
    		IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
    		"RunJettyRunWebAppClassPathProvider");

    // get the name for this launch configuration
    String launchConfigName = "";
    try {
      // try to base the launch config name on the current project
      launchConfigName = configuration.getAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
    } catch (CoreException e) {
      // ignore
    }
    if (launchConfigName == null || launchConfigName.length() == 0) {
      // if no project name was found, base on a default name
      launchConfigName = "Jetty Webapp";
    }
    // generate an unique name (e.g. myproject(2))
    launchConfigName = getLaunchConfigurationDialog().generateName(
        launchConfigName);
    configuration.rename(launchConfigName); // and rename the config
    configuration.setAttribute(Plugin.ATTR_JETTY_VERSION, Plugin.ATTR_JETTY6);
    configuration.setAttribute(Plugin.ATTR_JETTY_XML, "");

    return;
  }

  protected void handleProjectButtonSelected() {
    FileDialog dialog = new FileDialog(getControl().getShell());
    dialog.setText("Choose a jetty.xml file");
    String res = dialog.open();
    if (res != null)
      fJettyXmlText.setText(res);
  }
}
