
package database;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
/**
 *
 * @author Nancy
 */
public class Writer {
    private String path;
    private boolean append_to_text = false;
    
    public Writer(String file_path){
        path = file_path;
    }
    
    public Writer(String file_path, boolean append_value){
        path = file_path;
        append_to_text = append_value;
    }
    
    public void writeToFile(String textLine) throws IOException{
        FileWriter write = new FileWriter(path, append_to_text);
        PrintWriter print_line = new PrintWriter(write);
        print_line.printf("%s" + "%n", textLine);
        print_line.close();
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @param append_to_text the append_to_text to set
     */
    public void setAppend_to_text(boolean append_to_text) {
        this.append_to_text = append_to_text;
    }
}
