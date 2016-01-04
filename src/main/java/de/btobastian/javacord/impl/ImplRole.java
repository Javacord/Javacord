package de.btobastian.javacord.impl;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;




import de.btobastian.javacord.Channel;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.permissions.Permissions;
import de.btobastian.javacord.permissions.Role;

class ImplRole implements Role {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);
    
    private String id;
    private String name;
    private ImplServer server;
    private ImplPermissions permissions;
    private int position;
    private Color color;
    private boolean hoist;
    
    private List<User> users = new ArrayList<>();
    
    // key = channelId
    private HashMap<String, Permissions> overriddenPermissions = new HashMap<>();
    
    protected ImplRole(JSONObject roleJSON, ImplServer server) {
        this.server = server;
        
        id = roleJSON.getString("id");
        name = roleJSON.getString("name");
        permissions = new ImplPermissions(roleJSON.getInt("permissions"));
        position = roleJSON.getInt("position");
        color = new Color(roleJSON.getInt("color"));
        hoist = roleJSON.getBoolean("hoist");
        
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

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#getPosition()
     */
    @Override
    public int getPosition() {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#updateName(java.lang.String)
     */
    @Override
    public boolean updateName(String name) {
        return update(name, color.getRGB(), hoist, permissions.getAllow());
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#getHoist()
     */
    @Override
    public boolean getHoist() {
        return hoist;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#updateHoist(boolean)
     */
    @Override
    public boolean updateHoist(boolean hoist) {
        return update(name, color.getRGB(), hoist, permissions.getAllow());
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#getColor()
     */
    @Override
    public Color getColor() {
        return color;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#updateColor(java.awt.Color)
     */
    @Override
    public boolean updateColor(Color color) {
        return update(name, color.getRGB(), hoist, permissions.getAllow());
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#updatePermissions(de.btobastian.javacord.permissions.Permissions)
     */
    @Override
    public boolean updatePermissions(Permissions permissions) {
        return update(name, color.getRGB(), hoist, ((ImplPermissions) permissions).getAllow());
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#addUser(de.btobastian.javacord.User)
     */
    @Override
    public boolean addUser(User user) {
        return updateMembership(user, true);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#removeUser(de.btobastian.javacord.User)
     */
    @Override
    public boolean removeUser(User user) {
        return updateMembership(user, false);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.Role#updateOverriddenPermissions(de.btobastian.javacord.Channel, de.btobastian.javacord.permissions.Permissions)
     */
    @Override
    public boolean updateOverriddenPermissions(Channel channel, Permissions permissions) {
        JSONObject jsonParam = new JSONObject()
            .put("allow", ((ImplPermissions) permissions).getAllow())
            .put("deny", ((ImplPermissions) permissions).getDeny())
            .put("type", "role")
            .put("id", id);
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + channel.getId() + "/permissions/" + id, jsonParam.toString(), true, "PUT");
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        overriddenPermissions.put(channel.getId(), permissions);
        return true;
    }
    
    protected void setOverriddenPermissions(Channel channel, Permissions permissions) {
        overriddenPermissions.put(channel.getId(), permissions);
    }
    
    protected void addUserWithoutUpdate(User user) {
        users.add(user);
    }
    
    protected void removeUserWithoutUpdate(User user) {
        users.remove(user);
    }
    
    protected void setName(String name) {
        this.name = name;
    }
    
    protected void setPermissions(Permissions permissions) {
        this.permissions = (ImplPermissions) permissions;
    }
    
    protected void setPosition(int newPosition) {
        position = newPosition;
    }
    
    private boolean update(String name, int color, boolean hoist, int allow) {
        JSONObject jsonParam = new JSONObject().put("name", name).put("color", color).put("hoist", hoist).put("permissions", allow);
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/guilds/" + server.getId() + "/roles/" + id, jsonParam.toString(), true, "PATCH");
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        this.name = name;
        this.color = new Color(color);
        this.hoist = hoist;
        this.permissions = new ImplPermissions(allow);
        return true;
    }
    
    private boolean updateMembership(User user, boolean add) {
        if (add && users.contains(user) || !add && !users.contains(user)) {
            return true;
        }
        List<Role> roles = new ArrayList<>();
        for (Role role : server.getRoles()) {
            if (((ImplRole) role).users.contains(user)) {
                roles.add(role);
            }
        }
        if (add) {
            roles.add(this);
        }
        String[] rolesArray = new String[roles.size()];
        for (int i = 0; i < rolesArray.length; i++) {
            rolesArray[i] = roles.get(i).getId();
        }
        
        JSONObject jsonParam = new JSONObject().put("roles", rolesArray);
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/guilds/" + server.getId() + "/members/" + user.getId(), jsonParam.toString(), true, "PATCH", false);
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        if (add) {
            users.add(user);
        } else {
            users.remove(user);
        }
        return true;
    }

}
