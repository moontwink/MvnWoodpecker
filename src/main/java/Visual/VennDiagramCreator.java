/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual;

/**
 *
 * @author mathewmichael
 */

/**
 *
 * The MIT License
 *
 * Copyright (c) 2011 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import static com.googlecode.charts4j.Color.*;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.Plot;
import com.googlecode.charts4j.Plots;
import static com.googlecode.charts4j.UrlUtil.normalize;
import com.googlecode.charts4j.VennDiagram;
import gui.TM_DrillDown;
import java.awt.*;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.TMDrillModel;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class VennDiagramCreator extends JFrame{
    
        private static final int count =3;
        private static final int Topic1 = 1; 
        private static final int Topic2 = 2;
        private static final int Topic3 = 3;
        private static TMDrillModel VennContents;
        private static ArrayList<String> commonkeywords;
        private static String commonkeyword = " ";
        
//    public VennDiagramCreator()
//    {
//        int i =0;
//        arraylist.add(Topic1);
//        arraylist.add(Topic2);
//        arraylist.add(Topic3);
//        
//    }
    
    public static String VennCreate(TMDrillModel tmDM) throws IOException
    {
      VennContents = tmDM;
      int i =0;
      
      String url = VennDiagramCreator.setVenn(Topic1,Topic2,Topic3,count);
      return url;
    }
        
    public static String setVenn(int topic1, int topic2, int topic3,int counter) throws IOException
    {
        String url = "";
        int i = 0,BCVennscore = 0, ABVennscore = 0,ACVennscore = 0;
        double abIntersect = 0.0 , caIntersect = 0.0 , bcIntersect = 0.0;
        //9 - 1 word //18 - 2 words //27 - 3 words//36 - 4 words//45 - 5 words 
        //abIntersect = Topic 1 and Topic 2 //caIntersect = Topic 1 and Topic 3 //bcIntersect =  Topic 2 and Topic 3
        //topic 1 orange topic 2 green topic 3 blue
        
        if (counter == 1)
        {  
            final VennDiagram chart = GCharts.newVennDiagram(100.0, 0.0, 0.0, abIntersect, caIntersect, bcIntersect, 0.0);
            chart.setTitle("Topic Modeller Results", BLACK, 20);
            chart.setSize(700, 250);
            chart.setCircleLegends("Topic " +topic1, " ", " ");
            chart.setCircleColors(FIREBRICK,VIOLET,GOLD);
            chart.setBackgroundFill(Fills.newSolidFill(WHITE));
//            displayUrlString(TM_DrillDown.venndiagrampanel,chart.toURLString());
            return chart.toURLString();
        }
        else if (counter == 2)
        {
//            BCVennscore = getVennBCScore(topic1,topic2,topic3,counter,VennContents);
//            ACVennscore = getVennACScore(topic1,topic2,topic3,counter,VennContents);
            ABVennscore = getVennABScore(topic1,topic2,topic3,counter,VennContents);
            System.out.println("The venn score passed to creator is " +ABVennscore);
            final VennDiagram chart = GCharts.newVennDiagram(100.0, 100.0, 0.0, ABVennscore, caIntersect, bcIntersect, 0.0);
            chart.setTitle("Topic Modeller Results", BLACK, 20);
            chart.setSize(700, 250);
            chart.setCircleLegends("Topic " +topic1,"Topic " +topic2," The common terms are  "+commonkeywords);
            chart.setCircleColors(FIREBRICK,VIOLET,GOLD);
            chart.setBackgroundFill(Fills.newSolidFill(WHITE));
//            displayUrlString(TM_DrillDown.venndiagrampanel,chart.toURLString());
            return chart.toURLString();
        }
        else if (counter == 3)
        {
            BCVennscore = getVennBCScore(topic1,topic2,topic3,counter,VennContents);
            ACVennscore = getVennACScore(topic1,topic2,topic3,counter,VennContents);
            ABVennscore = getVennABScore(topic1,topic2,topic3,counter,VennContents);
            final VennDiagram chart = GCharts.newVennDiagram(100.0, 100.0, 100.0, ABVennscore, ACVennscore, BCVennscore, 0.0);
            chart.setTitle("Topic Modeller Results", BLACK, 20);
            chart.setSize(700, 250);
            chart.setCircleLegends("Topic " +topic1,"Topic " +topic2,"Topic " +topic3);
            chart.setCircleColors(FIREBRICK,VIOLET,GOLD);
            chart.setBackgroundFill(Fills.newSolidFill(WHITE));
//            displayUrlString(TM_DrillDown.venndiagrampanel,chart.toURLString());
            return chart.toURLString();
        }
       return url;
    }
   
    
   public static String displayUrlString(JPanel p,final String urlString) throws IOException
   {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(ImageIO.read(new URL(urlString))); 
        JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(urlString))));
//        p.setOpaque(true);
//        p.setBackground(Color.white);
//        JLabel label = new JLabel(); 
//        label.setIcon(icon);    
//        p.add(label); 
        
        frame.add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
         
        return urlString;
        
        
        
    }

    private static int getVennBCScore(int topic1, int topic2, int topic3, int counter, TMDrillModel venn) //gets the relationship of two venn diagrams
    {
        int vennscore = 0;
        
        //minus 1 since index of arraylist begins at 0
        int firsttopic =topic1 -1;
        int secondtopic = topic2 -1;
        int thirdtopic = topic3 -1;
        
        //venn b and c are the inputs
        ArrayList <String> topic2keywords = new ArrayList();
        ArrayList <String> topic3keywords = new ArrayList();
        int i = 0,j=0; //iterator
            
        System.out.println("You are entering the danger zone");
        System.out.println("The relationship between topic " +topic2+ " and topic " +topic3);
        
            System.out.println("--------");
            System.out.println("Topic "+secondtopic);
            for(String s : venn.getTopics().get(secondtopic).getTopic().getKeywords())    
            {
                topic2keywords.add(i, s);
                System.out.println("Keyword " +topic2keywords.get(i)); 
                i++;
            }
           
            System.out.println("--------");
            System.out.println("Topic "+thirdtopic);
            for(String s : venn.getTopics().get(thirdtopic).getTopic().getKeywords())    
            {
                topic3keywords.add(j, s);
                System.out.println("Keyword " +topic3keywords.get(j));   
                j++;
            }
            
            
            for(i = 0; i < topic2keywords.size(); i++)
            {
                for(int x =0; x < topic3keywords.size(); x++)
                {
                    if(topic2keywords.get(i).equals(topic3keywords.get(x)))
                    {
                        vennscore +=9;
//                      commonkeywords.add(i, topic2keywords.get(i));
                    }
                    else
                        System.out.println("Not common");
                }
            }
            
            System.out.println("The venn score for the two topics: topic " +topic2+ " and topic " +topic3+ " is " +vennscore);
//            if (!commonkeywords.isEmpty())
//            {
//                for (i = 0; i < commonkeywords.size(); i++)
//                    System.out.println("Common Keyword [" +i+ "]  " + commonkeywords.get(i) );
//            }
            return vennscore;
    }
       
    private static int getVennACScore(int topic1, int topic2, int topic3, int counter, TMDrillModel venn) //gets the relationship of two venn diagrams
    {
        int vennscore = 0;
        
        //minus 1 since index of arraylist begins at 0
     
        int firsttopic =topic1 -1;
        int secondtopic = topic2 -1;
        int thirdtopic = topic3 -1;
        //venn a and c are the inputs
        ArrayList <String> topic1keywords = new ArrayList();
        ArrayList <String> topic3keywords = new ArrayList(); 
        int i = 0,j=0; //iterator
            
        System.out.println("You are entering the danger zone");
        System.out.println("The relationship between topic " +topic1+ " and topic " +topic3);
            System.out.println("--------");
            System.out.println("Topic "+firsttopic);
            for(String s : venn.getTopics().get(firsttopic).getTopic().getKeywords())    
            {
                topic1keywords.add(i, s);
                System.out.println("Keyword " +topic1keywords.get(i)); 
                i++;
            }
           
            System.out.println("--------");
            System.out.println("Topic "+thirdtopic);
            for(String s : venn.getTopics().get(thirdtopic).getTopic().getKeywords())    
            {
                topic3keywords.add(j, s);
                System.out.println("Keyword " +topic3keywords.get(j));   
                j++;
            }
            
            
           for(i = 0; i < topic1keywords.size(); i++)
            {
                for(int x =0; x < topic3keywords.size(); x++)
                {
                    if(topic1keywords.get(i).equals(topic3keywords.get(x)))
                    {
                        vennscore +=9;
//                        commonkeyword = topic1keywords.get(i);
//                        commonkeywords.add(commonkeyword);
                    }
                    else
                        System.out.println("Not common");
                }
            }
            System.out.println("The venn score for the two topics: topic  "+topic1+ " and topic " +topic3+ " is " +vennscore);
//            if (!commonkeywords.isEmpty())
//            {
//                for (i = 0; i < commonkeywords.size(); i++)
//                    System.out.println("Common Keyword [" +i+ "]  " + commonkeywords.get(i) );
//            }
            return vennscore;
    }
    
    private static int getVennABScore(int topic1, int topic2, int topic3, int counter, TMDrillModel venn) //gets the relationship of two venn diagrams
    {
        int vennscore = 0;
        
        //minus 1 since index of arraylist begins at 0
        
        int firsttopic =topic1 -1;
        int secondtopic = topic2 -1;
        int thirdtopic = topic3 -1;
        //venn a and b are the inputs
        ArrayList <String> topic1keywords = new ArrayList();
        ArrayList <String> topic2keywords = new ArrayList();
        int i = 0,j=0; //iterator
            
        System.out.println("You are entering the danger zone");
        System.out.println("The relationship between topic " +topic1+ " and topic " +topic2);
            System.out.println("--------");
            System.out.println("Topic" +firsttopic);
            for(String s : venn.getTopics().get(firsttopic).getTopic().getKeywords())    
            {
                topic1keywords.add(i, s);
                System.out.println("Keyword " +topic1keywords.get(i)); 
                i++;
            }
           
            System.out.println("--------");
            System.out.println("Topic "+secondtopic);
            for(String s : venn.getTopics().get(secondtopic).getTopic().getKeywords())    
            {
                topic2keywords.add(j, s);
                System.out.println("Keyword " +topic2keywords.get(j));   
                j++;
            }
            
            
            for(i = 0; i < topic1keywords.size(); i++)
            {
                for(int x =0; x < topic2keywords.size(); x++)
                {
                    if(topic1keywords.get(i).equals(topic2keywords.get(x)))
                    {
//                        vennscore +=9;
//                        commonkeyword = topic1keywords.get(i);
//                        commonkeywords.add(commonkeyword);
                    }
                    else
                        System.out.println("Not common");
                }
            }
            
            System.out.println("The venn score for the two topics: topic" +topic1+ " and topic " +topic2+ " is " +vennscore);
//            System.out.println("+++++++++++++++++++++++");
//            System.out.println("The output of the common keywords arraylist");
//            
//            for(String s : commonkeywords)    
//            {
//                System.out.println("Keyword " +commonkeywords.get(j));   
//                j++;
//            }
            return vennscore;
    }
           
}   
    
//   


