
package database;

import gui.Woodpecker;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import model.tweetModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mallet.TopicModeler;
import model.FeatureStatistics;
import model.LMDrillModel;
import model.TMDrillModel;
import ngram.NGramDriver;
import tfidf.TM_TfidfDriver;
import tfidf.LM_TfidfDriver;

/**
 *
 * @author Nancy
 */
public class tweetHandler {
    
    private static ArrayList<String> tweetlinks = new ArrayList<>();
    private static String expandedLinks = " ";
    private static String shortenedLinks = " ";
    
    /**
     * This method writes the tweet in a text file.
     * @param tweet
     * @return utf-8 format string
     */
    public static String RewriteTweet(String tweet){
        String filePath = "writetweet.txt";
        String tweetLine = tweet;
        
        //Rewrites tweet to text file
        try{
            Writer write = new Writer(filePath, false);
            write.writeToFile(tweet);
        }catch(IOException ex){
        }
      
        //Reades tweet as pure text
        Reader read = new Reader(filePath);
        read.OpenFile();
        tweetLine = read.ReadFile();
        
        return tweetLine;
    }
    
    /**
     * Adds tweet to the database.
     * @param tm
     * @return success or failure message
     */
    public static String addTweet(tweetModel tm){
        String message = "* Saving Failed.";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `Tweets` "
                    + "(statusId, username, message, latitude, longitude, date) VALUES (?,?,?,?,?,?)"); 
            
            ps.setString(1, tm.getStatusId());
            ps.setString(2, tm.getUsername());
            ps.setString(3, tm.getMessage());
            ps.setDouble(5, tm.getLatitude());
            ps.setDouble(6, tm.getLongitude());
            ps.setString(7, tm.getDate());
            
            int i = ps.executeUpdate();
            
            if (i == 1) {
                message = "* Saving successful.";
            }
            
            ps.close();
            c.close();
            
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message;
        
    }
    
    /**
     * This method makes the tweet messages to lower case.
     * @param tweet
     * @return String
     */
    public static String normalizeTweet(String tweet){
        String tweetLine = tweet.toLowerCase();
        return tweetLine;
    }
    
    /**
     * Cleans tweet of links and normalizes it.
     * @param tweet
     * @return String Returns cleaned tweets
     */
    public static String cleanTweet(String tweet){
        tweet = RemoveLinks(tweet);
        tweet = normalizeTweet(tweet);
        
        if(tweet.startsWith("rt ")){
            tweet = tweet.replaceFirst("rt ", "");
        }
        
        while(tweet.contains("@")){
            String mention = "";
            int atindex = tweet.indexOf("@");
            
            while(tweet.charAt(atindex) != ' '){
                mention = mention.concat(tweet.charAt(atindex)+"");
                atindex++;
                if(atindex >= tweet.length())
                    break;
            }
//            System.out.println(mention);
            tweet = tweet.replace(mention, "").trim();
            
        }
        return tweet;
    }
    
    /**
     * Removes links from tweet
     * @param tweet
     * @return String Return the tweet without the links
     */
    public static String RemoveLinks(String tweet)
   {
        
       while(tweet.contains("http")||tweet.contains("t.co"))
       {
            String message = "";
            int indexhttp = tweet.indexOf("http");
            int indextco = tweet.indexOf("t.co");
            if(tweet.contains("http"))
            {
                while(tweet.charAt(indexhttp)!=' ')
                {
                message = message.concat(tweet.charAt(indexhttp)+"");
                indexhttp++;
//                System.out.println("This is the message" +message);
                    if(indexhttp >= tweet.length())
                        break;
                }
            }
            else if (tweet.contains("t.co"))
            {
                while(tweet.charAt(indextco)!=' ')
                {
                message = message.concat(tweet.charAt(indextco)+"");
                indextco++;
                
                    if(indextco >= tweet.length())
                        break;
                }
            }
            
            /* Checks if last character of link is a special character */
            if(message.contains("\"")){
                int lastCharacter = message.indexOf("\"");
                message = message.substring(0, lastCharacter);
            } else if(message.contains("”")) {
                int lastCharacter = message.indexOf("”");
                message = message.substring(0, lastCharacter);
            } else {
                char lastCharacter = message.charAt(message.length()-1);
                if(!Character.isLetterOrDigit(lastCharacter)) {
                    message = message.substring(0, message.length()-1);
                }
            }
            
            shortenedLinks=message;
            tweet = tweet.replace(message, "").trim();
           
            
           try {
               expandedLinks = expandShortURL(shortenedLinks);
           } catch (IOException ex) {
           }
            
            if(expandedLinks != null)
            {
                if(expandedLinks.contains("bit.ly")||expandedLinks.contains("fb"))
                   try {
                       expandedLinks = expandShortURL(expandedLinks);
                } catch (IOException ex) {
                       System.out.println("Unable to expand wrapped URL.");
                }
                else {
                    getTweetlinks().add(expandedLinks);
                }
            }
            
            System.out.println("Short link " +shortenedLinks+ " -->  Expanded Link " +expandedLinks );
       }
       return tweet;
   }
    
   /**
    * Expands the address to its expanded version
    * @param address
    * @return String Returns expanded URL.
    * @throws IOException
    */
   public static String expandShortURL(String address) throws IOException {
        URL url = new URL(address);
 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); //using proxy may increase latency
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        String expandedURL = connection.getHeaderField("Location");
        connection.getInputStream().close();
        return expandedURL;
   }
    
   /**
    * Gets all tweets from a table
    * @param tablename
    * @return ArrayList<tweetModel> 
    **/
    public static ArrayList<tweetModel> getAllTweets(String tablename){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM `" + tablename + "`");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setMessage(cleanTweet(rs.getString("message")));
                t.setDate(rs.getString("date"));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * LANGUAGE MODELER - This method gets all tweets in the database given the keywords and date.
     * @param dbtablename 
     * @param keywords
     * @param startDate
     * @param endDate
     * @return LMDrillModel
     */
    public static LMDrillModel getAllTweetsByKeywordAndDate(String dbtablename, String keywords, String startDate, String endDate, CalendarType format){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String[] start = startDate.split(" ");
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-"+keywords+"-"+start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "_");
            Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
           
        keywords = keywords.replaceAll(",", "%\' and message like \'%");
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//          System.out.println(keywords);
 
        String whereCondition = buildDateWhereCondition(start, end, format);
        
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE ("+whereCondition+") " +
                "AND (message like '%" + keywords + "%')"); 
//            System.out.println(ps);
                ps.execute();   
                
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
            Woodpecker.systemOutArea.append("\tCleaning tweets\n\tN-gram Tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                NGramDriver.NGramTweet(cleanTweet(t.getMessage()));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                NGramDriver.sortNgramAndRemoveOutliers();
                LM_TfidfDriver.idfchecker(results);
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, LM_TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    /**
     * TOPIC MODELER - This method gets all tweets in the database given the keywords and date.
     * @param dbtablename 
     * @param keywords
     * @param startDate
     * @param endDate
     * @return TMDrillModel
     */
    public static TMDrillModel TMgetAllTweetsByKeywordAndDate(String dbtablename, String keywords, String startDate, String endDate, CalendarType format){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String[] start = startDate.split(" ");
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-"+keywords+"-"+start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "_");
            Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
           
        keywords = keywords.replaceAll(",", "%\' and message like \'%");
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//          System.out.println(keywords);
 
       String whereCondition = buildDateWhereCondition(start, end, format);
        
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE ("+whereCondition+") " +
                "AND (message like '%" + keywords + "%')"); 
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
            Woodpecker.systemOutArea.append("\tCleaning tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
            TopicModeler tm = new TopicModeler();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                tm.trainTopics();
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                tmDrillModel = new TMDrillModel(0, tablename, TM_TfidfDriver.getTopTopics(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tmDrillModel;
    }
    
    /**
     * LANGUAGE MODELER - This method gets all tweets in the database given the keywords.
     * @param dbtablename  
     * @param keywords
     * @return LMDrillModel
     */
    public static LMDrillModel getAllTweetsByKeyword(String dbtablename, String keywords){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String tablename = "temp-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "_");
           Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
//                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

            Woodpecker.systemOutArea.append("\tCleaning tweets\n\tN-gram Tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                NGramDriver.NGramTweet(cleanTweet(t.getMessage()));
                results.add(t);
            }
            
            
            rs.close();
            ps.close();
            c.close();

            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                NGramDriver.sortNgramAndRemoveOutliers();
                LM_TfidfDriver.idfchecker(results);
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, LM_TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    /**
     * TOPIC MODELER - This method gets all tweets in the database given the keywords.
     * @param dbtablename 
     * @param keywords
     * @return TMDrillModel
     */
    public static TMDrillModel TMgetAllTweetsByKeyword(String dbtablename, String keywords){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String tablename = "temp-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "_");
           Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
//                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

            Woodpecker.systemOutArea.append("\tCleaning tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();

            TopicModeler tm = new TopicModeler();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                try{
                tm.trainTopics();
                }catch(ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null, "Mallet Index Out of Bounds.", "Mallet", JOptionPane.ERROR_MESSAGE);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Mallet Exception.", "Mallet", JOptionPane.ERROR_MESSAGE);
                }
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
                
               FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
               tmDrillModel = new TMDrillModel(0, tablename, TM_TfidfDriver.getTopTopics(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tmDrillModel;
    }
    
    /**
     * LANGUAGE MODELER - This method gets all tweets in the database given the dates.
     * @param dbtablename 
     * @param startDate
     * @param endDate
     * @return LMDrillModel
     */
    public static LMDrillModel getAllTweetsByDate(String dbtablename, String startDate, String endDate, CalendarType format){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String[] start = startDate.split(" ");  //[0] month, [1] day, [2] year
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-" + start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];
          Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//        System.out.println(tablename);
        String whereCondition = buildDateWhereCondition(start, end, format);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE " + whereCondition);
                ps.execute();   
//                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
            Woodpecker.systemOutArea.append("\tCleaning tweets\n\tN-gram Tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                NGramDriver.NGramTweet(cleanTweet(t.getMessage()));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
//            if(results.isEmpty()){
//                lmDrillModel = new LMDrillModel(-1);
//            }else{
//                sortNgramAndRemoveOutliers();
//                LM_TfidfDriver.idfchecker(results);
//                /* 
//                * Initialize Influence Models
//                */
//               Influencer.initializeInfluenceModels();
//               
//                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
//                lmDrillModel = new LMDrillModel(0, tablename, LM_TfidfDriver.getToplist(), stat);
//            }
            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                NGramDriver.sortNgramAndRemoveOutliers();
                LM_TfidfDriver.idfchecker(results);
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, LM_TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return lmDrillModel;
    }

    /**
     * This method returns the where condition for the MySQL statement for the date.
     * @param start
     * @param end
     * @param format
     * @return String
     * @throws NumberFormatException 
     */
    private static String buildDateWhereCondition(String[] start, String[] end, CalendarType format) throws NumberFormatException {
        String whereCondition = "";
        
        if(format == CalendarType.GMT){
//            start[0] = Integer.toString(CalendarHandler.monthNumber(start[0]));
//            end[0] = Integer.toString(CalendarHandler.monthNumber(end[0]));
//            whereCondition = "STR_TO_DATE(SUBSTR(date,1,12), '%d %M %Y') "
//                    + "BETWEEN '" + start[2] + "-" + start[0] + "-" + start[1] + "' "
//                    + "AND '" + end[2] + "-" + end[0] + "-" + end[1] + "' ";
//            whereCondition = "date BETWEEN " + "'" + start[1] + " " + start[0] + " " + start[2] + "%' "
//                    + "AND " + "'" + end[1] + " " + end[0] + " " + end[2] + "%' ";
            whereCondition = "DATE_FORMAT(STR_TO_DATE(date, '%e %b %Y %T GMT'), '%e %b %Y') "
                    + "BETWEEN " + "'" + start[1] + " " + start[0] + " " + start[2] + "' "
                    + "AND " + "'" + end[1] + " " + end[0] + " " + end[2] + "' ";
        }else if(format == CalendarType.CST){
            int day = Integer.parseInt(start[1]);
            if(day <= 9) {
                start[1] = "0"+start[1];
            }
            day = Integer.parseInt(end[1]);
            if(day <= 9) {
                end[1] = "0"+end[1];
            }
            
            whereCondition = "DATE_FORMAT(STR_TO_DATE(date, '%a %b %d %T CST %Y'), '%d %b %Y') "
                    + "BETWEEN " + "'" + start[1] + " " + start[0] + " " + start[2] + "' "
                    + "AND " + "'" + end[1] + " " + end[0] + " " + end[2] + "' ";
        }else if(format == CalendarType.SGT){
            int day = Integer.parseInt(start[1]);
            if(day <= 9) {
                start[1] = "0"+start[1];
            }
            day = Integer.parseInt(end[1]);
            if(day <= 9) {
                end[1] = "0"+end[1];
            }
            
            whereCondition = "DATE_FORMAT(STR_TO_DATE(date, '%a %b %d %T SGT %Y'), '%d %b %Y') "
                    + "BETWEEN " + "'" + start[1] + " " + start[0] + " " + start[2] + "' "
                    + "AND " + "'" + end[1] + " " + end[0] + " " + end[2] + "' ";
        }
        System.out.println(whereCondition);
        return whereCondition;
    }
    
    /**
     * TOPIC MODELER - This method gets all tweets in the database given the dates.
     * @param dbtablename 
     * @param startDate
     * @param endDate
     * @return TMDrillModel
     */
    public static TMDrillModel TMgetAllTweetsByDate(String dbtablename, String startDate, String endDate, CalendarType format){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String[] start = startDate.split(" ");  //[0] month, [1] day, [2] year
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-" + start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];
        Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//        System.out.println(tablename);
        
        String whereCondition = buildDateWhereCondition(start, end, format);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + dbtablename + "` " +
                "WHERE " + whereCondition);
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
            Woodpecker.systemOutArea.append("\tCleaning tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
            TopicModeler tm = new TopicModeler();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                tm.trainTopics();
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                tmDrillModel = new TMDrillModel(0, tablename, TM_TfidfDriver.getTopTopics(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return tmDrillModel;
    }
    
    /**
     * Gets all retweets from a table.
     * @param tablename
     * @return ArrayList<tweetModel>
     */
    public static ArrayList<tweetModel> getAllRetweets(String tablename){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM `" + tablename + "` "
                    + "WHERE message like 'RT %' or message like '\"@%' or message like '“@%'");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setMessage(rs.getString("message"));
                t.setDate(rs.getString("date"));
                
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * Gets the earliest date from the table.
     * @param tablename
     * @return String
     */
    public static String getEarliestDate(String tablename){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM `"+ tablename + "`"
                    + "ORDER BY statusid ASC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date");
        
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    /**
     * Gets the lates date from the table.
     * @param tablename
     * @return String
     */
    public static String getLatestDate(String  tablename){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM `"+ tablename + "`"
                    + "ORDER BY statusid DESC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date");
        
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    /**
     * LANGUAGE MODELER - This method gets the tweets for the Drill Down.
     * @param keywords
     * @param currentlmDM
     * @return LMDrillModel
     */
    public static LMDrillModel drillDownByLM(String keywords, LMDrillModel currentlmDM){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String tablename = "temp-dd-"+keywords+"-LMx"+(currentlmDM.getLevel()+1);
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
            Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + currentlmDM.getTablename() + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
//                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

            Woodpecker.systemOutArea.append("\tCleaning tweets\n\tN-gram Tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                NGramDriver.NGramTweet(cleanTweet(t.getMessage()));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();

            System.out.println("******************************* ");
            NGramDriver.sortNgramAndRemoveOutliers();
            LM_TfidfDriver.idfchecker(results);
           
            FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
            lmDrillModel = new LMDrillModel(currentlmDM.getLevel()+1, tablename, LM_TfidfDriver.getToplist(), stat);
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    /**
     * TOPIC MODELER - This method gets the tweets for the Drill Down.
     * @param keywords
     * @param currenttmDM
     * @return TMDrillModel
     */
    public static TMDrillModel drillDownByTM(String keywords, TMDrillModel currenttmDM){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String tablename = "temp-dd-"+keywords+"-TMx"+(currenttmDM.getLevel()+1);
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
            Woodpecker.systemOutArea.append("--- Creating subcorpus --- \n\t["+tablename+"]\n");
//           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
//           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
//                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + currenttmDM.getTablename() + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
//                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

            Woodpecker.systemOutArea.append("\tCleaning tweets\n\tN-gram Tweets\n");
            while(rs.next()){
                t = new tweetModel();
                t.setUsername(rs.getString("username"));
                t.setDate(rs.getString("date"));
                t.setMessage(cleanTweet(rs.getString("message")));
                results.add(t);
            }
            
            rs.close();
            ps.close();
            c.close();

            TopicModeler tm = new TopicModeler();
            tm.importData(results);
            tm.trainTopics();
            TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
           
            FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
            tmDrillModel = new TMDrillModel(currenttmDM.getLevel()+1, tablename, TM_TfidfDriver.getTopTopics(), stat);
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tmDrillModel;
    }

    /**
     * @return the tweetlinks
     */
    public static ArrayList<String> getTweetlinks() {
        return tweetlinks;
    }

    /**
     * @param aTweetlinks the tweetlinks to set
     */
    public static void setTweetlinks(ArrayList<String> aTweetlinks) {
        tweetlinks = aTweetlinks;
    }
}
