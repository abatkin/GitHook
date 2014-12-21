GitHook
=======

Simple server to send e-mail notifications about GitHub push events.

Configuration Options
=====================
 * `RecipientConfig` - Path to a YAML file (see below)
 * `spring.mail.host` - SMTP Host
 * `FromAddress` - From address on e-mails
 * `TestOnly` - Boolean (default `false`) - log but don't actually send notification e-mails
 * `SleepTime` - (default 10000) Time (ms) between checks to see if the RecipientConfig file has changed
 
RecipientConfig
===============
YAML file to configure where to send notifications. There must be exactly one top-level element, `notifications` which must contain an array of structures, each of which should contain two arrays of strings, `repositories` and `emails`.

For example:

notifications:
  - repositories: ["https://github.com/abatkin/GitHook"]
    emails: ["me@example.com"]

Repository URLs and e-mails can all appear multiple times.
