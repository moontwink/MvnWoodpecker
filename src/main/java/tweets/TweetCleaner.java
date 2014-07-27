
package tweets;

import database.CalendarType;
import database.tweetHandler;
import model.tweetModel;
import java.util.ArrayList;
import model.LMDrillModel;
import model.TMDrillModel;

/**
 *
 * @author Nancy
 */
public class TweetCleaner {
    ArrayList<tweetModel> tweets;

    /**
     * General constructor of TweetCleaner.
     */
    public TweetCleaner() {
        tweetHandler.setTweetlinks(new ArrayList<String>());
    }
    
    /**
     * Gets all tweets by keyword and returns LMDrillModel containing data
     * @param dbtablename 
     * @param keyword
     * @return LMDrillModel
     */
    public LMDrillModel cleanByKeyword(String dbtablename, String keyword){
        
        LMDrillModel lmDrillModel = tweetHandler.getAllTweetsByKeyword(dbtablename, keyword);
        
        return lmDrillModel;
    }
    
    /**
     * Gets all tweets by keyword and returns TMDrillModel containing data.
     * @param dbtablename 
     * @param keyword
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByKeyword(String dbtablename, String keyword){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByKeyword(dbtablename, keyword);
        return tmDrillModel;
    }
    
    /**
     * Gets all tweets by date and returns LMDrillModel containing data.
     * @param dbtablename 
     * @param start
     * @param end
     * @return LMDrillModel
     */
    public LMDrillModel cleanByDate(String dbtablename, String start, String end, CalendarType format){
        LMDrillModel lmDrillModel = tweetHandler.getAllTweetsByDate(dbtablename, start, end, format);
//        writeTweets(tweets);
        return lmDrillModel;
    }
    
    /**
     * Gets all tweets by date and returns TMDrillModel containing data
     * @param dbtablename 
     * @param start
     * @param end
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByDate(String dbtablename, String start, String end, CalendarType format){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByDate(dbtablename, start, end, format);
        return tmDrillModel;
    }
    
    /**
     * Gets all tweets by keywords and date and returns LMDrillModel containing data
     * @param dbtablename 
     * @param keywords
     * @param start
     * @param end
     * @return LMDrillModel
     */
    public LMDrillModel cleanByKeywordsAndDate(String dbtablename, String keywords, String start, String end, CalendarType format){
        LMDrillModel lmDrillModel = tweetHandler.getAllTweetsByKeywordAndDate(dbtablename, keywords, start, end, format);
//        writeTweets(tweets);
        return lmDrillModel;
    }
    
    /**
     * Gets all tweets by keywords and date and returns TMDrillModel containing data
     * @param dbtablename 
     * @param keywords
     * @param start
     * @param end
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByKeywordsAndDate(String dbtablename, String keywords, String start, String end, CalendarType format){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByKeywordAndDate(dbtablename, keywords, start, end, format);
        return tmDrillModel;
    }
}
