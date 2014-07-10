
package Visual.timeline;


import static database.tweetHandler.cleanTweet;
import static database.tweetHandler.getAllTweets;
import java.io.IOException;
import java.util.ArrayList;
import model.tweetModel;
import ngram.NGram;
import ngram.NGramDriver;
import static ngram.NGramDriver.sortngramlist;
import tfidf.Tfidf;

/**
 *
 * @author JOY
 */
public class LM_Timeline {
    
    
    public static String timelineTopics(String filename, ArrayList<Tfidf> lmDM) throws IOException{
         String[] keywords= splitTableName(filename);
         ArrayList<tweetModel> tweets =  getAllTweets(filename);
         ArrayList<TimelineTopics> topics = new ArrayList<TimelineTopics>();
      
         if(keywords!=null)
        for(int i =0; i<keywords.length;i++){
            System.out.println("keywords: "+ keywords[i]);
        }
         
        
       if(keywords==null){
           withoutKeywords(tweets, topics, lmDM);
       }else{
           withKeywords(keywords, tweets, topics);
       } 
       String url = TimelineWriter.write(filename+".html", topics);
       return url;
    }
    
     public static void withoutKeywords(ArrayList<tweetModel> tweets,ArrayList<TimelineTopics> topics, ArrayList<Tfidf> lmDM){
         NGramDriver grammy = new NGramDriver();
         NGramDriver.setNgramlist(new ArrayList<NGram>());
          ArrayList<NGram> keywords = new ArrayList<NGram>();          
          ArrayList<NGram> keywords2 = new ArrayList<NGram>();
          
         for(int i=0;i<lmDM.size();i++){
             System.out.println("lmdm ___ ->"+lmDM.get(i).getTweet());
            grammy.TimelineNGramTweet(cleanTweet(lmDM.get(i).getTweet()));
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

//            System.out.println("__!! temp " + temp);
//            System.out.println("__!! temp " + temp2);
//            System.out.println("__!! temp " + temp3);
        for (int i=0; i<months.length;i++)
        {
            if(temp3.length>1)
          //  if(temp2.length>1)
          //  System.out.println("hiiiii "+ temp3[0]+" month: "+months[i]);
            if (temp3[0].matches(months[i]))
            {
                return null;
            }else if(temp[1].matches("dd")){
                temp = filename.split("-");
                temp = temp[2].split("~");
                
                return temp;
            }
        }
        return temp2;
    }
    //17 Oct 2013 10:01:19 GMT
      public static String splitDate(String date){
        String[] temp= null;       
        String dates= null; 
        
         temp = date.split(" ");
         dates = temp[2]+", "+monthNumber(temp[1])+", "+temp[0];
      
      
        return dates;
    }
    // [Date.UTC(year,  month, day), frequency ]
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
