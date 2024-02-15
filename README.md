Fritz
-----
Fritz2 feature tests.

Notes
-----
1. Fritz2 lenses do not work. Neither does Arrow lenses. Is it a Ksp configuration issue?
2. Prefer Helidon, with virtual thread support, to Ktor and coroutines.

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
1. gradle jvmRun

Curl
----
1. curl -v http://localhost:7979/now

Logs
----
1. build/kotlin.log
2. build/kotlin.test.log

Resources
---------
* [Fritz2 Github](https://github.com/jwstegemann/fritz2)
* [Fritz2 Docs](https://www.fritz2.dev/docs/)