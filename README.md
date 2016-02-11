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
  <version>2.0.0</version>
</dependency>
```

#Download
For those of you how don't use maven: http://ci.ketrwu.de/job/Javacord/lastSuccessfulBuild/

Thanks to ketrwu (https://github.com/KennethWussmann).

#Javadocs
The javadocs can be found here: http://ci.ketrwu.de/job/Javacord/javadoc/

Thanks to ketrwu, too.

#How to connect
First of all you have to get an api instance:
```java
DiscordAPI api = Javacord.getApi("your@mail.com", "creativePassword");
```
There are two ways how to connect to your account.
- Blocking:
```java
api.connectBlocking();
```
This will freeze your current Thread until the ready packet was received.
- Non-Blocking:
```java
api.connect(new FutureCallback<DiscordAPI>() {
    @Override
    public void onSuccess(DiscordAPI api) {
        System.out.println("Bot logged in!");
    }

    @Override
    public void onFailure(Throwable t) {
        // login failed
        t.printStackTrace();
    }
});
```
The non-blocking version won't block your main thread and informs you when the connection succeeded or failed.

Now you're connected. :)

#Listeners
- ChannelChangeNameListener
- ChannelChangePositionListener
- ChannelChangeTopicListener
- ChannelCreateListener
- MessageCreateListener
- MessageDeleteListener
- MessageEditListener
- TypingStartListener
- RoleChangeNameListener
- RoleChangeOverwrittenPermissionsListener
- RoleChangePermissionsListener
- RoleChangePositionListener
- RoleCreateListener
- RoleDeleteListener
- ServerJoinListener
- ServerMemberAddListener
- ServerMemberRemoveListener
- UserChangeOverwrittenPermissionsListener

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
