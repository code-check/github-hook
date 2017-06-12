package services.github.actions

import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.DefaultEvent
import codecheck.github.events.GitHubEvent
import codecheck.github.events.IssueEvent
import codecheck.github.events.IssueCommentEvent
import codecheck.github.events.PullRequestEvent
import codecheck.github.models.PullRequestAction

class PriorityAction extends GitHubAction {

  val labels = "Urgent" :: "High" :: "Middle" :: "Low" :: Nil

  private def getPriority(msg: GitHubEvent): Option[String] = {
    val text = msg match {
      case x: IssueEvent if x.get("action") == "opened" =>
        x.get("issue.body")
      case x: DefaultEvent if x.name == "issues" && x.get("action") == "opened" =>
        x.get("issue.body")
      case x: PullRequestEvent if x.action == PullRequestAction.opened =>
        x.pull_request.body
      case x: IssueCommentEvent => x.comment.body
      case _ => ""
    }
    text.toLowerCase match {
      case x: String if x.indexOf("priority:urgent") != -1 => Some("Urgent")
      case x: String if x.indexOf("priority:high") != -1 => Some("High")
      case x: String if x.indexOf("priority:middle") != -1 => Some("Middle")
      case x: String if x.indexOf("priority:low") != -1 => Some("Low")
      case _ => None
    }

  }
  def isMatch(msg: GitHubEvent): Boolean = getPriority(msg).isDefined

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    def getNumber(msg: GitHubEvent): Long = msg match {
      case x: IssueEvent => x.get("issue.number").toLong
      case x: PullRequestEvent => x.pull_request.number
      case x: IssueCommentEvent => x.issue.number
      case x: DefaultEvent => x.opt("issue.number").map(_.toLong).getOrElse(-1)
    }
    def getLabels(msg: GitHubEvent): List[String] = msg match {
      case x: IssueCommentEvent => x.issue.labels.map(_.name)
      case _ => Nil
    }
    getPriority(msg) match {
      case Some(newLabel) => doProcess(api, getNumber(msg), getLabels(msg), newLabel)
      case None =>
    }
  }

  private def doProcess(api: RepositoryAPI, number: Long, oldLabels: List[String], newLabel: String): Unit = {
    Akka.system.scheduler.scheduleOnce(2 seconds) {
      oldLabels.filter(oldLabel => labels.exists(v => oldLabel == v && oldLabel != newLabel)).foreach { x => api.removeLabel(number, x)}
      if (!oldLabels.contains(newLabel)) {
        api.addLabels(number, newLabel)
      }
    }
  }
}
