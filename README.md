![](http://bastian-oppermann.de/javacord3_banner.png)
# Javacord <a href="#"><img src="https://img.shields.io/badge/Version-3.0.0-brightgreen.svg?&style=flat-square" alt="Latest version"></a> <a href="https://ci.javacord.org/javadoc/"><img src="https://img.shields.io/badge/JavaDoc-latest-yellow.svg?style=flat-square" alt="Latest JavaDocs"></a> <a href="https://github.com/BtoBastian/Javacord/wiki"><img src="https://img.shields.io/badge/Wiki-Home-red.svg?style=flat-square" alt="Javacord Wiki"></a> <a href="https://discord.gg/0qJ2jjyneLEgG7y3"><img src="https://img.shields.io/discord/151037561152733184.svg?colorB=%237289DA&label=Discord&style=flat-square" alt="Discord Server"></a>
A multithreaded but easy to use library to create a Discord bot in Java.

## IMPORTANT
This README is for the rewrite of Javacord (aka. Javacord 3) and not complete yet (e.g. the wiki is still for version 2.x).
If you have any trouble with Javacord 3, it's highly recommended to join the Javacord Discord server ([Invite](https://discord.gg/0qJ2jjyneLEgG7y3))
and ask for help there.

## Feature Coverage

Javacord covers every action a Discord bot is able to perform (e.g. sending messages, banning users, editing servers, etc.)
**besides** sending voice, which will be added in a later release. New features introduced by Discord are usually added
in less than one week, depending on their size.

## Download
The recommended way to "download" Javacord is to use a build manager like Maven.
If you are not familiar with Maven, you can take a look at the [Setup Guide](https://github.com/BtoBastian/Javacord/wiki#setup) 
or directly download it from [Jenkins](http://ci.ketrwu.de/job/Javacord/branch/master/lastSuccessfulBuild/).

**Repository**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
**Javacord Dependency**
```xml
<dependency>
    <groupId>de.btobastian.Javacord</groupId>
    <artifactId>javacord</artifactId>
    <!-- See below what to insert here -->
    <version>COMMIT_ID</version>
</dependency>
```
Replace `COMMIT_ID` with the latest commit id. Once the rewrite is finished, there will be proper version numbers.
In this example the version would be `5255914`:
![](https://i.imgur.com/FSAYqVq.png)

**Optional Logger Dependency**
```xml
<!-- Any SLF4J compatible logging framework. logback-classic is recommended -->
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.2.3</version>
</dependency>
```

## IDE Setup (for beginners)

If you never used maven before you should take a look at the setup tutorial:
* [IntelliJ Maven Setup](https://github.com/BtoBastian/Javacord3-Docs/wiki/How-to-setup-(IntelliJ-and-Maven))
* Eclipse Maven Setup - coming soon

## Support

* [Javacord server](https://discord.gg/0qJ2jjyneLEgG7y3) (recommended)
* [DiscordAPI #java_javacord channel](https://discord.gg/0SBTUU1wZTVXVKEo)

## Wiki

For detailed information take a look at the wiki: [Wiki](https://github.com/BtoBastian/Javacord3-Docs/wiki)

The wiki for Javacord 3 is a work in progress and not complete!

## JavaDocs
The latest JavaDocs can be found here: [JavaDocs](https://ci.javacord.org/javadoc/)

## Logging in

Logging in is very simple
```java
public class MyFirstBot {

    public static void main(String[] args) {
        String token = args[0];

        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
            
            // Add a listener which answers with "Pong!" if someone writes "!ping"
            api.addMessageCreateListener(event -> {
                if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                    event.getChannel().sendMessage("Pong!");
                }
            });
            
            // Print the invite url of your bot
            System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
            
        }).exceptionally(ExceptionLogger.get());
    }

}
```

You can also login blocking which throws an exception if the login failed:
```java
public class MyFirstBot {

    public static void main(String[] args) {
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

An example bot can be found here: [Example Bot](https://github.com/BtoBastian/JavacordExampleBot)

## How to create a bot user and get its token 

Click here: [Creating a Bot Account](https://github.com/BtoBastian/Javacord3-Docs/wiki/Creating-a-Bot-Account)

## Version numbers

The version number has the a 3-digit format: `major.minor.trivial`
* `major`: Increased extremely rarely to mark a major release (usually a rewrite affecting very huge parts of the library).
 You can expect this digit to not change for several years.
* `minor`: Any backwards incompatible change to the api. You can expect this digit to change about 1-3 times per year.
* `trivial`: A backwards compatible change to the **api**. This is usually an important bugfix (or a bunch of smaller ones)
 or a backwards compatible feature addition. You can expect this digit to change 1-2 times per month.
 
## Deprecation policy

A method/class which was marked as deprecated can be removed with the next minor release (but it will usually stay for
several minor releases). A minor release might remove a class/method without having it deprecated, but we will do our
best to deprecate it before removing it. We are unable to guarantee this though, because we might have to remove/replace
something due to changes made by Discord which we are unable to control. Usually you can expect a deprecated method/class
to stay for at least 6 months before it finally gets removed, but this is not guaranteed.

## Discord Server

Javacord has its own Discord Server. You can join it ([Invite](https://discord.gg/0qJ2jjyneLEgG7y3)) for support,
status updates or just chatting with other users.