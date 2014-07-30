
package ngram;

/**
 *
 * @author JOY
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NGramDriver{
   private static  LinkedList<String> ngrams=null;
   private static ArrayList<NGram> ngramlist= new ArrayList<>();
   private static final Pattern englishfunctionwords = Pattern.compile("\\b(?:"
        + "YOURSELVES|YOURSELF|YOURS|YOUR|YOU'VE|YOU'RE|YOU'LL|YOU'D|YOU|YET|YESTERDAY|YES|WOULDN'T|WOULD|WON'T|"
           + "WITHOUT|WITHIN|WITH|WILL|WHY|WHOSE|WHOM|WHO|WHITHER|WHILE|WHICH|WHEREAS|WHERE|WHENCE|WHEN|WHAT|WEREN'T|"
           + "WERE|WE'VE|WE'RE|WE'LL|WE'D|WE|WASN'T|WAS|VERY|US|UP|UNTIL|UNLESS|UNDERNEATH|UNDER|TOWARDS|TOO|TOMORROW|"
           + "TODAY|TO|TILL|THUS|THROUGH|THOUGH|THOSE|THITHER|THIS|THEY'RE|THEY'LL|THEY'D|THEY|THESE|THEREFORE|THERE|"
           + "THENCE|THEN|THEMSELVES|THEM|THEIRS|THEIR|THE|THAT'S|THAT|THAT|THAN|SUCH|STILL|SOON|SOMEWHERE|SOMETIMES|"
           + "SOMETHING|SOMEONE|SOMEBODY|SOME|SO|SINCE|SHOULDN'T|SHOULD|SHE'S|SHE'LL|SHE'D|SHE|SHAN'T|SHALL|ROUND|"
           + "RATHER|QUITE|OVER|OUT|OURSELVES|OURS|OUR|OUGHTN'T|OUGHT|OTHERS|OTHER|OR|ONLY|ON|OFTEN|OFF|OF|NOWHERE|NOW|"
           + "NOTHING|NOT|NOR|NOONE|NONE|NOBODY|NO|NEVER|NEITHER|NEARLY|NEARBY|NEAR|NEAR|MYSELF|MY|MUSTN'T|MUST|MUCH|"
           + "MOST|MORE|MINE|MIGHT|ME|MAY|MANY|LESS|ITSELF|ITS|IT'S|IT|ISN'T|IS|INTO|IN|IF|I'VE|I'M|I'LL|I'D|I|HOWEVER|"
           + "HOW|HITHER|HIS|HIMSELF|HIM|HERSELF|HERS|HERE|HER|HENCE|HE'S|HE'LL|HE'D|HE|HAVING|HAVEN'T|HAVE|HASN'T|HAS|"
           + "HADN'T|HAD|GOT|GETTING|GETS|GET|FROM|FOR|FEWER|FEW|FAR|EXCEPT|EVERYWHERE|EVERYTHING|EVERYONE|EVERYBODY|"
           + "EVERY|EVER|EVEN|ENOUGH|ELSE|EITHER|EACH|DURING|DOWN|DONE|DON'T|DOING|DOESN'T|DOES|DO|DIDN'T|DID|COULDN'T|"
           + "COULD|CAN'T|CAN|BY|BUT|BOTH|BEYOND|BETWEEN|BESIDE|BENEATH|BELOW|BEING|BEFORE|BEEN|BE|BACK|AT|AS|AROUND|"
           + "AREN'T|ARE|ANYWHERE|ANYTHING|ANYBODY|ANY|ANOTHER|AND|AN|AMONG|AM|ALWAYS|ALTHOUGH|ALSO|ALREADY|ALONG|ALMOST|"
           + "ALL|AGO|AGAIN|AFTER|ABOVE|ABOUT|A)\\b\\s*", Pattern.CASE_INSENSITIVE);
   
   private static final Pattern filipinofunctionwords = Pattern.compile("\\b(?:"
        + "yung|yun|yan|upang|ung|tungkol|tayo|sya|subalit|siya|sino|sila|si|sapagkat|san|samin|saka|saan|sa|pati|para|"
           + "pala|pag|pa|nyon|nyo|nya|niyon|niya|nito|ninyo|nila|ngunit|nga|nga|ng|natin|namin|na|mong|mo|mga|may|mang|"
           + "maging|mag|kung|ko|kina|kayo|kay|kasi|kapag|kaniya|kanila|kami|ka|iyon|iyo|iyan|ito|inyo|ikaw|hindi|heto|"
           + "hayun|hayan|ganyan|ganoon|ganon|ganiyan|ganito|eto|dito|din|di|daw|dahil|ayun|ayon|ayan|ay|ating|atin|at|"
           + "anong|ano|ang|aming|amin|aling|alin|akong|ako|aking|akin)\\b\\s*", Pattern.CASE_INSENSITIVE);
    
   /**
     * This method removes the outliers from the NGramlist.
     */
   public static void removeOutliers(){
        double trim = ngramlist.size() * 0.20;
//        System.out.print("********************trim**** " + trim);
        for(int top = 0; top < trim; top++){
            ngramlist.remove(top);
            ngramlist.remove(ngramlist.size()-1);
        }
   }
   
   /**
     * This method removes the function words from tweets
     * @param tweet
     * @return String, the string without function words
     */
   public static String cleanFunctionWordsFromTweet(String tweet){
           
        Matcher matcher = englishfunctionwords.matcher(tweet);
        String clean = matcher.replaceAll("_ ");
        tweet = clean;

        matcher = filipinofunctionwords.matcher(tweet);
        clean = matcher.replaceAll("# ");
        tweet = clean;
       
        return tweet;
   }
   
   /**
    * This method removes all NGrams in the NGramlist
    */
   public static void emptyNgram(){
       if(!ngramlist.isEmpty()){
           ngramlist.removeAll(ngramlist);
       }
   }
   
   /**
     * This method sends the tweet message to the tokenizer.
     * @param tweet
     */
    public static void NGramTweet(String tweet){
	NGramExtractor extractor = new NGramExtractor();
        
        try{
            for(int ng=1;ng<5;ng++){
	    extractor.extract(tweet , ng, false, true);
	    setNgrams(extractor.getUniqueNGrams());
            
            for (String s : getNgrams()){
//		System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                NGramList(s, extractor.getNGramFrequency(s));
            }
	   }
	}
	catch (Exception e){
	    System.err.println(e.toString());
	}
         
        //return ngrams;
    }
    
    /**
     * This method sends the tweet message to the tokenizer for Timeline.
     * @param tweet
     */
    public static void TimelineNGramTweet(String tweet){
	NGramExtractor extractor = new NGramExtractor();
        
        try{
            for(int ng=1;ng<2;ng++){
	    extractor.extract(tweet , 1, false, true);
	    setNgrams(extractor.getNGrams());
	   }
	}
	catch (Exception e){
	    System.err.println(e.toString());
	}
         for (String s : getNgrams()){
//		System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                NGramList(s, extractor.getNGramFrequency(s));
         }
        //return ngrams;
    }
    
    /**
     * This method adds Ngrams to the list and checks if it exists already.
     * @param tweet
     * @param frequency
     */
    public static void NGramList(String tweet, int frequency){
//        System.out.println(tweet + " [" + frequency + "]");
        if(CompareMessage(ngramlist,tweet, frequency)){
            NGram n= new NGram(tweet, frequency);
            getNgramlist().add(n);
        }
    }
    
    /**
     * This method compares the each NGram in the list
     * @param list
     * @param tweet
     * @param frequency
     * @return boolean
     */
     public static boolean CompareMessage(ArrayList<NGram> list,String tweet, int frequency){

        int fre = 0; 
   		for(int num = 0; num < list.size() ; num++){
                  if(tweet.compareTo( list.get(num).getTweet())==0){
                    fre = list.get(num).getFrequency() + frequency;
//                    System.out.println("!! Ngram exists.. updating frequency from " + list.get(num).getFrequency() + " >>> " + fre);
                    list.get(num).setFrequency(fre);
                    
                    int tweetcount = list.get(num).getTweetcount() + 1;
                    list.get(num).setTweetcount(tweetcount);
                    return false;
                  }
                       //  model.addRow(new Object[]{t,i});
   		}
                return true;
    }
    
        /**
        * Sorts Ngram list in DESCENDING order by frequency
        * @param list
        */
      public static void sortngramlist(ArrayList<NGram> list){
        
        //Collections.sort(list, Collections.reverseOrder());
           

        Collections.sort(list, new MyComparator());

//         System.out.println("Arraylist in descending order: " + list);
    

      }
      
       /**
        * Comparator used by sortngramlist to sort list
        */
      public static class MyComparator implements Comparator<NGram> {
   

        @Override
        public int compare(NGram o1, NGram o2) {
          
        try{
            if (o1.getFrequency() > o2.getFrequency()) {
        return -1;
    } else if (o1.getFrequency() < o2.getFrequency()) {
        return 1;
    }
        }catch(Exception e){
            System.err.println(e.toString());
        }
        return 0;
        }
    }
      
     
    /**
     * @return the ngrams
     */
    public static LinkedList<String> getNgrams() {
        return ngrams;
    }

    /**
     * @param aNgrams the ngrams to set
     */
    public static void setNgrams(LinkedList<String> aNgrams) {
        ngrams = aNgrams;
    }

    /**
     * @return the ngramlist
     */
    public static ArrayList<NGram> getNgramlist() {
        //sortngramlist(ngramlist);
        return ngramlist;
    }

    /**
     * @param aNgramlist the ngramlist to set
     */
    public static void setNgramlist(ArrayList<NGram> aNgramlist) {
        ngramlist = aNgramlist;
    }
    
    
    
}