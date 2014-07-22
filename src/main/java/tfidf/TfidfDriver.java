
package tfidf;

/**
 *
 * @author Matt
 */

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import model.tweetModel;
import ngram.NGramDriver;

public class TfidfDriver {
    private static ArrayList<ngram.NGram> ngramlist; //contains the list of the ngrams and the frequency counts
    private static ArrayList <Tfidf> toplist; // contains the list of the top ngrams given tf-idf scores
    
    /**
     * This method computes the IDF value of all ngrams.
     * @param newList
     */
    public static void idfchecker(ArrayList<tweetModel> newList)//gets the idf element by checkin the ngram results against the filtered corpus
    {
        int count=0;
        ngramlist = ngram.NGramDriver.getNgramlist();    //list of ngrams
            System.out.println("*****>>> " + ngramlist + "\n\t " + newList.size());
        toplist = new ArrayList<>();
        String tweet = "";
        
        for (int i = 0; i< ngramlist.size(); i++)
        {
            for(int j=0; j < newList.size(); j++)
            {
                tweet = newList.get(j).getMessage().replaceAll("[^a-zA-Z0-9]", " ");
                tweet = tweet.replaceAll("\\s+", " ");
//                    System.out.println("$$$ " + tweet);
                    
                if(tweet.contains(ngramlist.get(i).getTweet()))
                {
                        System.out.println("%%%%%%%%%%%%% " + tweet);
                    count++;
                        System.out.println("_______>>> " + count + "\n\t[" + ngramlist.get(i).getTweet() +"]" +
                            "\n\t " + newList.get(j).getMessage());
                }
            }
            tfidfscore(i, count,newList.size());
            count = 0;
        }
        printTopList();
    }
    
    @SuppressWarnings("empty-statement")
    /**
     * This method gets the TF-IDF score of the ngram.
     * @param ngramindex 
     * @param count
     * @param tweetListCount
     */
    public static void tfidfscore(int ngramindex, int count, int tweetListCount) //compute for the tf-idf scores
    {
//        tf * log(idf)
        ngramlist = ngram.NGramDriver.getNgramlist();    //list of ngrams
        String tweet = NGramDriver.cleanFunctionWordsFromTweet(ngramlist.get(ngramindex).getTweet());
        
        if(tweet.length()==0);
        else{
            double tfscore = 0;
            if(count == 0) count = 1;
        
            //System.out.println("\t\t___tweetlistcount______ "+tweetListCount);

            tfscore = ngramlist.get(ngramindex).getFrequency()*java.lang.Math.log10(tweetListCount/count);
                System.out.println("\t\t[["+ngramlist.get(ngramindex).getTweet()+"]] has "+count);
                System.out.println("\t\t_frequency_ "+ngramlist.get(ngramindex).getFrequency());
                System.out.println("\t\t_tweetlistcount_ "+tweetListCount);
                System.out.println("\t\t___tfscore___ "+tfscore);

            Tfidf newtf = new Tfidf(tweet, tfscore);
            getToplist().add(newtf);
        }
    }
    
    /**
     * Prints the list of ngrams in order
     */
    private static void printTopList(){
        sorttoplist(getToplist());
        
        for(Tfidf tf : getToplist()){
            System.out.println("\t\t\t[[" + tf.getTweet() +"]] == " + tf.getScore());
        }
    }
    
    /**
     * Sorts the list of ngrams in DESCENDING order by TF-IDF score
     * @param list
     */
    public static void sorttoplist(ArrayList<Tfidf> list){

        Collections.sort(list, new MyComparator());

    }

    /**
     * @return the toplist
     */
    public static ArrayList <Tfidf> getToplist() {
        return toplist;
    }
    
    /**
     * Comparator used by sorttoplist to sort
     */
    public static class MyComparator implements Comparator<Tfidf> {
   
        @Override
        public int compare(Tfidf o1, Tfidf o2) {
          
            try{
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
            }catch(Exception e){
                System.err.println(e.toString());
        }
            return 0;
        }
    }
}
