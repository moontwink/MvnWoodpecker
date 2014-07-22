
package database;

import gui.Start;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.tweetModel;

/**
 *
 * @author Nancy
 */
public class ImportHandler {
    static String importname = "import-x0";
    static int importNumber = 0;

    public ImportHandler() {
        importNumber += 1;
    }
    
    public String importCSVData(String csvname, List<tweetModel> tweets) {
        String tablename = importname + importNumber + "-" + csvname;
        
        try{
            Connection c = DBFactory.getConnection();
            System.out.println("Status: Dropping existing table from database");
            PreparedStatement ps = c.prepareStatement(
                "DROP TABLE IF EXISTS `" + tablename + "`; "
                );
                ps.execute();
                
            System.out.println("Status: Creating table ["+tablename+"] in database");
            ps = c.prepareStatement(
                "CREATE TABLE `" + tablename + "` (" +
                "`statusId` varchar(20) NOT NULL," +
                "`username` varchar(20) NOT NULL," +
                "`message` varchar(180) NOT NULL," +
                "`date` varchar(30) NOT NULL," +
                "`latitude` double NOT NULL," +
                "`longitude` double NOT NULL" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                
            System.out.println("Status: Inserting CSV entries into database");
            for(int i = 0; i < tweets.size(); i++){
                ps = c.prepareStatement(
                    "INSERT INTO `" + tablename + "` VALUES (?,?,?,?,?,?);");
                    ps.setString(1, tweets.get(i).getStatusId());
                    ps.setString(2, tweets.get(i).getUsername());
                    ps.setString(3, tweets.get(i).getMessage());
                    ps.setString(4, tweets.get(i).getDate());
                    ps.setDouble(5, tweets.get(i).getLatitude());
                    ps.setDouble(6, tweets.get(i).getLongitude());
                    ps.executeUpdate();
                Start.progressBar.setMaximum(tweets.size());
                Start.progressBar.setValue(i);
            }
            
            ps.close();
            c.close();
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tablename;
    }
}
