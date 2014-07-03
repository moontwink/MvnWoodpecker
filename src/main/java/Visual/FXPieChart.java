
package Visual;


import gui.LM_DrillDown;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.FeatureStatistics;
import model.LMDrillModel;

public class FXPieChart {

    static JFXPanel javafxPanel;
    static FeatureStatistics statistics;

  public static void PieApplicationFrame(JPanel panel, FeatureStatistics stat){

    javafxPanel = new JFXPanel();
    statistics = stat;
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

          Scene scene = new Scene(new Group());

          ObservableList<javafx.scene.chart.PieChart.Data> pieChartData =
                  FXCollections.observableArrayList(
                  new javafx.scene.chart.PieChart.Data("Tweets", statistics.getTweets()),
                  new javafx.scene.chart.PieChart.Data("Retweets", statistics.getRetweets()),
                  new javafx.scene.chart.PieChart.Data("Links", statistics.getLinks()));

          final javafx.scene.chart.PieChart chart = new javafx.scene.chart.PieChart(pieChartData);
          chart.setTitle("Tweet Statistics");
          final javafx.scene.control.Label caption = new javafx.scene.control.Label("");
          caption.setTextFill(javafx.scene.paint.Color.BLACK);
          caption.setStyle("-fx-font: 24 arial;");

          for (final javafx.scene.chart.PieChart.Data data : chart.getData()) {
              data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                      new EventHandler<MouseEvent>() {
                          @Override public void handle(MouseEvent e) {
                              caption.setTranslateX(e.getSceneX());
                              caption.setTranslateY(e.getSceneY());
                              caption.setText(String.valueOf(data.getPieValue()));
                          }
                      });
          }

          ((Group) scene.getRoot()).getChildren().addAll(chart, caption);
          //scene.getStylesheets().add("piechartsample/Chart.css");

          javafxPanel.setScene(scene);

        }
      });
    }
}


