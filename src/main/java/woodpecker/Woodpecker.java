
package woodpecker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
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
import tfidf.TfidfDriver;

/**
 *
 * @author Nancy
 */
public class Woodpecker {
    
    public static void main(String[] args) {
//        Reader reader = new StringReader("it's is a test string");
//        TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_36, reader);
//        tokenizer = new ShingleFilter(tokenizer, 2, 3);
//        CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
//        try {
//            while (tokenizer.incrementToken()) {
//                String token = charTermAttribute.toString();
//                System.out.println(token);
//                //Do something
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Woodpecker.class.getName()).log(Level.SEVERE, null, ex);
//        }
        NGramExtractor extractor = new NGramExtractor();
        try {
            for(int ng=1;ng<5;ng++){
	    extractor.extract("it's a whole it's new it's #whole it's new year 2013" , ng, false, true);
	    setNgrams(extractor.getNGrams());
            
            for (String s : getNgrams()){
		System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                NGramList(s, extractor.getNGramFrequency(s));
            }
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
        TfidfDriver.idfchecker(results);
    }
}
