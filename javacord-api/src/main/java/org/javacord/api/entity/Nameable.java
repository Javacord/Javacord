package org.javacord.api.entity;

import org.javacord.api.util.logging.ExceptionLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

/**
 * This class represents an entity which has a name.
 *
 * <p>If used in a format string, a {@code Nameable} will be rendered as
 * the entity's name unless the alternate flag is given.
 *
 * @see String#format(String, Object...)
 */
public interface Nameable extends Formattable {

    /**
     * Gets the name of the entity.
     *
     * @return The name of the entity.
     */
    String getName();

    @Override
    default void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) != 0;
        String representation = alternate ? this.toString() : this.getName();
        boolean uppercase = (flags & FormattableFlags.UPPERCASE) != 0;
        boolean leftAlign = (flags & FormattableFlags.LEFT_JUSTIFY) != 0;
        boolean doPad = representation.length() < width;
        String padString = null;

        if (doPad) {
            char[] spaces = new char[width - representation.length()];
            Arrays.fill(spaces, ' ');
            padString = new String(spaces);
        }

        try {
            if (doPad && !leftAlign) {
                formatter.out().append(padString);
            }

            formatter.out().append(uppercase ? representation.toUpperCase() : representation);

            if (doPad && leftAlign) {
                formatter.out().append(padString);
            }
        } catch (IOException e) {
            ExceptionLogger.getConsumer().accept(e);
        }
    }
}
