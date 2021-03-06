package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.events.IssueCommentEvent
import codecheck.github.models.IssueInput
import scala.concurrent.ExecutionContext.Implicits.global

class LGTMAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = msg match {
    case x: IssueCommentEvent =>
      x.comment.body.toUpperCase.indexOf("LGTM") != -1
    case _ => false
  }

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    val (number, opener, commenter, labels) = msg match {
      case x: IssueCommentEvent => (x.issue.number, x.issue.user.login, x.comment.user.login, x.issue.labels)
    }
    val newLabels = "Ship it!" :: labels
      .filter(l => l.name != "Review me!" && l.name != "Fix me!")
      .map(_.name)
    api.editIssue(number, IssueInput(
      labels = newLabels,
      assignee = Some(opener)
    ))
    api.removeReviewRequest(number, commenter)
  }
}
