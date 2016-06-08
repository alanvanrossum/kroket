package nl.tudelft.kroket;

import com.jme3.system.AppSettings;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jmevr.app.VRApplication.PRECONFIG_PARAMETER;
import nl.tudelft.kroket.escape.EscapeVR;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Entry point for VR application. Launches a simple dialog window to prompt the user for name and
 * remote host.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("restriction")
public class Launcher extends Application {

  private static final int DIALOG_WIDTH = 500;
  private static final int DIALOG_HEIGHT = 300;
  private static final String DIALOG_TITLE = "Escaparade";

  private static String remoteHost = "localhost";
  private static String playerName = "VR-USER";

  private static EscapeVR mainApplication;

  /**
   * The main method.
   * 
   * @param args
   *          - String[]
   */
  public static void main(String[] args) {

    // allow remote address to be set using commandline arguments
    // (for now)
    if (args.length > 1 && !args[0].isEmpty()) {
      remoteHost = args[0];
    }

    System.out.println("...");

    launch(args);

    mainApplication = new EscapeVR();

    // set the hostname/ip address of the remote machine
    mainApplication.setRemoteHost(remoteHost);
    mainApplication.setPlayerName(playerName);

    // create AppSettings object to enable joysticks/gamepads
    // and set the title
    AppSettings settings = new AppSettings(true);
    try {
      BufferedImage buff16 = ImageIO.read(Launcher.class
          .getResourceAsStream("/Interface/icon16.png"));
      BufferedImage buff32 = ImageIO.read(Launcher.class
          .getResourceAsStream("/Interface/icon32.png"));
      settings.setIcons(new BufferedImage[] { buff16, buff32 });
    } catch (IOException e) {
      e.printStackTrace();
    }

    settings.setSettingsDialogImage("/Interface/splash.png");

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

  @Override
  public void start(Stage primaryStage) {

    primaryStage.setWidth(DIALOG_WIDTH);
    primaryStage.setHeight(DIALOG_HEIGHT);
    primaryStage.setTitle(DIALOG_TITLE);
    primaryStage.requestFocus();

    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      public void handle(WindowEvent we) {
        System.out.println("X pressed, closing...");
        System.exit(0);
      }
    });

    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text sceneTitle = new Text("Welcome");
    sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(sceneTitle, 0, 0, 2, 1);

    Label lblUserName = new Label("Username:");
    grid.add(lblUserName, 0, 1);

    TextField userTextField = new TextField();
    grid.add(userTextField, 1, 1);

    Label lblRemoteHost = new Label("Remote host:");
    grid.add(lblRemoteHost, 0, 2);

    TextField remoteField = new TextField();
    grid.add(remoteField, 1, 2);

    Button btn = new Button("Let's play!");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 1, 4);

    btn.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent e) {

        if (!remoteField.getText().isEmpty()) {
          remoteHost = remoteField.getText();
        }
        if (!userTextField.getText().isEmpty()) {
          playerName = userTextField.getText();
        }
        primaryStage.close();
      }

    });

    Scene scene = new Scene(grid, DIALOG_WIDTH, DIALOG_HEIGHT);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
