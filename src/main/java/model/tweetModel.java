
package model;

/**
 *
 * @author Nancy
 */
public class tweetModel {
    private int idTweets;
    private String statusId;
    private String username;
    private String message;
    private String retweetCount;
    private double latitude;
    private double longitude;
    private String date;

    /**
     * @return the idTweets
     */
    public int getIdTweets() {
        return idTweets;
    }

    /**
     * @param idTweets the idTweets to set
     */
    public void setIdTweets(int idTweets) {
        this.idTweets = idTweets;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the retweetCount
     */
    public String getRetweetCount() {
        return retweetCount;
    }

    /**
     * @param retweetCount the retweetCount to set
     */
    public void setRetweetCount(String retweetCount) {
        this.retweetCount = retweetCount;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
     * @return the statusId
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
    
    
}
