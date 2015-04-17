# GitHub Hook

## How to develop
Currently this app depends on SNAPSHOT version of [github-api-scala](https://github.com/code-check/github-api-scala).  
Because now we are developing both concurrency.

SNAPSHOT version of github-api-scala is not published to our [sbt-repo](https://github.com/givery-technology/sbt-repo).

So, at first you have to build github-api-scala in your local environment and publish it to local.

```
git clone git@github.com:code-check/github-api-scala.git
cd github-api-scala
sbt publishLocal
```

If you changed the code of github-api-scala, you have to do sbt publishLocal again and restart Playframework.

