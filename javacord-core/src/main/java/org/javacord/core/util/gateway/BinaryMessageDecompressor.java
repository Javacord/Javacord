package org.javacord.core.util.gateway;

import org.apache.logging.log4j.Logger;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class BinaryMessageDecompressor {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(BinaryMessageDecompressor.class);

    private BinaryMessageDecompressor() {
        throw new UnsupportedOperationException("You cannot create an instance of this class");
    }

    /**
     * Decompresses the given byte array.
     *
     * @param data The data to decompress.
     * @return The decompressed string.
     * @throws DataFormatException If the compressed data format is invalid.
     */
    public static String decompress(byte[] data) throws DataFormatException {
        Inflater decompressor = new Inflater();
        decompressor.setInput(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count;
            count = decompressor.inflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException ignored) { }
        byte[] decompressedData = bos.toByteArray();
        return new String(decompressedData, StandardCharsets.UTF_8);
    }

}
