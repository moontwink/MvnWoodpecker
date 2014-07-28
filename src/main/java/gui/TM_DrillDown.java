
package gui;

import components.TextAreaRenderer;
import Visual.FXPieChart;
import Visual.timeline.FXTimeline;
import static Visual.timeline.TM_Timeline.timelineTopics;
import Visual.venn.VennDiagramFX;
import static Visual.venn.VennDiagramWriter.write;
import components.ButtonTabComponent;
import database.Influencer;

import database.tweetHandler;
import java.io.IOException;
import model.tweetModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import mallet.TopicOutput;
import model.InfluenceModel;
import model.InfluencerType;
import model.TMDrillModel;
import tfidf.TM_TfidfDriver;
import tfidf.TM_TfidfModel;
import tweets.DDTweetCleaner;

/**
 *
 * @author Nancy
 */
public class TM_DrillDown extends javax.swing.JPanel {
 private static DefaultTableModel model;
 private TMDrillModel tmDM;
 private JTabbedPane tabPane;
 
 String title = null; 
         int i=1;
    /**
     * Creates new form DD_Level1
     */
    public TM_DrillDown(TMDrillModel tmDM, JTabbedPane tabPane) {
        this.tmDM = tmDM;
        this.tabPane = tabPane;
        
        initComponents();
        
        /* Insert Topics to Table */
        insertTopics();
        
        /* Inserts Tweet List to Table */
        insertTweetsList();
        
        /* Inserts Retweet List to Text Area */
        insertRetweetsList();
        
        /* Inserts Influencer Index to Table */
        insertInfluencerIndex();
        
        Start.systemOutArea.append("\tGenerating Visuals\n");
        
        /*Creates and Places Venn Diagram in Panel*/
        try {
            VennDiagramFX.VennApplicationFrame(venndiagrampanel, write(tmDM.getTablename()+".html",tmDM));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "There has been an error loading the venn diagram.", "Venn Diagram Construction", JOptionPane.ERROR_MESSAGE);
        }
        
        /*Creates and Places Timeline in Panel*/
        try {
            ArrayList<TopicOutput> timelineInput = new ArrayList<>();
            for(TM_TfidfModel tmtf : tmDM.getTopics()){
                timelineInput.add(tmtf.getTopic());
            }
            FXTimeline.TimelineApplicationFrame(timelinePanel, timelineTopics(tmDM.getTablename(), timelineInput));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "There has been an error loading the timeline.", "Timeline Construction", JOptionPane.ERROR_MESSAGE);
        }
        
        /*Creates Pie Chart for Feature Statistics*/
        FXPieChart.PieApplicationFrame(pieChartPanel, tmDM.getStatistics());
        
        
        
//     try {
         //        if((JTabbedPane)this.getParent() != null){
//            JTabbedPane j = (JTabbedPane)this.getParent();
//            title = j.getTitleAt(j.getSelectedIndex()).substring(j.getTitleAt(j.getSelectedIndex()).length()-3, j.getTitleAt(j.getSelectedIndex()).length());
//            System.out.println("~~~~~ "+tmDM.getTablename());
//            if(title.equals("LM")){
//             //   rawDataFld.setText("LANGUAGE MODEL");
//            }
//        }
        
        
    }
    
    /**
     * Inserts Topics output into topics table.
     */
    private void insertTopics(){
       model = (DefaultTableModel)topicstable.getModel();
       model.setRowCount(0);
       
       String t;
//       double i;
       
        for(int num = 0; num < tmDM.getTopics().size(); num++){
            t = "";
//            i = tmDM.getTopics().get(num).getScore();

            for(String s : tmDM.getTopics().get(num).getTopic().getKeywords())
                t = t.concat("| " + s + " |");

            model.addRow(new Object[]{num+1, t});
        }
        topicstable.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());    
    }
    
    /**
     * Inserts tweets into tweets table.
     */
    private void insertTweetsList(){
        model = (DefaultTableModel)tweetTable.getModel();
        model.setRowCount(0);
        
        ArrayList<tweetModel> tweets = tweetHandler.getAllTweets(getTmDM().getTablename());
        
        for(tweetModel tm : tweets) {
            model.addRow(new Object[]{tm.getDate(), tm.getMessage()});
        }
        tweetTable.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
    }
   
    /**
     * Insert influencers index into table.
     */
    private void insertInfluencerIndex(){
        try {
            Start.systemOutArea.append("\tIndexing Influencers\n");
            Influencer.initializeInfluenceModels();
            Influencer.indexLinks();
        } catch (IOException ex) {
            
        }
        
        ArrayList<InfluenceModel> influencerList = Influencer.getInfluencers();
        model = (DefaultTableModel)influencerTableNews.getModel();
        model.setRowCount(0);
        
        for(InfluenceModel im : influencerList) {
            if(InfluencerType.NEWS==im.getType())
                model.addRow(new Object[]{im.getTwitter_account(), im.getFollower_rank(), im.getLink_rank(), im.getInfluence_rank()});
        }
        
        model = (DefaultTableModel)influencerTableSocial.getModel();
        model.setRowCount(0);
        
        for(InfluenceModel im : influencerList) {
            if(InfluencerType.SOCIALMEDIA==im.getType())
                model.addRow(new Object[]{im.getTwitter_account(), im.getLink_rank()});
        }
    }
    
    /**
     * Inserts retweets into retweets table.
     */
    private void insertRetweetsList() {
        
        for(tweetModel tm : tmDM.getStatistics().getRetweets()) {
            retweetsArea.setText(retweetsArea.getText().concat(tm.getMessage() + "\n\n"));
        }
    }
    
    /**
     * Thread class for Drilling down.
     */
    public class DrilldownThread implements Runnable { 

        @Override
        public void run() {
           if( !drillkeyTF.getText().isEmpty()){
//                JTabbedPane tabPane = (JTabbedPane)this.getParent();
                String DDkeywords = drillkeyTF.getText();

                DDTweetCleaner ddTC = new DDTweetCleaner();
                TMDrillModel DDtmDrillModel = ddTC.TMcleanByKeyword(DDkeywords, tmDM);

                TM_DrillDown p = new TM_DrillDown(DDtmDrillModel, tabPane);
                String method = "TM";

                tabPane.add(method + " - LV" + DDtmDrillModel.getLevel() + " - " + DDkeywords, p);
                tabPane.setSelectedComponent(p);
                tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new ButtonTabComponent(tabPane));
                Start.setProgressToComplete();
            }
        }
    
    }
    
    /**
     * Thread class for Drilling down.
     */
    public class DrilldownThread2 implements Runnable { 

        @Override
        public void run() {
           if( !drillkeyTF1.getText().isEmpty()){
//                JTabbedPane tabPane = (JTabbedPane)this.getParent();
                String DDkeywords = drillkeyTF1.getText();

                DDTweetCleaner ddTC = new DDTweetCleaner();
                TMDrillModel DDtmDrillModel = ddTC.TMcleanByKeyword(DDkeywords, tmDM);

                TM_DrillDown p = new TM_DrillDown(DDtmDrillModel, tabPane);
                String method = "TM";

                tabPane.add(method + " - LV" + DDtmDrillModel.getLevel() + " - " + DDkeywords, p);
                tabPane.setSelectedComponent(p);
                tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new ButtonTabComponent(tabPane));
                Start.setProgressToComplete();
            }
        }
    
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        RawData = new javax.swing.JTabbedPane();
        vennDiaTabPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        drillkeyTF = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        drilldownBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        venndiagrampanel = new javax.swing.JPanel();
        rawDataTabPanel = new javax.swing.JPanel();
        drillkeyTF1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        saveBtn3 = new javax.swing.JButton();
        drilldownBtn1 = new javax.swing.JButton();
        topicsTableScrollPane = new javax.swing.JScrollPane();
        topicstable = new javax.swing.JTable();
        timelineTabPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        timelinePanel = new javax.swing.JPanel();
        featureStatTabPanel = new javax.swing.JPanel();
        pieChartPanel = new javax.swing.JPanel();
        retweetsScrollPane = new javax.swing.JScrollPane();
        retweetsArea = new javax.swing.JTextArea();
        influenceIndexTabPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        influencerTableNews = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        influencerTableSocial = new javax.swing.JTable();
        tweetDataTabPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tweetTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setMinimumSize(new java.awt.Dimension(774, 394));
        setPreferredSize(new java.awt.Dimension(774, 394));

        RawData.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        RawData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RawDataMouseClicked(evt);
            }
        });

        vennDiaTabPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("KEYWORD/S FOR DRILLDOWN :");

        drillkeyTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drillkeyTFActionPerformed(evt);
            }
        });

        drilldownBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        drilldownBtn.setText("DRILLDOWN");
        drilldownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drilldownBtnActionPerformed(evt);
            }
        });

        saveBtn.setText("Save...");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        venndiagrampanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout venndiagrampanelLayout = new javax.swing.GroupLayout(venndiagrampanel);
        venndiagrampanel.setLayout(venndiagrampanelLayout);
        venndiagrampanelLayout.setHorizontalGroup(
            venndiagrampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        venndiagrampanelLayout.setVerticalGroup(
            venndiagrampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout vennDiaTabPanelLayout = new javax.swing.GroupLayout(vennDiaTabPanel);
        vennDiaTabPanel.setLayout(vennDiaTabPanelLayout);
        vennDiaTabPanelLayout.setHorizontalGroup(
            vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vennDiaTabPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                        .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(drilldownBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(venndiagrampanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vennDiaTabPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(drillkeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)))
                        .addContainerGap())))
        );
        vennDiaTabPanelLayout.setVerticalGroup(
            vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(drillkeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drilldownBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(venndiagrampanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        RawData.addTab("Venn Diagram", vennDiaTabPanel);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("KEYWORD/S FOR DRILLDOWN :");

        saveBtn3.setText("Save...");

        drilldownBtn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        drilldownBtn1.setText("DRILLDOWN");
        drilldownBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drilldownBtn1ActionPerformed(evt);
            }
        });

        topicsTableScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Topic Modeller Output", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Trebuchet MS", 1, 12), java.awt.Color.black)); // NOI18N

        topicstable.setAutoCreateRowSorter(true);
        topicstable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        topicstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Topic", "Keywords"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        topicstable.setRowHeight(25);
        topicsTableScrollPane.setViewportView(topicstable);
        if (topicstable.getColumnModel().getColumnCount() > 0) {
            topicstable.getColumnModel().getColumn(1).setResizable(false);
            topicstable.getColumnModel().getColumn(1).setPreferredWidth(850);
        }

        javax.swing.GroupLayout rawDataTabPanelLayout = new javax.swing.GroupLayout(rawDataTabPanel);
        rawDataTabPanel.setLayout(rawDataTabPanelLayout);
        rawDataTabPanelLayout.setHorizontalGroup(
            rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rawDataTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drilldownBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(rawDataTabPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drillkeyTF1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 57, Short.MAX_VALUE))
                    .addComponent(topicsTableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        rawDataTabPanelLayout.setVerticalGroup(
            rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rawDataTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(drillkeyTF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drilldownBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topicsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        RawData.addTab("Raw Data", rawDataTabPanel);

        javax.swing.GroupLayout timelinePanelLayout = new javax.swing.GroupLayout(timelinePanel);
        timelinePanel.setLayout(timelinePanelLayout);
        timelinePanelLayout.setHorizontalGroup(
            timelinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        timelinePanelLayout.setVerticalGroup(
            timelinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(timelinePanel);

        javax.swing.GroupLayout timelineTabPanelLayout = new javax.swing.GroupLayout(timelineTabPanel);
        timelineTabPanel.setLayout(timelineTabPanelLayout);
        timelineTabPanelLayout.setHorizontalGroup(
            timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timelineTabPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(timelineTabPanelLayout.createSequentialGroup()
                        .addGap(754, 754, 754)
                        .addComponent(jLabel2)))
                .addGap(9, 9, 9))
        );
        timelineTabPanelLayout.setVerticalGroup(
            timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timelineTabPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(timelineTabPanelLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        RawData.addTab("Timeline", timelineTabPanel);

        pieChartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout pieChartPanelLayout = new javax.swing.GroupLayout(pieChartPanel);
        pieChartPanel.setLayout(pieChartPanelLayout);
        pieChartPanelLayout.setHorizontalGroup(
            pieChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
        );
        pieChartPanelLayout.setVerticalGroup(
            pieChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        retweetsScrollPane.setBorder(null);

        retweetsArea.setEditable(false);
        retweetsArea.setColumns(20);
        retweetsArea.setLineWrap(true);
        retweetsArea.setRows(5);
        retweetsArea.setWrapStyleWord(true);
        retweetsArea.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RETWEETS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Trebuchet MS", 1, 12), java.awt.Color.black)); // NOI18N
        retweetsScrollPane.setViewportView(retweetsArea);

        javax.swing.GroupLayout featureStatTabPanelLayout = new javax.swing.GroupLayout(featureStatTabPanel);
        featureStatTabPanel.setLayout(featureStatTabPanelLayout);
        featureStatTabPanelLayout.setHorizontalGroup(
            featureStatTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featureStatTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pieChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retweetsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );
        featureStatTabPanelLayout.setVerticalGroup(
            featureStatTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(featureStatTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(featureStatTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retweetsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .addComponent(pieChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        RawData.addTab("Feature Statistics", featureStatTabPanel);

        influencerTableNews.setAutoCreateRowSorter(true);
        influencerTableNews.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        influencerTableNews.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Influencer", "Follower Rank", "Link Rank", "Aggregate Rank"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(influencerTableNews);
        if (influencerTableNews.getColumnModel().getColumnCount() > 0) {
            influencerTableNews.getColumnModel().getColumn(0).setPreferredWidth(300);
        }

        influencerTableSocial.setAutoCreateRowSorter(true);
        influencerTableSocial.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        influencerTableSocial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Influencer", "Link Rank"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(influencerTableSocial);

        javax.swing.GroupLayout influenceIndexTabPanelLayout = new javax.swing.GroupLayout(influenceIndexTabPanel);
        influenceIndexTabPanel.setLayout(influenceIndexTabPanelLayout);
        influenceIndexTabPanelLayout.setHorizontalGroup(
            influenceIndexTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(influenceIndexTabPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(influenceIndexTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        influenceIndexTabPanelLayout.setVerticalGroup(
            influenceIndexTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(influenceIndexTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        RawData.addTab("Influencer Index", influenceIndexTabPanel);

        tweetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Date", "Tweet Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tweetTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tweetTable.setIntercellSpacing(new java.awt.Dimension(1, 2));
        jScrollPane3.setViewportView(tweetTable);
        if (tweetTable.getColumnModel().getColumnCount() > 0) {
            tweetTable.getColumnModel().getColumn(0).setPreferredWidth(180);
            tweetTable.getColumnModel().getColumn(1).setPreferredWidth(580);
        }

        javax.swing.GroupLayout tweetDataTabPanelLayout = new javax.swing.GroupLayout(tweetDataTabPanel);
        tweetDataTabPanel.setLayout(tweetDataTabPanelLayout);
        tweetDataTabPanelLayout.setHorizontalGroup(
            tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tweetDataTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tweetDataTabPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addGap(143, 143, 143)))
        );
        tweetDataTabPanelLayout.setVerticalGroup(
            tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tweetDataTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE))
            .addGroup(tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tweetDataTabPanelLayout.createSequentialGroup()
                    .addGap(173, 173, 173)
                    .addComponent(jLabel3)
                    .addGap(196, 196, 196)))
        );

        RawData.addTab("Tweet Data", tweetDataTabPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RawData, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(RawData, javax.swing.GroupLayout.PREFERRED_SIZE, 394, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void drilldownBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtn1ActionPerformed
        new Thread(new DrilldownThread()).start();
    }//GEN-LAST:event_drilldownBtn1ActionPerformed

    private void RawDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RawDataMouseClicked
      
    }//GEN-LAST:event_RawDataMouseClicked

    private void drilldownBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtnActionPerformed
        new Thread(new DrilldownThread()).start();
    }//GEN-LAST:event_drilldownBtnActionPerformed

    private void drillkeyTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drillkeyTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drillkeyTFActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        TM_SelectSave selectsavemenu = new TM_SelectSave(tmDM);
        selectsavemenu.setVisible(true);
    }//GEN-LAST:event_saveBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane RawData;
    private javax.swing.JButton drilldownBtn;
    private javax.swing.JButton drilldownBtn1;
    private javax.swing.JTextField drillkeyTF;
    private javax.swing.JTextField drillkeyTF1;
    private javax.swing.JPanel featureStatTabPanel;
    private javax.swing.JPanel influenceIndexTabPanel;
    private javax.swing.JTable influencerTableNews;
    private javax.swing.JTable influencerTableSocial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pieChartPanel;
    private javax.swing.JPanel rawDataTabPanel;
    private javax.swing.JTextArea retweetsArea;
    private javax.swing.JScrollPane retweetsScrollPane;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton saveBtn3;
    private javax.swing.JPanel timelinePanel;
    private javax.swing.JPanel timelineTabPanel;
    private javax.swing.JScrollPane topicsTableScrollPane;
    private javax.swing.JTable topicstable;
    private javax.swing.JPanel tweetDataTabPanel;
    private javax.swing.JTable tweetTable;
    private javax.swing.JPanel vennDiaTabPanel;
    public static javax.swing.JPanel venndiagrampanel;
    // End of variables declaration//GEN-END:variables

    private Iterable<String> read() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the tmDM
     */
    public TMDrillModel getTmDM() {
        return tmDM;
    }

    /**
     * @param tmDM the tmDM to set
     */
    public void setTmDM(TMDrillModel tmDM) {
        this.tmDM = tmDM;
    }
}
