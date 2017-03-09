# GitHub Hook
Receiving any GitHub events and do something.

## How to develop
Currently this app depends on SNAPSHOT version of [github-api-scala](https://github.com/code-check/github-api-scala).  
Because now we are developing both concurrency.

So, at first you have to build github-api-scala in your local environment and publish it to local.
```
git clone git@github.com:code-check/github-api-scala.git
cd github-api-scala
sbt publishLocal

```

If you changed the code of github-api-scala, you have to do sbt publishLocal again and restart Playframework.

## Receiving GitHub events with local environment
You can use [ngrok](https://ngrok.com/).

Make a tunnel by it, and set webhook to its adress.
