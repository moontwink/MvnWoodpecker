
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nancy
 */
public class TablesHandler {
    /**
     * Drops specified tablename from database.
     * @param tablename
     **/
    public static void dropTable(String tablename){
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "drop table `" + tablename + "`;"
                );
            ps.execute();
            
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Drops all temporary tables from database.
     **/
    public static void dropAllTempTables(){
        System.out.println("---------- Closing Database Operation ----------");
        System.out.println("Operation: Drop all temporary tables");
        System.out.println("Status: Beginning to drop all temporary tables");
        
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "select table_name " +
                "from information_schema.tables " +
                "where table_name like 'temp-%' " +
                "and table_schema = 'tweetdb'; "
                );
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                dropTable(rs.getString("table_name"));
                System.out.println("drop table `" + rs.getString("table_name") + "`;");
            }
            System.out.println("---------- End of Database Operation ----------\n\n");
            rs.close();
            ps.close();
            c.close();
            
        }catch(ClassNotFoundException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Gets all import tables from database.
     * @return ArrayList<String>
     **/
    public static ArrayList<String> getAllImportTables(){
        ArrayList<String> importtables = new ArrayList<>();
        try{
            Connection c = DBFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "select table_name " +
                "from information_schema.tables " +
                "where table_name like 'import-%' " +
                "and table_schema = 'finaltweetsdb'; "
                );
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                importtables.add(rs.getString("table_name"));
            }
            
            rs.close();
            ps.close();
            c.close();
        }catch(ClassNotFoundException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException ex){
            Logger.getLogger(TablesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return importtables;
    }
}
