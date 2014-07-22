
package Visual.timeline;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class FXTimeline {

    static JFXPanel javafxPanel;
    static WebView webComponent;
    static String htmlrelativeurl;

    /**
    * Loads the JFXPanel for timeline into the tabbed pane in the main gui
    * @param panel
    * @param url
    */
  public static void TimelineApplicationFrame(JPanel panel, String url){

            javafxPanel = new JFXPanel();
            htmlrelativeurl = url;
	    initSwingComponents(panel);
	    loadJavaFXScene();
  }

    /**
    * Instantiate the Swing components to be used
    */
    private static void initSwingComponents(JPanel mainPanel){
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(javafxPanel, BorderLayout.CENTER);
    }

    /**
    * Instantiate the JavaFX Components in
    * the JavaFX Application Thread.
    */
    private static void loadJavaFXScene(){
      Platform.runLater(new Runnable() {
        @Override
        public void run() {

            BorderPane borderPane = new BorderPane();
            webComponent = new WebView();

            String dir = System.getProperty("user.dir");
                System.out.println("//current dir =  " + dir);
            dir = dir.replace("\\", "/");
                System.out.println("//replaced dir = " + dir);
            String url = "file:///" + dir + "/" + htmlrelativeurl;
                System.out.println("//-url = " + url);

            webComponent.getEngine().load(url);

            borderPane.setCenter(webComponent);
            Scene scene = new Scene(borderPane,450,450);
            javafxPanel.setScene(scene);

        }
      });
    }
}


