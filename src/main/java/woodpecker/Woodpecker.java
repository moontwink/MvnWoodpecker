
package woodpecker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.tweetModel;
import ngram.NGram;
import ngram.NGramDriver;
import static ngram.NGramDriver.NGramList;
import static ngram.NGramDriver.getNgrams;
import static ngram.NGramDriver.setNgrams;
import ngram.NGramExtractor;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import tfidf.LM_TfidfDriver;

/**
 *
 * @author Nancy
 */
public class Woodpecker {
    
    public static void main(String[] args) {
//        String tweet = "rt hello world";
//        if(tweet.startsWith("rt ")){
//            tweet = tweet.replaceFirst("rt ", "");
//            System.out.println(tweet);
//        }
        
        /*
        NGramExtractor extractor = new NGramExtractor();
        try {
            for(int ng=1;ng<5;ng++){
                extractor.extract("it's a whole new it's new it's #whole it's new year 2013 whole" , ng, false, true);
                setNgrams(extractor.getUniqueNGrams());
            
                
                for (int x = 0; x < getNgrams().size(); x++){
                    String s = getNgrams().get(x);
                    System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                    NGramList(s, extractor.getNGramFrequency(s));
                }
                System.out.println("");
	   }
            
            for(int ng=1;ng<5;ng++){
                extractor.extract("it's a whole new year 2014" , ng, false, true);
                setNgrams(extractor.getUniqueNGrams());
            
                
                for (int x = 0; x < getNgrams().size(); x++){
                    String s = getNgrams().get(x);
                    System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                    
                    NGramList(s, extractor.getNGramFrequency(s));
                }
                System.out.println("");
	   }
            
            for(int ng=1;ng<5;ng++){
                extractor.extract("it's a whole new year 2014" , ng, false, true);
                setNgrams(extractor.getUniqueNGrams());
            
                
                for (int x = 0; x < getNgrams().size(); x++){
                    String s = getNgrams().get(x);
                    System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                    
                    NGramList(s, extractor.getNGramFrequency(s));
                }
                System.out.println("");
	   }
            
            for(int i = 0; i < NGramDriver.getNgramlist().size(); i++){
                NGram ngram = NGramDriver.getNgramlist().get(i);
                System.out.println(ngram.getTweet() + " [" + ngram.getFrequency() + "]");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Woodpecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList<tweetModel> results = new ArrayList<>();
        tweetModel tm = new tweetModel();
        tm.setMessage("it's a whole it's new it's #whole it's new year 2013");
        results.add(tm);
        tm = new tweetModel();
        tm.setMessage("it's new year 2014");
        results.add(tm);
        tm = new tweetModel();
        tm.setMessage("it's new year 2014");
        results.add(tm);
        LM_TfidfDriver.idfchecker(results);
        */
    }
}
