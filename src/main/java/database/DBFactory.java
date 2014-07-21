
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Nancy
 */
public class DBFactory {
    private static String DB_CONFIG_FILE_PATH="DB.conf";
    private static String driver = "com.mysql.jdbc.Driver";
    
    private static String url = "";
    private static String username = "";
    private static String password = ""; //"p@ssword";
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        initializeDBConnectionParameters();
        Connection connection = DriverManager.getConnection(url, username, password);
        
        return connection;
    }
    
    private static void initializeDBConnectionParameters()
    {
        System.out.println("---------- Initializing Database Operation ----------");
        System.out.println("Operation: Read database connection parameters from database configuration file");
        System.out.println("Status: Reading database configuration file");
        
        File dbConfigFile= new File(DB_CONFIG_FILE_PATH);
        
        try
        {
            Scanner scanner=new Scanner(dbConfigFile);
            if (scanner.hasNextLine())
            {
                String temp=scanner.nextLine();
                
                url = temp;
                System.out.println("URL: " + url);
                
                if (scanner.hasNextLine())
                {
                    username=scanner.nextLine();
                    System.out.println("Username: " + username);
                    
                    if (scanner.hasNextLine())
                    {
                        password=scanner.nextLine();
                        System.out.println("Password: " + password);
                    }
                }
            }           
        }
        catch (FileNotFoundException fnfex)
        {
            System.out.println("Error: DB.conf cannot be found in the specified file path");
            System.out.println(fnfex);
        }
        
        System.out.println("---------- End of Database Operation ----------\n\n");
    }
    
    public static void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
