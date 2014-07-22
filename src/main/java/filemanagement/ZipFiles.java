
package filemanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

/**
 *
 * @author Nancy
 */
public class ZipFiles {

    /**
     * This method writes the files into a zip file
     * @param files
     */
    public static void writeZipFiles(List<String> files){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("src/"));

        int retrieval = chooser.showSaveDialog(null);
        if(retrieval == JFileChooser.APPROVE_OPTION){
            try{
                FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile()+".zip");
                ZipOutputStream zos = new ZipOutputStream(fos);

                for(String fileName : files) {
                    addToZipFile(fileName, zos);
                }
                zos.close();
                fos.close();

            }catch(Exception ex){
//                ex.printStackTrace();
            }
        }
    }

    /**
     * This method adds the fileName into the zos zip file.
     * @param fileName 
     * @param zos
     */
    public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

            System.out.println("Writing '" + fileName + "' to zip file");

            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
            }

            zos.closeEntry();
            fis.close();
    }
}
