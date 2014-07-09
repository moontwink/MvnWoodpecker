
package database;

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
import mallet.TopicModel;
import model.FeatureStatistics;
import model.LMDrillModel;
import model.TMDrillModel;
import ngram.NGramDriver;
import tfidf.TM_TfidfDriver;
import tfidf.TfidfDriver;

/**
 *
 * @author Nancy
 */
public class tweetHandler {
    
    private static ArrayList<String> tweetlinks = new ArrayList<>();
    private static String expandedLinks = " ";
    private static String shortenedLinks = " ";
    
    //Adds tweet to database
    public static String addTweet(tweetModel tm){
        String message = "* Saving Failed.";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `Tweets` "
                    + "(statusId, username, message, retweetcount, latitude, longhitude, date) VALUES (?,?,?,?,?,?,?)"); 
            
            ps.setString(1, tm.getStatusId());
            ps.setString(2, tm.getUsername());
            ps.setString(3, tm.getMessage());
            ps.setString(4, tm.getRetweetCount());
            ps.setDouble(5, tm.getLatitude());
            ps.setDouble(6, tm.getLonghitude());
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
    
    //Normalizes tweet
    public static String normalizeTweet(String tweet){
        String tweetLine = tweet.toLowerCase();
        return tweetLine;
    }
    
    //Cleans tweet
    public static String cleanTweet(String tweet){
        tweet = RemoveLinks(tweet);
        tweet = normalizeTweet(tweet);
        
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
    
    public static String RemoveLinks(String tweet)
   {
        
       while(tweet.contains("http")||tweet.contains("t.co"))
       {
            String message = "";
            int indexhttp = tweet.indexOf("http");
            int indextco = tweet.indexOf("t.co");
            int indexlinks = 0;
            if(tweet.contains("http"))
            {
                while(tweet.charAt(indexhttp)!=' ')
                {
                message = message.concat(tweet.charAt(indexhttp)+"");
                indexhttp++;
                System.out.println("This is the message" +message);
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
            char lastCharacter = message.charAt(message.length()-1);
            if(!Character.isLetterOrDigit(lastCharacter)) {
                message = message.substring(0, message.length()-1);
            }
            
            shortenedLinks=message;
            tweet = tweet.replace(message, "").trim();
           
           try {
               expandedLinks = expandShortURL(shortenedLinks);
           } catch (IOException ex) {
               Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
           }
            
            if(expandedLinks != null)
            {
                if(expandedLinks.contains("bit.ly")||expandedLinks.contains("fb"))
                   try {
                       expandedLinks = expandShortURL(expandedLinks);
                } catch (IOException ex) {
                    Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                else
                    getTweetlinks().add(expandedLinks);
            }
            
            System.out.println("Short link" +shortenedLinks+ " -->  Expanded Link " +expandedLinks );
          
       }
       return tweet;
   }
    
   public static String expandShortURL(String address) throws IOException {
        URL url = new URL(address);
 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); //using proxy may increase latency
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        String expandedURL = connection.getHeaderField("Location");
        connection.getInputStream().close();
        return expandedURL;
   }
    
    //Retrieves all Tweets from specified table
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
    
   
    private static void sortNgramAndRemoveOutliers(){
        NGramDriver.sortngramlist(NGramDriver.getNgramlist());
        NGramDriver.removeOutliers();
    }
    
    //Language Modeller - Retrieves all Tweets via Keywords
    public static LMDrillModel getAllTweetsByKeywordAndDate(String keywords, String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String[] start = startDate.split(" ");
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-"+keywords+"-"+start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
           
        keywords = keywords.replaceAll(",", "%\' and message like \'%");
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
          System.out.println(keywords);
 
        String whereCondition = "";
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    if(whereCondition.equals(""))
                        whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
                    else
                        whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
//                    System.out.println(whereCondition);
                }
            }
//        }
        System.out.println("[3] " + whereCondition);
        
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE (message like '%" + keywords + "%')" +
                "and (date like "+whereCondition+" )"); 
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
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
            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                sortNgramAndRemoveOutliers();
                TfidfDriver.idfchecker(results);
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    //Topic Modeller - Retrieves all Tweets via Keywords
    public static TMDrillModel TMgetAllTweetsByKeywordAndDate(String keywords, String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String[] start = startDate.split(" ");
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-"+keywords+"-"+start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
           
        keywords = keywords.replaceAll(",", "%\' and message like \'%");
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
          System.out.println(keywords);
 
        String whereCondition = "";
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    if(whereCondition.equals(""))
                        whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
                    else
                        whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
//                    System.out.println(whereCondition);
                }
            }
//        }
        System.out.println("[3] " + whereCondition);
        
          try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE (message like '%" + keywords + "%')" +
                "and (date like "+whereCondition+" )"); 
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
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
            
            System.out.println("******************************* ");
            TopicModel tm = new TopicModel();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                tm.trainTopics();
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
               
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
    
    //Retrieves all Tweets via Keywords
    public static LMDrillModel getAllTweetsByKeyword(String keywords){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String tablename = "temp-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

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
            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                sortNgramAndRemoveOutliers();
                TfidfDriver.idfchecker(results);
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    //Topic Modeller - Retrieves all Tweets via Keywords
    public static TMDrillModel TMgetAllTweetsByKeyword(String keywords){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String tablename = "temp-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

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

            System.out.println("******************************* ");
            TopicModel tm = new TopicModel();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                tm.trainTopics();
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
                
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
    
    //Retrieves all Tweets via Date
    public static LMDrillModel getAllTweetsByDate(String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String[] start = startDate.split(" ");  //[0] month, [1] day, [2] year
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-" + start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];
        System.out.println(tablename);
        
        
        String whereCondition = "";
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
//                System.out.println("[2.5] "+whereCondition);
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    if(whereCondition.equals(""))
                        whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
                    else
                        whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
//                    System.out.println("[2] "+whereCondition);
                }
            }
//        }
        System.out.println("[3] " + whereCondition);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE date like " + whereCondition);
                ps.execute();   
                System.out.println(ps);
                //SELECT * FROM `Seasons` WHERE (date_field BETWEEN '2010-01-30 14:15:55' AND '2010-09-29 10:15:55')
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
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
            if(results.isEmpty()){
                lmDrillModel = new LMDrillModel(-1);
            }else{
                sortNgramAndRemoveOutliers();
                TfidfDriver.idfchecker(results);
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
               
                FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
                lmDrillModel = new LMDrillModel(0, tablename, TfidfDriver.getToplist(), stat);
            }
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return lmDrillModel;
    }
    
    //Topic Modeller - Retrieves all Tweets via Date
    public static TMDrillModel TMgetAllTweetsByDate(String startDate, String endDate){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String[] start = startDate.split(" ");  //[0] month, [1] day, [2] year
        String[] end = endDate.split(" ");
        tweetModel t;
        
        String tablename = "temp-" + start[0]+"."+start[1]+"."+start[2]+"-"+end[0]+"."+end[1]+"."+end[2];
        System.out.println(tablename);
        
        String whereCondition = "";
        
        int year = Integer.parseInt(start[2]);
//        for(int year = Integer.parseInt(start[2]); year <= Integer.parseInt(end[2]); year++){
            for(int month = monthNumber(start[0]); month <= monthNumber(end[0]); month++){
                int currentday = 1;
                if(month == monthNumber(start[0]))
                    currentday = Integer.parseInt(start[1]);
                
//                System.out.println("[2.5] "+whereCondition);
                for(int day = currentday; day <= numDaysinMonth(month); day++){
                    if(month == monthNumber(end[0]) && day > Integer.parseInt(end[1]))
                        break;
                    if(whereCondition.equals(""))
                        whereCondition = "'" + start[1] + " " + start[0] + " " + start[2] + "%'";
                    else
                        whereCondition = whereCondition.concat(" or date like '" + day + " " + monthName(month) + " " + year +"%'");
//                    System.out.println("[2] "+whereCondition);
                }
            }
//        }
        System.out.println("[3] " + whereCondition);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `tweets` " +
                "WHERE date like " + whereCondition);
                ps.execute();   
                System.out.println(ps);
                //SELECT * FROM `Seasons` WHERE (date_field BETWEEN '2010-01-30 14:15:55' AND '2010-09-29 10:15:55')
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();
            
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
            
            System.out.println("******************************* ");
            
            TopicModel tm = new TopicModel();
            
            if(results.isEmpty()){
                tmDrillModel = new TMDrillModel(-1);
            }else{
                tm.importData(results);
                tm.trainTopics();
                TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
                /* 
                * Initialize Influence Models
                */
               Influencer.initializeInfluenceModels();
               
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
    
    //Converts Month to its Number Equivalent
    private static int monthNumber(String month){
        int monthnum = 0;
        
        switch(month){
            case "Jan": return 1;
            case "Feb":return 2;
            case "Mar":return 3;
            case "Apr":return 4;
            case "May":return 5;
            case "Jun":return 6;
            case "Jul":return 7;
            case "Aug":return 8;
            case "Sep":return 9;
            case "Oct":return 10;
            case "Nov":return 11;
            case "Dec":return 12;
            default: return monthnum;
        }
    }
    
    //Converts Month Number to its name equivalent
    private static String monthName(int month){
        String name = " ";
        
        switch(month){
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return name;
        }
    }
    
    //Returns number of days in a month
    private static int numDaysinMonth(int month){
        int numdays = 30;
        
        switch(month){
            case 1: return 31;
            case 2: return 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            default: return numdays;
        }
    }
    
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
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    public static String getEarliestDate(){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM tweets "
                    + "ORDER BY idtweets ASC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date").substring(0, 11).trim();
        
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    public static String getLatestDate(){
        String date = "";
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT date FROM tweets "
                    + "ORDER BY idtweets DESC "
                    + "LIMIT 1;");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            date = rs.getString("date").substring(0, 11).trim();
        
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
    
    //Drilldown Via Language Modeller
    public static LMDrillModel drillDownByLM(String keywords, LMDrillModel currentlmDM){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        LMDrillModel lmDrillModel = new LMDrillModel();
        
        String tablename = "temp-dd-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + currentlmDM.getTablename() + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

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
            sortNgramAndRemoveOutliers();
            TfidfDriver.idfchecker(results);
            /* 
            * Initialize Influence Models
            */
           Influencer.initializeInfluenceModels();
           
            FeatureStatistics stat = new FeatureStatistics(results.size(), tweetlinks.size(), getAllRetweets(tablename));
            lmDrillModel = new LMDrillModel(currentlmDM.getLevel()+1, tablename, TfidfDriver.getToplist(), stat);
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(tweetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lmDrillModel;
    }
    
    //Drilldown Via Topic Modeller
    public static TMDrillModel drillDownByTM(String keywords, TMDrillModel currenttmDM){
        ArrayList<tweetModel> results = new ArrayList<tweetModel>();
        tweetModel t;
        TMDrillModel tmDrillModel = new TMDrillModel();
        
        String tablename = "temp-dd-"+keywords;
        tablename = tablename.replaceAll(",", "~");
        tablename = tablename.replaceAll(";", "~");
        tablename = tablename.replaceAll(" ", "");
           System.out.println(tablename);
        
        keywords = keywords.replaceAll(",", "%\' and message like \'%");  
        keywords = keywords.replaceAll(";", "%\' or message like \'%"); 
           System.out.println(keywords);
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`username` varchar(20) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`message` varchar(180) NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                System.out.println(ps);
            ps = c.prepareStatement(
                "INSERT INTO `" + tablename + "` (username, date, message) " + 
                "SELECT username, date, message FROM `" + currenttmDM.getTablename() + "` " +
                "WHERE message like '%" + keywords + "%';");
                ps.execute();   
                System.out.println(ps);
            
            ps = c.prepareStatement("SELECT * from `" + tablename + "`;");
            ResultSet rs = ps.executeQuery();

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

            System.out.println("******************************* ");
            TopicModel tm = new TopicModel();
            tm.importData(results);
            tm.trainTopics();
            TM_TfidfDriver.idfChecker(results, tm.getAllTopics());
            /* 
            * Initialize Influence Models
            */
           Influencer.initializeInfluenceModels();
           
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
