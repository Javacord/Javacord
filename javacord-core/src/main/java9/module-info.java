@SuppressWarnings("requires-transitive-automatic")
module org.javacord.core {
    requires logging.interceptor;
    requires xsalsa20poly1305;

    requires java.logging;

    requires transitive org.javacord.api;
    requires transitive okhttp;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive nv.websocket.client;
    requires transitive org.apache.logging.log4j;
    requires transitive io.vavr;

    requires transitive java.desktop;

    exports org.javacord.core.util to org.javacord.api;

    provides org.javacord.api.util.internal.DelegateFactoryDelegate
        with org.javacord.core.util.DelegateFactoryDelegateImpl;
}
