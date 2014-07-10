

package mallet;

import java.util.ArrayList;

/**
 *
 * @author Nancy
 */
public class TopicOutput {
    private int topicnum;
    private double relevance;
    private ArrayList<String> keywords;

    public TopicOutput() {
    }
    
    public TopicOutput(int topicnum, double relevance, ArrayList<String> keywords) {
        this.topicnum = topicnum;
        this.relevance = relevance;
        this.keywords = keywords;
    }

    /**
     * @return the topicnum
     */
    public int getTopicnum() {
        return topicnum;
    }

    /**
     * @param topicnum the topicnum to set
     */
    public void setTopicnum(int topicnum) {
        this.topicnum = topicnum;
    }

    /**
     * @return the relevance
     */
    public double getRelevance() {
        return relevance;
    }

    /**
     * @param relevance the relevance to set
     */
    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    /**
     * @return the keywords
     */
    public ArrayList<String> getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
    
}
