# Javacord <a href="#"><img src="https://img.shields.io/badge/Version-2.0.14-brightgreen.svg" alt="Latest version"></a> <a href="http://ci.ketrwu.de/job/Javacord/branch/master/javadoc/"><img src="https://img.shields.io/badge/JavaDoc-latest-yellow.svg" alt="Latest JavaDocs"></a> <a href="https://github.com/BtoBastian/Javacord/wiki"><img src="https://img.shields.io/badge/Wiki-Home-red.svg" alt="Latest JavaDocs"></a>
A multithreaded but simple to use library to create a Discord bot in Java.

##  Maven
```xml
<repository>
  <id>javacord-repo</id>
  <url>http://repo.bastian-oppermann.de</url>
</repository>
...
<dependency>
  <groupId>de.btobastian.javacord</groupId>
  <artifactId>javacord</artifactId>
  <version>2.0.14</version>
   <!-- This will use the shaded javacord which contains all required dependencies -->
  <classifier>shaded</classifier>
</dependency>
<!-- A SLF4J comaptible logging framework. I would recommend to use logback -->
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.1.3</version>
</dependency>
```

## IDE Setup (for beginners)

If you never used maven before you should take a look at the setup tutorial:
* [Eclipse Setup](https://github.com/BtoBastian/Javacord/wiki/How-to-setup-(Eclipse-and-Maven))
* [IntelliJ Setup](https://github.com/BtoBastian/Javacord/wiki/How-to-setup-(IntelliJ-and-Maven))

## Support
 
* [Javacord server](https://discord.gg/0qJ2jjyneLEgG7y3)
* [DiscordAPI #java_javacord channel](https://discord.gg/0SBTUU1wZTVXVKEo)

## Wiki

For detailed information take a look at the wiki: [Wiki](https://github.com/BtoBastian/Javacord/wiki)

## Download
For those of you how don't use maven: [Jenkins](http://ci.ketrwu.de/job/Javacord/branch/master/lastSuccessfulBuild/)

Thanks to ketrwu (https://github.com/KennethWussmann).

## Javadocs
The javadocs can be found here: [JavaDocs](http://ci.ketrwu.de/job/Javacord/branch/master/javadoc/)

Thanks to ketrwu, too.

## Examples

Creating a simple ping-pong bot:
```java
package <package>;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

/**
 * A simple ping-pong bot.
 */
public class MyPingPongBot {

    public MyPingPongBot(String token) {
        // See "How to get the token" below
        DiscordAPI api = Javacord.getApi(token, true);
        // connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate(DiscordAPI api, Message message) {
                        // check the content of the message
                        if (message.getContent().equalsIgnoreCase("ping")) {
                            // reply to the message
                            message.reply("pong");
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
```
More examples can be found in the wiki: [Examples](https://github.com/BtoBastian/Javacord/wiki/Examples)

## How to get the token

**1.** Open https://discordapp.com/developers/applications/me and click on "New App".

**2.** Enter a name for your bot and click "Create App"

**3.** Click on "Create a Bot user"

**4.** Reveal the bot's token. This token is used to login your bot.

>![](http://i.imgur.com/EbexbiD.gif)

## How to add a bot to your server

In order to add a bot to your server you need it's client id.

You can get your client id from the [same page](https://discordapp.com/developers/applications/me) where you created it. 

>![](http://i.imgur.com/qzPDsp2.png)

With this id you can create an invite link for your bot:

**https://discordapp.com/api/oauth2/authorize?client_id=123456789&scope=bot&permissions=0**

If you are the owner or admin of the server you can use this link to add your bot to your server. Otherwise you have to give the link to the server owner/admin and ask him to add your bot.

## Command Framework

I would recommend to use [sdcf4j](https://github.com/BtoBastian/sdcf4j) in order to create commands. It provides a clean and simple way to create commands. A ping-pong command would be as easy as this:
```java
public class PingCommand implements CommandExecutor {

    @Command(aliases = {"!ping"}, description = "Pong!")
    public String onCommand(String command, String[] args) {
        return "Pong!";
    }

}
```
Take a look at the [sdcf4j wiki](https://github.com/BtoBastian/sdcf4j/wiki) to find out how it works.
