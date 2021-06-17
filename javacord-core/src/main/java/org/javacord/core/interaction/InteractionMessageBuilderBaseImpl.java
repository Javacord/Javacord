package org.javacord.core.interaction;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.interaction.callback.InteractionMessageBuilderBase;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.Arrays;
import java.util.EnumSet;

public abstract class InteractionMessageBuilderBaseImpl<T> implements InteractionMessageBuilderBase<T> {
    protected final InteractionMessageBuilderDelegate delegate;
    protected final Class<T> myClass;

    /**
     * Class constructor.
     * @param myClass The interface to cast to for call chaining.
     */
    public InteractionMessageBuilderBaseImpl(Class<T> myClass) {
        this.myClass = myClass;
        this.delegate = DelegateFactory.createInteractionMessageBuilderDelegate();
    }

    /**
     * Class constructor.
     * @param myClass The interface to cast to for call chaining.
     * @param delegate The delegate to use if required.
     */
    public InteractionMessageBuilderBaseImpl(Class<T> myClass, InteractionMessageBuilderDelegate delegate) {
        this.myClass = myClass;
        this.delegate = delegate;
    }

    @Override
    public T appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return myClass.cast(this);
    }

    @Override
    public T append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return myClass.cast(this);
    }

    @Override
    public T append(Mentionable entity) {
        delegate.append(entity);
        return myClass.cast(this);
    }

    @Override
    public T append(Object object) {
        delegate.append(object);
        return myClass.cast(this);
    }

    @Override
    public T appendNewLine() {
        delegate.appendNewLine();
        return myClass.cast(this);
    }

    @Override
    public T setContent(String content) {
        delegate.setContent(content);
        return myClass.cast(this);
    }

    @Override
    public T addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return myClass.cast(this);
    }

    @Override
    public T addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(embeds);
        return myClass.cast(this);
    }

    @Override
    public T addComponents(HighLevelComponent... components) {
        delegate.addComponents(components);
        return myClass.cast(this);
    }

    @Override
    public T removeAllComponents() {
        delegate.removeAllComponents();
        return myClass.cast(this);
    }

    @Override
    public T removeComponent(int index) {
        delegate.removeComponent(index);
        return myClass.cast(this);
    }

    @Override
    public T removeComponent(HighLevelComponent builder) {
        delegate.removeComponent(builder);
        return myClass.cast(this);
    }

    @Override
    public T removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return myClass.cast(this);
    }

    @Override
    public T removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return myClass.cast(this);
    }

    @Override
    public T removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return myClass.cast(this);
    }

    @Override
    public T setTts(boolean tts) {
        delegate.setTts(tts);
        return myClass.cast(this);
    }

    @Override
    public T setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return myClass.cast(this);
    }

    @Override
    public T setFlags(MessageFlag... messageFlags) {
        setFlags(EnumSet.copyOf(Arrays.asList(messageFlags)));
        return myClass.cast(this);
    }

    @Override
    public T setFlags(EnumSet<MessageFlag> messageFlags) {
        delegate.setFlags(messageFlags);
        return myClass.cast(this);
    }

    @Override
    public StringBuilder getStringBuilder() {
        return delegate.getStringBuilder();
    }
}
