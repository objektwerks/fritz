Fritz
-----
Fritz2 feature tests.

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
1. gradle jvmRun ( fail: No main class specified and classpath is not an executable jar. )
2. ./gradlew jvmRun ( fail: No main class specified and classpath is not an executable jar. )
3. ./gradlew run ( fail: Could not find or load main class objektwerks.Server )
4. Right click Server > Run 'Server' ( success )

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