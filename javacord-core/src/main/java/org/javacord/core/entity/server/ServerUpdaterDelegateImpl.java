package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.server.internal.ServerUpdaterDelegate;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerUpdaterDelegate}.
 */
public class ServerUpdaterDelegateImpl implements ServerUpdaterDelegate {

    /**
     * The server to update.
     */
    private final Server server;

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
     * A map with all user muted states to update.
     */
    private final Map<User, Boolean> userMuted = new HashMap<>();

    /**
     * A map with all user deafened states to update.
     */
    private final Map<User, Boolean> userDeafened = new HashMap<>();

    /**
     * A map with all channels to move users to.
     */
    private final Map<User, ServerVoiceChannel> userMoveTargets = new HashMap<>();

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
     * The explicit content filter level to update.
     */
    private ExplicitContentFilterLevel explicitContentFilterLevel = null;

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
     * The banner to update.
     */
    private FileContainer banner = null;

    /**
     * Whether the banner should be updated or not.
     */
    private boolean updateBanner = false;

    /**
     * The rules channel to update.
     */
    private ServerChannel rulesChannel = null;

    /**
     * Whether the rules channel should be updated or not.
     */
    private boolean updateRulesChannel = false;

    /**
     * The moderators-only channel to update.
     */
    private ServerChannel moderatorsOnlyChannel = null;

    /**
     * Whether the moderators-only channel should be updated or not.
     */
    private boolean updateModeratorsOnlyChannel = false;

    /**
     * The locale to be updated.
     */
    private Locale locale = null;

    /**
     * Whether the locale should be updated or not.
     */
    private boolean updateLocale = false;

    /**
     * Creates a new server updater delegate.
     *
     * @param server The server to update.
     */
    public ServerUpdaterDelegateImpl(Server server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
        this.explicitContentFilterLevel = explicitContentFilterLevel;
    }

    @Override
    public void setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    @Override
    public void setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
    }

    @Override
    public void setAfkChannel(ServerVoiceChannel afkChannel) {
        this.afkChannel = afkChannel;
        updateAfkChannel = true;
    }

    @Override
    public void removeAfkChannel() {
        setAfkChannel(null);
    }

    @Override
    public void setAfkTimeoutInSeconds(int afkTimeout) {
        this.afkTimeout = afkTimeout;
    }

    @Override
    public void setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void removeIcon() {
        icon = null;
        updateIcon = true;
    }

    @Override
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public void setSplash(BufferedImage splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
    }

    @Override
    public void setSplash(BufferedImage splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
    }

    @Override
    public void setSplash(File splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
    }

    @Override
    public void setSplash(Icon splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
    }

    @Override
    public void setSplash(URL splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash);
        updateSplash = true;
    }

    @Override
    public void setSplash(byte[] splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
    }

    @Override
    public void setSplash(byte[] splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
    }

    @Override
    public void setSplash(InputStream splash) {
        this.splash = (splash == null) ? null : new FileContainer(splash, "png");
        updateSplash = true;
    }

    @Override
    public void setSplash(InputStream splash, String fileType) {
        this.splash = (splash == null) ? null : new FileContainer(splash, fileType);
        updateSplash = true;
    }

    @Override
    public void removeSplash() {
        splash = null;
        updateSplash = true;
    }

    @Override
    public void setBanner(BufferedImage banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner, "png");
        updateBanner = true;
    }

    @Override
    public void setBanner(BufferedImage banner, String fileType) {
        this.banner = (banner == null) ? null : new FileContainer(banner, fileType);
        updateBanner = true;
    }

    @Override
    public void setBanner(File banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner);
        updateBanner = true;
    }

    @Override
    public void setBanner(Icon banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner);
        updateBanner = true;
    }

    @Override
    public void setBanner(URL banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner);
        updateBanner = true;
    }

    @Override
    public void setBanner(byte[] banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner, "png");
        updateBanner = true;
    }

    @Override
    public void setBanner(byte[] banner, String fileType) {
        this.banner = (banner == null) ? null : new FileContainer(banner, fileType);
        updateBanner = true;
    }

    @Override
    public void setBanner(InputStream banner) {
        this.banner = (banner == null) ? null : new FileContainer(banner, "png");
        updateBanner = true;
    }

    @Override
    public void setBanner(InputStream banner, String fileType) {
        this.banner = (banner == null) ? null : new FileContainer(banner, fileType);
        updateBanner = true;
    }

    @Override
    public void removeBanner() {
        banner = null;
        updateBanner = true;
    }

    @Override
    public void setRulesChannel(ServerTextChannel rulesChannel) {
        this.rulesChannel = rulesChannel;
        updateRulesChannel = true;
    }

    @Override
    public void removeRulesChannel() {
        setRulesChannel(null);
    }

    @Override
    public void setModeratorsOnlyChannel(ServerTextChannel moderatorsOnlyChannel) {
        this.moderatorsOnlyChannel = moderatorsOnlyChannel;
        updateModeratorsOnlyChannel = true;
    }

    @Override
    public void removeModeratorsOnlyChannel() {
        setModeratorsOnlyChannel(null);
    }

    @Override
    public void setPreferredLocale(Locale locale) {
        this.locale = locale;
        updateLocale = true;
    }

    @Override
    public void setSystemChannel(ServerTextChannel systemChannel) {
        this.systemChannel = systemChannel;
        updateSystemChannel = true;
    }

    @Override
    public void removeSystemChannel() {
        setSystemChannel(null);
    }

    @Override
    public void setNickname(User user, String nickname) {
        userNicknames.put(user, nickname);
    }

    @Override
    public void setMuted(User user, boolean muted) {
        userMuted.put(user, muted);
    }

    @Override
    public void setDeafened(User user, boolean deafened) {
        userDeafened.put(user, deafened);
    }

    @Override
    public void setVoiceChannel(User user, ServerVoiceChannel channel) {
        userMoveTargets.put(user, channel);
    }

    @Override
    public void reorderRoles(List<Role> roles) {
        newRolesOrder = roles;
    }

    @Override
    public void addRoleToUser(User user, Role role) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRoles(u)));
        userRoles.add(role);
    }

    @Override
    public void addRolesToUser(User user, Collection<Role> roles) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRoles(u)));
        userRoles.addAll(roles);
    }

    @Override
    public void removeRoleFromUser(User user, Role role) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRoles(u)));
        userRoles.remove(role);
    }

    @Override
    public void removeRolesFromUser(User user, Collection<Role> roles) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRoles(u)));
        userRoles.removeAll(roles);
    }

    @Override
    public void removeAllRolesFromUser(User user) {
        Collection<Role> userRoles = this.userRoles.computeIfAbsent(user, u -> new ArrayList<>(server.getRoles(u)));
        userRoles.clear();
    }

    @Override
    public CompletableFuture<Void> update() {
        // A set with all members that get updates
        HashSet<User> members = new HashSet<>(userRoles.keySet());
        members.addAll(userNicknames.keySet());
        members.addAll(userMuted.keySet());
        members.addAll(userDeafened.keySet());
        members.addAll(userMoveTargets.keySet());

        // A list with all tasks
        List<CompletableFuture<?>> tasks = new ArrayList<>();

        members.forEach(member -> {
            boolean patchMember = false;
            ObjectNode updateNode = JsonNodeFactory.instance.objectNode();

            Collection<Role> roles = userRoles.get(member);
            if (roles != null) {
                ArrayNode rolesJson = updateNode.putArray("roles");
                roles.stream()
                        .map(DiscordEntity::getIdAsString)
                        .forEach(rolesJson::add);
                patchMember = true;
            }

            if (userNicknames.containsKey(member)) {
                String nickname = userNicknames.get(member);
                if (member.isYourself()) {
                    tasks.add(
                            new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.OWN_NICKNAME)
                                    .setUrlParameters(server.getIdAsString())
                                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                                    .setAuditLogReason(reason)
                                    .execute(result -> null));
                } else {
                    updateNode.put("nick", (nickname == null) ? "" : nickname);
                    patchMember = true;
                }
            }

            if (userMuted.containsKey(member)) {
                updateNode.put("mute", userMuted.get(member));
                patchMember = true;
            }

            if (userDeafened.containsKey(member)) {
                updateNode.put("deaf", userDeafened.get(member));
                patchMember = true;
            }

            if (userMoveTargets.containsKey(member)) {
                ServerVoiceChannel channel = userMoveTargets.get(member);
                if (member.isYourself()) {
                    ((DiscordApiImpl) server.getApi()).getWebSocketAdapter()
                            .sendVoiceStateUpdate(server, channel, null, null);
                } else if (channel != null) {
                    updateNode.put("channel_id", channel.getId());
                    patchMember = true;
                } else {
                    updateNode.putNull("channel_id");
                    patchMember = true;
                }
            }

            if (patchMember) {
                tasks.add(
                        new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                                .setUrlParameters(server.getIdAsString(), member.getIdAsString())
                                .setBody(updateNode)
                                .setAuditLogReason(reason)
                                .execute(result -> null));
            }
        });

        if (newRolesOrder != null) {
            tasks.add(server.reorderRoles(newRolesOrder, reason));
        }

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
        if (explicitContentFilterLevel != null) {
            body.put("explicit_content_filter", explicitContentFilterLevel.getId());
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
        if (updateModeratorsOnlyChannel) {
            if (moderatorsOnlyChannel != null) {
                body.put("public_updates_channel_id", moderatorsOnlyChannel.getIdAsString());
            } else {
                body.putNull("public_updates_channel_id");
            }
            patchServer = true;
        }
        if (updateRulesChannel) {
            if (rulesChannel != null) {
                body.put("rules_channel_id", rulesChannel.getIdAsString());
            } else {
                body.putNull("rules_channel_id");
            }
            patchServer = true;
        }
        if (updateBanner) {
            if (banner == null) {
                body.putNull("banner");
            }
            patchServer = true;
        }
        if (updateLocale) {
            if (locale == null) {
                body.putNull("preferred_locale");
            } else {
                body.put("preferred_locale", locale.toLanguageTag());
            }
            patchServer = true;
        }
        // Only make a REST call, if we really want to update something
        if (patchServer) {
            if (icon != null || splash != null || banner != null) {
                CompletableFuture<Void> iconFuture = null;
                if (icon != null) {
                    iconFuture = icon.asByteArray(server.getApi()).thenAccept(bytes -> {
                        String base64Icon = "data:image/" + icon.getFileType() + ";base64,"
                                + Base64.getEncoder().encodeToString(bytes);
                        body.put("icon", base64Icon);
                    });
                }
                CompletableFuture<Void> splashFuture = null;
                if (splash != null) {
                    splashFuture = splash.asByteArray(server.getApi()).thenAccept(bytes -> {
                        String base64Splash = "data:image/" + splash.getFileType() + ";base64,"
                                + Base64.getEncoder().encodeToString(bytes);
                        body.put("splash", base64Splash);
                    });
                }
                CompletableFuture<Void> bannerFuture = null;
                if (banner != null) {
                    bannerFuture = banner.asByteArray(server.getApi()).thenAccept(bytes -> {
                        String base64Banner = "data:image/" + banner.getFileType() + ";base64,"
                                + Base64.getEncoder().encodeToString(bytes);
                        body.put("banner", base64Banner);
                    });
                }
                CompletableFuture<Void> future;
                List<CompletableFuture<Void>> futureList = new ArrayList<>();
                if (iconFuture != null) {
                    futureList.add(iconFuture);
                }
                if (splashFuture != null) {
                    futureList.add(splashFuture);
                }
                if (bannerFuture != null) {
                    futureList.add(bannerFuture);
                }
                future = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

                tasks.add(future.thenCompose(
                        aVoid -> new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.SERVER)
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
