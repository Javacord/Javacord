package de.btobastian.javacord.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;
import de.btobastian.javacord.api.listener.Listener;
import de.btobastian.javacord.api.listener.MessageCreateListener;
import de.btobastian.javacord.api.listener.MessageEditListener;
import de.btobastian.javacord.api.listener.ReadyListener;
import de.btobastian.javacord.api.listener.TypingStartListener;

/**
 * The implementation of {@link DiscordAPI}.
 */
class ImplDiscordAPI implements DiscordAPI {

    private String email;
    private String password;
    private String token;
    private String encoding = "UTF-8";
    private DiscordWebsocket socket;
    private final boolean DEBUG = true;
    private String game = "";
    
    private final List<ImplServer> servers = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<ImplMessage> messages = new ArrayList<>();
    
    private RequestUtils requestUtils = new RequestUtils(this);
    
    private final List<MessageCreateListener> messageCreateListeners = new ArrayList<>();
    private final List<MessageEditListener> messageEditListeners = new ArrayList<>();
    private final List<TypingStartListener> typingStartListeners = new ArrayList<>();
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#setEmail(java.lang.String)
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#setPassword(java.lang.String)
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getEmail()
     */
    @Override
    public String getEmail() {
        return email;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#connect(de.btobastian.javacord.api.listener.ReadyListener)
     */
    @Override
    public void connect(ReadyListener listener) {
        if (email == null || password == null) {
            listener.onFail();
            return;
        }
        token = requestToken(email, password);
        if (token == null) {
            listener.onFail();
            return;
        }
        String gateway = requestGateway();
        if (gateway == null) {
            listener.onFail();
            return;
        }
        
        try {
            socket = new DiscordWebsocket(new URI(gateway), this, listener);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            listener.onFail();
            return;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#isReady()
     */
    @Override
    public boolean isReady() {
        return socket == null ? false : socket.isReady();
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#setEncoding(java.lang.String)
     */
    @Override
    public void setEncoding(String encoding) throws UnsupportedEncodingException {
        if (encoding == null) {
            throw new IllegalArgumentException("encoding cannot be null!");
        }
        
        // check the encoding
        "1337".getBytes(encoding);
        
        this.encoding = encoding;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getEncoding()
     */
    @Override
    public String getEncoding() {
        return encoding;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#setGame(java.lang.String)
     */
    @Override
    public void setGame(String game) {
        this.game = game;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getGame()
     */
    @Override
    public String getGame() {
        return game;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getUserById(java.lang.String)
     */
    @Override
    public User getUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getMessageById(java.lang.String)
     */
    @Override
    public Message getMessageById(String messageId) {
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getUsers()
     */
    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(users);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getServers()
     */
    @Override
    public List<Server> getServers() {
        return new ArrayList<Server>(servers);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#registerListener(de.btobastian.javacord.api.listener.Listener)
     */
    @Override
    public void registerListener(Listener listener) {
        if (listener instanceof MessageCreateListener) {
            messageCreateListeners.add((MessageCreateListener) listener);
        } else if (listener instanceof MessageEditListener) {
            messageEditListeners.add((MessageEditListener) listener);
        } else if (listener instanceof TypingStartListener) {
            typingStartListeners.add((TypingStartListener) listener);
        }
    }

    /* ==== protected and private methods ==== */
    
    /**
     * Gets the token.
     * 
     * @return The token.
     */
    protected String getToken() {
        return token;
    }
    
    protected boolean debug() {
        return DEBUG;
    }
    
    protected void addUser(ImplUser user) {
        users.add(user);
    }
    
    protected void addServer(ImplServer server) {
        servers.add(server);
    }
    
    protected void removeServer(ImplServer server) {
        servers.remove(server);
    }
    
    /**
     * Gets the user or creates a new one if none exists.
     * 
     * @param user The user.
     * @return The user.
     */
    protected User getUser(JSONObject user) {
        String userId = user.getString("id");
        for (User u : users) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return new ImplUser(user, this);
    }

    protected RequestUtils getRequestUtils() {
        return requestUtils;
    }
    
    /**
     * Adds a message.
     * 
     * @param message The message to add.
     */
    protected void addMessage(ImplMessage message) {
        messages.add(message);
        if (messages.size() > 1000) { // only cache the last 1000 messages.
            messages.remove(0);
        }
    }
    
    protected List<MessageCreateListener> getMessageCreateListeners() {
        return messageCreateListeners;
    }

    protected List<MessageEditListener> getMessageEditListeners() {
        return messageEditListeners;
    }

    protected List<TypingStartListener> getTypingStartListeners() {
        return typingStartListeners;
    }

    private String requestToken(String email, String password) {
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("password", password);
        params.put("email", email);
        String jsonParam = new JSONObject().put("password", password).put("email", email).toString();
        
        String result;
        try {
            result = requestUtils.request("https://discordapp.com/api/auth/login", jsonParam, false, "POST");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new JSONObject(result).getString("token");
    }
    
    private String requestGateway() {
        String result;
        try {
            result = requestUtils.request("https://discordapp.com/api/gateway", "", true, "GET");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new JSONObject(result).getString("url").replace("wws", "ws");
    }

}
