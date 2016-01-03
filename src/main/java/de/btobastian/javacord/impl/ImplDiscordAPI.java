package de.btobastian.javacord.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.ReadyListener;
import de.btobastian.javacord.message.Message;

/**
 * The implementation of {@link DiscordAPI}.
 */
class ImplDiscordAPI implements DiscordAPI {

    private String email;
    private String password;
    private String token;
    private String encoding = "UTF-8";
    private DiscordWebsocket socket;
    private final boolean DEBUG = false;
    private String game = "";
    
    private User yourself = null;
    
    private final HashMap<String, Server> servers = new HashMap<>(); // key = id
    private final HashMap<String, User> users = new HashMap<>(); // key = id
    private final List<ImplMessage> messages = new ArrayList<>();
    
    private RequestUtils requestUtils = new RequestUtils(this);
    
    private final HashMap<Class<?>, List<Listener>> listeners = new HashMap<>();
    
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
        return users.get(id);
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
    public Collection<User> getUsers() {
        return users.values();
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getServers()
     */
    @Override
    public Collection<Server> getServers() {
        return servers.values();
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#registerListener(de.btobastian.javacord.api.listener.Listener)
     */
    @Override
    public void registerListener(Listener listener) {
        for (Class<?> iface : listener.getClass().getInterfaces()) {
            if (Listener.class.isAssignableFrom(iface)) {
                List<Listener> listenersList = listeners.get(iface);
                if (listenersList == null) {
                    listenersList = new ArrayList<Listener>();
                    listeners.put(iface, listenersList);
                }
                listenersList.add(listener);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#acceptInvite(java.lang.String)
     */
    @Override
    public boolean acceptInvite(String inviteCode) {
        try {
            getRequestUtils().request("https://discordapp.com/api/invite/" + inviteCode, "", true, "POST");
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getSelf()
     */
    @Override
    public User getYourself() {
        return yourself;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.DiscordAPI#getServerById(java.lang.String)
     */
    @Override
    public Server getServerById(String id) {
        return servers.get(id);
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
        users.put(user.getId(), user);
    }
    
    protected void addServer(ImplServer server) {
        servers.put(server.getId(), server);
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
        User u = users.get(userId);
        if (u != null) {
            return u;
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
    
    protected void setYourself(User you) {
        yourself = you;
    }
    
    protected List<Listener> getListeners(Class<?> listenerClass) {
        List<Listener> listenersList = listeners.get(listenerClass);
        return listenersList == null ? new ArrayList<Listener>() : listenersList;
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
            result = requestUtils.getFromWebsite("https://discordapp.com/api/gateway?", "authorization", token);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new JSONObject(result).getString("url").replace("wss", "ws");
    }

}
