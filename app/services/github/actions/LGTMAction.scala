package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.events.IssueCommentEvent
import codecheck.github.models.IssueEditParams

class LGTMAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = msg match {
    case x: IssueCommentEvent =>
      x.comment.body.toUpperCase.indexOf("LGTM") != -1
    case _ => false
  }

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    val (number, opener, labels) = msg match {
      case x: IssueCommentEvent => (x.issue.number, x.issue.user.name, x.issue.labels)
    }
    val newLabels = "Review me!" :: labels
      .filter(_.name != "Ship it!")
      .map(_.name)
    api.editIssue(number, IssueEditParams(
      labels = newLabels,
      assignee = Some(opener)
    ))
  }
}