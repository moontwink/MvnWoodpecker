/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Visual.FXPieChart;
import Visual.timeline.FXTimeline;
import static Visual.timeline.TM_Timeline.timelineTopics;
import Visual.venn.VennDiagramFX;
import static Visual.venn.VennDiagramWriter.write;
import database.Influencer;

import database.TablesHandler;
import database.tweetHandler;
import java.io.IOException;
import model.tweetModel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import mallet.TopicOutput;
import model.InfluenceModel;
import model.TMDrillModel;
import tfidf.TM_TfidfModel;
import tweets.DDTweetCleaner;

/**
 *
 * @author Nancy
 */
public class TM_DrillDown extends javax.swing.JPanel {
 private static DefaultTableModel model;
 private TMDrillModel tmDM;
 
 String title = null; 
         int i=1;
    /**
     * Creates new form DD_Level1
     */
    public TM_DrillDown(TMDrillModel tmDM) {
        this.tmDM = tmDM;
        
        initComponents();
        
        /* Insert Topics to Table */
        insertTopics();
        
        /* Inserts Tweet List to Table */
        insertTweetsList();
        
        /* Inserts Retweet List to Text Area */
        insertRetweetsList();
        
        /* Inserts Influencer Index to Table */
        insertInfluencerIndex();
        
        /*Creates and Places Venn Diagram in Panel*/
        try {
            VennDiagramFX.VennApplicationFrame(venndiagrampanel, write(tmDM.getTablename()+".html",tmDM));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "There has been an error loading the venn diagram.", "Venn Diagram Construction", JOptionPane.ERROR_MESSAGE);
        }
        
        /*Creates and Places Timeline in Panel*/
        try {
            ArrayList<TopicOutput> timelineInput = new ArrayList<>();
            for(TM_TfidfModel tmtf : tmDM.getTopics()){
                timelineInput.add(tmtf.getTopic());
            }
            FXTimeline.TimelineApplicationFrame(timelinePanel, timelineTopics(tmDM.getTablename(), timelineInput));
        } catch (IOException ex) {
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
                
    }
    
    private void insertTweetsList(){
        model = (DefaultTableModel)tweetTable.getModel();
        model.setRowCount(0);
        
        ArrayList<tweetModel> tweets = tweetHandler.getAllTweets(getTmDM().getTablename());
        
        for(tweetModel tm : tweets) {
            model.addRow(new Object[]{tm.getDate(), tm.getMessage()});
        }
    }
   
    private void insertInfluencerIndex(){
        try {
            Influencer.initializeInfluenceModels();
            Influencer.linksExpander();
        } catch (IOException ex) {
            
        }
        
        ArrayList<InfluenceModel> influencerList = Influencer.getInfluencers();
        model = (DefaultTableModel)influencerTable.getModel();
        model.setRowCount(0);
        
        for(InfluenceModel im : influencerList) {
            model.addRow(new Object[]{im.getTwitter_account(), im.getFollower_rank(), im.getLink_rank(), im.getInfluence_rank()});
        }
    }
    
    private void insertRetweetsList() {
        
        for(tweetModel tm : tmDM.getStatistics().getRetweets()) {
            retweetsArea.setText(retweetsArea.getText().concat(tm.getMessage() + "\n\n"));
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
        closetabBtn = new javax.swing.JButton();
        drilldownBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        viewtweetsBtn = new javax.swing.JButton();
        venndiagrampanel = new javax.swing.JPanel();
        rawDataTabPanel = new javax.swing.JPanel();
        drillkeyTF1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        saveBtn3 = new javax.swing.JButton();
        viewtweetsBtn1 = new javax.swing.JButton();
        closetabBtn3 = new javax.swing.JButton();
        drilldownBtn1 = new javax.swing.JButton();
        topicsTableScrollPane = new javax.swing.JScrollPane();
        topicstable = new javax.swing.JTable();
        timelineTabPanel = new javax.swing.JPanel();
        drillkeyTF2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        viewtweetsBtn2 = new javax.swing.JButton();
        drilldownBtn3 = new javax.swing.JButton();
        saveBtn4 = new javax.swing.JButton();
        closetabBtn4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        timelinePanel = new javax.swing.JPanel();
        featureStatTabPanel = new javax.swing.JPanel();
        pieChartPanel = new javax.swing.JPanel();
        retweetsScrollPane = new javax.swing.JScrollPane();
        retweetsArea = new javax.swing.JTextArea();
        influenceIndexTabPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        influencerTable = new javax.swing.JTable();
        tweetDataTabPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tweetTable = new javax.swing.JTable();
        drillkeyTF3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        closetabBtn1 = new javax.swing.JButton();
        drilldownBtn2 = new javax.swing.JButton();
        saveBtn1 = new javax.swing.JButton();
        viewtweetsBtn3 = new javax.swing.JButton();

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

        closetabBtn.setText("X");
        closetabBtn.setToolTipText("close this tab");
        closetabBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closetabBtnActionPerformed(evt);
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

        viewtweetsBtn.setText("View Tweet Data");
        viewtweetsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewtweetsBtnActionPerformed(evt);
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
                .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, vennDiaTabPanelLayout.createSequentialGroup()
                        .addComponent(venndiagrampanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(92, 751, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, vennDiaTabPanelLayout.createSequentialGroup()
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(drilldownBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(viewtweetsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drillkeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closetabBtn)
                        .addContainerGap())))
        );
        vennDiaTabPanelLayout.setVerticalGroup(
            vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vennDiaTabPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(drillkeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closetabBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(vennDiaTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(drilldownBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewtweetsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(venndiagrampanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        RawData.addTab("Venn Diagram", vennDiaTabPanel);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("KEYWORD/S FOR DRILLDOWN :");

        saveBtn3.setText("Save...");

        viewtweetsBtn1.setText("View Tweet Data");
        viewtweetsBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewtweetsBtn1ActionPerformed(evt);
            }
        });

        closetabBtn3.setText("X");
        closetabBtn3.setToolTipText("close this tab");
        closetabBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closetabBtn3ActionPerformed(evt);
            }
        });

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
                    .addGroup(rawDataTabPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drillkeyTF1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closetabBtn3))
                    .addGroup(rawDataTabPanelLayout.createSequentialGroup()
                        .addComponent(saveBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(drilldownBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewtweetsBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(topicsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
                .addContainerGap())
        );
        rawDataTabPanelLayout.setVerticalGroup(
            rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rawDataTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(drillkeyTF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(closetabBtn3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rawDataTabPanelLayout.createSequentialGroup()
                        .addGroup(rawDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(drilldownBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addComponent(viewtweetsBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(topicsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        RawData.addTab("Raw Data", rawDataTabPanel);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("KEYWORD/S FOR DRILLDOWN :");

        viewtweetsBtn2.setText("View Tweet Data");
        viewtweetsBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewtweetsBtn2ActionPerformed(evt);
            }
        });

        drilldownBtn3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        drilldownBtn3.setText("DRILLDOWN");
        drilldownBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drilldownBtn3ActionPerformed(evt);
            }
        });

        saveBtn4.setText("Save...");

        closetabBtn4.setText("X");
        closetabBtn4.setToolTipText("close this tab");
        closetabBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closetabBtn4ActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(timelineTabPanelLayout.createSequentialGroup()
                            .addComponent(saveBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(drilldownBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(viewtweetsBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(timelineTabPanelLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(drillkeyTF2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(closetabBtn4)))
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        timelineTabPanelLayout.setVerticalGroup(
            timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timelineTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(drillkeyTF2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closetabBtn4))
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timelineTabPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(48, 48, 48))
                    .addGroup(timelineTabPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)))
                .addGroup(timelineTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewtweetsBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn4, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(drilldownBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        influencerTable.setAutoCreateRowSorter(true);
        influencerTable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        influencerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        jScrollPane4.setViewportView(influencerTable);

        javax.swing.GroupLayout influenceIndexTabPanelLayout = new javax.swing.GroupLayout(influenceIndexTabPanel);
        influenceIndexTabPanel.setLayout(influenceIndexTabPanelLayout);
        influenceIndexTabPanelLayout.setHorizontalGroup(
            influenceIndexTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(influenceIndexTabPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        influenceIndexTabPanelLayout.setVerticalGroup(
            influenceIndexTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(influenceIndexTabPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        RawData.addTab("Influencer Index", influenceIndexTabPanel);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("KEYWORD/S FOR DRILLDOWN :");

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

        drillkeyTF3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drillkeyTF3ActionPerformed(evt);
            }
        });

        closetabBtn1.setText("X");
        closetabBtn1.setToolTipText("close this tab");
        closetabBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closetabBtn1ActionPerformed(evt);
            }
        });

        drilldownBtn2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        drilldownBtn2.setText("DRILLDOWN");
        drilldownBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drilldownBtn2ActionPerformed(evt);
            }
        });

        saveBtn1.setText("Save...");

        viewtweetsBtn3.setText("View Tweet Data");
        viewtweetsBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewtweetsBtn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tweetDataTabPanelLayout = new javax.swing.GroupLayout(tweetDataTabPanel);
        tweetDataTabPanel.setLayout(tweetDataTabPanelLayout);
        tweetDataTabPanelLayout.setHorizontalGroup(
            tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tweetDataTabPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(drillkeyTF3, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closetabBtn1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tweetDataTabPanelLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(saveBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(drilldownBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewtweetsBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
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
                .addGroup(tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(drillkeyTF3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closetabBtn1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(tweetDataTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(drilldownBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(viewtweetsBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
            .addComponent(RawData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 394, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void viewtweetsBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewtweetsBtn1ActionPerformed
        viewTweets vt = new viewTweets();
        vt.setVisible(true);
    }//GEN-LAST:event_viewtweetsBtn1ActionPerformed

    private void closetabBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closetabBtn3ActionPerformed
        TablesHandler.dropTable(tmDM.getTablename());
        this.getParent().remove(this);
    }//GEN-LAST:event_closetabBtn3ActionPerformed

    private void drilldownBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtn1ActionPerformed
        if( !drillkeyTF1.getText().isEmpty()){
            JTabbedPane tabPane = (JTabbedPane)this.getParent();
            String DDkeywords = drillkeyTF1.getText();
          
            DDTweetCleaner ddTC = new DDTweetCleaner();
            TMDrillModel DDtmDrillModel = ddTC.TMcleanByKeyword(DDkeywords, tmDM);
            
            TM_DrillDown p = new TM_DrillDown(DDtmDrillModel);
            String method = "TM";
                
            tabPane.add("LV" + DDtmDrillModel.getLevel() + " - " + DDkeywords + " - " + method, p);
            tabPane.setSelectedComponent(p);
        }
    }//GEN-LAST:event_drilldownBtn1ActionPerformed

    private void viewtweetsBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewtweetsBtn2ActionPerformed
        viewTweets vt = new viewTweets();
        vt.setVisible(true);
    }//GEN-LAST:event_viewtweetsBtn2ActionPerformed

    private void drilldownBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtn3ActionPerformed
     
    }//GEN-LAST:event_drilldownBtn3ActionPerformed

    private void closetabBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closetabBtn4ActionPerformed
        this.getParent().remove(this);
    }//GEN-LAST:event_closetabBtn4ActionPerformed

    private void RawDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RawDataMouseClicked
      
        /*  JTabbedPane j = (JTabbedPane)this.getParent();
        String title = j.getTitleAt(j.getSelectedIndex()).substring(j.getTitleAt(j.getSelectedIndex()).length()-3, j.getTitleAt(j.getSelectedIndex()).length());
        System.out.println(title);
        
        if(title.contains("LM")){   //topic modeler raw output
            Reader read = new Reader("src\\ngramuaaptweets.txt");
            List<String> lines = read.read();
            String rawdata = "";
            for(String s : lines){
                rawdata = rawdata.concat(s+"\n");
                System.out.println(s);
            }
            rawDataFld.setText(rawdata);
        }else{  //language modeler raw output
            Reader read = new Reader("src\\stm uaap.txt");
            List<String> lines = read.read();
            String rawdata = "";
            for(String s : lines){
                rawdata = rawdata.concat(s+"\n");
                System.out.println(s);
            }
            rawDataFld.setText(rawdata);
        }*/
    }//GEN-LAST:event_RawDataMouseClicked

    private void drillkeyTF3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drillkeyTF3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drillkeyTF3ActionPerformed

    private void closetabBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closetabBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_closetabBtn1ActionPerformed

    private void drilldownBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drilldownBtn2ActionPerformed

    private void viewtweetsBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewtweetsBtn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewtweetsBtn3ActionPerformed

    private void viewtweetsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewtweetsBtnActionPerformed
        viewTweets vt = new viewTweets();
        vt.setVisible(true);
    }//GEN-LAST:event_viewtweetsBtnActionPerformed

    private void drilldownBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldownBtnActionPerformed
        if( !drillkeyTF.getText().isEmpty()){
            JTabbedPane tabPane = (JTabbedPane)this.getParent();
            String DDkeywords = drillkeyTF.getText();

            DDTweetCleaner ddTC = new DDTweetCleaner();
            TMDrillModel DDtmDrillModel = ddTC.TMcleanByKeyword(DDkeywords, tmDM);

            TM_DrillDown p = new TM_DrillDown(DDtmDrillModel);
            String method = "TM";

            tabPane.add("LV" + DDtmDrillModel.getLevel() + " - " + DDkeywords + " - " + method, p);
            tabPane.setSelectedComponent(p);
        }
    }//GEN-LAST:event_drilldownBtnActionPerformed

    private void closetabBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closetabBtnActionPerformed
        TablesHandler.dropTable(tmDM.getTablename());
        this.getParent().remove(this);
    }//GEN-LAST:event_closetabBtnActionPerformed

    private void drillkeyTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drillkeyTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drillkeyTFActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane RawData;
    private javax.swing.JButton closetabBtn;
    private javax.swing.JButton closetabBtn1;
    private javax.swing.JButton closetabBtn3;
    private javax.swing.JButton closetabBtn4;
    private javax.swing.JButton drilldownBtn;
    private javax.swing.JButton drilldownBtn1;
    private javax.swing.JButton drilldownBtn2;
    private javax.swing.JButton drilldownBtn3;
    private javax.swing.JTextField drillkeyTF;
    private javax.swing.JTextField drillkeyTF1;
    private javax.swing.JTextField drillkeyTF2;
    private javax.swing.JTextField drillkeyTF3;
    private javax.swing.JPanel featureStatTabPanel;
    private javax.swing.JPanel influenceIndexTabPanel;
    private javax.swing.JTable influencerTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pieChartPanel;
    private javax.swing.JPanel rawDataTabPanel;
    private javax.swing.JTextArea retweetsArea;
    private javax.swing.JScrollPane retweetsScrollPane;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton saveBtn1;
    private javax.swing.JButton saveBtn3;
    private javax.swing.JButton saveBtn4;
    private javax.swing.JPanel timelinePanel;
    private javax.swing.JPanel timelineTabPanel;
    private javax.swing.JScrollPane topicsTableScrollPane;
    private javax.swing.JTable topicstable;
    private javax.swing.JPanel tweetDataTabPanel;
    private javax.swing.JTable tweetTable;
    private javax.swing.JPanel vennDiaTabPanel;
    public static javax.swing.JPanel venndiagrampanel;
    private javax.swing.JButton viewtweetsBtn;
    private javax.swing.JButton viewtweetsBtn1;
    private javax.swing.JButton viewtweetsBtn2;
    private javax.swing.JButton viewtweetsBtn3;
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
