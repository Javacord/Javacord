@SuppressWarnings("module")
module org.javacord.api {
    requires transitive java.desktop;

$exports

    uses org.javacord.api.util.internal.DelegateFactoryDelegate;
}
