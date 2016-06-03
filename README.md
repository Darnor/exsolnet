# exsolnet
Modifizierte Version des README fuer EPJ Projektabgabe


## Requirements
install postgres v9.4<

run the commands in createdb.sql on your psql console

# Development
Run the app with `sbt run`

Run tests with `sbt test` (this will also insert some test data into the database)

* For the selenium tests to work you might need to downgrade your firefox (Selenium sometimes fails to communicate with newer verions of firefox)

Alternatively you can use IntelliJ IDEA Ultimate play integration to run it from IDE.

# Deployment
Build the app with `sbt dist` and unpack the resulting ./target/universal/exsolnet-xxx.zip on your server. EPJ: Hab ich schon laufen gelassen

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
