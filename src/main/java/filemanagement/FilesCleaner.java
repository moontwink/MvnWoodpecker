
package filemanagement;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Nancy
 */
public class FilesCleaner {
    private static final String vennfolderpath = "src/visual/d3-venndiagram/";
    private static final String wordcloudfolderpath = "src/visual/d3-wordcloud/";
    private static final String timelinefolderpath = "src/visual/highcharts-timeline/";
    private static final String LM_datafolderpath = "src/data/languagemodeller/";
    private static final String TM_datafolderpath = "src/data/topicmodeller/";
    
    /**
     * Cleans all visual files found in the visual directory.
     */
    public static void cleanAllVisualFiles(){
        System.out.println("---------- Clean Visual Files Operation ----------");
        System.out.println("Operation: Clean all HTML files");
        System.out.println("Status: Beginning to clean all HTML files");
        cleanDirHTMLFiles(vennfolderpath);
        cleanDirHTMLFiles(wordcloudfolderpath);
        cleanDirHTMLFiles(timelinefolderpath);
        System.out.println("---------- End of Visual Files Operation ----------\n\n");
    }
    
    /**
     * This method cleans all HTML files from the path.
     * @param path
     */
    private static void cleanDirHTMLFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        
        for(File file : listOfFiles) {
            if(file.isFile()){
//                System.out.print("file name: " + file.getName());
                String ext = FilenameUtils.getExtension(file.getName());
//                System.out.print(" == ext: " + ext + "\n");
                if(ext.equals("html")){
                    file.delete();
//                    System.out.println("file deleted!");
                }
            }
        }
    }
    
    /**
     * This method cleans all TXT files in the data directory
     */
    public static void cleanDataFiles(){
        System.out.println("---------- Clean Data Files Operation ----------");
        System.out.println("Operation: Clean all TXT files");
        System.out.println("Status: Beginning to clean all TXT files");
        
        File folder = new File(LM_datafolderpath);
        File[] listOfFiles = folder.listFiles();
        
        for(File file : listOfFiles) {
            if(file.isFile()){
                String ext = FilenameUtils.getExtension(file.getName());
                if(ext.equals("txt")){
                    file.delete();
                }
            }
        }
        
        folder = new File(TM_datafolderpath);
        listOfFiles = folder.listFiles();
        
        for(File file : listOfFiles) {
            if(file.isFile()){
                String ext = FilenameUtils.getExtension(file.getName());
                if(ext.equals("txt")){
                    file.delete();
                }
            }
        }
        System.out.println("---------- End of Data Files Operation ----------\n\n");
    }
}
