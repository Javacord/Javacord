package org.javacord.core.entity.permission;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.RoleTags;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.Member;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.listener.server.role.InternalRoleAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Role}.
 */
public class RoleImpl implements Role, InternalRoleAttachableListenerManager {

    private static final Comparator<Role> ROLE_COMPARATOR = Comparator
            .comparingInt(Role::getRawPosition)
            .thenComparing(Comparator.comparing(Role::getId).reversed());

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The server of the role.
     */
    private final ServerImpl server;

    /**
     * The role tags of the role.
     */
    private final RoleTagsImpl roleTags;

    /**
     * The id of the role.
     */
    private final long id;

    /**
     * The name of the role.
     */
    private volatile String name;

    /**
     * The raw position of the role.
     */
    private volatile int rawPosition;

    /**
     * The color of the role.
     */
    private volatile int color;

    /**
     * Whether this role is pinned in the user listing or not.
     */
    private volatile boolean hoist;

    /**
     * Whether this role can be mentioned or not.
     */
    private volatile boolean mentionable;

    /**
     * The permissions of the role.
     */
    private volatile PermissionsImpl permissions;

    /**
     * Whether this role is managed by an integration or not.
     */
    private final boolean managed;

    /**
     * Creates a new role object.
     *
     * @param api    The discord api instance.
     * @param server The server of the role.
     * @param data   The json data of the role.
     */
    public RoleImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        this.api = api;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.rawPosition = data.get("position").asInt();
        this.color = data.get("color").asInt(0);
        this.hoist = data.get("hoist").asBoolean(false);
        this.mentionable = data.get("mentionable").asBoolean(false);
        this.permissions = new PermissionsImpl(data.get("permissions").asLong(), 0);
        this.managed = data.get("managed").asBoolean(false);
        this.roleTags = data.has("tags") ? new RoleTagsImpl(data.get("tags")) : null;
    }

    /**
     * Gets the color of the role as {@code int}.
     *
     * @return The color of the role as {@code int}.
     */
    public int getColorAsInt() {
        return color;
    }

    /**
     * Sets the color of the role.
     *
     * @param color The color to set.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the hoist flag of the role.
     *
     * @param hoist The hoist flag to set.
     */
    public void setHoist(boolean hoist) {
        this.hoist = hoist;
    }

    /**
     * Sets the mentionable flag of the role.
     *
     * @param mentionable The mentionable flag to set.
     */
    public void setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
    }

    /**
     * Sets the name of the role.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the permissions of the role.
     *
     * @param permissions The permissions to set.
     */
    public void setPermissions(PermissionsImpl permissions) {
        this.permissions = permissions;
    }

    /**
     * Sets the raw position of the role.
     *
     * @param position The raw position to set.
     */
    public void setRawPosition(int position) {
        this.rawPosition = position;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<RoleTags> getRoleTags() {
        return Optional.ofNullable(roleTags);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRawPosition() {
        return rawPosition;
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color == 0 ? null : new Color(color));
    }

    @Override
    public boolean isMentionable() {
        return mentionable;
    }

    @Override
    public boolean isDisplayedSeparately() {
        return hoist;
    }

    @Override
    public Collection<User> getUsers() {
        if (isEveryoneRole()) {
            return getServer().getMembers();
        }

        return server.getRealMembers().stream()
                .filter(member -> member.hasRole(this))
                .map(Member::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasUser(User user) {
        return ((UserImpl) user).getMember()
                .filter(member -> member.getServer().equals(server))
                .map(Member.class::cast)
                // TODO Replace with Optional#or when we upgrade to Java 9+
                .map(Optional::of)
                .orElseGet(() -> server.getRealMemberById(user.getId()))
                .map(member -> member.hasRole(this))
                .orElse(false);
    }

    @Override
    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.ROLE)
                .setUrlParameters(getServer().getIdAsString(), getIdAsString())
                .execute(result -> null);
    }

    /**
     * {@inheritDoc}
     *
     * <p><b><i>Implementation note:</i></b> Only roles from the same server can be compared
     *
     * @throws IllegalArgumentException If the roles are on different servers.
     */
    @Override
    public int compareTo(Role role) {
        if (!role.getServer().equals(getServer())) {
            throw new IllegalArgumentException("Only roles from the same server can be compared for order");
        }
        return ROLE_COMPARATOR.compare(this, role);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
                || !((o == null)
                || (getClass() != o.getClass())
                || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Role (id: %s, name: %s, server: %#s)", getIdAsString(), getName(), getServer());
    }

}
