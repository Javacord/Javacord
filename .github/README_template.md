![](http://bastian-oppermann.de/javacord3_banner.png)
# Javacord <a href="#"><img src="https://shields.javacord.org/badge/Version-3.0.0-brightgreen.svg?&style=flat-square" alt="Latest version"></a> <a href="https://ci.javacord.org/javadoc/"><img src="https://shields.javacord.org/badge/JavaDoc-latest-yellow.svg?style=flat-square" alt="Latest JavaDocs"></a> <a href="https://github.com/Javacord/Javacord/wiki"><img src="https://shields.javacord.org/badge/Wiki-Home-red.svg?style=flat-square" alt="Javacord Wiki"></a> <a href="https://discord.gg/0qJ2jjyneLEgG7y3"><img src="https://shields.javacord.org/discord/151037561152733184.svg?colorB=%237289DA&label=Discord&style=flat-square" alt="Discord Server"></a>
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

The recommended way to get Javacord is to use a build manager like Gradle or Maven.  
If you are not familiar with build managers, you can follow this [Setup Guide](#ide-setup) 
or download it directly from
[TeamCity](https://ci.javacord.org/viewType.html?buildTypeId=Javacord_PublishSnapshots&branch_Javacord=v_3&tab=buildTypeStatusDiv&state=successful).
Just click on the latest build and there go to the "Artifacts" tab to download the files.

### Javacord Dependency

#### Gradle
```groovy
repositories { maven { url "https://oss.sonatype.org/content/repositories/snapshots/" } }
dependencies { compile 'org.javacord:javacord:$version' }
```

#### Maven
```xml
<repositories>
    <repository>
        <id>Sonatype Snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.javacord</groupId>
        <artifactId>javacord</artifactId>
        <version>$version</version>
        <type>pom</type>
    </dependency>
</dependencies>
```

### Optional Logger Dependency

Any SLF4J compatible logging framework can be used to provide a more sophisticated logging experience
with being able to configure log format, log targets (console, file, database, Discord direct message, ...),
log levels per class and much more.

For example Log4j in Gradle
```groovy
dependencies { runtime 'org.apache.logging.log4j:log4j-slf4j-impl:2.11.0' }
```

or Logback in Gradle
```groovy
dependencies { runtime 'ch.qos.logback:logback-classic:1.2.3' }
```

## IDE Setup

If you never used Maven before you should take a look at the setup tutorial:
* **[IntelliJ & Maven Setup](https://github.com/Javacord/Javacord/wiki/How-to-setup-(IntelliJ-and-Maven))**
* **[Eclipse & Maven Setup](https://github.com/Javacord/Javacord/wiki/How-to-setup-(Eclipse-and-Maven))**

## Support

Javacord's Discord community is an excellent resource if you have questions about the library.  
* **[The Javacord server](https://discord.gg/0qJ2jjyneLEgG7y3)**

## Wiki

For additional information, take a look at the [Javacord wiki](https://github.com/Javacord/Javacord/wiki).  
The wiki for Javacord 3 is a work in progress and not complete!

## JavaDoc

The JavaDoc for the latest snapshot build can be found on [TeamCity](https://ci.javacord.org/javadoc/).  

## Logging in

Logging in is very simple
```java
public class MyFirstBot {

    public static void main(String[] args) {
        // Read the token from the first program parameter when invoking the bot
        String token = args[0];

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {
            if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
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
        // Read the token from the first program parameter when invoking the bot
        String token = args[0];

        new DiscordApiBuilder() .setToken(token) .login() .thenAccept(api -> {
                    // Add a listener which answers with "Pong!" if someone writes "!ping"
                    api.addMessageCreateListener(event -> {
                        if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
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

Check out the [JavacordExampleBot](https://github.com/Javacord/JavacordExampleBot) to learn more.

## How to create a bot user and get its token 

* **[Creating a Bot User Account](https://github.com/Javacord/Javacord/wiki/Creating-a-Bot-Account)**

## Version numbers

The version number has the a 3-digit format: `major.minor.trivial`
* `major`: Increased extremely rarely to mark a major release (usually a rewrite affecting very huge parts of the library).
 You can expect this digit to not change for several years.
* `minor`: Any backwards incompatible change to the api. You can expect this digit to change about 1-3 times per year.
* `trivial`: A backwards compatible change to the **api**. This is usually an important bugfix (or a bunch of smaller ones)
 or a backwards compatible feature addition. You can expect this digit to change 1-2 times per month.
 
## Deprecation policy

A method or class which was marked as deprecated can be removed with the next minor release (but it will usually stay for
several minor releases). A minor release might remove a class or method without having it deprecated, but we will do our
best to deprecate it before removing it. We are unable to guarantee this though, because we might have to remove / replace
something due to changes made by Discord which we are unable to control. Usually you can expect a deprecated method or
class to stay for at least 6 months before it finally gets removed, but this is not guaranteed.

## Discord Server

Join the [Javacord server](https://discord.gg/0qJ2jjyneLEgG7y3) for support, status updates, or just chatting with other users.
