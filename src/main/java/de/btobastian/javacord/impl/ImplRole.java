package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.Role;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.permissions.Permissions;

class ImplRole implements Role {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);
    
    private String id;
    private String name;
    private ImplServer server;
    private Permissions permissions;
    
    private List<User> users = new ArrayList<>();
    
    // key = channelId
    private HashMap<String, Permissions> overriddenPermissions = new HashMap<>();
    
    protected ImplRole(JSONObject roleJSON, ImplServer server) {
        this.server = server;
        
        id = roleJSON.getString("id");
        name = roleJSON.getString("name");
        permissions = new ImplPermissions(roleJSON.getInt("permissions"));
        
        server.addRole(this);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getServer()
     */
    @Override
    public Server getServer() {
        return server;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getPermission()
     */
    @Override
    public Permissions getPermission() {
        return permissions;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getOverriddenPermissions(de.btobastian.javacord.api.Channel)
     */
    @Override
    public Permissions getOverriddenPermissions(Channel channel) {
        Permissions overriddenPermissions = this.overriddenPermissions.get(channel.getId());
        if (overriddenPermissions == null) {
            overriddenPermissions = emptyPermissions;
        }
        return overriddenPermissions;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Role#getUsers()
     */
    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
    
    protected void setOverriddenPermissions(Channel channel, Permissions permissions) {
        overriddenPermissions.put(channel.getId(), permissions);
    }
    
    protected void addUser(User user) {
        users.add(user);
    }
    
    protected void removeUser(User user) {
        users.remove(user);
    }
    
    protected void setName(String name) {
        this.name = name;
    }
    
    protected void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

}
