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
  <version>1.0.10</version>
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
DiscordAPI api = Javacord.getApi();
```
Now you have to set your email and password:
```java
api.setEmail("your@mail.com");
api.setPassword("creativePassword");
```
Cause the connection is non-blocking the ReadyListener informs you, when the api is connected:
```java
api.connect(new ReadyListener() {

  @Override
  public void onReady() {
    System.out.println("Connected");
    // go on doing funny stuff
  }
  
  @Override
  public void onFail() {
    System.out.println("Connection failed!");
  }

});
```
Now you're connected. :)

#Listeners
- MessageCreateListener
- MessageEditListener
- TypingStartListener
- MessageDeleteListener
- UserChangeNameListener
- RoleCreateListener
- RoleChangeNameListener
- RoleChangePermissionsListener
- ServerMemberAddListener
- ServerMemberRemoveListener
- UserChangeRoleListener
- ServerJoinListener

#Examples

Creating a simple ping-pong bot:
```java
api = Javacord.getApi();
api.setEmail("your@mail.com");
api.setPassword("creativePassword");

api.connect(new ReadyListener() {

  @Override
  public void onReady() {
    System.out.println("Connected");
    
    // example how to register a listener
    api.registerListener(new MessageCreateListener() {
 
      @Override
      public void onMessageCreate(DiscordAPI api, Message message) {
        if (message.getContent().equals("ping")) {
          message.reply("pong");
        }
      }
    });
    
    // go on doing funny stuff
  }
  
  @Override
  public void onFail() {
    System.out.println("Connection failed!");
    System.exit(-1);
  }

});
```

Using the MessageBuilder:
```java
channel.sendMessage(new MessageBuilder()
  .appendCode("java", "System.out.println(\"Hi\");")
  .appendDecoration(MessageDecoration.UNDERLINE_BOLD, "That's a test!\n")
  .appendMention(api.getUserById("98487852668563456"))
  .build());
```
will result in something like this:
![alt tag](http://screenshots.bastian-oppermann.de/01.01.2016-15-44-23.png)

#Comming soon

- File Upload
- Voice Support
- Tons of new Listeners
