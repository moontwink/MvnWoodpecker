
package Visual;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import tfidf.LM_TfidfModel;


public class WordCloudWriter{
    
    
    
/**
* This method writes the html file for the word clouds
* @param filename
* @param lmTopList
* @return url
* @throws IOException
*/ 
public static String write (String filename, ArrayList<LM_TfidfModel> lmTopList) throws IOException{
   //lmDM.getTopList();
  BufferedWriter outputWriter = null;
  String url = "src/visual/d3-wordcloud/wordcloud-"+filename;
  outputWriter = new BufferedWriter(new FileWriter(url));
  //String t;
  //double i;
  outputWriter.write("<!DOCTYPE html>\n" +
    "<meta charset=\"utf-8\">\n" +
    "<body>\n" +
    "    <script src=\"d3.js\"></script>\n" +
    "    <script src=\"d3.layout.cloud.js\"></script>\n" +
    "    <script>\n" +
    "	var fill = d3.scale.category20();\n" +
    "        \n" +
    "		var tarr = [");
                for(int num = 0; num < lmTopList.size(); num++){
                  // t= lmTopList.getTopList().get(num).getTweet();
                    String newString = lmTopList.get(num).getTweet();
                    newString = newString.replaceAll(" _ ", "");
                    newString = newString.replaceAll("_", "");
                    newString = newString.replaceAll(" # ", "");
                    newString = newString.replaceAll("#", "");
                   outputWriter.write("\""+newString+"\",");
                  // i= lmTopList.getTopList().get(num).getScore();
                 }
         outputWriter.write("];\n"+
    "		var freq = [");

        for (int i = 0; i < lmTopList.size(); i++) {
        // Maybe:
      //  outputWriter.write(frequency.toString(x));
        outputWriter.write("\""+lmTopList.get(i).getScore()+"\",");

      }
        outputWriter.write("];\n" +
    "\n" +
    " var width = 720;\n" +
    "var height = 1000;\n" +
    "\n" +
    "  for( var i = 0, len = freq.length; i < len; i++ ) {\n" +
    "    freq[i] = parseFloat( freq[i] );\n" +
    "}\n" +
    "d3.layout.cloud()\n" +
    "    .size([width, height])\n" +
    "    .words(d3.zip(tarr, freq).map(function(d) {\n" +
    "			return {text: d[0] , size: d[1]+ Math.random(1)* 30};\n" +
    "		}))\n" +
    "    .padding(5)\n" +
    "    .rotate(function () {\n" +
    "    return~~ (Math.random() * 2) * 90;\n" +
    "})\n" +
    "    .font(\"Impact\")\n" +
    "    .fontSize(function (d) {\n" +
    "    return d.size;\n" +
    "})\n" +
    "    .on(\"end\", draw)\n" +
    "    .start();\n" +
    "\n" +
    "function draw(words) {\n" +
    "    d3.select(\"body\")\n" +
    "        .append(\"svg\")\n" +
    "        .attr(\"width\", width)\n" +
    "        .attr(\"height\", height)\n" +
    "        .append(\"g\")\n" +
    "        .attr(\"transform\", \"translate(\" + width / 2 + \",\" + height / 2 + \")\")\n" +
    "        .selectAll(\"text\")\n" +
    "        .data(words)\n" +
    "        .enter()\n" +
    "        .append(\"text\")\n" +
    "        .style(\"font-size\", function (d) {\n" +
    "        return d.size + \"px\"; \n" +
    "    })\n" +
    "        .style(\"font-family\", \"Impact\")\n" +
    "        .style(\"fill\", function (d, i) {\n" +
    "        return fill(i);\n" +
    "    })\n" +
    "        .attr(\"text-anchor\", \"middle\")\n" +
    "        .attr(\"transform\", function (d) {\n" +
    "        return \"translate(\" + [d.x, d.y] + \")rotate(\" + d.rotate + \")\";\n" +
    "    })\n" +
    "        .text(function (d) {\n" +
    "        return d.text;\n" +
    "    });\n" +
    "\n" +
    "        };\n" +
    "\n" +
    "    </script>\n" +
    "\n" +
    "\n" +
    "</body>"
                + "\n</html>");

      outputWriter.flush();  
      outputWriter.close();  
      
      return url;
    }
    
}