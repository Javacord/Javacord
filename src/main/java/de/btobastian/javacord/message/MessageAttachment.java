package de.btobastian.javacord.message;

import java.net.URL;

/**
 * An attachment of a message.
 */
public interface MessageAttachment {

    /**
     * Gets the url of the attachment.
     * 
     * @return The url of the attachment.
     */
    public URL getUrl();
    
    /**
     * Gets the proxy url of the attachment.
     * 
     * @return The proxy url of the attachment.
     */
    public URL getProxyUrl();
    
    /**
     * Gets the size of the attachment in bytes.
     * 
     * @return The size of the attachment in bytes.
     */
    public int getSize();
    
    /**
     * Gets the id of the attachment.
     * 
     * @return The id of the attachment.
     */
    public String getId();
    
    /**
     * Gets the file name of the attachment.
     * 
     * @return The file name of the attachment.
     */
    public String getFileName();
    
}
