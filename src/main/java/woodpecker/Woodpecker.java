/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woodpecker;

import database.Reader;
import database.TablesHandler;
import database.tweetHandler;
import model.tweetModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tweets.TweetCleaner;

import twitter4j.VersionStream;

/**
 *
 * @author Nancy
 */
public class Woodpecker {
    
    public static void main(String[] args) {
        VersionStream.main(args);
        
        /*
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "./lib/tmt-0.4.0.jar");
            Process process = pb.start();
        } catch (IOException ex) {
            
        }
        */
        /*
        Reader read = new Reader("C:\\mallet\\sample-tweets\\uaap.txt");
        read.readMalletData();
        */
    }
}
