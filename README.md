# Javacord
A simple library to create a discord bot.

#Maven
```
<repository>
  <id>javacord-repo</id>
  <url>http://repo.bastian-oppermann.de</url>
</repository>
...
<dependency>
  <groupId>de.btobastian.javacord</groupId>
  <artifactId>javacord</artifactId>
  <version>2.0.2</version>
</dependency>
```

#Wiki

For detailed information take a look at the wiki: https://github.com/BtoBastian/Javacord/wiki

#Download
For those of you how don't use maven: http://ci.ketrwu.de/job/Javacord/lastSuccessfulBuild/

Thanks to ketrwu (https://github.com/KennethWussmann).

#Javadocs
The javadocs can be found here: http://ci.ketrwu.de/job/Javacord/javadoc/

Thanks to ketrwu, too.

#Examples

Creating a simple ping-pong bot:
```java
DiscordAPI api = Javacord.getApi("your@mail.com", "creativePassword");
api.connect(new FutureCallback<DiscordAPI>() {
    @Override
    public void onSuccess(DiscordAPI api) {
        api.registerListener(new MessageCreateListener() {
            @Override
            public void onMessageCreate(DiscordAPI api, Message message) {
                if (message.getContent().equalsIgnoreCase("ping")) {
                    message.reply("pong");
                }
            }
        });
    }

    @Override
    public void onFailure(Throwable t) {
        // login failed
        t.printStackTrace();
    }
});
```