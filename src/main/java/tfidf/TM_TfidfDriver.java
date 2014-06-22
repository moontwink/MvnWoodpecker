
package tfidf;

import java.util.ArrayList;
import mallet.TopicOutput;
import model.tweetModel;

/**
 *
 * @author Nancy
 */
public class TM_TfidfDriver {
//    private static ArrayList<TopicOutput> allTopics;
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
            tfidfscore(allTopics.get(i), count, tweetList.size());
            count = 0;
        }
        printTopList();
    }
    
    private static void tfidfscore(TopicOutput topic, int count, int tweetListCount)
    {
        if(topic.getKeywords().isEmpty());
        else {
            if(count == 0) count = 1;

            double tfidfscore = count*java.lang.Math.log10(tweetListCount/count);
                System.out.println("\t\t[["+topic.getTopicnum()+"]] has "+count);
                System.out.println("\t\t_frequency_ "+count);
                System.out.println("\t\t___tfscore___ "+tfidfscore);

            TM_TfidfModel newtf = new TM_TfidfModel(topic, tfidfscore);
            topTopics.add(newtf);
        }
    }
    
    public static void printTopList()
    {
        for(TM_TfidfModel tf : topTopics) {
            System.out.print("\t\t\t[[");
            for(String s : tf.getTopic().getKeywords()){
                 System.out.print(s + " ");
            }
            System.out.print("]] == " + tf.getScore() + "\n");
        }
    }
}
