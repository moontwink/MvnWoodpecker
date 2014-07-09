/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual.timeline;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.LMDrillModel;
import tfidf.Tfidf;


public class TimelineWriter{
    
    
    
    
public static String write (String filename, ArrayList<TimelineTopics> topics) throws IOException{
   //lmDM.getTopList();
  BufferedWriter outputWriter = null;
  String url = "src/visual/jsfiddle-timeline/timeline-"+filename;
  outputWriter = new BufferedWriter(new FileWriter(url));
  //String t;
  //double i;
  outputWriter.write("<!DOCTYPE html>\n" +
"<meta charset=\"utf-8\">\n" +
"<body>\n" +
"\n" +
"\n" +
"<script src =\"jquery-1.8.0.min.js\"></script>"+
"<script src=\"highcharts.js\"></script>\n" +
"<script src=\"exporting.js\"></script>\n" +
"\n" +
"<div id=\"container\" style=\"min-width: 310px; height: 430px; margin: 0 auto\"></div>\n" +
"\n" +
"\n" +
"<script>"+
"$(function () {\n" +
"        $('#container').highcharts({\n" +
"            chart: {\n" +
"                type: 'spline'\n" +
"            },\n" +
"            title: {\n" +
"                text: 'Tweet Timeline'\n" +
"            },\n" +
"            subtitle: {\n" +
"                text: 'Language/Topic Modeller'\n" +
"            },\n" +
"            xAxis: {\n" +
"                type: 'datetime',\n" +
"                dateTimeLabelFormats: { // don't display the dummy year\n" +
"                    month: '%e. %b',\n" +
"                    year: '%b'\n" +
"                },\n" +
"                title: {\n" +
"                    text: 'Date'\n" +
"                }\n" +
"            },\n" +
"            yAxis: {\n" +
"                title: {\n" +
"                    text: 'Frequency'\n" +
"                },\n" +
"                min: 0\n" +
"            },\n" +
"            tooltip: {\n" +
"                headerFormat: '<b>{series.name}</b><br>',\n" +
"                pointFormat: '{point.x:%e. %b}: {point.y}'\n" +
"            },\n" +
"\n" +
"            series: [");
        for(int i =0;i<topics.size();i++){
        outputWriter.write("{\n" +
"                name: '"+ topics.get(i).getTopics()+"', \n"+
                "data:[\n");
             for(int j = 0; j<topics.get(i).getDetails().size();j++){
                 outputWriter.write("[Date.UTC("+topics.get(i).getDetails().get(j).getDate()+"),"+topics.get(i).getDetails().get(j).getFrequency()+"], \n");
             }
        outputWriter.write("]\n" +
"            },\n");
        }
outputWriter.write("]"+
"        });\n" +
"    });\n" +
"    </script>"
        + "</body></html>");
  
  outputWriter.flush();  
  outputWriter.close(); 
  
  return url;
}




}