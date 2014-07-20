
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
    public static void main(String[] args) {

                String sb = "TEST CONTENT";
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("src/"));
                
//		try {
                int retrieval = chooser.showSaveDialog(null);
                if(retrieval == JFileChooser.APPROVE_OPTION){
                    try{
                        FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile()+".zip");
                        ZipOutputStream zos = new ZipOutputStream(fos);

                        String file1Name = "src/malletfile.txt";
                        addToZipFile(file1Name, zos);
                        zos.close();
                        fos.close();

                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                        
//			FileOutputStream fos = new FileOutputStream("src/atest.zip");
//			ZipOutputStream zos = new ZipOutputStream(fos);
//
//			String file1Name = "src/malletfile.txt";
//			String file2Name = "file2.txt";
//			String file3Name = "folder/file3.txt";
//			String file4Name = "folder/file4.txt";
//			String file5Name = "f1/f2/f3/file5.txt";

//			addToZipFile(file1Name, zos);
//			addToZipFile(file2Name, zos);
//			addToZipFile(file3Name, zos);
//			addToZipFile(file4Name, zos);
//			addToZipFile(file5Name, zos);

//			zos.close();
//			fos.close();

//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

        public static void writeZipFiles(List<String> files){
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("src/"));
                
            int retrieval = chooser.showSaveDialog(null);
            if(retrieval == JFileChooser.APPROVE_OPTION){
                try{
                    FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile()+".zip");
                    ZipOutputStream zos = new ZipOutputStream(fos);

//                    String file1Name = "src/malletfile.txt";
                    for(String fileName : files) {
                        addToZipFile(fileName, zos);
                    }
                    zos.close();
                    fos.close();

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    
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
