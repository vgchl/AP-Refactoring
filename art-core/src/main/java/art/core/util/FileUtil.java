package art.core.util;


import art.core.SourceFile;

import java.io.*;
import java.util.*;

/**
 * Contains helpers for common operations involving files and directories.
 */
public final class FileUtil {

    /**
     * Private constructor to prevent class initialization.
     */
    private FileUtil() {
        // Private constructor to prevent class initialization.
    }

    /**
     * Recursively list all files in a directory.
     *
     * @param directory The directory to start searching in.
     * @return List of files.
     */
    public static List<File> listFilesRecursively(final File directory) {
        return listFilesRecursively(directory, null);
    }

    /**
     * Recursively list all files with a certain extension in a directory.
     *
     * @param directory The directory to start searching in.
     * @param extension Only include files with this extension.
     * @return List of files.
     */
    public static List<File> listFilesRecursively(final File directory, final String extension) {
        if (null == directory) {
            throw new IllegalArgumentException("Directory must not be null");
        }

        List<File> files = new ArrayList<>();
        Queue<File> directories = new LinkedList<>();
        directories.add(directory);
        while (!directories.isEmpty()) {
            for (File file : directories.poll().listFiles()) {
                if (file.isDirectory()) {
                    directories.add(file);
                } else if (file.isFile()) {
                    if (extension == null || file.getName().endsWith(extension)) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    /**
     * Gets the contents of a file as a String.
     *
     * @param file The file to read.
     * @return The contents of a file
     * @throws java.io.IOException When the given file could not be found or read.
     */
    public static String getFileContent(File file) throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        StringBuffer buffer;
        try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(file.getAbsoluteFile()), "UTF8");
             BufferedReader reader = new BufferedReader(fileReader)) {
            String nextLine;
            buffer = new StringBuffer();
            while ((nextLine = reader.readLine()) != null) {
                buffer.append(nextLine);
                buffer.append(lineSeparator);
            }
        }
        return buffer.toString();
    }

    /**
     * Sets the contents of a file from a String.
     *
     * @param file    The file to write to.
     * @param content The contents to set.
     * @throws java.io.IOException When the file could not be found or written to.
     */
    public static void setFileContent(File file, String content) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }


    public static String[] filePaths(Set<SourceFile> files) {
        String[] filePaths = new String[files.size()];
        int i = 0;
        for (SourceFile file : files) {
            filePaths[i] = file.getFile().getPath();
            i++;
        }
        return filePaths;
    }

    public static String[] directoryPaths(Set<SourceFile> files) {
        String[] filePaths = new String[files.size()];
        int i = 0;
        for (SourceFile file : files) {
            filePaths[i] = file.getFile().getParentFile().getPath();
            i++;
        }
        return filePaths;
    }
}
