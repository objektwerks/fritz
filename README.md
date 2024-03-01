Fritz
-----
>Fritz2 web app using Ktor, Exposed, Aedile(Caffeine), H2 and Kotlin 2.0.

Todo
----
1. Build Fritz2 jsMain.

Notes
-----
1. Fritz2 lenses do not work. Neither do Arrow lenses. Is it a Ksp configuration issue or defect?
2. This project mimics [Pool Balance](https://github.com/objektwerks/pool.balance.w), less Postgresql and JoddMail.

Build
-----
1. gradle clean build

Test
----
1. gradle clean build test

Run JS
------
1. gradle jsRun

Run JVM
-------
>The default port is: 7979
1. gradle jvmRun [--args="7676"]

Curl
----
1. curl -v http://localhost:7979/now
2. curl -X POST http://localhost:7979/command -H "Content-Type: application/json" -d '{"email":"my@email.com"}'

Logs
----
1. build/fritz.log
2. build/fritz.test.log

Postgresql
----------
1. config:
    1. on osx intel: /usr/local/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
    2. on osx m1: /opt/homebrew/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
2. run:
    1. brew services start postgresql@14
3. logs:
    1. on osx intel: /usr/local/var/log/postgres.log
    2. on m1: /opt/homebrew/var/log/postgres.log

Database
--------
>Example database url: postgresql://localhost:5432/poolbalance?user=mycomputername&password=poolbalance"
1. psql postgres
2. CREATE DATABASE poolbalance OWNER [your computer name];
3. GRANT ALL PRIVILEGES ON DATABASE poolbalance TO [your computer name];
4. \l
5. \q
6. psql poolbalance
7. \i ddl.sql
8. \q

DDL
---
>Alternatively run: psql -d poolbalance -f ddl.sql
1. psql poolbalance
2. \i ddl.sql
3. \q

Drop
----
1. psql postgres
2. drop database poolbalance;
3. \q

Environment
-----------
>The following environment variables must be defined:
```
export FRITZ_POSTGRESQL_URL="jdbc:postgresql://localhost:5432/fritz"
export FRITZ_POSTGRESQL_DRIVER="org.postgresql.Driver"
export FRITZ_POSTGRESQL_USER="tripletail"
export FRITZ_POSTGRESQL_PASSWORD="fritz"
```

Resources
---------
* [Fritz2 Github](https://github.com/jwstegemann/fritz2)
* [Fritz2 Docs](https://www.fritz2.dev/docs/)