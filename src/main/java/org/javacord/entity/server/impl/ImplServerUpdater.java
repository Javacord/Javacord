package org.javacord.entity.server.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.permission.Role;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ServerUpdater;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.user.User;
import org.javacord.util.FileContainer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ServerUpdater;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.user.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ServerUpdater}.
 */
public class ImplServerUpdater implements ServerUpdater {

    /**
     * The server to update.
     */
    private final ImplServer server;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * A map with all user roles to update.
     */
    private final Map<User, Collection<Role>> userRoles = new HashMap<>();

    /**
     * A map with all user nicknames to update.
     */
    private final Map<User, String> userNicknames = new HashMap<>();

    /**
     * A list with the new order of the roles.
     */
    private List<Role> newRolesOrder = null;

    /**
     * The name to update.
     */
    private String name = null;

    /**
     * The region to update.
     */
    private Region region = null;

    /**
     * The verification level to update.
     */
    private VerificationLevel verificationLevel = null;

    /**
     * The default message notification level to update.
     */
    private DefaultMessageNotificationLevel defaultMessageNotificationLevel = null;

    /**
     * The afk channel to update.
     */
    private ServerChannel afkChannel = null;

    /**
     * Whether the afk channel should be updated or not.
     */
    private boolean updateAfkChannel = false;

    /**
     * The afk timeout to update.
     */
    private Integer afkTimeout = null;

    /**
     * The icon to update.
     */
    private FileContainer icon = null;

    /**
     * Whether the icon should be updated or not.
     */
    private boolean updateIcon = false;

    /**
     * The owner to update.
     */
    private User owner = null;

    /**
     * The splash to update.
     */
    private FileContainer splash = null;

    /**
     * Whether the splash should be updated or not.
     */
    private boolean updateSplash = false;

    /**
     * The system channel to update.
     */
    private ServerChannel systemChannel = null;

    /**
     * Whether the system channel should be updated or not.
     */
    private boolean updateSystemChannel = false;

    /**
     * Creates a new server updater.
     *
     * @param server The server to update.
     */
    public ImplServerUpdater(ImplServer server) {
        this.server = server;
    }

    @Override
    public ServerUpdater setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ServerUpdater setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerUpdater setRegion(Region region) {
        this.region = region;
        return this;
    }

    @Override
    public ServerUpdater setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
        return this;
    }

    @Override
    public ServerUpdater setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        return this;
    }

    @Override
    public ServerUpdater setAfkChannel(ServerVoiceChannel afkChannel) {
        this.afkChannel = afkChannel;
        updateAfkChannel = true;
        return this;
    }

    @Override
    public ServerUpdater removeAfkChannel() {
        return setAfkChannel(null);
    }

    @Override
    public ServerUpdater setAfkTimeoutInSeconds(int afkTimeout) {
        this.afkTimeout = afkTimeout;
        return this;
    }

    @Override
    public ServerUpdater setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater removeIcon() {
        icon = null;
        updateIcon = true;
        return this;
    }

    @Override
    public ServerUpdater setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public ServerUpdater setSplash(BufferedImage splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(BufferedImage splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(File splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(Icon splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(URL splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(byte[] splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(byte[] splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(InputStream splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSplash(InputStream splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater removeSplash() {
        splash = null;
        updateSplash = true;
        return this;
    }

    @Override
    public ServerUpdater setSystemChannel(ServerTextChannel systemChannel) {
        this.systemChannel = systemChannel;
        updateSystemChannel = true;
        return this;
    }

    @Override
    public ServerUpdater removeSystemChannel() {
        return setSystemChannel(null);
    }

    @Override
    public ServerUpdater setNickname(User user, String nickname) {
        userNicknames.put(user, nickname);
        return this;
    }

    @Override
    public ServerUpdater reorderRoles(List<Role> roles) {
        newRolesOrder = roles;
        return this;
    }

    @Override
    public ServerUpdater addRoleToUser(User user, Role role) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRolesOf(u)));
        userRoles.add(role);
        return this;
    }

    @Override
    public ServerUpdater addAllRolesToUser(User user, Collection<Role> roles) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRolesOf(u)));
        userRoles.addAll(roles);
        return this;
    }

    @Override
    public ServerUpdater removeRoleFromUser(User user, Role role) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRolesOf(u)));
        userRoles.remove(role);
        return this;
    }

    @Override
    public ServerUpdater removeAllRolesFromUser(User user, Collection<Role> roles) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRolesOf(u)));
        userRoles.removeAll(roles);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        // A list with all tasks, initialized with all role updates
        ArrayList<CompletableFuture<?>> tasks = userRoles.entrySet().stream()
                .map(entry -> server.updateRoles(entry.getKey(), entry.getValue(), reason))
                .collect(Collectors.toCollection(ArrayList::new));
        // User nicknames
        tasks.addAll(userNicknames.entrySet().stream()
                .map(entry -> server.updateNickname(entry.getKey(), entry.getValue(), reason))
                .collect(Collectors.toList()));
        if (newRolesOrder != null) {
            tasks.add(server.reorderRoles(newRolesOrder, reason));
        }

        // TODO nickname update and role update use the same endpoint -> There's potential for saving some REST calls

        // Server settings
        boolean patchServer = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchServer = true;
        }
        if (region != null) {
            body.put("region", region.getKey());
            patchServer = true;
        }
        if (verificationLevel != null) {
            body.put("verification_level", verificationLevel.getId());
            patchServer = true;
        }
        if (defaultMessageNotificationLevel != null) {
            body.put("default_message_notifications", defaultMessageNotificationLevel.getId());
            patchServer = true;
        }
        if (updateAfkChannel) {
            if (afkChannel != null) {
                body.put("afk_channel_id", afkChannel.getIdAsString());
            } else {
                body.putNull("afk_channel_id");
            }
            patchServer = true;
        }
        if (afkTimeout != null) {
            body.put("afk_timeout", afkTimeout.intValue());
            patchServer = true;
        }
        if (updateIcon) {
            if (icon == null) {
                body.putNull("icon");
            }
            patchServer = true;
        }
        if (updateSplash) {
            if (splash == null) {
                body.putNull("splash");
            }
            patchServer = true;
        }
        if (owner != null) {
            body.put("owner_id", owner.getIdAsString());
            patchServer = true;
        }
        if (updateSystemChannel) {
            if (systemChannel != null) {
                body.put("system_channel_id", systemChannel.getIdAsString());
            } else {
                body.putNull("system_channel_id");
            }
            patchServer = true;
        }
        // Only make a REST call, if we really want to update something
        if (patchServer) {
            if (icon != null || splash != null) {
                CompletableFuture<Void> iconFuture = null;
                if (icon != null) {
                    iconFuture = icon.asByteArray(server.getApi()).thenAccept(bytes -> {
                        String base64Icon = "data:image/" + icon.getFileType() + ";base64," +
                                Base64.getEncoder().encodeToString(bytes);
                        body.put("icon", base64Icon);
                    });
                }
                CompletableFuture<Void> splashFuture = null;
                if (splash != null) {
                    splashFuture = splash.asByteArray(server.getApi()).thenAccept(bytes -> {
                        String base64Splash = "data:image/" + splash.getFileType() + ";base64," +
                                Base64.getEncoder().encodeToString(bytes);
                        body.put("splash", base64Splash);
                    });
                }
                CompletableFuture<Void> future;
                if (iconFuture == null) {
                    future = splashFuture;
                } else if (splashFuture == null) {
                    future = iconFuture;
                } else {
                    future = CompletableFuture.allOf(splashFuture, iconFuture);
                }
                tasks.add(future.thenCompose(aVoid -> new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.SERVER)
                        .setUrlParameters(server.getIdAsString())
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> null)));
            } else {
                tasks.add(new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.SERVER)
                        .setUrlParameters(server.getIdAsString())
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> null));
            }
        }

        CompletableFuture<?>[] tasksArray = tasks.toArray(new CompletableFuture<?>[tasks.size()]);
        return CompletableFuture.allOf(tasksArray);
    }

}
