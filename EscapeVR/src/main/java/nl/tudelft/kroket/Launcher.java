package nl.tudelft.kroket;

import com.jme3.system.AppSettings;
import jmevr.app.VRApplication.PRECONFIG_PARAMETER;
import nl.tudelft.kroket.escape.EscapeVR;

/**
 * Entry point for VR application.
 * 
 * @author Team Kroket
 *
 */
public class Launcher {

  private static EscapeVR mainApplication;

  /**
   * The main method.
   * 
   * @param args
   *          - String[]
   */
  public static void main(String[] args) {

    String remoteHost = "127.0.0.1";

    // allow remote address to be set using commandline arguments
    // (for now)
    if (args.length > 1 && !args[0].isEmpty()) {
      remoteHost = args[0];
    }

    mainApplication = new EscapeVR();

    // set the hostname/ip address of the remote machine
    mainApplication.setRemoteHost(remoteHost);

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

    // enable the mirror window to be used, this will show whatever is shown in the
    // VR goggles
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, true);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW, true);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
    mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
    mainApplication.preconfigureFrustrumNearFar(0.1f, 512f);

    // finally, start the application
    mainApplication.start();

  }

}
