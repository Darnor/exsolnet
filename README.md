# exsolnet
Online platform to provide any exercises and to share your solution. Based on a voting-system inspired by StackOverflow.

The exsolnet application  is currently only available in german - i18n is yet to be implemented.

## Requirements
install postgres v9.4<

run the commands in createdb.sql on your psql console

# Development
Run the app with `sbt run`

Run tests with `sbt test`

* For the selenium tests to work you might need to downgrade your firefox (The newest version sometimes does not work with selenium)

# Deployment
Build the app with `sbt dist` and unpack the resulting ./target/universal/exsolnet-xxx.zip on your server.

Run it with

```
exsolnet-xxx/bin/exsolnet -Dplay.crypto.secret=secret
```

for additional configuration options see the [wiki](https://github.com/darnor/exsolnet/wiki) (add them with -D)

The process does not fork by itself. Consider using [daemonize](http://software.clapper.org/daemonize/) or `tmux` to keep it running without your terminal.

# Caveats

It is not possible for users to create Categories (nor is it intended), yet they are a mandatory field on creating exercises. Until an administration GUI is implemented to add categories, they have to be added manually to the database, i.e

```
INSERT INTO tag VALUES (8000, 'SE1', 'Software Engineering 1', true);
```
