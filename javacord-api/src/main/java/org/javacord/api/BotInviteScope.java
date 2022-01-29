package org.javacord.api;


/**
 * An enum for the Invite Scope that is used for
 * building bot invite links.
 */
public enum BotInviteScope {

    IDENTIFY("identify"),
    EMAIL("email"),
    CONNECTIONS("connections"),
    GUILDS("guilds"),
    GUILDS_JOIN("guilds.join"),
    GUILDS_MEMBERS_READ("guilds.members.read"),
    GDM_JOIN("gdm.join"),
    RPC("rpc"),
    RPC_NOTIFICATIONS_READ("rpc.notifications.read"),
    RPC_VOICE_READ("rpc.voice.read"),
    RPC_VOICE_WRITE("rpc.voice.write"),
    RPC_ACTIVITIES_WRITE("rpc.activities.write"),
    BOT("bot"),
    WEBHOOK_INCOMING("webhook.incoming"),
    MESSAGES_READ("messages.read"),
    APPLICATIONS_BUILDS_UPLOAD("applications.builds.upload"),
    APPLICATIONS_BUILDS_READ("applications.builds.read"),
    APPLICATIONS_COMMANDS("applications.commands"),
    APPLICATIONS_COMMANDS_PERMISSIONS_UPDATE("applications.commands.permissions.update"),
    APPLICATIONS_STORE_UPDATE("applications.store.update"),
    APPLICATIONS_ENTITLEMENTS("applications.entitlements"),
    ACTIVITIES_READ("activities.read"),
    ACTIVITIES_WRITE("activities.write"),
    RELATIONSHIPS_READ("relationships.read");

    private final String scope;

    /**
     * Creates a {@link BotInviteScope} which can be used in the
     * bot invite builder for scoping.
     *
     * @param scope The value for the query of this scope.
     */
    BotInviteScope(String scope) {
        this.scope = scope;
    }

    /**
     * Gets the query paramater value for this scope.
     *
     * @return The parameter for the query value of this scope.
     */
    public String getScope() {
        return scope;
    }
}
