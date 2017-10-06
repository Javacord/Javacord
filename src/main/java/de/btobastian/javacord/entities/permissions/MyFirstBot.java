package de.btobastian.javacord.entities.permissions;

import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.Javacord;

public class MyFirstBot {

    public static void main(String[] args) {
        String token = "MzU1ODcyOTQ2NjUwMDg3NDI2.DLlx8A.Bjbzq09A-rVyei98FJgfkVNo2zM";

        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
        	
            // Login successful
            api.getTextChannelById("355215135511412737").ifPresent(channel ->{
            	channel.sendMessage("done");
            	PermissionsBuilder pb = new PermissionsBuilder();
            	pb.setState(PermissionType.BAN_MEMBERS, PermissionState.ALLOWED);
            	pb.setState(PermissionType.READ_MESSAGES, PermissionState.DENIED);
            	Role R = api.getRoleById("364785159221936128").get();
                 R.setPermissions(pb.build()).exceptionally(Javacord.exceptionLogger());
            	R.getColor();
            });
        }).exceptionally(Javacord.exceptionLogger());
    }

}