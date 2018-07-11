package demo.json.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Includes basic file operations.
 * 
 * @author resul.avan@gmail.com
 * 
 */
public class FileUtility {

    /**
     * Adds the text in the file. If the file does not exist, a new file is created.
     *
     * @param file        {@link File }
     * @param arg0        {@link String} as text to write
     * @param append2File {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine  {@link boolean } to add new line after record
     * @throws IOException
     */
    public static void write(File file, String arg0, boolean append2File, boolean putNewLine)
            throws IOException {

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new java.io.FileWriter(file, append2File));

            writer.write(arg0);

            if (putNewLine) {
                writer.newLine();
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    public static List<File> getFolderStructure(String folderPath) {
        return getFolderStructure(new File(folderPath));
    }

    public static List<File> getFolderStructure(File folder) {
        List<File> fileList = new ArrayList<File>();

        File[] files = folder.listFiles();
        for (File _file : files) {
            if (_file.isFile()) {
                continue;
            }
            fileList.add(_file);
            fileList.addAll(getFolderStructure(_file.getAbsolutePath()));
        }
        return fileList;
    }
}
