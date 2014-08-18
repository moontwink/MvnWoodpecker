
package Visual.venn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import model.TMDrillModel;

/**
 *
 * @author mathewmichael
 */
public class VennDiagramWriter {
    private static TMDrillModel VennContents;
    
/**
 * This method writes the html file for the venn diagram. 
 * @param filename
 * @param tmDM
 * @return String returns the URL of the file
 * @throws IOException
 */
  public static String write (String filename, TMDrillModel tmDM) throws IOException
  {
  BufferedWriter outputWriter = null;
//  TopicModel tm = new TopicModel();
  String url = "src/visual/d3-venndiagram/venn-"+filename;
  outputWriter = new BufferedWriter(new FileWriter(url));
  Map<String, VennScoreModel> finalVennScores = new ConcurrentHashMap<>();
  int vennscore = 0;
  
   ArrayList<VennTopicModel> vennuniquetopics = new ArrayList<>();
    for(int i = 0 ; i < tmDM.getTopics().size(); i++) //for all topics
    {			
//        System.out.println("Hello!");
        
        VennTopicModel vtm = new VennTopicModel(i, tmDM.getTopics().get(i).getTopic().getKeywords());
        vennscore = 0;
        
	for(int x = i+1 ; x < tmDM.getTopics().size(); x++)
        {	//iterator for x topic
//                System.out.println("Hello value of i " +i+ " value of x " +x);
                
                VennScoreModel vennmodel = new VennScoreModel();
//		for(String firstkey : tmDM.getTopics().get(i).getTopic().getKeywords())
                for(int f = 0; f < tmDM.getTopics().get(i).getTopic().getKeywords().size(); f++)
                {	//iterator for i topic's keywords
                        String firstkey = tmDM.getTopics().get(i).getTopic().getKeywords().get(f);
			if(tmDM.getTopics().get(x).getTopic().getKeywords().contains(firstkey))
                        {   //if topic x contains firstkey
                            vennscore += 1;
                            vennmodel.addSimilarWord(firstkey);
                            
                            //causing concurrentmodificationexception
//                            System.out.println(".."+vtm.findKeywordIndex(firstkey));
                            vtm.deleteKeyword(vtm.findKeywordIndex(firstkey));
                            
//                            System.out.println("Current word is " +firstkey+ "compared to word in " +x+ " which is " +tmDM.getTopics().get(x).getTopic().getKeywords());
			}
		}
//              
                vennmodel.setVennscore(vennscore);
		finalVennScores.put(""+i+""+x, vennmodel); //put into hashmap with values like ["01", 3] wherein 01 equivalent to topic 1 and topic 2
//                System.out.println(" " +vennscore);
                
                vennscore = 0;
        }
        vennuniquetopics.add(vtm);
    }
    Set keys = finalVennScores.keySet();
    
  outputWriter.write("<!doctype html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"    <meta charset=\"utf-8\">\n" +
"    <title>Venn Diagram</title>\n" +
"</head>\n" +
"\n" +
"<body>\n" +
"    <div class=\"venn\" style=\"margin-left: 50px\"></div>\n" +
"</body>\n" +
"\n" +
"<style>\n" +
".venntooltip {\n" +
"  position: absolute;\n" +
"  text-align: center;\n" +
"  font-family:\"Trebuchet MS\",Georgia,Serif;\n" +
"  font-weight:bold;" +
"  width: 128px;\n" +
"  height: 18px;\n" +
"  color: #000000;\n" +
"  border: 0px;\n" +
"  border-radius: 8px;\n" +
"  opacity: 0;\n" +
"}\n" +
"</style>\n" +
"\n" +
"<script src=\"d3.js\"></script>\n" +
"<script src=\"venn.js\"></script>\n" +
"<script>"
          + "var sets = [");
  
  for(VennTopicModel vtm : vennuniquetopics){
//        System.out.println("~~~~ UNIQUE TOPICS FOR " + (vtm.getTopicnumber()+1));
        outputWriter.write("{\"label\" : \"Topic "+(vtm.getTopicnumber()+1)+"\" , \"size\" : 10, \"value\" : \"");
        for(String t : vtm.getUniquewords()){
//            System.out.println("----- "+t);
            outputWriter.write(t+" ");
        }
        outputWriter.write("\"},\n");
    }
  outputWriter.write("],"
          + "overlaps  = [ ");


  for(Iterator it = keys.iterator(); it.hasNext();) 
    {
	String key = (String) it.next();
        VennScoreModel vennScoreModel = finalVennScores.get(key);
//	int vennvalue = finalVennScores.get(key);
//	System.out.println("KEY: "+ key );
//	System.out.println("SCORE: "+ vennScoreModel.getVennscore() );
//        System.out.println("SIMILAR KEYWORDS: ");
        outputWriter.write("{\"sets\" : ["+key.charAt(0)+","+key.charAt(1)+"], \"size\": "+vennScoreModel.getVennscore()+", \"value\" : \"");
            for(String s : vennScoreModel.getSimilarwords()){
//                System.out.println("\t == " + s);
                outputWriter.write(s+"\\n");
            }
        outputWriter.write("\"},\n");
    }
        

outputWriter.write("];"
        + "sets = venn.venn(sets, overlaps);\n" +
"\n" +
"// draw the diagram in the 'venn' div\n" +
"var diagram = venn.drawD3Diagram(d3.select(\".venn\"), sets, 600, 600);\n" +
"\n" +
"// add a tooltip showing the size of each set/intersection\n" +
"var tooltip = d3.select(\"body\").append(\"div\")\n" +
"    .attr(\"class\", \"venntooltip\");\n" +
"\n" +
"d3.selection.prototype.moveParentToFront = function() {\n" +
"  return this.each(function(){\n" +
"    this.parentNode.parentNode.appendChild(this.parentNode);\n" +
"  });\n" +
"};\n" +
"\n" +
"// hover on all the circles\n" +
"diagram.circles\n" +
"    .style(\"stroke-opacity\", 0)\n" +
"    .style(\"stroke\", \"gray\")\n" +
"    .style(\"stroke-width\", \"2\")\n" +
"    .on(\"mousemove\", function() {\n" +
"        tooltip.style(\"left\", (d3.event.pageX) + \"px\")\n" +
"               .style(\"top\", (d3.event.pageY - 28) + \"px\");\n" +
"    })\n" +
"    .on(\"mouseover\", function(d, i) {\n" +
"        var selection = d3.select(this);\n" +
"        d3.select(this).moveParentToFront()\n" +
"            .transition()\n" +
"            .style(\"fill-opacity\", .5)\n" +
"            .style(\"stroke-opacity\", 1);\n" +
"\n" +
"        tooltip.transition().style(\"opacity\", .9);\n" +
"        tooltip.text(d.value);\n" +
"    })\n" +
"    .on(\"mouseout\", function(d, i) {\n" +
"        d3.select(this).transition()\n" +
"            .style(\"fill-opacity\", .3)\n" +
"            .style(\"stroke-opacity\", 0);\n" +
"        tooltip.transition().style(\"opacity\", 0);\n" +
"    });\n" +
"\n" +
"// draw a path around each intersection area, add hover there as well\n" +
"diagram.svg.select(\"g\").selectAll(\"path\")\n" +
"    .data(overlaps)\n" +
"    .enter()\n" +
"    .append(\"path\")\n" +
"    .attr(\"d\", function(d) { \n" +
"        return venn.intersectionAreaPath(d.sets.map(function(j) { return sets[j]; })); \n" +
"    })\n" +
"    .style(\"fill-opacity\",\"0\")\n" +
"    .style(\"fill\", \"black\")\n" +
"    .style(\"stroke-opacity\", 0)\n" +
"    .style(\"stroke\", \"white\")\n" +
"    .style(\"stroke-width\", \"2\")\n" +
"    .on(\"mouseover\", function(d, i) {\n" +
"        d3.select(this).transition()\n" +
"            .style(\"fill-opacity\", .1)\n" +
"            .style(\"stroke-opacity\", 1);\n" +
"        tooltip.transition().style(\"opacity\", .9);\n" +
"        tooltip.text(d.value);\n" +
"    })\n" +
"    .on(\"mouseout\", function(d, i) {\n" +
"        d3.select(this).transition()\n" +
"            .style(\"fill-opacity\", 0)\n" +
"            .style(\"stroke-opacity\", 0);\n" +
"        tooltip.transition().style(\"opacity\", 0);\n" +
"    })\n" +
"    .on(\"mousemove\", function() {\n" +
"        tooltip.style(\"left\", (d3.event.pageX) + \"px\")\n" +
"               .style(\"top\", (d3.event.pageY - 28) + \"px\");\n" +
"    })\n" +
"</script>\n" +
"</html>");

      outputWriter.flush();  
      outputWriter.close();  
      
      return url;
    }
  }
