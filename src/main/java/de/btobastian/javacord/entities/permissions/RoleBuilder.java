package de.btobastian.javacord.entities.permissions;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpMethod;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;

public class RoleBuilder {
	/**
	 * The name of the new role
	 */
	private String name;
	/**
	 * The permissions to be set on the role, represented by {@link Permissions}
	 */
	private Permissions perms;
	/**
	 * Determines if role should be shown on the sidebar seperately. This value is
	 * true by default.
	 */
	private boolean hoist = true;
	/**
	 * RGB color the role should have
	 */
	private int color;
	/**
	 * Determines if role can be mentioned
	 */
	private boolean mentionable = false;
	private Server server;

	/**
	 * Creates a new instance of RoleBuilder
	 */
	public RoleBuilder(Server theServ) {
		this.server = theServ;
	}

	/**
	 * Sets the name of the role
	 * 
	 * @param name
	 *            - The desired role name.
	 * @return The current instance of this RoleBuilder to chain methods.
	 */
	public RoleBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Sets the desired permissions
	 * 
	 * @param perm
	 *            - The permissions object
	 * @return The current instance of this RoleBuilder to chain methods.
	 * @see {@link PermissionsBuilder} to create the Permissions object.
	 */
	public RoleBuilder setPermissions(Permissions perm) {
		this.perms = perm;
		return this;
	}

	/**
	 * Sets if role should be displayed seperately on the sidebar
	 * 
	 * @param hoist
	 *            - Boolean value of hoist
	 * @return The current instance of this RoleBuilder to chain methods.
	 */
	public RoleBuilder setHoist(boolean hoist) {
		this.hoist = hoist;
		return this;
	}

	/**
	 * Sets the color of this role
	 * 
	 * @param c
	 *            - The color object to decode to RGB
	 * @return The current instance of this RoleBuilder to chain methods.
	 * @see {@link Color}
	 */
	public RoleBuilder setColor(Color c) {
		setColor(c.getRGB() & 0xFFFFFF);
		return this;
	}

	/**
	 * Sets the role color
	 * 
	 * @param rgb
	 *            - The raw RGB value
	 * @return The current instance of this RoleBuilder to chain methods.
	 */
	public RoleBuilder setColor(int rgb) {
		this.color = rgb;
		return this;
	}

	/**
	 * Sets if the role can be mentioned
	 * 
	 * @param mention
	 *            - Boolean value of mentionanle
	 * @return The current instance of this RoleBuilder to chain methods.
	 */
	public RoleBuilder setMentionable(boolean mention) {
		this.mentionable = mention;
		return this;
	}

	/**
	 * Packs data into the required JSON object and makes the API call.
	 * 
	 * @return The new role if completed OK.
	 */
	public CompletableFuture<Role> create() {
		JSONObject body = new JSONObject();
		if (name == null) {
			throw new IllegalStateException("Name can't be null.");
		}
		body.put("name", name);

		if (perms == null) {
			throw new IllegalStateException("Permissions can't be null");
		}
		ImplPermissions imp = (ImplPermissions) perms;
		body.put("permissions", imp.getAllowed());
		body.put("color", color);
		body.put("hoist", hoist);
		body.put("mentionable", mentionable);
		ImplServer realServer = (ImplServer) this.server;
		return new RestRequest<Role>(server.getApi(), HttpMethod.POST, RestEndpoint.ROLES)
				.setUrlParameters(String.valueOf(server.getId())).setBody(body)
				.execute(res -> realServer.getOrCreateRole(res.getBody().getObject()));
	}

	
}
