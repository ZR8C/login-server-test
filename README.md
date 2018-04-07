# Testing a login server
Logins are persisted for an hour both in memory and in DB with potential for multiple servers.

## Screenshots

### Register
![register success](https://i.imgur.com/1vdZDQr.png)
![register failure](https://i.imgur.com/mCWHuBh.png)

### Login
![login success](https://i.imgur.com/urShgNa.png)
![login failure](https://i.imgur.com/NcHmLbs.png)

### Token check
![token check success](https://i.imgur.com/kovskLK.png)
![token check failure](https://i.imgur.com/liryZKv.png)

# Stack
- [Requery](https://github.com/requery/requery) for object mapping / sql generation.
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt) for hashing passwords in the DB.
- [H2](http://www.h2database.com) for easy to use test DB.
- [Jetty](https://www.eclipse.org/jetty/) easy to use in memory web server.
- [Jackson](https://github.com/FasterXML/jackson) for mapping objects to json.
- [Jackson Kotlin](https://github.com/FasterXML/jackson-module-kotlin) jackson especially for kotlin.
- [Caffeine](https://github.com/ben-manes/caffeine) used for easy caching.