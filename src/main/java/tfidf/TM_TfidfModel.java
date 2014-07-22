
package tfidf;

import mallet.TopicOutput;

/**
 *
 * @author Nancy
 */
public class TM_TfidfModel {
    private TopicOutput topic;
    private double score;

    /**
     * General constructor of TM_TfidfModel.
     * @param topic
     * @param score
     */
    public TM_TfidfModel(TopicOutput topic, double score) {
        this.topic = topic;
        this.score = score;
    }
    
    /**
     * @return the topic
     */
    public TopicOutput getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(TopicOutput topic) {
        this.topic = topic;
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
}
