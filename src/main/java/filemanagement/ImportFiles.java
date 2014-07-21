
package filemanagement;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import database.ImportHandler;
import java.io.FileReader;
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
    
    public static void importCSVFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter ext = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.addChoosableFileFilter(ext);
        chooser.setFileFilter(ext);

        int retrieval = chooser.showOpenDialog(null);
        if(retrieval == JFileChooser.APPROVE_OPTION){
            try{
                System.out.println("---------- Initializing Database Operation ----------");
                System.out.println("Operation: Import CSV file into database");
                System.out.println("Status: Reading CSV file");
                CSVReader reader = new CSVReader(new FileReader(chooser.getSelectedFile()));

                ColumnPositionMappingStrategy<tweetModel> strat = new ColumnPositionMappingStrategy<>();
                strat.setType(tweetModel.class);
                String[] columns = new String[] {"statusId", "username", "message", "date", "latitude", "longitude"}; // the fields to bind in JavaBean
                strat.setColumnMapping(columns);

                CsvToBean<tweetModel> csv = new CsvToBean<>();
                List<tweetModel> list = csv.parse(strat, reader);
                
//                for(tweetModel t : list){
//                    System.out.println(t.getStatusId());
//                    System.out.println("\t" + t.getUsername());
//                    System.out.println("\t" + t.getMessage());
//                    System.out.println("\t" + t.getDate());
//                    System.out.println("\t" + t.getLatitude());
//                    System.out.println("\t" + t.getLongitude());
//                }
                reader.close();
                
                System.out.println("Status: Storing CSV entries into database");
                ImportHandler importer = new ImportHandler();
                importer.importCSVData(list);
                System.out.println("Status: CSV entries successfully stored into database");
                System.out.println("---------- End of Database Operation ----------\n\n");
                
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "There's seems to be an error in your CSV File caused by any of the following: \n"
                        + "(1) You are not following the REQUIRED format \n"
                        + "(2) You are MISSING some data \n"
                        + "(3) You are trying to import a CORRUPTED file", "Import CSV Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    }
        public static void main(String[] args) {

            String sb = "TEST CONTENT";
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter ext = new FileNameExtensionFilter("CSV Files", "csv");
            chooser.addChoosableFileFilter(ext);
            chooser.setFileFilter(ext);

            int retrieval = chooser.showOpenDialog(null);
            if(retrieval == JFileChooser.APPROVE_OPTION){
                try{

                    CSVReader reader = new CSVReader(new FileReader(chooser.getSelectedFile()));

                    ColumnPositionMappingStrategy<tweetModel> strat = new ColumnPositionMappingStrategy<>();
                    strat.setType(tweetModel.class);
                    String[] columns = new String[] {"statusId", "username", "message", "date", "latitude", "longitude"}; // the fields to bind do in your JavaBean
                    strat.setColumnMapping(columns);

                    CsvToBean<tweetModel> csv = new CsvToBean<>();
                    List<tweetModel> list = csv.parse(strat, reader);


                    for(tweetModel t : list){
                        System.out.println(t.getStatusId());
                        System.out.println("\t" + t.getUsername());
                        System.out.println("\t" + t.getMessage());
                        System.out.println("\t" + t.getDate());
                        System.out.println("\t" + t.getLatitude());
                        System.out.println("\t" + t.getLongitude());
                    }

                    reader.close();
                    
                    ImportHandler importer = new ImportHandler();
                    importer.importCSVData(list);

                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "There's seems to be an error in your CSV File caused by any of the following: \n"
                        + "(1) You are not following the REQUIRED format \n"
                        + "(2) You are MISSING some data \n"
                        + "(3) You are trying to import a CORRUPTED file", "Import CSV Error", JOptionPane.ERROR_MESSAGE);
                }
            }
  
	}
}
