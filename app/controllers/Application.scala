package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import codecheck.github.api.GitHubAPI
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  val api = new GitHubAPI(Play.configuration.getString("github.token").get)

  def index = Action.async {

    api.getOrganization("code-check").map { org =>
      Ok(org.toString)
    }
  }

}