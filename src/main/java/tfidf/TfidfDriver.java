/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

/**
 *
 * @author Matt
 */

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.lang.Math;
import model.tweetModel;
import ngram.NGramDriver;

public class TfidfDriver {
    private static ArrayList<ngram.NGram> ngramlist; //contains the list of the ngrams and the frequency counts
    private static ArrayList <Tfidf> toplist; // contains the list of the top ngrams given tf-idf scores
    
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
                System.out.println("\t\t___tfscore___ "+ngramlist.get(ngramindex).getFrequency()*java.lang.Math.log10(tweetListCount/count));

            Tfidf newtf = new Tfidf(tweet, tfscore);
            getToplist().add(newtf);
        }
    }
    
    private static void printTopList(){
        sorttoplist(getToplist());
        
        for(Tfidf tf : getToplist()){
            System.out.println("\t\t\t[[" + tf.getTweet() +"]] == " + tf.getScore());
        }
    }
    
    public static void sorttoplist(ArrayList<Tfidf> list){

        Collections.sort(list, new MyComparator());

    }

    /**
     * @return the toplist
     */
    public static ArrayList <Tfidf> getToplist() {
        return toplist;
    }
    
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
