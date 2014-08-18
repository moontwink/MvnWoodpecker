
package model;

import java.util.ArrayList;

/**
 *
 * @author mathewmichael
 */
public class InfluenceModel {
    private String twitter_account;
    private ArrayList<String> linknames;
    private int links_count = 0;
    private int follower_rank = 0;
    private int link_rank = 0;
    private int influence_rank = 0;
    private int influence = 0;
    private InfluencerType type;

    /**
     * InfluenceModel constructor.
     * @param twitter_account 
     * @param type 
     * @param linknames 
     * @param follower_rank 
     */
    public InfluenceModel(String twitter_account, InfluencerType type, ArrayList<String> linknames, int follower_rank) {
        this.twitter_account = twitter_account;
        this.type = type;
        this.linknames = linknames;
        this.follower_rank = follower_rank;
    }
    
    /**
     * @return the twitter_account
     */
    public String getTwitter_account() {
        return twitter_account;
    }

    /**
     * @param twitter_account the twitter_account to set
     */
    public void setTwitter_account(String twitter_account) {
        this.twitter_account = twitter_account;
    }

    /**
     * @return the links_count
     */
    public int getLinks_count() {
        return links_count;
    }

    /**
     * @param links_count the links_count to set
     */
    public void setLinks_count(int links_count) {
        this.links_count = links_count;
    }

    /**
     * @return the follower_rank
     */
    public int getFollower_rank() {
        return follower_rank;
    }

    /**
     * @param follower_rank the follower_rank to set
     */
    public void setFollower_rank(int follower_rank) {
        this.follower_rank = follower_rank;
    }

    /**
     * @return the influence
     */
    public int getInfluence() {
        return influence;
    }

    /**
     * @param influence the influence to set
     */
    public void setInfluence(int influence) {
        this.influence = influence;
    }

    /**
     * @return the linknames
     */
    public ArrayList<String> getLinknames() {
        return linknames;
    }

    /**
     * @param linknames the linknames to set
     */
    public void setLinknames(ArrayList<String> linknames) {
        this.linknames = linknames;
    }

    /**
     * @return the type
     */
    public InfluencerType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(InfluencerType type) {
        this.type = type;
    }

    /**
     * @return the link_rank
     */
    public int getLink_rank() {
        return link_rank;
    }

    /**
     * @param link_rank the link_rank to set
     */
    public void setLink_rank(int link_rank) {
        this.link_rank = link_rank;
    }

    /**
     * @return the influence_rank
     */
    public int getInfluence_rank() {
        return influence_rank;
    }

    /**
     * @param influence_rank the influence_rank to set
     */
    public void setInfluence_rank(int influence_rank) {
        this.influence_rank = influence_rank;
    }
    
}
