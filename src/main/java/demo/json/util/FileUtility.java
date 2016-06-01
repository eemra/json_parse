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
     * @param file
     *            {@link File }
     * @param arg0
     *            {@link String} as text to write
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine
     *            {@link boolean } to add new line after record
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

    /**
     * Adds the text in the file. If the file does not exist, a new file is created.
     * 
     * @param file
     *            {@link String } as file full name
     * @param arg0
     *            {@link String} as text to write
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine
     *            {@link boolean } to add new line after record
     * @throws IOException
     */
    public static void write(String file, String arg0, boolean append2File, boolean putNewLine)
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

    /**
     * Adds the text in the file. If the file does not exist, a new file is created.
     * 
     * @param file
     *            {@link String } as file full name
     * @param arg0
     *            {@link String} as text to write
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine
     *            {@link boolean } to add new line after record
     * @param charSet
     *            {@link String } as character set to read
     * @throws IOException
     */
    public static void write(String file, String arg0, boolean append2File, boolean putNewLine,
                             String charSet) throws IOException {

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(file, append2File);

            fos.write(arg0.getBytes(charSet));

            if (putNewLine) {
                fos.write(System.getProperty("line.separator").getBytes(charSet));
            }

            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    /**
     * Adds the content of the list in the file. If the file does not exist, a new file is created.
     * 
     * @param file
     *            {@link File }
     * @param lineList
     *            {@link List} includes texts to write in the file
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @throws IOException
     */
    public static void write(File file, List<String> lineList, boolean append2File)
            throws IOException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new java.io.FileWriter(file, append2File));

            for (String line : lineList) {
                writer.write(line);
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

    /**
     * Adds the content of the list in the file. If the file does not exist, a new file is created.
     * 
     * @param file
     *            {@link String } as file full name
     * @param lineList
     *            {@link List} includes texts to write in the file
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @throws IOException
     * @throws InterruptedException
     */
    public static void write(String file, List<String> lineList, boolean append2File)
            throws IOException, InterruptedException {

        BufferedWriter writer = null;
        try {

            File _file = new File(file);

            writer = new BufferedWriter(new java.io.FileWriter(_file, append2File));

            long count = 0;
            for (String line : lineList) {

                writer.write(line);
                writer.newLine();
                writer.flush();

                if (++count % 50000 == 0)
                    Thread.sleep(1);
            }

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

    // /**
    // * Adds the content of the list in the file. If the file does not exist, a new file is created.
    // *
    // * @param file
    // * {@link String } as file full name
    // * @param lineList
    // * {@link List} includes texts to write in the file
    // * @param append2File
    // * {@link boolean } if file exist, true: append to file, false: create new file
    // * @param putNewLine
    // * {@link boolean } to add new line after record
    // * @throws IOException
    // * @throws InterruptedException
    // */
    // public static void write(String file, TextCompiser textCompiser, boolean append2File, boolean putNewLine) throws IOException,
    // InterruptedException {
    //
    // BufferedWriter writer = null;
    // try {
    //
    // textCompiser.convert2TextList();
    // List<String> lineList = textCompiser.getStringList();
    //
    // File _file = new File(file);
    //
    // writer = new BufferedWriter(new java.io.FileWriter(_file, append2File));
    //
    // long count = 0;
    // for (String line : lineList) {
    //
    // writer.write(line);
    // writer.newLine();
    // writer.flush();
    //
    // if (++count % 50000 == 0)
    // Thread.sleep(1);
    // }
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // throw e;
    // } finally {
    // if (writer != null) {
    // try {
    // writer.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // throw e;
    // }
    // }
    // }
    //
    // }

    /**
     * Adds the content of the list in the file. If the file does not exist, a new file is created.
     * 
     * @param file
     *            {@link File } as file to write
     * @param charSet
     *            {@link String } as character set to write
     * @param lineList
     *            {@link List} includes texts to write in the file
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @throws IOException
     */
    public static void write(File file, List<String> lineList, String charSet, boolean append2File)
            throws IOException {

        FileOutputStream fos = null;
        try {

            byte[] newLine = System.getProperty("line.separator").getBytes(charSet);

            fos = new FileOutputStream(file, append2File);

            for (int i = 0; i < lineList.size(); i++) {

                if (i != 0) {
                    fos.write(newLine);
                }
                fos.write(lineList.get(i).getBytes(charSet));
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    /**
     * Adds the text in a file under the directory. If the directory does not exist, it is created. If the file does not exist, a new file
     * is created.
     * 
     * @param directory
     *            {@link String } as directory full path
     * @param fileName
     *            {@link String } as file name
     * @param arg0
     *            {@link String } as text to write
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine
     *            {@link boolean } to add new line after record
     * @throws IOException
     */
    public static void write(String directory, String fileName, String arg0, boolean append2File,
                             boolean putNewLine) throws IOException {

        BufferedWriter writer = null;
        try {

            createFolder(directory);

            File file = new File(directory + System.getProperty("file.separator") + fileName);

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

    /**
     * Adds the text in a file under the directory. If the directory does not exist, it is created. If the file does not exist, a new file
     * is created.
     * 
     * @param directory
     *            {@link String } as directory full path
     * @param fileName
     *            {@link String } as file name
     * @param arg0
     *            {@link String } as text to write
     * @param charSet
     *            {@link String } as character set to write
     * @param append2File
     *            {@link boolean } if file exist, true: append to file, false: create new file
     * @param putNewLine
     *            {@link boolean } to add new line after record
     * @throws IOException
     */
    public static void write(String directory, String fileName, String arg0, String charSet,
                             boolean append2File, boolean putNewLine) throws IOException {

        FileOutputStream fos = null;
        try {

            createFolder(directory);

            File file = new File(directory + System.getProperty("file.separator") + fileName);

            fos = new FileOutputStream(file, append2File);

            fos.write(arg0.getBytes(charSet));
            if (putNewLine) {
                fos.write(System.getProperty("line.separator").getBytes(charSet));
            }

            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file name
     * @return {@link List } includes line texts
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> read(String fileName) throws IOException, InterruptedException {

        List<String> listValues = new ArrayList<String>();

        BufferedReader buffer = null;

        try {

            buffer = new BufferedReader(new FileReader(fileName));

            String line = buffer.readLine();

            long count = 0;

            while (line != null) {

                listValues.add(line.trim());
                line = buffer.readLine();

                if (++count % 10000 == 0) {
                    Thread.sleep(1);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }

        }

        return listValues;
    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file full name
     * @param charSet
     *            {@link String } as character set to read
     * @return {@link List } includes line texts
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> read(String fileName, String charSet) throws IOException,
            InterruptedException {

        List<String> listValues = new ArrayList<String>();

        BufferedReader buffer = null;

        try {

            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                              charSet));

            String line = buffer.readLine();

            long count = 0;

            while (line != null) {

                listValues.add(line.trim());
                line = buffer.readLine();

                if (++count % 100000 == 0) {
                    Thread.sleep(1);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }

        }

        return listValues;
    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file full name
     * @return {@link StringBuilder } includes file contents
     * @throws InterruptedException
     * @throws IOException
     */
    public static StringBuilder readStringBuilder(String fileName) throws IOException,
            InterruptedException {

        StringBuilder sb = new StringBuilder();

        BufferedReader buffer = null;
        try {

            buffer = new BufferedReader(new FileReader(fileName));

            String line = buffer.readLine();

            long count = 0;

            while (line != null) {

                sb.append(line.trim()).append("\n");
                line = buffer.readLine();

                if (++count % 100000 == 0) {
                    Thread.sleep(1);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }
        }

        return sb;
    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file full name
     * @param charSet
     *            {@link String } as character set to read
     * @return {@link StringBuilder } includes file contents
     * @throws InterruptedException
     * @throws IOException
     */
    public static StringBuilder readStringBuilder(String fileName, String charSet)
            throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder();

        BufferedReader buffer = null;
        try {

            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                              charSet));

            String line = buffer.readLine();

            long count = 0;

            while (line != null) {

                sb.append(line.trim()).append("\n");
                line = buffer.readLine();

                if (++count % 100000 == 0) {
                    Thread.sleep(1);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }
        }

        return sb;
    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file name
     * @param startIndex
     *            {@link long } as start index in the file
     * @param readLength
     *            {@link long } as line count to read
     * @return {@link List } includes partition line texts
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> read(String fileName, long startIndex, long readLength)
            throws IOException, InterruptedException {

        List<String> listValues = new ArrayList<String>();

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(fileName));

            String line = buffer.readLine();
            long count = 0;

            while (line != null) {

                line = buffer.readLine();

                /*
                 * Go to start index
                 */
                if (count < startIndex) {
                    continue;
                }

                /*
                 * Break if it is reached to read length
                 */
                if (count > (startIndex + readLength - 1)) {
                    break;
                }

                /*
                 * Store in a list the lines
                 */
                listValues.add(line.trim());

                ++count;

                if (count % 10000 == 0)
                    Thread.sleep(1);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }
        }

        return listValues;
    }

    /**
     * Read the file
     * 
     * @param fileName
     *            {@link String } as file full name
     * @param charSet
     *            {@link String } as character set to read
     * @param startIndex
     *            {@link long } as start index in the file
     * @param readLength
     *            {@link long } as line count to read
     * @return {@link List } includes partition line texts
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<String> read(String fileName, String charSet, long startIndex,
                                    long readLength) throws IOException, InterruptedException {

        List<String> listValues = new ArrayList<String>();

        BufferedReader buffer = null;
        try {

            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                                                              charSet));

            String line = buffer.readLine();
            long count = 0;

            while (line != null) {

                line = buffer.readLine();

                /*
                 * Go to start index
                 */
                if (count < startIndex) {
                    continue;
                }

                /*
                 * Break if it is reached to read length
                 */
                if (count > (startIndex + readLength - 1)) {
                    break;
                }

                /*
                 * Store in a list the lines
                 */
                listValues.add(line.trim());

                ++count;

                if (count % 10000 == 0)
                    Thread.sleep(1);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }
        }

        return listValues;
    }

    /**
     * retrieve line count in the file
     * 
     * @param fileName
     *            {@link String } as file name
     * @return {@link long } as line count
     * @throws IOException
     * @throws InterruptedException
     * @throws FileNotFoundException
     */
    public static long getLineCount(String fileName) throws IOException {

        long count = 0;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(fileName));

            while (buffer.readLine() != null) {
                count++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {

            if (buffer != null) {
                buffer.close();
            }
        }
        return count;
    }

    /**
     * creates a folder, if the folder does not exist
     * 
     * @param folderName
     *            {@link String } as folder name
     */
    public static boolean createFolder(String folderName) {
        File folder = new File(folderName);

        if (!folder.exists()) {
            return folder.mkdir();
        }
        return false;
    }

    /**
     * Creates a temporary file
     * 
     * @return {@link File } as temporary file
     * @throws IOException
     */
    public static File createTempFile() throws IOException {
        File f = File.createTempFile("tapFile", ".tmp");
        return f;
    }

    /**
     * searches a file under a folder. Search operation is filter.
     * 
     * @param folder
     *            {@link String } as folder name
     * @param filter
     *            {@link FilenameFilter } as filter to search
     * @return {@link File} array includes matched files. If any file does not exist with the prefix, it returns null
     */
    public static File[] search(String folder, FilenameFilter filter) {
        return new File(folder).listFiles(filter);
    }

    public static List<File> getFileStructure(String folderPath) {
        return getFileStructure(new File(folderPath));
    }

    public static List<File> getFileStructure(File folder) {
        List<File> fileList = new ArrayList<File>();

        File[] files = folder.listFiles();
        for (File _file : files) {
            if (_file.isDirectory()) {
                fileList.addAll(getFileStructure(_file));
                continue;
            }
        }
        return fileList;
    }

    public static List<File> getFileStructure(String folderPath, FilenameFilter filter) {
        return getFileStructure(new File(folderPath), filter);
    }

    public static List<File> getFileStructure(File folder, FilenameFilter filter) {
        List<File> fileList = new ArrayList<File>();

        File[] files = folder.listFiles(filter);
        for (File _file : files) {
            if (_file.isDirectory()) {
                fileList.addAll(getFileStructure(_file, filter));
                continue;
            }
        }
        return fileList;
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

    public static File getFile(String fileName, String rootFolder, String... subFolders) {

        StringBuilder sb = new StringBuilder();

        if (rootFolder != null) {
            sb.append(new File(rootFolder).getAbsolutePath());
        } else {
            sb.append(new File("").getAbsolutePath());
        }

        if (subFolders != null && subFolders.length > 0) {
            for (String subFolder : subFolders) {
                if (sb.length() > 0) {
                    sb.append("/");
                }
                sb.append(subFolder);
            }
        }

        if (sb.length() > 0) {
            sb.append("/");
        }

        File file = new File(sb.toString());

        if (fileName != null) {
            file = new File(file.getAbsolutePath() + "/" + fileName);
        }

        return file;
    }
}
