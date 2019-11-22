![](https://javacord.org/img/javacord3_banner.png)
# Javacord [![Latest version](https://shields.javacord.org/github/release/Javacord/Javacord.svg?label=Version&colorB=brightgreen&style=flat-square)](https://github.com/Javacord/Javacord/releases/latest) [![Latest JavaDocs](https://shields.javacord.org/badge/JavaDoc-Latest-yellow.svg?style=flat-square)](https://docs.javacord.org/api/v/latest/) [![Javacord Wiki](https://shields.javacord.org/badge/Wiki-Home-red.svg?style=flat-square)](https://javacord.org/wiki/) [![Javacord Discord server](https://shields.javacord.org/discord/151037561152733184.svg?colorB=%237289DA&label=Discord&style=flat-square)](https://discord.gg/0qJ2jjyneLEgG7y3)
An easy to use multithreaded library for creating Discord bots in Java.

## Feature Coverage

Javacord supports every action a Discord bot is able to perform.  
Some of these features include, but are not limited to:

- sending messages
- attaching files
- event listening
- user management
- server administration
- *... **and many others***

**Sending voice will be added in an upcoming release**.  
New features introduced by Discord are typically added in less than one week, depending on scale.

## Download / Installation

The recommended way to get Javacord is to use a build manager, like Gradle or Maven.  
If you are not familiar with build managers, you can follow this [Setup Guide](#ide-setup) 
or download Javacord directly from [GitHub](https://github.com/Javacord/Javacord/releases/latest).

### Javacord Dependency

#### Gradle
```groovy
repositories { mavenCentral() }
dependencies { implementation 'org.javacord:javacord:3.0.5' }
```

#### Maven
```xml
<dependency>
    <groupId>org.javacord</groupId>
    <artifactId>javacord</artifactId>
    <version>3.0.5</version>
    <type>pom</type>
</dependency>
```

### Optional Logger Dependency

Any Log4j-2-compatible logging framework can be used to provide a more sophisticated logging experience
with being able to configure log format, log targets (console, file, database, Discord direct message, ...),
log levels per class, and much more.

For example, Log4j Core in Gradle
```groovy
dependencies { runtimeOnly 'org.apache.logging.log4j:log4j-core:2.11.0' }
```
Take a look at the [Logger Configuration](https://javacord.org/wiki/basic-tutorials/logger-configuration/) wiki article for further information.

## IDE Setup

If you have never used Gradle or Maven before, you should take a look at one of the setup tutorials:
* **[IntelliJ & Gradle](https://javacord.org/wiki/getting-started/intellij-gradle/)** _(recommended)_
* **[IntelliJ & Maven](https://javacord.org/wiki/getting-started/intellij-maven/)**
* **[Eclipse & Maven](https://javacord.org/wiki/getting-started/eclipse-maven/)**

## Support

Javacord's Discord community is an excellent resource if you have questions about the library.  
* **[The Javacord Server](https://discord.gg/0qJ2jjyneLEgG7y3)**

## Documentation

* The [Javacord Wiki](https://javacord.org/wiki/) is a great place to get started 
* Additional documentation can be found in the [JavaDoc](https://docs.javacord.org/api/v/latest/)

## Logging in

Logging in is very simple
```java
public class MyFirstBot {

    public static void main(String[] args) {
        // Insert your bot's token here
        String token = "your token";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });

        // Print the invite url of your bot
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }

}
```

You can also login non-blocking asynchronously
```java
public class MyFirstBot {

    public static void main(String[] args) {
        // Insert your bot's token here
        String token = "your token";

        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
                    // Add a listener which answers with "Pong!" if someone writes "!ping"
                    api.addMessageCreateListener(event -> {
                        if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                            event.getChannel().sendMessage("Pong!");
                        }
                    });

                    // Print the invite url of your bot
                    System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
                })
                // Log any exceptions that happened
                .exceptionally(ExceptionLogger.get());
    }

}
```

Check out the [Example Bot](https://github.com/Javacord/Example-Bot) to learn more.

## How to create a bot user and get its token 

* **[Creating a Bot User Account](https://javacord.org/wiki/essential-knowledge/creating-a-bot-account/)**

## Version numbers

The version number has a 3-digit format: `major.minor.trivial`
* `major`: Increased extremely rarely to mark a major release (usually a rewrite affecting very huge parts of the library).
 You can expect this digit to not change for several years.
* `minor`: Any backwards incompatible change to the api. You can expect this digit to change about 1-3 times per year.
* `trivial`: A backwards compatible change to the **api**. This is usually an important bugfix (or a bunch of smaller ones)
 or a backwards compatible feature addition. You can expect this digit to change 1-2 times per month.
 
## Deprecation policy

A method or class that is marked as deprecated can be removed with the next minor release (but it will usually stay for
several minor releases). A minor release might remove a class or method without having it deprecated, but we will do our
best to deprecate it before removing it. We are unable to guarantee this though, because we might have to remove / replace
something due to changes made by Discord, which we are unable to control. Usually you can expect a deprecated method or
class to stay for at least 6 months before it finally gets removed, but this is not guaranteed.

## Discord Server

Join the [Javacord Server](https://discord.gg/0qJ2jjyneLEgG7y3) for support, status updates, or just chatting with other users.
