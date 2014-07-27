
package Visual.timeline;

import database.CalendarHandler;
import static database.tweetHandler.cleanTweet;
import static database.tweetHandler.getAllTweets;
import java.io.IOException;
import java.util.ArrayList;
import mallet.TopicOutput;
import model.tweetModel;
import ngram.NGram;
import ngram.NGramDriver;
import static ngram.NGramDriver.sortngramlist;

/**
 *
 * @author JOY
 */
public class TM_Timeline {
    
    /**
     * Generates the data for Timeline
     * @param filename
     * @param tmDM
     * @return url
     * @throws IOException
     */
    public static String timelineTopics(String filename, ArrayList<TopicOutput> tmDM) throws IOException{
         String[] keywords= splitTableName(filename);
         ArrayList<tweetModel> tweets =  getAllTweets(filename);
         ArrayList<TimelineTopics> topics = new ArrayList<TimelineTopics>();
      
         if(keywords!=null)
        for(int i =0; i<keywords.length;i++){
            System.out.println("keywords: "+ keywords[i]);
        }
         
        
       if(keywords==null){
           withoutKeywords(tweets, topics, tmDM);
       }else{
           withKeywords(keywords, tweets, topics);
       } 
       String url = TimelineWriter.write(filename+".html", topics);
       return url;
    }
    
    /**
     * Checks if the user doesn't have keywords input and fixes the data for timeline.
     * @param tweets
     * @param topics
     * @param tmDM
     */
     public static void withoutKeywords(ArrayList<tweetModel> tweets,ArrayList<TimelineTopics> topics, ArrayList<TopicOutput> tmDM){
         NGramDriver grammy = new NGramDriver();
         NGramDriver.setNgramlist(new ArrayList<NGram>());
          ArrayList<NGram> keywords = new ArrayList<NGram>();          
          ArrayList<NGram> keywords2 = new ArrayList<NGram>();
         for(int j=0;j<tmDM.size();j++){
         for(int i=0;i<tmDM.get(j).getKeywords().size();i++){
             System.out.println("lmdm ___ ->"+tmDM.get(j).getKeywords().get(i));
            grammy.TimelineNGramTweet(cleanTweet(tmDM.get(j).getKeywords().get(i)));
        }
         }
           sortngramlist(ngram.NGramDriver.getNgramlist());
           //removeOutliers();
           keywords= ngram.NGramDriver.getNgramlist();
        
           if (keywords.size()>10){
               keywords2= keywords;
               keywords = new ArrayList<>();  
                for(int i= 0;i<10;i++){
                keywords.add(i, new NGram(keywords2.get(i).getTweet(),keywords2.get(i).getFrequency()));
                }
           }
         for(int i= 0;i<keywords.size();i++){
             String key = keywords.get(i).getTweet();
             System.out.println("topic key: "+key);
         
             TimelineTopics ttops = new TimelineTopics(key,new ArrayList<KeywordOccurence>());
           topics.add(ttops);
                   
         }
         
         
            for(int i= 0;i<tweets.size();i++){
               
                     for(int j = 0;j<keywords.size();j++){
                String key = keywords.get(j).getTweet();
             if(tweets.get(i).getMessage().matches("(.*)"+key+"(.*)")){
                 int test = (topics.get(j).getDetails().size());
                 System.out.println("Get topic "+test);
                 if(i!=0 &&!topics.get(j).getDetails().isEmpty()&& topics.get(j).getDetails()!= null && topics.get(j).getDetails().get(test-1).getDate().equals(splitDate(tweets.get(i).getDate()))){
                    topics.get(j).getDetails().get(topics.get(j).getDetails().size()-1).setFrequency(topics.get(j).getDetails().get(topics.get(j).getDetails().size()-1).getFrequency()+1);
                 }else{
                      KeywordOccurence data = new KeywordOccurence(splitDate(tweets.get(i).getDate()),1);
                      System.out.println(data);
                      topics.get(j).getDetails().add(data);
                              //.get(i).setFrequency(1);
                   //   topics.get(0).getDetails().get(i).setDate(tweets.get(i).getDate());
                 }
             }
             
             
            }  
         
                    
                }
            
            
       
     }
    
     /**
     * Checks if the user have keywords input and fixes the data for timeline.
     * @param keywords
     * @param tweets
     * @param topics
     */
     public static void  withKeywords(String[] keywords, ArrayList<tweetModel> tweets,ArrayList<TimelineTopics> topics){
         
        
         for(int i= 0;i<keywords.length;i++){
             String key = keywords[i];
             System.out.println("topic key: "+key);
         
             TimelineTopics ttops = new TimelineTopics(key,new ArrayList<KeywordOccurence>());
           topics.add(ttops);
                   
         }
 
         for(int i= 0;i<tweets.size();i++){
            for(int j = 0;j<keywords.length;j++){
                
             if(tweets.get(i).getMessage().matches("(.*)"+keywords[j]+"(.*)")){
                 int test = (topics.get(j).getDetails().size());
                 System.out.println("Get topic "+test);
                 if(i!=0 &&!topics.get(j).getDetails().isEmpty()&& topics.get(j).getDetails()!= null && topics.get(j).getDetails().get(test-1).getDate().equals(splitDate(tweets.get(i).getDate()))){
                    topics.get(j).getDetails().get(topics.get(j).getDetails().size()-1).setFrequency(topics.get(j).getDetails().get(topics.get(j).getDetails().size()-1).getFrequency()+1);
                 }else{
                      KeywordOccurence data = new KeywordOccurence(splitDate(tweets.get(i).getDate()),1);
                      System.out.println(data);
                      topics.get(j).getDetails().add(data);
                              //.get(i).setFrequency(1);
                   //   topics.get(0).getDetails().get(i).setDate(tweets.get(i).getDate());
                 }
             }
             
             
            }  
         }
     }
     
     /**
     * From the table name, keywords are extracted.
     * @param filename
     * @return String[] 
     */
     public static String[] splitTableName(String filename){
        String[] temp;       
        String[] temp2;       
        String[] temp3; 
        String[] months= new String[]{"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"}; 
        //String[] dd= new String[]{"dd"}; 
        filename= filename.toLowerCase();
         temp = filename.split("-");
       temp2 = temp[1].split("~");
       temp3 = temp2[0].split("\\.");

     
        for (int i=0; i<months.length;i++)
        {
//            if(temp3.length>1)
          //  if(temp2.length>1)
          //  System.out.println("hiiiii "+ temp3[0]+" month: "+months[i]);
            if (temp3[0].matches(months[i]))
            {
                return null;
            }else if(temp2[0].trim().equalsIgnoreCase("dd")){
                temp = filename.split("-");
                temp = temp[2].split("~");
                
                return temp;
            }
        }
        return temp2;
    }
    //17 Oct 2013 10:01:19 GMT
     /**
     * Changes the date format for the timeline.
     * @param date
     * @return String
     */
      public static String splitDate(String date){
        String[] temp= null;       
        String dates= null; 
      
         switch(CalendarHandler.identifyDateType(date)){
            case GMT:
                temp = date.split(" ");
                dates = temp[2]+", "+monthNumber(temp[1])+", "+temp[0];
                break;
            case CST:
                temp = CalendarHandler.getDateFormatted(date);
                dates = temp[2]+", "+monthNumber(temp[1])+", "+temp[0];
                break;
        }
        return dates;
    }
    // [Date.UTC(year,  month, day), frequency ]
    /**
     * Changes months to number format.
     * @param month
     * @return int
     */   
      private static int monthNumber(String month){
        int monthnum = 0;
        
        switch(month){
            case "Jan": return 0;
            case "Feb":return 1;
            case "Mar":return 2;
            case "Apr":return 3;
            case "May":return 4;
            case "Jun":return 5;
            case "Jul":return 6;
            case "Aug":return 7;
            case "Sep":return 8;
            case "Oct":return 9;
            case "Nov":return 10;
            case "Dec":return 11;
            default: return monthnum;
        }
    }
}
