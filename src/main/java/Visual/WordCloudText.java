/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual;
import java.util.ArrayList;
import javax.swing.*;
import tfidf.Tfidf;

public class WordCloudText
{
  // private instance variables
  private static String text = null;
  private static String[] words;
  
  public WordCloudText(ArrayList<Tfidf> tweets, String[] Keyword)
  { text = null;
          words=null;
    // gets user-entered text
    //text = JOptionPane.showInputDialog("Please enter text:");
    // removes all spaces in the string
      
       //words = new String[tweets.size()];
     for(int i=0;i<tweets.size();i++){
         
         System.out.println("twwwwwww   "+ tweets.get(i).getTweet());
                    // words[i] = tweets.get(i).getTweet();
         
                    
             
         
                     if(text!=null)
                         
                     text += tweets.get(i).getTweet() + " ";
                     else
                         text = tweets.get(i).getTweet() + " ";
                    // ,tm.getScore()
                   //  System.out.println(words[i]);
                     System.out.println(text);
                     
                     
                       for(int u = 0; u<Keyword.length;u++)
                    text=text.replaceAll(Keyword[u], "");
               }
    
    words = text.split("\\s+");
  }
  
  // sets the words in the string array
  public void setWords(String[] strArray)
  {
    words = strArray;
  }
  
  // returns the words in the string array
  public String[] getWords()
  {
    return words;
  }
  
  // sets the text
  public void setText(String str)
  {
    text = str;
  }
  
  // returns the text
  public String getText()
  {
    return text;
  }
}