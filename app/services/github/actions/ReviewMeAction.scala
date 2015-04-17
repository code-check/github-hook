package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.events.IssueCommentEvent
import codecheck.github.events.PullRequestEvent
import codecheck.github.models.IssueEditParams
import codecheck.github.models.PullRequestAction

class ReviewMeAction extends GitHubAction {
  private def findAssignee(text: String): Option[String] = {
    text.split("\n").map(_.split("[\t\r\n\\., ]")).flatMap { words =>
      val user = words.find(_.startsWith("@"))
      val review = words.find(_.equalsIgnoreCase("review")).isDefined

      if (review) user else None
    }.headOption
  }
  def isMatch(msg: GitHubEvent): Boolean = {
    val text = msg match {
      case x: PullRequestEvent if x.action == PullRequestAction.opened => 
        x.pull_request.body
      case x: IssueCommentEvent => x.comment.body
      case _ => ""
    }
    text.split("\n").map(_.split("[\t\r\n\\., ]")).filter { words =>
      words.find(_.startsWith("@")).isDefined && 
      words.find(_.equalsIgnoreCase("review")).isDefined
    }.length == 0
  }

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    val (number, labels, text) = msg match {
      case x: PullRequestEvent => (
        x.pull_request.number,
        Nil,
        x.pull_request.body
      )
      case x: IssueCommentEvent => (
        x.issue.number, 
        x.issue.labels,
        x.comment.body
      )
    }
    val newLabels = "Review me!" :: labels
      .filter(_.name != "Fix me!")
      .map(_.name)
    api.editIssue(number, IssueEditParams(
      labels = newLabels,
      assignee = findAssignee(text)
    ))
  }
}