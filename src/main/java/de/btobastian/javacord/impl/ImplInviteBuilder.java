package de.btobastian.javacord.impl;

import java.io.IOException;

import org.json.JSONObject;

import de.btobastian.javacord.api.InviteBuilder;

/**
 * The implementation of {@link InviteBuilder}.
 */
class ImplInviteBuilder implements InviteBuilder {

    private ImplChannel channel;
    private int maxUses = -1;
    private byte temporary = -1;
    private int maxAge = -1;
    
    protected ImplInviteBuilder(ImplChannel channel) {
        this.channel = channel;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.InviteBuilder#setMaxUses(int)
     */
    @Override
    public InviteBuilder setMaxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.InviteBuilder#setTemporary(boolean)
     */
    @Override
    public InviteBuilder setTemporary(boolean temporary) {
        this.temporary = temporary ? (byte) 1 : 0;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.InviteBuilder#setMaxAge(int)
     */
    @Override
    public InviteBuilder setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.InviteBuilder#create()
     */
    @Override
    public String create() {
        JSONObject jsonParam = new JSONObject();
        if (maxUses > 0) {
            jsonParam.put("max_uses", maxUses);
        }
        if (temporary > -1) {
            jsonParam.put("temporary", temporary == 1);
        }
        if (maxAge > 0) {
            jsonParam.put("max_age", maxAge);
        }
        String respone;
        try {
            respone = ((ImplServer) channel.getServer()).getApi().getRequestUtils().request(
                    "https://discordapp.com/api/channels/" + channel.getId() + "/invites", jsonParam.toString(), true, "POST");
        } catch (IOException e) {
            return null;
        }
        return new JSONObject(respone).getString("code");
    }

}
