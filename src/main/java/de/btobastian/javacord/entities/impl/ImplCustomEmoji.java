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
package de.btobastian.javacord.entities.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listener.server.CustomEmojiDeleteListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the custom emoji interface.
 */
public class ImplCustomEmoji implements CustomEmoji {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplCustomEmoji.class);

    private final ImplDiscordAPI api;

    private final ConcurrentHashMap<String, Role> roles = new ConcurrentHashMap<>();

    private final ImplServer server;

    private final String id;
    private String name;
    private boolean managed;
    private boolean requiresColons;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param server The server of the emoji.
     * @param api The api.
     */
    public ImplCustomEmoji(JSONObject data, ImplServer server, ImplDiscordAPI api) {
        this.api = api;
        this.server = server;

        id = data.getString("id");
        name = data.getString("name");
        managed = data.getBoolean("managed");
        requiresColons = data.getBoolean("require_colons");

        // Add emoji to server
        server.addCustomEmoji(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public boolean requiresColons() {
        return requiresColons;
    }

    @Override
    public Collection<Role> getRoles() {
        return roles.values();
    }

    @Override
    public String getMentionTag() {
        // https://discordapp.com/developers/docs/resources/channel#message-formatting
        return "<:" + name + ":" + id + ">";
    }

    @Override
    public Future<byte[]> getEmojiAsByteArray() {
        return getEmojiAsByteArray(null);
    }

    @Override
    public Future<byte[]> getEmojiAsByteArray(FutureCallback<byte[]> callback) {
        ListenableFuture<byte[]> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        logger.debug("Trying to get emoji {} from server {}", ImplCustomEmoji.this, server);
                        URL url = getImageUrl();
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        out.close();
                        in.close();
                        byte[] emoji = out.toByteArray();
                        logger.debug("Got emoji {} from server {} (size: {})",
                                ImplCustomEmoji.this, server, emoji.length);
                        return emoji;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<BufferedImage> getEmoji() {
        return getEmoji(null);
    }

    @Override
    public Future<BufferedImage> getEmoji(FutureCallback<BufferedImage> callback) {
        ListenableFuture<BufferedImage> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<BufferedImage>() {
                    @Override
                    public BufferedImage call() throws Exception {
                        byte[] imageAsBytes = getEmojiAsByteArray().get();
                        if (imageAsBytes.length == 0) {
                            return null;
                        }
                        InputStream in = new ByteArrayInputStream(imageAsBytes);
                        return ImageIO.read(in);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public URL getImageUrl() {
        try {
            return new URL("https://cdn.discordapp.com/emojis/" + id + ".png");
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the emoji is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Future<Void> delete() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                logger.debug("Trying to delete emoji {}", ImplCustomEmoji.this);
                HttpResponse<JsonNode> response = Unirest
                        .delete("https://discordapp.com/api/guilds/" + server.getId() + "/emojis/" + id)
                        .header("authorization", api.getToken())
                        .asJson();
                api.checkResponse(response);
                api.checkRateLimit(response, RateLimitType.UNKNOWN, server, null);
                server.removeCustomEmoji(ImplCustomEmoji.this);
                logger.info("Deleted emoji {}", ImplCustomEmoji.this);
                // call listener
                api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                    @Override
                    public void run() {
                        List<CustomEmojiDeleteListener> listeners = api.getListeners(CustomEmojiDeleteListener.class);
                        synchronized (listeners) {
                            for (CustomEmojiDeleteListener listener : listeners) {
                                listener.onCustomEmojiDelete(api, ImplCustomEmoji.this);
                            }
                        }
                    }
                });
                return null;
            }
        });
    }

    @Override
    public String toString() {
        return getName() + " (id: " + getId() + ", server: " + getServer().toString() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
