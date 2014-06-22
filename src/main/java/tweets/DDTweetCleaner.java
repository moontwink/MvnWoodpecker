
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

    public DDTweetCleaner() {
        tweetHandler.setTweetlinks(new ArrayList<String>());
    }
    
    public LMDrillModel cleanByKeyword(String keyword, LMDrillModel currentlmDM){
        LMDrillModel DDlmDrillModel = tweetHandler.drillDownByLM(keyword, currentlmDM);
        return DDlmDrillModel;
    }
    
    public TMDrillModel TMcleanByKeyword(String keyword, TMDrillModel currenttmDM){
        TMDrillModel DDtmDrillModel = tweetHandler.drillDownByTM(keyword, currenttmDM);
        return DDtmDrillModel;
    }
}
