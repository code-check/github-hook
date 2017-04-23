package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._
import play.api.libs.ws.WSClient
import akka.actor.{ActorSystem, Props}
import scala.concurrent.ExecutionContext.Implicits.global

import com.github.tototoshi.play2.json4s.jackson.Json4s

import codecheck.github.api.GitHubAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.api.OAuthAPI
import codecheck.github.transport.asynchttp20.AsyncHttp20Transport
import org.asynchttpclient.AsyncHttpClient
import services.github.GitHubService
import services.github.ActionManager
import models.AsyncHttpClientHolder.instance

import services.github.actions._
import org.json4s._

class ApplicationController @Inject() (ws: WSClient, json4s: Json4s, actorSystem: ActorSystem) extends Controller {

  import json4s._
  implicit val formats = DefaultFormats

  implicit val transport = new AsyncHttp20Transport(ws.underlying[AsyncHttpClient])

  val clientId: String = sys.env.getOrElse("GITHUB_CLIENT_ID", "")
  val clientSecret: String = sys.env.getOrElse("GITHUB_CLIENT_SECRET", "")

  private val gh = actorSystem.actorOf(Props(
    new GitHubService(
      sys.env("GITHUB_TOKEN"),
      new ActionManager(List(
        new LogAction(),
        new ReviewMeAction(),
        new LGTMAction(),
        new FixMeAction(),
        new PriorityAction()
      ))
    )
  ))

  private def callbackUrl[T](implicit request: Request[T]) = {
    val protocol = if (request.secure) "https" else "http"
    s"${protocol}://${request.host}/login/github/callback"
  }

  def index = Action { implicit request =>
    val oauth = OAuthAPI(clientId, clientSecret, callbackUrl)
    val url = oauth.requestAccessUri("user", "repo")
    Ok(views.html.index(url))
  }

  def hook = Action(json) { request =>
    val name = request.headers("X-Github-Event")
    val msg = GitHubEvent(name, request.body)
    gh ! msg
    Ok("OK")
  }

  def githubCallback = Action.async { implicit request =>
    val oauth = OAuthAPI(clientId, clientSecret, callbackUrl)
    val code: String = request.getQueryString("code").getOrElse("")
    oauth.requestToken(code).map(token =>
      Ok(token.access_token + ", " + token.token_type + ", " + token.scope)
    )
  }

}
