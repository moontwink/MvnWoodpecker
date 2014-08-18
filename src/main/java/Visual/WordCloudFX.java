
package Visual;


import javafx.application.Platform;
	import javafx.embed.swing.JFXPanel;
	import javafx.scene.Scene;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.web.WebView;
	 
	import javax.swing.*;
	import java.awt.*;
	 
	public class WordCloudFX {
            
	 static JFXPanel javafxPanel;
         static WebView webComponent;
         static String htmlrelativeurl;
	 // JPanel mainPanel;
	 
        /**
         * Loads the JFXPanel for Wordcloud into the tabbed pane in the main gui
         * @param panel
         * @param url
         */
	  public static void ApplicationFrame(JPanel panel, String url){
	 
	    javafxPanel = new JFXPanel();
            htmlrelativeurl = url;
	    initSwingComponents(panel);
	    loadJavaFXScene();
	  }
	 
	  /**
49	  * Instantiate the Swing compoents to be used
50	  */
	  private static void initSwingComponents(JPanel mainPanel){
	    //mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
	    mainPanel.add(javafxPanel, BorderLayout.CENTER);
	 
	    /**
63	     * Handling the loading of new URL, when the user
64	     * enters the URL and clicks on Go button.
6	     */
	    
	   // mainPanel.add(BorderLayout.NORTH);
	 
	   // this.add(mainPanel);
	   // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   // this.setSize(700,600);
	  }
	 
	  /**
91	  * Instantiate the JavaFX Components in
92	  * the JavaFX Application Thread.
93	  */
	  private static void loadJavaFXScene(){
	    Platform.runLater(new Runnable() {
	      @Override
	      public void run() {
	 
	        BorderPane borderPane = new BorderPane();
	        webComponent = new WebView();
	 
                String dir = System.getProperty("user.dir");
//                    System.out.println("//current dir =  " + dir);
                dir = dir.replace("\\", "/");
//                    System.out.println("//replaced dir = " + dir);
                String url = "file:///" + dir + "/" + htmlrelativeurl;
//                    System.out.println("//-url = " + url);
                
//	        webComponent.getEngine().load("file:///C:/Users/Nancy/Desktop/MvnWoodpecker-master/src/main/java/Visual/d3/temp-oscars.html");
                webComponent.getEngine().load(url);
                    
                borderPane.setCenter(webComponent);
	        Scene scene = new Scene(borderPane,450,450);
	        javafxPanel.setScene(scene);
	 
	      }
	    });
	  }
        }
	

