
package Visual.venn;

import java.util.ArrayList;

/**
 *
 * @author mathewmichael
 */
public class VennScoreModel {
    private ArrayList<String> similarwords;
    private int vennscore;

    /**
     * Constructor for VennScoreModel
     */
    public VennScoreModel() {
        this.similarwords = new ArrayList<>();
        this.vennscore = 0;
    }
    
    /**
     * Adds new similar word to list
     * @param newword
     */
    public void addSimilarWord(String newword) {
        similarwords.add(newword);
    }
    
    /**
     * @return the similarwords
     */
    public ArrayList<String> getSimilarwords() {
        return similarwords;
    }

    /**
     * @param similarwords the similarwords to set
     */
    public void setSimilarwords(ArrayList<String> similarwords) {
        this.similarwords = similarwords;
    }

    /**
     * @return the vennscore
     */
    public int getVennscore() {
        return vennscore;
    }

    /**
     * @param vennscore the vennscore to set
     */
    public void setVennscore(int vennscore) {
        this.vennscore = vennscore;
    }
}
