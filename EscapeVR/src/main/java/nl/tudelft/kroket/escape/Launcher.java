package nl.tudelft.kroket.escape;

import jmevr.app.VRApplication.PRECONFIG_PARAMETER;

import com.jme3.system.AppSettings;

/**
 * Entry point for VR application.
 * 
 * @author Team Kroket
 *
 */
public class Launcher {

  public static EscapeVR mainApplication;

  /**
   * The main method.
   * 
   * @param args
   *          - String[]
   */
  public static void main(String[] args) {
    
    String remoteHost = "127.0.0.1";

    if (args.length > 1) {
      remoteHost = args[0];
    }

    mainApplication = new EscapeVR(remoteHost);

    // create AppSettings object to enable joysticks/gamepads
    // and set the title
    AppSettings settings = new AppSettings(true);

    // enable joysticks/gamepads
    settings.setUseJoysticks(true);

    // set application/window title
    settings.setTitle("EscapeVR");

    // throw settings at the application

    mainApplication.setSettings(settings);

    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, true);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW, true);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
    mainApplication.preconfigureFrustrumNearFar(0.1f, 512f);

    // finally, start the application
    mainApplication.start();


  }

}
