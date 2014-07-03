/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual;

/**
 *
 * @author MAZacarias
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.FeatureStatistics;

public class PieChart {
    
    public static void createPieChart(String name, FeatureStatistics stat) throws IOException{

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Tweets", stat.getTweets());
        pieDataset.setValue("Links", stat.getLinks());
        pieDataset.setValue("Retweets", stat.getRetweets());
        JFreeChart chart = ChartFactory.createPieChart("Feature Statistics", pieDataset, true, true, false);
        
        String url = "src\\visual\\pie\\"+name+"Chart.jpeg";
        try {
            ChartUtilities.saveChartAsJPEG(new File(url), chart, 500, 300);
        } catch (Exception e) {
            System.out.println("Problem occurred creating chart.");
        }
        
        String path = url;
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);
        JLabel label = new JLabel(new ImageIcon(image));
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.getContentPane().add(label);
        f.pack();
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
