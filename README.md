GitHook
=======

Simple server to send e-mail notifications about GitHub push events.

Configuration Options
=====================
 * `RecipientConfig` - Path to a YAML file (see below)
 * `spring.mail.host` - SMTP Host (also [additional](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/mail/MailProperties.java) SMTP settings from Spring Boot)
 * `FromAddress` - From address on e-mails
 * `TestOnly` - Boolean (default `false`) - log but don't actually send notification e-mails
 * `SleepTime` - (default 10000) Time (ms) between checks to see if the RecipientConfig file has changed

If you run using the provided `main` method in `com.internetitem.githook.GithookReceiverServer` then you can customize the HTTP listener using Spring Boot's [ServerProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/ServerProperties.java) (for example, `server.port=9090`).

All configuration properties can either be passed on the command line in the format `--key=value` or using a standard Java Properties file named [`application.properties`](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config). The latter option is better when run as a WAR file in a Servlet container.

RecipientConfig
===============
YAML file to configure where to send notifications. There must be exactly one top-level element, `notifications` which must contain an array of structures, each of which should contain two arrays of strings, `repositories` and `emails`.

For example:

    notifications:
      - repositories: ["https://github.com/abatkin/GitHook"]
        emails: ["me@example.com"]

Repository URLs and e-mails can all appear multiple times.

Customization
=============
You may want to customize the embedded `logback.xml` file to change logging configuration. Also, the Freemarker template `push.ftl` can be used to change the format for e-mails. The model passed in is `com.internetitem.githook.dataModel.ws.GitHubPush`.
