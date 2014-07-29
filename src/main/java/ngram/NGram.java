
package ngram;

/**
 *
 * @author JOY
 */
public class NGram {
     private String tweet;
     private int frequency;
     private int tweetcount;

     /**
     *
     * @param tweet
     * @param frequency
     */
    public NGram(String tweet, int frequency){
     this.tweet = tweet;
     this.frequency = frequency;
     this.tweetcount = 1;
    }
    /**
     * @return the tweet
     */
    public String getTweet() {
        return tweet;
    }

    /**
     * @param tweet the tweet to set
     */
    public void setTweet(String tweet) {
        this.tweet = tweet;
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

    /**
     * @return the tweetcount
     */
    public int getTweetcount() {
        return tweetcount;
    }

    /**
     * @param tweetcount the tweetcount to set
     */
    public void setTweetcount(int tweetcount) {
        this.tweetcount = tweetcount;
    }
}
