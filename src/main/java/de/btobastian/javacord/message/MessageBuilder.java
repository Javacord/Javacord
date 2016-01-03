package de.btobastian.javacord.message;

import de.btobastian.javacord.User;

/**
 * This class helps to build messages.
 */
public class MessageBuilder {
    
    private StringBuilder strBuilder;
    
    /**
     * Class constructor.
     */
    public MessageBuilder() {
        strBuilder = new StringBuilder();
    }
    
    /**
     * Appends the decoration to the text.
     * 
     * @param decoration The decoration/style.
     * @param message The message.
     * @return This object to reuse it.
     */
    public MessageBuilder appendDecoration(MessageDecoration decoration, String message) {
        strBuilder.append(decoration.getPrefix()).append(message).append(decoration.getSuffix());
        return this;
    }
    
    /**
     * Appends code to the text.
     * 
     * @param language The language, e.g. "java".
     * @param message The message.
     * @return This object to reuse it.
     */
    public MessageBuilder appendCode(String language, String message) {
        strBuilder.append(MessageDecoration.CODE_LONG.getPrefix()).append(language).append("\n")
            .append(message).append(MessageDecoration.CODE_LONG.getSuffix());
        return this;
    }
    
    /**
     * Appends a user (@user).
     * 
     * @param user The user so append.
     * @return This object to reuse it.
     */
    public MessageBuilder appendMention(User user) {
        strBuilder.append("<@").append(user.getId()).append(">");
        return this;
    }
    
    /**
     * Generates the String to send.
     * 
     * @return The String to send.
     */
    public String build() {
        return strBuilder.toString();
    }

}
