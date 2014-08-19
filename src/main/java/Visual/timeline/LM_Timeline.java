
package Visual.timeline;


import database.CalendarHandler;
import static database.tweetHandler.cleanTweet;
import static database.tweetHandler.getAllTweets;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.tweetModel;
import ngram.NGram;
import ngram.NGramDriver;
import static ngram.NGramDriver.sortngramlist;
import tfidf.LM_TfidfModel;

/**
 *
 * @author JOY
 */
public class LM_Timeline {
    
    /**
     *
     * @param filename
     * @param lmDM
     * @return url
     * @throws IOException
     */
    public static String timelineTopics(String filename, ArrayList<LM_TfidfModel> lmDM) throws IOException{
         String[] keywords= splitTableName(filename);
         ArrayList<tweetModel> tweets =  getAllTweets(filename);
         ArrayList<TimelineTopics> topics = new ArrayList<TimelineTopics>();
      
//         if(keywords!=null)
//        for(int i =0; i<keywords.length;i++){
//            System.out.println("keywords: "+ keywords[i]);
//        }
         
        
       if(keywords==null){
           withoutKeywords(tweets, topics, lmDM);
       }else{
           withKeywords(keywords, tweets, topics);
       } 
       
       //   sortDate(topics);
       for(int i=0;i<topics.size();i++){
//            System.out.println("sort me out: " + i);
       Collections.sort(topics.get(i).getDetails(),new MyComparator() );
       }
//       for(int index=0;index<topics.get(0).getDetails().size();index++)
//            System.out.println("sorted dates: " + topics.get(0).getDetails().get(index).getDate());
       
       
       String url = TimelineWriter.write(filename+".html", topics);
       return url;
    }
    
    /**
     * Checks if the user doesn't have keywords input and fixes the data for timeline.
     * @param tweets
     * @param topics
     * @param lmDM
     */
     private static void withoutKeywords(ArrayList<tweetModel> tweets,ArrayList<TimelineTopics> topics, ArrayList<LM_TfidfModel> lmDM){
         NGramDriver grammy = new NGramDriver();
         NGramDriver.setNgramlist(new ArrayList<NGram>());
          ArrayList<NGram> keywords = new ArrayList<NGram>();          
          ArrayList<NGram> keywords2 = new ArrayList<NGram>();
          
         for(int i=0;i<lmDM.size();i++){
//             System.out.println("lmdm ___ ->"+lmDM.get(i).getTweet());
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
//             System.out.println("topic key: "+key);
         
             TimelineTopics ttops = new TimelineTopics(key,new ArrayList<KeywordOccurence>());
           topics.add(ttops);
                   
         }
         
         boolean checker =false;
         for(int i= 0;i<tweets.size();i++){
            for(int j = 0;j<keywords.size();j++){
                String key = keywords.get(j).getTweet();
             if(tweets.get(i).getMessage().matches("(.*)"+key+"(.*)")){
                 int test = (topics.get(j).getDetails().size());
//                 System.out.println("Get topic "+test);
                 
                 if(i!=0 && !topics.get(j).getDetails().isEmpty()&& topics.get(j).getDetails()!= null ){
                     checker= false;
                     for(int k=0;k<topics.get(j).getDetails().size();k++){
                        if(topics.get(j).getDetails().get(k).getDate().equals(splitDate(tweets.get(i).getDate()))){
                            topics.get(j).getDetails().get(k).setFrequency(topics.get(j).getDetails().get(k).getFrequency()+1);
                            checker=true;
                        }
                     }
                     
                 }
                 if(i==0 || checker==false){
                      KeywordOccurence data = new KeywordOccurence(splitDate(tweets.get(i).getDate()),1);
//                      System.out.println(data.getDate());
                      topics.get(j).getDetails().add(data);
                              //.get(i).setFrequency(1);
                   //   topics.get(0).getDetails().get(i).setDate(tweets.get(i).getDate());
                 //checker = false;
                 }
             }
            }  
         }
         
         /*
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
            */
            
       
     }
    
     /**
     * Checks if the user have keywords input and fixes the data for timeline.
     * @param keywords
     * @param tweets
     * @param topics
     */
     private static void  withKeywords(String[] keywords, ArrayList<tweetModel> tweets,ArrayList<TimelineTopics> topics){
         
        
         for(int i= 0;i<keywords.length;i++){
             String key = keywords[i];
//             System.out.println("topic key: "+key);
         
             TimelineTopics ttops = new TimelineTopics(key,new ArrayList<KeywordOccurence>());
           topics.add(ttops);
                   
         }
         
         boolean checker =false;
         for(int i= 0;i<tweets.size();i++){
            for(int j = 0;j<keywords.length;j++){
                
             if(tweets.get(i).getMessage().matches("(.*)"+keywords[j]+"(.*)")){
                 int test = (topics.get(j).getDetails().size());
//                 System.out.println("Get topic "+test);
                 
                 if(i!=0 && !topics.get(j).getDetails().isEmpty()&& topics.get(j).getDetails()!= null ){
                     checker= false;
                     for(int k=0;k<topics.get(j).getDetails().size();k++){
                        if(topics.get(j).getDetails().get(k).getDate().equals(splitDate(tweets.get(i).getDate()))){
                            topics.get(j).getDetails().get(k).setFrequency(topics.get(j).getDetails().get(k).getFrequency()+1);
                            checker=true;
                        }
                     }
                     
                 }
                 if(i==0 || checker==false){
                      KeywordOccurence data = new KeywordOccurence(splitDate(tweets.get(i).getDate()),1);
//                      System.out.println(data.getDate());
                      topics.get(j).getDetails().add(data);
                              //.get(i).setFrequency(1);
                   //   topics.get(0).getDetails().get(i).setDate(tweets.get(i).getDate());
                 //checker = false;
                 }
             }
             
             
            }  
         }
         
        /*
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
         */
     }
     
     /**
     * From the table name, keywords are extracted.
     * @param filename
     * @return String[] 
     */
     private static String[] splitTableName(String filename){
        String[] temp;       
        String[] temp2;       
        String[] temp3; 
        String[] months= new String[]{"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"}; 
        //String[] dd= new String[]{"dd"}; 
        filename= filename.toLowerCase();
         temp = filename.split("-");
       temp2 = temp[1].split("~");
       temp3 = temp2[0].split("\\.");

//            System.out.println("__!! temp " + temp[0]);
//            System.out.println("__!! temp2 " + temp2[0]);
//            System.out.println("__!! temp3 " + temp3[0]);
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
        
        for(int x = 0; x < temp2.length; x++){
            if(temp2[x].contains("_")){
                temp2[x] = temp2[x].replaceAll("_", " "); }
        }
        
        return temp2;
    }
    //17 Oct 2013 10:01:19 GMT
     
     /**
     * Changes the date format for the timeline.
     * @param date
     * @return String
     */
      private static String splitDate(String date){
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
            case SGT:
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
      
    /**
     * Comparator for the date sorting in timeline
     * @param Topics
     */
   // public static void sortDate(ArrayList<TimelineTopics> Topics){
   private static class MyComparator implements Comparator<KeywordOccurence> {
        @Override
        public int compare(KeywordOccurence o1, KeywordOccurence o2) {
          // int d=0;
           String[] sam = o1.getDate().split(" ");
           String[] sam2 = o2.getDate().split(" ");
          // sam2[1].replaceAll(" ", "");
           int oo = parseInt(sam[2]);
           int ooo = parseInt(sam2[2]);
           int month = parseInt(sam[1].replaceAll(",", ""));
           int month2 = parseInt(sam2[1].replaceAll(",", ""));
          int d= o1.getDate().compareTo(o2.getDate());
             //  d = -1;
          
//            System.out.println("sorting dates!! ");
            
             if(sam[0].compareTo(sam2[0])==0){
             if(sam[1].compareTo(sam2[1])!=0) 
              if(month>month2)
                return 1;
             else if(month<month2)
                 return -1;
             
          }
          
          if(sam[0].compareTo(sam2[0])==0){
          if(sam[1].compareTo(sam2[1])==0)
             if(oo>ooo)
                 return 1;
             else if(oo<ooo)
                 return -1;
             else
                 return 0;
          }
          
             return d;
}}  
    
}
