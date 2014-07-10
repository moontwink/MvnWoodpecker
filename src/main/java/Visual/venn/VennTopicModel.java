
package Visual.venn;

import java.util.ArrayList;

/**
 *
 * @author mathewmichael
 */
public class VennTopicModel {
    private int topicnumber;
    private ArrayList<String> uniquewords;

    public VennTopicModel(int topicnumber, ArrayList<String> uniquewords) {
        this.topicnumber = topicnumber;
        this.uniquewords = uniquewords;
    }
    
    public void deleteKeyword(int index) {
        uniquewords.remove(index);
    }
    
    public int findKeywordIndex(String keyword) {
        int index = uniquewords.indexOf(keyword);
        return index;
    }
    
    /**
     * @return the uniquewords
     */
    public ArrayList<String> getUniquewords() {
        return uniquewords;
    }

    /**
     * @param uniquewords the uniquewords to set
     */
    public void setUniquewords(ArrayList<String> uniquewords) {
        this.uniquewords = uniquewords;
    }

    /**
     * @return the topicnumber
     */
    public int getTopicnumber() {
        return topicnumber;
    }

    /**
     * @param topicnumber the topicnumber to set
     */
    public void setTopicnumber(int topicnumber) {
        this.topicnumber = topicnumber;
    }
    
    
}
