package org.javacord.core.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

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

    /**
     * Converts a file to the data URI scheme.
     *
     * @param file The file to convert.
     * @return The data URI.
     * @throws IOException If the file could not be read.
     */
    public static String convertFileToDataUri(File file) throws IOException {
        String contentType = Files.probeContentType(file.toPath());

        byte[] data = Files.readAllBytes(file.toPath());

        String base64str = new String(Base64.getEncoder().encode(Files.readAllBytes(file.toPath())),
                StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("data:");
        sb.append(contentType);
        sb.append(";base64,");
        sb.append(base64str);
        return sb.toString();
    }

}
