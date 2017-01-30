/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.utils.handler.voice;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.listener.voice.UserJoinVoiceChannelListener;
import de.btobastian.javacord.listener.voice.UserLeaveVoiceChannelListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;

/**
 * Handles the voice state update packet.
 */
public class VoiceStateUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(VoiceStateUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceStateUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "VOICE_STATE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
    	User user = null;
		try {
			user = api.getUserById(packet.getString("user_id")).get();
		} catch (JSONException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		final User userPassed = user;
		String channelId = null;
		try {
			channelId = packet.getString("channel_id");
		} catch (JSONException ignored) {
		}
    	if (channelId != null) {
    		final VoiceChannel channel = api.getVoiceChannelById(channelId);
	    	listenerExecutorService.submit(new Runnable() {
	            @Override
	            public void run() {
	                List<UserJoinVoiceChannelListener> listeners = api.getListeners(UserJoinVoiceChannelListener.class);
	                synchronized (listeners) {
	                    for (UserJoinVoiceChannelListener listener : listeners) {
	                        try {
	                            listener.onUserJoinVoiceChannel(api, userPassed, channel);
	                        } catch (Throwable t) {
	                            logger.warn("Uncaught exception in UserJoinVoiceChannelListener!", t);
	                        }
	                    }
	                }
	            }
	        });
    	} else {
    		listenerExecutorService.submit(new Runnable() {
	            @Override
	            public void run() {
	                List<UserLeaveVoiceChannelListener> listeners = api.getListeners(UserLeaveVoiceChannelListener.class);
	                synchronized (listeners) {
	                    for (UserLeaveVoiceChannelListener listener : listeners) {
	                        try {
	                            listener.onUserLeaveVoiceChannel(api, userPassed);
	                        } catch (Throwable t) {
	                            logger.warn("Uncaught exception in UserLeaveVoiceChannelListener!", t);
	                        }
	                    }
	                }
	            }
	        });
    	}
    }

}
