# Javacord
A simple library to create a discord bot.
Supports the new application system!

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
  <version>2.0.11</version>
   <!-- This will use the shaded javacord which contains all required dependencies -->
  <classifier>shaded</classifier>
</dependency>
<!-- A SLF4J comaptible logging framework. I would recommend to use logback -->
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.0.13</version>
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
        // Get the token from here: https://discordapp.com/developers/applications/me
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