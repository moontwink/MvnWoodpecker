
package gui;

import filemanagement.ZipFiles;
import filemanagement.FileDataWriter;
import java.util.ArrayList;
import java.util.List;
import model.LMDrillModel;

/**
 *
 * @author Nancy
 */
public class LM_SelectSave extends javax.swing.JFrame {
    private LMDrillModel lmDM;
    private static String wordcloudUrl = "src/visual/d3-wordcloud/";
    private static String timelineUrl = "src/visual/highcharts-timeline/";
    
    /**
     * Creates new form selectSave
     */
    public LM_SelectSave(LMDrillModel lmDM) {
        this.lmDM = lmDM;
        initComponents();
        visualCB.setSelected(true);
        rawDataCB.setSelected(true);
        timelineCB.setSelected(true);
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
        visualCB = new javax.swing.JCheckBox();
        rawDataCB = new javax.swing.JCheckBox();
        timelineCB = new javax.swing.JCheckBox();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Data to Save", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Trebuchet MS", 1, 12), java.awt.Color.black)); // NOI18N

        visualCB.setText("Word Cloud");

        rawDataCB.setText("Raw Data");

        timelineCB.setText("Timeline");

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(visualCB)
                    .addComponent(rawDataCB, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timelineCB, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(38, 38, 38)
                .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(visualCB)
                        .addGap(18, 18, 18)
                        .addComponent(rawDataCB)
                        .addGap(18, 18, 18)
                        .addComponent(timelineCB)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        List<String> filesToSave = new ArrayList<>();
        if(visualCB.isSelected()){
            filesToSave.add(wordcloudUrl+"wordcloud-"+lmDM.getTablename()+".html");
            filesToSave.add(wordcloudUrl+"d3.js");
            filesToSave.add(wordcloudUrl+"d3.layout.cloud.js");
        }
        if(rawDataCB.isSelected()){
            filesToSave.add(FileDataWriter.writeLMToplist(lmDM.getTablename(), lmDM.getTopList()));
        }
        if(timelineCB.isSelected()){
            filesToSave.add(timelineUrl+"timeline-"+lmDM.getTablename()+".html");
            filesToSave.add(timelineUrl+"exporting.js");
            filesToSave.add(timelineUrl+"highcharts.js");
            filesToSave.add(timelineUrl+"jquery-1.8.0.min.js");
        }
        
        ZipFiles.writeZipFiles(filesToSave);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_saveBtnActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TM_SelectSave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TM_SelectSave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TM_SelectSave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TM_SelectSave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TM_SelectSave(new TMDrillModel()).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox rawDataCB;
    private javax.swing.JButton saveBtn;
    private javax.swing.JCheckBox timelineCB;
    private javax.swing.JCheckBox visualCB;
    // End of variables declaration//GEN-END:variables
}
