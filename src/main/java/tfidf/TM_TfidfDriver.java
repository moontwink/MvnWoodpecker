
package tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import mallet.TopicOutput;
import model.tweetModel;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Nancy
 */
public class TM_TfidfDriver {
    private static ArrayList<TM_TfidfModel> topTopics;
    
    public static void idfChecker(ArrayList<tweetModel> tweetList, ArrayList<TopicOutput> allTopics)
    {
        int count = 0;
        topTopics = new ArrayList<>();
        
        for(int i = 0; i < allTopics.size(); i++){
            for(String s : allTopics.get(i).getKeywords()){
                for(tweetModel tm : tweetList){
                    if(tm.getMessage().contains(s)){
                        System.out.println("___############## " + tm.getMessage());
                        count++;
                        System.out.println("__>>[" + i + "]>>> " + count + "\n\t[" + s +"]" +
                            "\n\t " + tm.getMessage());
                    }
                }
            }
            TopicOutput currentTopic = allTopics.get(i);
            int keywordFreq = getTopicFrequency(tweetList, currentTopic);
            tfidfscore(currentTopic, keywordFreq, count, tweetList.size());
            count = 0;
        }
        sortTopicsList(topTopics);
        printTopList();
    }
    
    @SuppressWarnings("empty-statement")
    private static void tfidfscore(TopicOutput topic, int keyFreq, int count, int tweetListCount)
    {
        if(topic.getKeywords().isEmpty());
        else {
            if(count == 0) count = 1;

            int numOfKeywords = topic.getKeywords().size();
            int numeratorTweetList = tweetListCount*numOfKeywords;
            double tfidfscore = Math.sqrt(keyFreq)*java.lang.Math.log10(numeratorTweetList/count);
                System.out.println("\t\t[["+topic.getTopicnum()+"]] has "+count);
                System.out.println("\t\t_frequency_ "+keyFreq);
                System.out.println("\t\t_tweetlistcount_ "+numeratorTweetList);
                System.out.println("\t\t___tfscore___ "+tfidfscore);

            TM_TfidfModel newtf = new TM_TfidfModel(topic, tfidfscore);
            getTopTopics().add(newtf);
        }
    }
    
//    private static double topic_tfidfscore(ArrayList<TopicOutput> allTopics, TopicOutput topic){
//        if(topic.getKeywords().isEmpty());
//        else {
//            if(count == 0) count = 1;
//        }
//    }
//    
//    private static int getTopicKeywordsCount(ArrayList<TopicOutput> allTopics, TopicOutput topic) {
//        int count = 0;
//        
//        
//        for(String s : topic.getKeywords()){
//            for(TopicOutput t : allTopics){
//                if(t.getKeywords().contains(s)){
//                    count++;
//                }
//            }
//        }
//        
//        return count;
//    }
//    
//    private static int getTopicKeywordsFrequency(ArrayList<TopicOutput> allTopics, TopicOutput currentTopic) {
//        int freq = 0;
//        double tfidf = 0;
//        
//        for(TopicOutput t : allTopics){                         //for every topic T
//            for(String r : t.getKeywords()){                    //for every keyword R of T
//                for(String s : currentTopic.getKeywords()){     //for every keyword S of current topic C
//                    freq += StringUtils.countMatches(r, s);     //how many times R appeared in C
//                }
//            }
////            tfidf += topic_tfidfscore(allTopics, currentTopic);
//        }
//        return freq;
//    }
    
    private static int getTopicFrequency(ArrayList<tweetModel> tweetList, TopicOutput topic) {
        int freq = 0;
        
        for(String s : topic.getKeywords()){
            for(tweetModel t : tweetList){
                freq += StringUtils.countMatches(t.getMessage(), s);
            }
        }
        return freq;
    }
    
    
    
    private static void printTopList()
    {
        for(TM_TfidfModel tf : getTopTopics()) {
            System.out.print("\t\t\t[[");
            for(String s : tf.getTopic().getKeywords()){
                 System.out.print(s + " | ");
            }
            System.out.print("]] == " + tf.getScore() + "\n");
        }
    }

    /**
     * @return the topTopics
     */
    public static ArrayList<TM_TfidfModel> getTopTopics() {
        return topTopics;
    }
    
    public static void sortTopicsList(ArrayList<TM_TfidfModel> list){
        Collections.sort(list, new MyComparator());
    }
    
      public static class MyComparator implements Comparator<TM_TfidfModel> {
   
        @Override
        public int compare(TM_TfidfModel o1, TM_TfidfModel o2) {
          
        try{
            if (o1.getScore() > o2.getScore()) {
                return -1;
            } else if (o1.getScore()< o2.getScore()) {
                return 1;
        }
        }catch(Exception e){
            System.err.println(e.toString());
        }
        return 0;
        }
    }
}
