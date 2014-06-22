/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author Nancy
 */
public class FeatureStatistics {
    private int tweets;
    private int links;
    private int retweets;

    public FeatureStatistics(int tweets, int links, int retweets) {
        this.tweets = tweets;
        this.links = links;
        this.retweets = retweets;
    }
    
    /**
     * @return the tweets
     */
    public int getTweets() {
        return tweets;
    }

    /**
     * @param tweets the tweets to set
     */
    public void setTweets(int tweets) {
        this.tweets = tweets;
    }

    /**
     * @return the links
     */
    public int getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(int links) {
        this.links = links;
    }

    /**
     * @return the retweets
     */
    public int getRetweets() {
        return retweets;
    }

    /**
     * @param retweets the retweets to set
     */
    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }
}
