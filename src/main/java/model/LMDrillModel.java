
package model;

import java.util.ArrayList;
import tfidf.LM_TfidfModel;

/**
 *
 * @author Nancy
 */
public class LMDrillModel {
    private int level;
    private String tablename;
    private ArrayList<LM_TfidfModel> topList;
    private String[] keywords;
    private FeatureStatistics statistics;

    /**
     * LMDrillModel constructor.
     */
    public LMDrillModel() {
        level = 0;
    }
    
    /**
     * LMDrillModel constructor.
     * @param level
     */
    public LMDrillModel(int level) {
        this.level = level;
    }

    /**
     * LMDrillModel constructor.
     * @param level
     * @param topList
     * @param statistics
     */
    public LMDrillModel(int level, String tablename, ArrayList<LM_TfidfModel> topList, FeatureStatistics statistics) {
        this.level = level;
        this.tablename = tablename;
        this.topList = topList;
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
     * @return the topList
     */
    public ArrayList<LM_TfidfModel> getTopList() {
        return topList;
    }

    /**
     * @param topList the topList to set
     */
    public void setTopList(ArrayList<LM_TfidfModel> topList) {
        this.topList = topList;
    }

    /**
     * @return the keywords
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
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
