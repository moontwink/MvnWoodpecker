
package tfidf;

/**
 *
 * @author Matt
 */
public class LM_TfidfModel {
    private double score;
    private String tweet;

    /**
     * General constructor of Tfidf.
     * @param tweet
     * @param score
     */
    public LM_TfidfModel(String tweet, double score)
    {
        this.score = score;
        this.tweet = tweet;
        
    }
    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
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
    
}
