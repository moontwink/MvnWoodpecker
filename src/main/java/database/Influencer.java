/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import static database.tweetHandler.getAllRetweets;
import static database.tweetHandler.getTweetlinks;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.InfluenceModel;
import model.InfluencerType;
import tfidf.Tfidf;
import tfidf.TfidfDriver;


/**
 *
 * @author mathewmichael
 */
public class Influencer {

    /**
     *
     */
      public static ArrayList<InfluenceModel> influencers;
      public static ArrayList<String> shortenedlinks = new ArrayList();
      public static ArrayList<String> expandedlinks = new ArrayList();
      public static ArrayList<Integer> LinksCount = new ArrayList();
      public static ArrayList<String> LinksName = new ArrayList();
      public static ArrayList<Integer> SocialCount = new ArrayList();
      
      public static void initializeInfluenceModels() {
          influencers = new ArrayList<>();
          ArrayList<String> linknames = new ArrayList<>();
          
          linknames.add("gma");
          linknames.add("gmane.ws");
          influencers.add(new InfluenceModel("GMA", InfluencerType.NEWS, linknames,1));
          
          linknames = new ArrayList<>();
          linknames.add("ow.ly");
          linknames.add("abs");
          influencers.add(new InfluenceModel("ABS", InfluencerType.NEWS, linknames,2));
          
          linknames = new ArrayList<>();
          linknames.add("j.mp");
          linknames.add("anc");
          influencers.add(new InfluenceModel("ANC", InfluencerType.NEWS, linknames,3));
          
          linknames = new ArrayList<>();
          linknames.add("pagasa");
          linknames.add("PAGASA.DOST.GOV.PH");
          influencers.add(new InfluenceModel("PAGASA", InfluencerType.NEWS, linknames,4));
          
          linknames = new ArrayList<>();
          linknames.add("mmda.gov.ph");
          linknames.add("mmda");
          influencers.add(new InfluenceModel("MMDA", InfluencerType.NEWS, linknames,5));
          
          linknames = new ArrayList<>();
          linknames.add("abscbn");
          influencers.add(new InfluenceModel("TV PATROL", InfluencerType.NEWS, linknames,6));
          
          linknames = new ArrayList<>();
          linknames.add("deped");
          influencers.add(new InfluenceModel("DEPED", InfluencerType.NEWS, linknames,7));
          
          linknames = new ArrayList<>();
          linknames.add("juanforfun");
          linknames.add("cebupacific");
          influencers.add(new InfluenceModel("CEBUPACIFIC", InfluencerType.NEWS, linknames,8));
          
          linknames = new ArrayList<>();
          linknames.add("philippineairlines");
          linknames.add("philippine-airlines");
          influencers.add(new InfluenceModel("PHILIPPINEAIRLINES", InfluencerType.NEWS, linknames,9));
          
          linknames = new ArrayList<>();
          linknames.add("INQUIRER");
          linknames.add("Inquirer");
          linknames.add("bandera.inquirer");
          linknames.add("bandera");
          linknames.add("inq.ph");
          influencers.add(new InfluenceModel("INQUIRER", InfluencerType.NEWS, linknames,10));
          
          linknames = new ArrayList<>();
          linknames.add("phredcross");
          linknames.add("redcross");
          influencers.add(new InfluenceModel("PHILIPPINEREDCROSS", InfluencerType.NEWS, linknames,11));
          
          linknames.add("gmane.ws");
          influencers.add(new InfluenceModel("GMANEWS", InfluencerType.NEWS, linknames,12));
          
          linknames = new ArrayList<>();
          linknames.add("RAPPLER");
          linknames.add("rappler");
          linknames.add("rplr");
          influencers.add(new InfluenceModel("RAPPLER", InfluencerType.NEWS, linknames,13));
          
          linknames = new ArrayList<>();
          linknames.add("Magic89.9");
          linknames.add("magic899fm");
          influencers.add(new InfluenceModel("MAGIC89.9FM", InfluencerType.NEWS, linknames,14));
          
          linknames = new ArrayList<>();
          linknames.add("dzmm");
          influencers.add(new InfluenceModel("DZMM", InfluencerType.NEWS, linknames,15));
          
          linknames = new ArrayList<>();
          linknames.add("mb.com.ph");
          influencers.add(new InfluenceModel("MANILABULLETIN", InfluencerType.NEWS, linknames,16));
          
          linknames = new ArrayList<>();
          linknames.add("gov.ph");
          influencers.add(new InfluenceModel("PHILLIPINEOFFICIALGAZZETTE", InfluencerType.NEWS, linknames,17)); 
          
          linknames = new ArrayList<>();
          linknames.add("smart.com.ph");
          linknames.add("smrt.ph");
          influencers.add(new InfluenceModel("SMARTTELECOMMUNICATIONS", InfluencerType.NEWS, linknames,18));
          
          linknames = new ArrayList<>();
          linknames.add("bandila.abs-cbnnews.com");
          linknames.add("Bandila.OFFICIAL");
          influencers.add(new InfluenceModel("BANDILA", InfluencerType.NEWS, linknames,19)); 
          
          linknames = new ArrayList<>();
          linknames.add("meralco");
          influencers.add(new InfluenceModel("MERALCO", InfluencerType.NEWS, linknames,20));
          
          linknames = new ArrayList<>();
          linknames.add("globe.com.ph");
          linknames.add("globeph");
          influencers.add(new InfluenceModel("GLOBECOMMUNICATIONS", InfluencerType.NEWS, linknames,21));
          
          linknames = new ArrayList<>();
          linknames.add("dohgov");
          linknames.add("OfficialDOHgov");
          influencers.add(new InfluenceModel("DOH", InfluencerType.NEWS, linknames,22));
          
          linknames = new ArrayList<>();
          linknames.add("PhCHED");
          influencers.add(new InfluenceModel("CHED", InfluencerType.NEWS, linknames,23));
          
          linknames = new ArrayList<>();
          linknames.add("yahoophilippines");
          linknames.add("ph.yahoo.com");
          influencers.add(new InfluenceModel("YAHOOPHILIPPINES", InfluencerType.NEWS, linknames,24));
          
          linknames = new ArrayList<>();
          linknames.add("philstar");
          linknames.add("philstarnews");
          influencers.add(new InfluenceModel("PHILIPPINESTAR", InfluencerType.NEWS, linknames,25));
          
          linknames = new ArrayList<>();
          linknames.add("uaap.sports");
          linknames.add("The-Official-ABS-CBN-Sports-Page");
          influencers.add(new InfluenceModel("UAAPSPORTS", InfluencerType.NEWS, linknames,26));
          
          linknames = new ArrayList<>();
          linknames.add("interaksyon");
          linknames.add("n5e");
          linknames.add("tv5");
          influencers.add(new InfluenceModel("INTERAKSYON", InfluencerType.NEWS, linknames,27));
          
          linknames = new ArrayList<>();
          linknames.add("suncellular.com.ph");
          linknames.add("sun.cellular");
          influencers.add(new InfluenceModel("SUNCELLULAR", InfluencerType.NEWS, linknames,28));
          
          linknames = new ArrayList<>();
          linknames.add("up.edu.ph");
          influencers.add(new InfluenceModel("UPSYSTEM", InfluencerType.NEWS, linknames,29)); 
          
          linknames = new ArrayList<>();
          linknames.add("dotc.gov.ph");
          influencers.add(new InfluenceModel("DOTC", InfluencerType.SOCIALMEDIA, linknames,30));
          
          linknames = new ArrayList<>();
          linknames.add("Instagram");
          linknames.add("instagram");
          influencers.add(new InfluenceModel("INSTAGRAM", InfluencerType.SOCIALMEDIA, linknames,31));
          
          linknames = new ArrayList<>();
          linknames.add("twitter");
          linknames.add("Twitter");
          influencers.add(new InfluenceModel("TWITTER", InfluencerType.SOCIALMEDIA, linknames,32));
          
          linknames = new ArrayList<>();
          linknames.add("4sq");
          influencers.add(new InfluenceModel("FOURSQUARE", InfluencerType.SOCIALMEDIA, linknames,33)); 
          
          linknames = new ArrayList<>();
          linknames.add("youtube");
          influencers.add(new InfluenceModel("YOUTUBE", InfluencerType.SOCIALMEDIA, linknames,34));
          
          linknames = new ArrayList<>();
          linknames.add("google");
          linknames.add("goo.gl");
          influencers.add(new InfluenceModel("GOOGLE", InfluencerType.SOCIALMEDIA, linknames,35));
          
          linknames = new ArrayList<>();
          linknames.add("tmblr");
          influencers.add(new InfluenceModel("TUMBLER", InfluencerType.SOCIALMEDIA, linknames,36));
          
          try {
              linksExpander();
          } catch (IOException ex) {
              Logger.getLogger(Influencer.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      public static void linksExpander() throws IOException
      {
    
          for (int i = 0; i < getTweetlinks().size(); i++)
          {
                  System.out.println("link ["+i+ "] "+ getTweetlinks().get(i));
          }
          
          for (int x = 0; x < getTweetlinks().size(); x++)
          {
              boolean linkIsCounted = false;
              
              for(InfluenceModel m : influencers) {
                for(String linkname : m.getLinknames()) 
                {
                    if(getTweetlinks().get(x).contains(linkname))
                    {
                        System.out.println("The value of get tweet is ->" +getTweetlinks().get(x)+ "The value of link name is ->" +linkname );
                        m.setLinks_count(m.getLinks_count()+1);
                        System.out.println("###### "+m.getLinks_count());
                        linkIsCounted = true;
                        break;
                    }
                }
                if(linkIsCounted){
                    break;
                }
              }
          }
      
          Collections.sort(influencers, new Influencer.MyComparator());
          
          for(InfluenceModel f : influencers) {
              System.out.println("The count of "+ f.getTwitter_account() + " links is "+ f.getLinks_count()+ " while the follower rank " +f.getFollower_rank());
          }
          
          InfluenceComputer();

      }
      
      public static class MyComparator implements Comparator<InfluenceModel> {
   
        @Override
        public int compare(InfluenceModel o1, InfluenceModel o2) {
          
            try{
                if (o1.getLinks_count() > o2.getLinks_count()) {
                    return -1;
                } else if (o1.getLinks_count() < o2.getLinks_count()) {
                    return 1;
                }
            }catch(Exception e){
                System.err.println(e.toString());
        }
            return 0;
        }
    }
      
    public static class MyComparatorInfluence implements Comparator<InfluenceModel> {
   
        @Override
        public int compare(InfluenceModel o1, InfluenceModel o2) {
          
            try{
                if (o1.getInfluence() > o2.getInfluence()) {
                    return -1;
                } else if (o1.getInfluence() < o2.getInfluence()) {
                    return 1;
                }
            }catch(Exception e){
                System.err.println(e.toString());
        }
            return 0;
        }
    }  
      
      public static void InfluenceComputer ()
      {
          int count = 0;
          
          rankcreator();
          
          for(int i = 0; i < influencers.size(); i++)
          {
              count =  influencers.get(i).getFollower_rank() + influencers.get(i).getLink_rank();
              influencers.get(i).setInfluence(count);
          }
           
          Collections.sort(influencers, new Influencer.MyComparatorInfluence());
          
          System.out.println("--------------------------------");
          int z = 35;
           for(int i = 0; i < influencers.size(); i++) 
           {
              
              System.out.println("Twitter account -> "+influencers.get(i).getTwitter_account() +" link rank is " +influencers.get(i).getLink_rank()+ " follower rank is " +influencers.get(i).getFollower_rank()+ " = Aggregate Influence Rank " + (z-i+1) );
              System.out.println("--------------");
           }
      }
      
      public static void rankcreator()
      {
          int count = 0 ;
            for(int i = 0; i < influencers.size(); i++)
            {
                count+=1;
                influencers.get(i).setLink_rank(count);
            }
      }
      
      
}
