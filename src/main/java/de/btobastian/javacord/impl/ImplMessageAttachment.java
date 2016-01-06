package de.btobastian.javacord.impl;

import java.net.MalformedURLException;
import java.net.URL;

import de.btobastian.javacord.message.MessageAttachment;

class ImplMessageAttachment implements MessageAttachment {

    private String url = null;
    private String proxyUrl = null;
    private int size = -1;
    private String id = null;
    private String name = null;
    
    protected ImplMessageAttachment(String url, String proxyUrl, int size, String id, String name) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.size = size;
        this.id = id;
        this.name = name;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageAttachment#getUrl()
     */
    @Override
    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageAttachment#getProxyUrl()
     */
    @Override
    public URL getProxyUrl() {
        try {
            return new URL(proxyUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageAttachment#getSize()
     */
    @Override
    public int getSize() {
        return size;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageAttachment#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageAttachment#getFileName()
     */
    @Override
    public String getFileName() {
        return name;
    }
    
}
