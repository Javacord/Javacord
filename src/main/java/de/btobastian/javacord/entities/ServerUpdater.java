package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.impl.ImplServer;

/**
 * This class is used to perform server updates.
 */
public class ServerUpdater {

    /**
     * The server to update.
     */
    private ImplServer server;

    /**
     * The new name of the server.
     */
    private String name = null;

    /**
     * The new region of the server.
     */
    private Region region = null;

    /**
     * Creates a new server updater.
     *
     * @param server The server to update.
     */
    public ServerUpdater(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the new name of the server.
     *
     * @param name The new name of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the new region of the server.
     *
     * @param region
     * @return
     */
    public ServerUpdater setRegion(Region region) {
        this.region = region;
        return this;
    }

}
