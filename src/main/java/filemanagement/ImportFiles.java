
package filemanagement;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import database.ImportHandler;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.tweetModel;

/**
 *
 * @author Nancy
 */
public class ImportFiles {
    
    /**
     * This method import a CSV file selected from the JFileChooser.
     * @return String
     */
    public static String importCSVFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter ext = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.addChoosableFileFilter(ext);
        chooser.setFileFilter(ext);
        String tablename = "";

        int retrieval = chooser.showOpenDialog(null);
        if(retrieval == JFileChooser.APPROVE_OPTION){
            try{
                System.out.println("---------- Initializing Database Operation ----------");
                System.out.println("Operation: Import CSV file into database");
                System.out.println("Status: Reading CSV file");
                
                int option = JOptionPane.showConfirmDialog(chooser, "Would you like to skip the first line?", "CSV Import Question", JOptionPane.YES_NO_OPTION);
                CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile()), "UTF-8"));; 
                
                if(option == 0) { //FIRST LINE IS SKIPPED
                    reader = new CSVReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile()), "UTF-8"), ',', '"', 1);
                }
                
                String filename = chooser.getSelectedFile().getName();

                ColumnPositionMappingStrategy<tweetModel> strat = new ColumnPositionMappingStrategy<>();
                strat.setType(tweetModel.class);
                String[] columns = new String[] {"statusId", "username", "message", "date", "latitude", "longitude"}; // the fields to bind in JavaBean
                strat.setColumnMapping(columns);

                CsvToBean<tweetModel> csv = new CsvToBean<>();
                List<tweetModel> list = csv.parse(strat, reader);
                
                reader.close();
                
                System.out.println("Status: Storing CSV entries into database");
                ImportHandler importer = new ImportHandler();
                tablename = importer.importCSVData(filename, list);
                System.out.println("Status: CSV entries successfully stored into database");
                System.out.println("---------- End of Database Operation ----------\n\n");
                
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "There's seems to be an error in your CSV File caused by any of the following: \n"
                        + "(1) You are not following the REQUIRED format \n"
                        + "(2) You are MISSING some data \n"
                        + "(3) You are trying to import a CORRUPTED file \n\n"
                        + "Error found: " + ex.getCause().getMessage(), "Import CSV Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return tablename;
    }
}
