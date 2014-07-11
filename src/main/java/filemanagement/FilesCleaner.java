
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
    private static final String timelinefolderpath = "src/visual/jsfiddle-timeline/";
    
    public static void cleanAllVisualFiles(){
        cleanDirHTMLFiles(vennfolderpath);
        cleanDirHTMLFiles(wordcloudfolderpath);
        cleanDirHTMLFiles(timelinefolderpath);
    }
    
    private static void cleanDirHTMLFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        
        for(File file : listOfFiles) {
            if(file.isFile()){
                System.out.print("file name: " + file.getName());
                String ext = FilenameUtils.getExtension(file.getName());
                System.out.print(" == ext: " + ext + "\n");
                if(ext.equals("html")){
                    file.delete();
                    System.out.println("file deleted!");
                }
            }
        }
    }
}
