/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual.timeline;

import java.util.ArrayList;

/**
 *
 * @author JOY
 */
public class TimelineTopics {
   private String topics;
   private ArrayList<KeywordOccurence> details;
   
    public TimelineTopics(String topics, ArrayList<KeywordOccurence> details)
    {
        this.topics = topics;
        this.details = details;
        
    }

    /**
     * @return the topics
     */
    public String getTopics() {
        return topics;
    }

    /**
     * @param topics the topics to set
     */
    public void setTopics(String topics) {
        this.topics = topics;
    }

    /**
     * @return the details
     */
    public ArrayList<KeywordOccurence> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(ArrayList<KeywordOccurence> details) {
        this.details = details;
    }
   
}
