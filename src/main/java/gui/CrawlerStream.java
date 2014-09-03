
package gui;

import database.tweetHandler;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import model.tweetModel;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author nannaval
 */
public class CrawlerStream extends javax.swing.JFrame {
    private static final String OAUTH_CONFIG_FILE_PATH="OAUTH.conf";
    private static final String BOUNDARYBOX_CONFIG_FILE_PATH="BOUNDARYBOX.conf";
    private static final String VERSION = "4.0.1";
    private static final String TITLE = "Twitter4J Streaming API support";
    private static ConfigurationBuilder config;
    TwitterStream twitterStream;
    
    /**
     * Creates new form Crawler
     */
    public CrawlerStream() {
        initComponents();
        initialize();
    }
    
    /**
     * Initialize user interface.
     */
    private void initialize() {
        config = configBuild();
        titleVersion.setText(TITLE+ " " +VERSION);
    }
    
    /**
     * Set OAuth Configurations.
     */
    private ConfigurationBuilder configBuild(){
        //Setting OAuth Configurations
        String OAuthConsumerKey = "";
        String OAuthConsumerSecret = "";
        String OAuthAccessToken = "";
        String OAuthAccessTokenSecret = "";
        
        File dbConfigFile= new File(OAUTH_CONFIG_FILE_PATH);
        
        try
        {
            Scanner scanner=new Scanner(dbConfigFile);
            if (scanner.hasNextLine())
            {
                String temp=scanner.nextLine();
                
                OAuthConsumerKey = temp;
                
                if (scanner.hasNextLine())
                {
                    OAuthConsumerSecret=scanner.nextLine();
                    
                    if (scanner.hasNextLine())
                    {
                        OAuthAccessToken=scanner.nextLine();
                    }
                    if (scanner.hasNextLine())
                    {
                        OAuthAccessTokenSecret=scanner.nextLine();
                    }
                }
            }           
        }
        catch (FileNotFoundException fnfex)
        {
            JOptionPane.showMessageDialog(null, "OUATH.conf cannot be found in the specified file path.", "Database Requirement", JOptionPane.ERROR_MESSAGE);
        }
        
        config = new ConfigurationBuilder();
        config.setDebugEnabled(false)
            .setOAuthConsumerKey(OAuthConsumerKey)
            .setOAuthConsumerSecret(OAuthConsumerSecret)
            .setOAuthAccessToken(OAuthAccessToken)
            .setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
        return config;
    }
    
    /**
     * Start streaming from Twitter.
     */
    public void startStream() {
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        
        /* STREAM TWEETS */
        
        twitterStream = new TwitterStreamFactory(config.build()).getInstance();
        StatusListener listener = new StatusListener() {
            int tweetCounter = 0;
            @Override
            public void onStatus(Status status) {
                
                //Store Tweet in Database
                tweetModel tw = new tweetModel();
                tw.setStatusId(String.valueOf(status.getId()));
                
                String screenname = status.getUser().getScreenName();
                tw.setUsername(screenname);
                
                String tweet = tweetHandler.RewriteTweet(status.getText());
                tw.setMessage(tweet);   //Rewrites Tweet for Emoji Statuses
                
                tw.setLatitude(status.getGeoLocation().getLatitude());
                tw.setLongitude(status.getGeoLocation().getLongitude());
                tw.setDate(status.getCreatedAt().toString());
                
                tweetHandler.addTweet(tw);
                
                //Display tweet status in UI
                usernameField.setText("@"+screenname);
                tweetField.setText(tweet);
                latitudeField.setText(Double.toString(status.getGeoLocation().getLatitude()));
                longitudeField.setText(Double.toString(status.getGeoLocation().getLongitude()));
                tweetCounter++;
                counter.setText(Integer.toString(tweetCounter));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        ManilaStreamer(twitterStream);   //Start streaming w/ GeoLocation
        
        System.out.println(TITLE + " " + VERSION);
    }
    
    /**
     * Set Manila Streamer Boundary Box.
     */
    private static void ManilaStreamer(TwitterStream twitterStream){
        //Setting Location Coordinates and Retrieving Tweets
        double southCoordinate = 120.90;
        double westCoordinate = 14.370583;
        double northCoordinate = 121.81;
        double eastCoordinate = 14.583333;
        
        File dbConfigFile= new File(BOUNDARYBOX_CONFIG_FILE_PATH);
        
        try
        {
            Scanner scanner=new Scanner(dbConfigFile);
            if (scanner.hasNextLine())
            {
                String temp=scanner.nextLine();
                
                southCoordinate = Double.parseDouble(temp);
                
                if (scanner.hasNextLine())
                {
                    westCoordinate = Double.parseDouble(scanner.nextLine());
                    
                    if (scanner.hasNextLine())
                    {
                        northCoordinate = Double.parseDouble(scanner.nextLine());
                    }
                    if (scanner.hasNextLine())
                    {
                        eastCoordinate = Double.parseDouble(scanner.nextLine());
                    }
                }
            }           
        }
        catch (FileNotFoundException fnfex)
        {
            JOptionPane.showMessageDialog(null, "BOUNDARYBOX.conf cannot be found in the specified file path.", "Database Requirement", JOptionPane.ERROR_MESSAGE);
        }
        
        double[][] manilaBox = new double[][]{
            {southCoordinate, westCoordinate},    //sw
            {northCoordinate, eastCoordinate}     //ne
        };
        
        FilterQuery query = new FilterQuery().locations(manilaBox);
        twitterStream.filter(query);
        
        //Closing Stream
//        try {
//            Thread.sleep(3000000);    
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        
//        twitterStream.cleanUp();
//        twitterStream.shutdown();
        
    }
    
    private void closeStream(TwitterStream twitterStream){
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        startBtn = new javax.swing.JButton();
        username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tweetField = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        latitudeField = new javax.swing.JTextField();
        longitudeField = new javax.swing.JTextField();
        counter = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        stopBtn = new javax.swing.JButton();
        titleVersion = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tweet Crawler");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setOpaque(false);

        startBtn.setText("Start Crawling");
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        username.setText("USERNAME");

        tweetField.setEditable(false);
        tweetField.setColumns(20);
        tweetField.setLineWrap(true);
        tweetField.setRows(5);
        tweetField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tweetField.setFocusable(false);
        jScrollPane1.setViewportView(tweetField);

        jLabel1.setText("TWEET");

        usernameField.setEditable(false);
        usernameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameField.setFocusable(false);
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        jLabel2.setText("LATITUDE");

        jLabel5.setText("LONGITUDE");

        latitudeField.setEditable(false);
        latitudeField.setFocusable(false);

        longitudeField.setEditable(false);
        longitudeField.setFocusable(false);

        counter.setEditable(false);
        counter.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        counter.setBorder(null);
        counter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counterActionPerformed(evt);
            }
        });

        jLabel6.setText("Tweet Count");

        stopBtn.setText("Stop Crawling");
        stopBtn.setEnabled(false);
        stopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopBtnActionPerformed(evt);
            }
        });

        titleVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleVersion.setText("Twitter4J Streaming API Support 4.0.1");
        titleVersion.setEnabled(false);
        titleVersion.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(stopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(username))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(188, 188, 188)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(counter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabel1))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(40, 40, 40)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel5)))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(longitudeField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(latitudeField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 33, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(titleVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator3)
                            .addComponent(jSeparator4))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(latitudeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(longitudeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(counter, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        startStream();

    }//GEN-LAST:event_startBtnActionPerformed

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed

    }//GEN-LAST:event_usernameFieldActionPerformed

    private void counterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_counterActionPerformed

    private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopBtnActionPerformed
        closeStream(twitterStream);
        this.dispose();
    }//GEN-LAST:event_stopBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CrawlerStream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrawlerStream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrawlerStream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrawlerStream.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrawlerStream().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField counter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField latitudeField;
    private javax.swing.JTextField longitudeField;
    private javax.swing.JButton startBtn;
    private javax.swing.JButton stopBtn;
    private javax.swing.JLabel titleVersion;
    private javax.swing.JTextArea tweetField;
    private javax.swing.JLabel username;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
