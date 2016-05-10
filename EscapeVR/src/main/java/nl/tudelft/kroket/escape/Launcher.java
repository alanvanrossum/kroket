package nl.tudelft.kroket.escape;

import jmevr.app.VRApplication.PRECONFIG_PARAMETER;

import com.jme3.system.AppSettings;

public class Launcher {

	public static void main(String[] args) {
		Main mainApplication = new Main();
		// try {
		// Client client = new Client();
		// } catch (IOException ex) {
		// Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		// }

		// create AppSettings object to enable joysticks/gamepads
		// and set the title
		AppSettings settings = new AppSettings(true);

		// enable joysticks/gamepads
		settings.setUseJoysticks(true);

		// set application/window title
		settings.setTitle("EscapeVR");

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
		mainApplication.start();
	}

}
