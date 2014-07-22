
package tweets;

import database.Writer;
import database.tweetHandler;
import model.tweetModel;
import java.io.IOException;
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
     * Gets all tweets by keyword and returns TMDrillModel containing data
     * @param keyword
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByKeyword(String keyword){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByKeyword(keyword);
        return tmDrillModel;
    }
    
    /**
     * Gets all tweets by date and returns LMDrillModel containing data
     * @param start
     * @param end
     * @return LMDrillModel
     */
    public LMDrillModel cleanByDate(String start, String end){
        LMDrillModel lmDrillModel = tweetHandler.getAllTweetsByDate(start, end);
//        writeTweets(tweets);
        return lmDrillModel;
    }
    
    /**
     * Gets all tweets by date and returns TMDrillModel containing data
     * @param start
     * @param end
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByDate(String start, String end){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByDate(start, end);
        return tmDrillModel;
    }
    
    /**
     * Gets all tweets by keywords and date and returns LMDrillModel containing data
     * @param keywords
     * @param start
     * @param end
     * @return LMDrillModel
     */
    public LMDrillModel cleanByKeywordsAndDate(String keywords, String start, String end){
        LMDrillModel lmDrillModel = tweetHandler.getAllTweetsByKeywordAndDate(keywords, start, end);
//        writeTweets(tweets);
        return lmDrillModel;
    }
    
    /**
     * Gets all tweets by keywords and date and returns TMDrillModel containing data
     * @param keywords
     * @param start
     * @param end
     * @return TMDrillModel
     */
    public TMDrillModel TMcleanByKeywordsAndDate(String keywords, String start, String end){
        TMDrillModel tmDrillModel = tweetHandler.TMgetAllTweetsByKeywordAndDate(keywords, start, end);
        return tmDrillModel;
    }
}
