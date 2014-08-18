
package database;

import static gui.Woodpecker.progressBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.tweetModel;

/**
 *
 * @author Nancy
 */
public class ImportHandler {
    private static String IMPORT_FILE_PATH="import";
    private static String importname = "import-x0";
    private static int importNumber = 0;

    /**
     * ImportHandler constructor.
     **/
    public ImportHandler() {
        File importConfFile= new File(IMPORT_FILE_PATH);
        try {
            Scanner scanner=new Scanner(importConfFile);
            if (scanner.hasNextLine())
            {
                String temp = scanner.nextLine();
                importNumber = Integer.parseInt(temp);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Import CSV data into database.
     * @param csvname
     * @param tweets
     * @return tablename of imported table
     **/
    public String importCSVData(String csvname, List<tweetModel> tweets) {
        String tablename = importname + importNumber + "-" + csvname;
        
        progressBar.setString("Importing...");
        
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
//                    System.out.println(ps);
                progressBar.setMaximum(tweets.size());
                progressBar.setValue(i);
            }
            
            ps.close();
            c.close();
            updateImportFile();
            
            progressBar.setString("Finished Import!");
            progressBar.setIndeterminate(false);
            
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tablename;
    }
    
    /**
     * Updates import file of the import number.
     */
    private void updateImportFile(){
        Writer writer = new Writer(IMPORT_FILE_PATH);
        try {
            writer.writeToFile(""+(importNumber+1));
        } catch (IOException ex) {
            Logger.getLogger(ImportHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
