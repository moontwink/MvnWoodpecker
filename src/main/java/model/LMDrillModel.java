
package model;

import java.util.ArrayList;
import tfidf.Tfidf;

/**
 *
 * @author Nancy
 */
public class LMDrillModel {
    private int level;
    private String tablename;
    private ArrayList<Tfidf> topList;
    private String[] keywords;

    public LMDrillModel() {
        level = 0;
    }
    
    public LMDrillModel(int level) {
        this.level = level;
    }

    public LMDrillModel(int level, String tablename, ArrayList<Tfidf> topList) {
        this.level = level;
        this.tablename = tablename;
        this.topList = topList;
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
    public ArrayList<Tfidf> getTopList() {
        return topList;
    }

    /**
     * @param topList the topList to set
     */
    public void setTopList(ArrayList<Tfidf> topList) {
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
    
    
    
    
}
