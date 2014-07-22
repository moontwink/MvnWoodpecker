
package Visual.timeline;

/**
 *
 * @author JOY
 */
public class KeywordOccurence {
    private String date= "";
    private int frequency = 0;
    
    /**
     *
     * @param date
     * @param frequency
     */
     public KeywordOccurence(String date, int frequency)
    {
        this.date = date;
        this.frequency = frequency;
        
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
