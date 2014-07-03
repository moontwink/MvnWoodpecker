
package model;

import java.util.ArrayList;
import tfidf.TM_TfidfDriver;
import tfidf.TM_TfidfModel;

/**
 *
 * @author Nancy
 */
public class TMDrillModel {
    private int level;
    private String tablename;
    private ArrayList<TM_TfidfModel> topics;
    private FeatureStatistics statistics;

    public TMDrillModel() {
        level = 0;
    }
    
    public TMDrillModel(int level) {
        this.level = level;
    }

    public TMDrillModel(int level, String tablename, ArrayList<TM_TfidfModel> topics) {
        this.level = level;
        this.tablename = tablename;
        this.topics = topics;
    }

    public TMDrillModel(int level, String tablename, ArrayList<TM_TfidfModel> topics, FeatureStatistics statistics) {
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
    public ArrayList<TM_TfidfModel> getTopics() {
        return topics;
    }

    /**
     * @param topics the topics to set
     */
    public void setTopics(ArrayList<TM_TfidfModel> topics) {
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
