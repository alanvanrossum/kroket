package nl.tudelft.kroket.escape;

import jmevr.app.VRApplication.PRECONFIG_PARAMETER;

import com.jme3.system.AppSettings;

/**
 * Entry point for VR application.
 * @author Team Kroket
 *
 */
public class Launcher {

	static EscapeVR mainApplication;

	public static void main(String[] args) {
		mainApplication = new EscapeVR();

		// create AppSettings object to enable joysticks/gamepads
		// and set the title
		AppSettings settings = new AppSettings(true);

		// enable joysticks/gamepads
		settings.setUseJoysticks(true);

		// set application/window title
		settings.setTitle("EscapeVR");

		
		// throw settings at the application

		mainApplication.setSettings(settings);

		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE,
				false);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW,
				true);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
		mainApplication.preconfigureFrustrumNearFar(0.1f, 512f);
		
		// finally, start the application

		mainApplication.start();

	}

}
