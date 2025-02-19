package org.javacord.testing;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * This is a sample test class for the Javacord project. Do not modify this file!
 */
public class SampleTestBot {

    /**
     * The main method of this test bot.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // Insert your bot's token here
        String token = "your token";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        SlashCommand.with("ping", "A simple ping pong command!").createGlobal(api).join();

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                slashCommandInteraction.createImmediateResponder()
                        .setContent("Pong!")
                        .setFlags(MessageFlag.EPHEMERAL) // Only visible for the user which invoked the command
                        .respond();
            }
        });
    }
}
