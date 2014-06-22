
package model;

import java.util.ArrayList;
import mallet.TopicOutput;

/**
 *
 * @author Nancy
 */
public class TMDrillModel {
    private int level;
    private String tablename;
    private ArrayList<TopicOutput> topics;
    private FeatureStatistics statistics;

    public TMDrillModel() {
        level = 0;
    }
    
    public TMDrillModel(int level) {
        this.level = level;
    }

    public TMDrillModel(int level, String tablename, ArrayList<TopicOutput> topics) {
        this.level = level;
        this.tablename = tablename;
        this.topics = topics;
    }

    public TMDrillModel(int level, String tablename, ArrayList<TopicOutput> topics, FeatureStatistics statistics) {
        this.level = level;
        this.tablename = tablename;
        this.topics = topics;
        this.statistics = statistics;
    }
    
    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the tablename
     */
    public String getTablename() {
        return tablename;
    }

    /**
     * @param tablename the tablename to set
     */
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    /**
     * @return the topics
     */
    public ArrayList<TopicOutput> getTopics() {
        return topics;
    }

    /**
     * @param topics the topics to set
     */
    public void setTopics(ArrayList<TopicOutput> topics) {
        this.topics = topics;
    }

    /**
     * @return the statistics
     */
    public FeatureStatistics getStatistics() {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    public void setStatistics(FeatureStatistics statistics) {
        this.statistics = statistics;
    }
}
