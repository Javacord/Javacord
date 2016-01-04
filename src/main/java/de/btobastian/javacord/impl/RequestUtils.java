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
        conn.setRequestProperty("User-Agent", "Javacord");
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
        return request(urlToRead, jsonParam, sendToken, method, true, properties);
    }
        
    
    protected String request(String urlToRead, String jsonParam, boolean sendToken, String method, boolean expectAnswer, String... properties) throws IOException {
        URL url = new URL(urlToRead);
        byte[] postDataBytes = jsonParam.toString().getBytes(api.getEncoding());

        URLConnection conn = url.openConnection();
        if (method.equalsIgnoreCase("PATCH")) {
            ((HttpURLConnection) conn).setRequestMethod("POST");
            Socket clientSocket = new Socket("discordapp.com", 80);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBytes("PATCH " + url + " HTTP/1.1\n" + // see https://www.mnot.net/blog/2012/09/05/patch
                    "Host: discordapp.com\r\n" +
                    "Content-Length: " + postDataBytes.length + "\r\n" +
                    "User-Agent: Javacord\r\n" +
                    "Content-Type: application/json\r\n" +
                    "authorization: " + api.getToken() + "\r\n\r\n" + jsonParam);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), api.getEncoding()));
            
            String jsonResult = "";
            String line;
            boolean expectData = expectAnswer;
            byte counterSinceFirstEmptyLine = 0;
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase("Content-Length: 0")) {
                    expectData = false;
                }
                if (counterSinceFirstEmptyLine != 0) {
                    counterSinceFirstEmptyLine++;
                }
                if (line.length() == 0) {
                    if (counterSinceFirstEmptyLine >= 3 || !expectData) {
                        break;
                    }
                    counterSinceFirstEmptyLine++;
                }
                if (counterSinceFirstEmptyLine == 3) {
                    jsonResult = line;
                }
            }
            clientSocket.close();
            return jsonResult;
        }
        
        ((HttpURLConnection) conn).setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("User-Agent", "Javacord");
        
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
