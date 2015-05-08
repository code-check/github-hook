# GitHub Hook
Receiving any GitHub events and do something.

## How to develop
Currently this app depends on SNAPSHOT version of [github-api-scala](https://github.com/code-check/github-api-scala).  
Because now we are developing both concurrency.

So if you modified github-api-scala, you have to copy jar file.
```
git clone git@github.com:code-check/github-api-scala.git
cd github-api-scala
sbt package
cp target/scala-2.11/github-api_2.11-x.x.x-SNAPSHOT.jar [GITHUBHOOK_HOME]/lib/

```

## Receiving GitHub events with local environment
You can use [ngrok](https://ngrok.com/).

Make a tunnel by it, and set webhook to its adress.
