package de.btobastian.javacord.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

class RequestUtils {

    private ImplDiscordAPI api;
    
    protected RequestUtils(ImplDiscordAPI api) {
        this.api = api;
    }
    
    protected String getFromWebsite(String urlToRead, String... properties) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("User-Agent", "discordApi");
        for (int i = 0; i < properties.length / 2; i++) {
            conn.setRequestProperty(properties[i*2], properties[i*2 + 1]);
        }
        conn.setDoOutput(true);
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
    
    protected String request(String urlToRead, String jsonParam, boolean sendToken, String method, String... properties) throws IOException {
        URL url = new URL(urlToRead);
        
        byte[] postDataBytes = jsonParam.toString().getBytes(api.getEncoding());

        URLConnection conn = url.openConnection();
        if (method.equalsIgnoreCase("PATCH")) { // some trick to support patch ( stolen from jDiscord :( )
            ((HttpURLConnection) conn).setRequestMethod("POST");
            Socket clientSocket = new Socket("discordapp.com", 80);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBytes("PATCH " + url + " HTTP/1.1\n" +
                    "Host: discordapp.com\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: " + postDataBytes.length + "\n" +
                    "Origin: http://discordapp.com\n" +
                    "User-Agent: discordApi\n" +
                    "Content-Type: application/json\n" +
                    "Accept: */*\n" +
                    "authorization: " + api.getToken() + "\n\n" + jsonParam);
            out.close();
            clientSocket.close();
            return null;
        }
        
        ((HttpURLConnection) conn).setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("User-Agent", "discordApi");
        
        if (sendToken) {
            conn.setRequestProperty("authorization", api.getToken());
        }
        for (int i = 0; i < properties.length / 2; i++) {
            conn.setRequestProperty(properties[i*2], properties[i*2 + 1]);
        }
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), api.getEncoding()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
    
}
