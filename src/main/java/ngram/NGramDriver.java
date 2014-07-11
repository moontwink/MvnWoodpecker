
package ngram;

/**
 *
 * @author JOY
 */
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NGramDriver{
   private static  LinkedList<String> ngrams=null;
   private static ArrayList<NGram> ngramlist= new ArrayList<>();
   private static final Pattern englishfunctionwords = Pattern.compile("\\b(?:"
        + "a|about|above|after|again|ago|all|almost|along|"
        +"already|also|although|always|am|among|an|and|another|any|"
        +"anybody|anything|anywhere|are|aren't|around|as|at|back|be|"
        +"been|before|being|below|beneath|beside|between|beyond|both|but|"
        +"by|can|can't|could|couldn't|did|didn't|do|does|doesn't|"
        +"doing|don't|done|down|during|each|either|else|enough|even|"
        +"ever|every|everybody|everyone|everything|everywhere|except|far|few|fewer|"
        +"for|from|get|gets|getting|got|had|hadn't|has|hasn't|"
        +"have|haven't|having|he|he'd|he'll|he's|hence|her|here|"
        +"hers|herself|him|himself|his|hither|how|however|i|i'd|"
        +"i'll|i'm|i've|if|in|into|is|isn't|it|it's|"
        +"its|itself|less|many|may|me|might|mine|more|most|"
        +"much|must|mustn't|my|myself|near|near|nearby|nearly|neither|"
        +"never|no|nobody|none|noone|nor|not|nothing|now|nowhere|"
        +"of|off|often|on|only|or|other|others|ought|oughtn't|"
        +"our|ours|ourselves|out|over|quite|rather|round|shall|shan't|"
        +"she|she'd|she'll|she's|should|shouldn't|since|so|some|somebody|"
        +"someone|something|sometimes|somewhere|soon|still|such|than|that|that|"
        +"that's|the|their|theirs|them|themselves|then|thence|there|therefore|"
        +"these|they|they'd|they'll|they're|this|thither|those|though|through|"
        +"thus|till|to|today|tomorrow|too|towards|under|underneath|unless|"
        +"until|up|us|very|was|wasn't|we|we'd|we'll|we're|"
        +"we've|were|weren't|what|when|whence|where|whereas|which|while|"
        +"whither|who|whom|whose|why|will|with|within|without|won't|"
        +"would|wouldn't|yes|yesterday|yet|you|you'd|you'll|you're|you've|"
        +"your|yours|yourself|yourselves|s|t|re|ll|d|ve)\\b\\s*", Pattern.CASE_INSENSITIVE);
   
   private static final Pattern filipinofunctionwords = Pattern.compile("\\b(?:"
        +"akin|aking|ako|akong|alin|aling|amin|aming|ang|"
        +"ano|anong|at|atin|ating|ay|ayan|ayon|ayun|dahil|"
        +"daw|di|din|dito|eto|ganito|ganiyan|ganon|ganoon|ganyan|"
        +"hayan|hayun|heto|hindi|ikaw|inyo|ito|iyan|iyo|iyon|"
        +"ka|kami|kanila|kaniya|kapag|kasi|kay|kayo|kina|ko|"
        +"kung|mag|maging|mang|may|mga|mo|mong|na|namin|"
        +"natin|ng|nga|nga|ngunit|nila|ninyo|nito|niya|niyon|"
        +"nya|nyo|nyon|pa|pag|pala|para|pati|sa|saan|"
        +"saka|samin|san|sapagkat|si|sila|sino|siya|subalit|sya|"
        +"tayo|tungkol|ung|upang|yan|yun|yung|eh|ehh|lang)\\b\\s*", Pattern.CASE_INSENSITIVE);
    
   /*
    * 
   public static void cleanFunctionWords(){
       for(int n = 0; n < ngramlist.size(); n++){
           NGram currentNgram = ngramlist.get(n);
           String ngramtweet = currentNgram.getTweet();
           System.out.println(ngramtweet +" *****************");
           
           Matcher matcher = englishfunctionwords.matcher(ngramtweet);
           String clean = matcher.replaceAll("");
           
           ngramtweet = clean;
           
           matcher = filipinofunctionwords.matcher(ngramtweet);
           clean = matcher.replaceAll("");
           
           ngramlist.get(n).setTweet(clean);
           System.out.println("***************** "+ngramlist.get(n).getTweet());
       }
       System.out.println("CLEANED FUNCTION WORDS******************8");
   }
   * 
   */
   
   public static void removeOutliers(){
        double trim = ngramlist.size() * 0.20;
        System.out.print("********************trim**** " + trim);
        for(int top = 0; top < trim; top++){
            ngramlist.remove(ngramlist.get(top));
            ngramlist.remove(ngramlist.get(ngramlist.size()-1-top));
        }
   }
   
   public static String cleanFunctionWordsFromTweet(String tweet){
           
        Matcher matcher = englishfunctionwords.matcher(tweet);
        String clean = matcher.replaceAll("");
        tweet = clean;

        matcher = filipinofunctionwords.matcher(tweet);
        clean = matcher.replaceAll("");
        tweet = clean;
       
        return tweet;
   }
   
   public static void emptyNgram(){
       if(!ngramlist.isEmpty()){
           ngramlist.removeAll(ngramlist);
       }
   }
   
    public static void NGramTweet(String tweet){
	NGramExtractor extractor = new NGramExtractor();
        
        try{
            for(int ng=1;ng<5;ng++){
	    extractor.extract(tweet , ng, false, true);
	    setNgrams(extractor.getNGrams());
	   }
	}
	catch (Exception e){
	    System.err.println(e.toString());
	}
         for (String s : getNgrams()){
		System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                NGramList(s, extractor.getNGramFrequency(s));
         }
        //return ngrams;
    }
    
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
		System.out.println("Ngram '" + s + "' occurs " + extractor.getNGramFrequency(s) + " times");
                NGramList(s, extractor.getNGramFrequency(s));
         }
        //return ngrams;
    }
    
    public static void NGramList(String tweet, int frequency){
        System.out.println(tweet + " [" + frequency + "]");
        if(CompareMessage(ngramlist,tweet, frequency)){
        NGram n= new NGram(tweet, frequency);
        getNgramlist().add(n);
        }
    }
    
     public static boolean CompareMessage(ArrayList<NGram> list,String tweet, int frequency){
//        System.out.println(tweet + frequency);
        
        int fre = 0; 
   		for(int num = 0; num < list.size() ; num++){
                  if(tweet.compareTo( list.get(num).getTweet())==0){
                    fre=list.get(num).getFrequency();
                    fre++;
                    list.get(num).setFrequency(fre);
                    return false;
                  }
                       //  model.addRow(new Object[]{t,i});
   		}
                return true;
    }
    
      public static void sortngramlist(ArrayList<NGram> list){
        
        //Collections.sort(list, Collections.reverseOrder());
           

        Collections.sort(list, new MyComparator());

//         System.out.println("Arraylist in descending order: " + list);
    

      }
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