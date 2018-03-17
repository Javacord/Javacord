package org.javacord.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class contains some helpers to handle classes.
 */
public class ClassHelper {

    private ClassHelper() {
        throw new UnsupportedOperationException("You cannot create an instance of this class");
    }

    /**
     * Get all interfaces of the given class including extended interfaces and interfaces of all superclasses.
     * If the given class is an interface, it will be included in the result, otherwise not.
     *
     * @param clazz The class to get the interfaces for.
     * @return The interfaces of the given class.
     */
    public static List<Class<?>> getInterfaces(Class<?> clazz) {
        return getInterfacesAsStream(clazz).collect(Collectors.toList());
    }

    /**
     * Get a stream of all interfaces of the given class including extended interfaces and interfaces of all
     * superclasses.
     * If the given class is an interface, it will be included in the result, otherwise not.
     *
     * @param clazz The class to get the interfaces for.
     * @return The stream of interfaces of the given class.
     */
    public static Stream<Class<?>> getInterfacesAsStream(Class<?> clazz) {
        return getSuperclassesAsStream(clazz, true)
                .flatMap(superClass -> Stream.concat(
                        superClass.isInterface() ? Stream.of(superClass) : Stream.empty(),
                        Arrays.stream(superClass.getInterfaces()).flatMap(ClassHelper::getInterfacesAsStream)))
                .distinct();
    }

    /**
     * Get all superclasses of the given class.
     * If the given class is an interface, the result will be empty.
     * The given class will not be included in the result.
     *
     * @param clazz The class to get the superclasses for.
     * @return The superclasses of the given class.
     */
    public static List<Class<?>> getSuperclasses(Class<?> clazz) {
        return getSuperclassesAsStream(clazz).collect(Collectors.toList());
    }

    /**
     * Get a stream of all superclasses of the given class.
     * If the given class is an interface, the result will be empty.
     * The given class will not be included in the result.
     *
     * @param clazz The class to get the superclasses for.
     * @return The stream of superclasses of the given class.
     */
    public static Stream<Class<?>> getSuperclassesAsStream(Class<?> clazz) {
        return getSuperclassesAsStream(clazz, false);
    }

    /**
     * Get a stream of all superclasses of the given class.
     * If the given class is an interface, the result will be empty, except if {@code includeArgument} is {@code true}.
     * Whether the given class will be included in the result is controlled via the parameter {@code includeArgument}.
     *
     * @param clazz The class to get the superclasses for.
     * @param includeArgument Whether to include the given class in the result.
     * @return The stream of superclasses of the given class.
     */
    private static Stream<Class<?>> getSuperclassesAsStream(Class<?> clazz, boolean includeArgument) {
        return Stream.concat(includeArgument ? Stream.of(clazz) : Stream.empty(),
                             Optional.ofNullable(clazz.getSuperclass())
                                     .map(Stream::of)
                                     .orElseGet(Stream::empty)
                                     .flatMap(superclass -> getSuperclassesAsStream(superclass, true)));
    }

}
