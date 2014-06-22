package Visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
 
public class Timeline {
 
    public static void createTimeSeriesXYChart(ArrayList<String> dates) throws IOException, ParseException
    {
        //SimpleDateFormat dt = new SimpleDateFormat("yyyy MMM dd hh:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        
        
        /*for(int i = 1; i<dates.size(); i++){
            String date = dates.get(i).substring(0, 11);
            //Date day = dt.parse(dates.get(i));
            //String date = sdf.format(day);
            dates.set(i, date);
            System.out.println("#############");
            System.out.println(dates.get(i));
            
        }*/
        
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<Integer> ctr = new ArrayList<Integer>();
        day.add("2013/10/07");
        day.add("2013/10/07");
        day.add("2013/10/07");
        day.add("2013/01/01");
        day.add("2013/02/03");
        //day.add("2013/02/03");
        //day.add("2014 Feb 03");
        //day.add("2013/05/03");
        
        
        /*for(int i = 0; i<dates.size(); i++){
            String date = dates.get(i).substring(1, 11);
            dates.set(i, date);
        }*/
        
        TimeSeries series = new TimeSeries( "", Day.class );
        String curDate = day.get(0);
        int count = 0;
        
        for(int i = 0; i<day.size(); i++){
            if(day.get(i).equals(curDate)){
                count++;
            }
            else
            {
                System.out.println("hello "+ curDate + " #" + count);
                series.add(new Day(new Date(curDate)), count);
                count = 0;
                curDate = day.get(i);
                
            }
                           
        }
        
        /*series.add(new Day(new Date("2013/01/01")), 23);
        series.add(new Day(new Date("2014/01/01")), 43);
        series.add(new Day(new Date("2014/02/03")), 43);
        series.add(new Day(new Date("2013/02/02")), 33);
        series.add(new Day(new Date("2013/03/03")), 99);
        series.add(new Day(new Date("2013/04/04")), 13);
        series.add(new Day(new Date("2013/05/05")), 100);
        series.add(new Day(new Date("2013/06/06")), 24);
        series.add(new Day(new Date("2013/07/07")), 20);*/
 
        TimeSeriesCollection dataset=new TimeSeriesCollection();
        dataset.addSeries(series);
 
        JFreeChart chart = ChartFactory.createTimeSeriesChart
        ("Timeline",    // Title
         "Day",                     // X-Axis label
         "Tweet Count",             // Y-Axis label
         dataset,               // Dataset
         true,                      // Show legend
         true,              //tooltips
         false              //url
        );  
 
        
        try {
            ChartUtilities.saveChartAsJPEG(new File("src\\timeline.jpg"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem occurred in creating chart.");
        }
        
        String path = "src\\timeline.jpg";
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