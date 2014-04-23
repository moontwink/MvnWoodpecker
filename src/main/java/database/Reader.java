/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nancy
 */
public class Reader {
    private Scanner scan;
    private String path;
    
    public Reader(String file_path){
        path = file_path;
    }
    
    public void OpenFile(){
        try {
            scan = new Scanner(new File(path));
//            System.out.println("It is working.");
        } catch (FileNotFoundException ex) {
            System.out.println("Not working.");
        }
    }
    
    public String ReadFile(){
        String tweetLine;
        
        scan.reset();
        tweetLine = scan.nextLine();
        
        return tweetLine;
    }
    
    public void readMalletData(){
        String tweetLine;
        String filePath = "C:\\mallet\\sample-tweets\\uaap\\";
        int num = 1;
        
        OpenFile();
        
        while(scan.hasNextLine()){
            tweetLine = scan.nextLine();
            Writer write = new Writer(filePath+num+".txt");
            try {
                write.writeToFile(tweetLine);
            } catch (IOException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            num++;
        }
        
    }
    
    public void readText(){
        String worded = "";
        
        OpenFile();
        int x = 0;
        while(scan.hasNextLine()){
            x++;
            if(x % 10 == 0) 
                worded = worded.concat("\"\n+\"");
            worded = worded.concat(""+scan.nextLine().trim().toLowerCase() +"|");
        }
        
        System.out.println(worded);
    }
}
