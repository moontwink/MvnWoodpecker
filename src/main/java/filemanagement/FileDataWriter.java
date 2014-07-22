
package filemanagement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfidf.TM_TfidfModel;
import tfidf.Tfidf;

/**
 *
 * @author Nancy
 */
public class fileDataWriter {
    private static String topicsUrl = "src/data/topicmodeller/";
    private static String ngramsUrl = "src/data/languagemodeller/";
    
    public static String writeTMTopics(String tablename, ArrayList<TM_TfidfModel> topics){
        BufferedWriter outputWriter = null;
        String url = topicsUrl + "tm-"+tablename+".txt";
        try {
            outputWriter = new BufferedWriter(new FileWriter(url));
            
            for(int t = 0; t < topics.size(); t++) {
                outputWriter.write("TOPIC " + (t+1) + "\n");
                for(String s : topics.get(t).getTopic().getKeywords()){
                    outputWriter.write("\t" + s + "\n"); 
                }
            }
            
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(fileDataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }
    
    public static String writeLMToplist(String tablename, ArrayList<Tfidf> toplist) {
        BufferedWriter outputWriter = null;
        String url = ngramsUrl + "lm-"+tablename+".txt";
        try {
            outputWriter = new BufferedWriter(new FileWriter(url));
            
            for(Tfidf entry : toplist) {
                outputWriter.write(entry.getTweet()+"\n");
            }
            
            outputWriter.flush();
            outputWriter.close();
        } catch(IOException ex) {
            Logger.getLogger(fileDataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }
}
