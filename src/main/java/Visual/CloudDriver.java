/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Visual;

import java.util.ArrayList;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import tfidf.Tfidf;
import tfidf.TfidfDriver;

/**
 *
 * @author MAZacarias
 */
public class CloudDriver {
    
    
        
    
    public static void createCloud(ArrayList<Tfidf> tweets){
        //ArrayList<Tfidf> tweets = TfidfDriver.getToplist();
        Cloud cloud = new Cloud();
        
        String t;
        double score;
        
        cloud.setMinWeight(0); //minimum score accepted
        
        for(int ctr = 0; ctr<tweets.size(); ctr++){
            t = tweets.get(ctr).getTweet();
            score = tweets.get(ctr).getScore();
            cloud.addTag(new Tag(t, score));
        }
        
        cloud.tags();
        //System.out.print(cloud.tags());
    }
    
}
