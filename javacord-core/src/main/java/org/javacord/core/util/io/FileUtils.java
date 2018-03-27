package org.javacord.core.util.io;

import java.io.File;

/**
 * Some utilities for files.
 */
public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("You cannot create an instance of this class");
    }

    /**
     * Gets the extension of the given file or <code>"png"</code> if the file has no extension.
     *
     * @param file The file.
     * @return The extension of the given file.
     */
    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    /**
     * Gets the extension of the given file name or <code>"png"</code> if the file name has no extension.
     *
     * @param fileName The file name.
     * @return The extension of the given file.
     */
    public static String getExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "png";
    }

}
