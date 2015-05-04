package services.github

import akka.actor.Actor
import codecheck.github.api.GitHubAPI
import codecheck.github.events.GitHubEvent
import com.ning.http.client.AsyncHttpClient

class GitHubService(token: String, am: ActionManager)(implicit client: AsyncHttpClient) extends Actor {

  val api = GitHubAPI(token)
  def receive = { 
    case x: GitHubEvent => doProcess(x)
  }

  private def doProcess(msg: GitHubEvent) = {
    val repoApi = api.repositoryAPI(msg.repository.owner.login, msg.repository.name)
    am.get(msg).foreach(_.process(repoApi, msg))
  }

}
