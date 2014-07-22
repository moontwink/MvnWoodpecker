
package tweets;

import database.tweetHandler;
import java.util.ArrayList;
import model.LMDrillModel;
import model.TMDrillModel;

/**
 *
 * @author Nancy
 */
public class DDTweetCleaner {

    /**
     * General constructor of DDTweetCleaner.
     */
    public DDTweetCleaner() {
        tweetHandler.setTweetlinks(new ArrayList<String>());
    }
    
    /**
     * Drilldown - Gets all tweets by keyword and returns LMDrillModel containing data
     * @param keyword
     * @param currentlmDM 
     * @return LMDrillModel
     */
    public LMDrillModel cleanByKeyword(String keyword, LMDrillModel currentlmDM){
        LMDrillModel DDlmDrillModel = tweetHandler.drillDownByLM(keyword, currentlmDM);
        return DDlmDrillModel;
    }
    
    /**
     * Drilldown - Gets all tweets by keyword and returns TMDrillModel containing data
     * @param keyword
     * @param currenttmDM 
     * @return LMDrillModel
     */
    public TMDrillModel TMcleanByKeyword(String keyword, TMDrillModel currenttmDM){
        TMDrillModel DDtmDrillModel = tweetHandler.drillDownByTM(keyword, currenttmDM);
        return DDtmDrillModel;
    }
}
