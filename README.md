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