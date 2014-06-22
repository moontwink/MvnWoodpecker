
package database;

import java.sql.*;

/**
 *
 * @author Nancy
 */
public class DBFactory {
    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/tweetdb";
    static String username = "root";
    static String password = "moontwink"; //"p@ssword";
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        
        return connection;
    }
    
    public static void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
