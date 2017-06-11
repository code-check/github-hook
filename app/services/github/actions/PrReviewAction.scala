package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.events.PullRequestReviewEvent
import codecheck.github.models.{PullRequestReviewAction, PullRequestReviewState}
import codecheck.github.models.IssueInput
import scala.concurrent.ExecutionContext.Implicits.global

class PrReviewAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = msg match {
    case x: PullRequestReviewEvent =>
      x.action == PullRequestReviewAction.submitted
    case _ => false
  }

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    msg match {
      case x: PullRequestReviewEvent if x.review.state == PullRequestReviewState.approved =>
        val repo = x.repository
        api.getIssue(x.pull_request.number).map {
          case Some(issue) =>
            val newLabels = "Ship it!" :: issue.labels
              .filter(l => l.name != "Review me!" && l.name != "Fix me!")
              .map(_.name)
            api.editIssue(issue.number, IssueInput(
              labels = newLabels,
              assignee = Some(issue.user.login)
            ))
          case None =>
        }
    }
  }
}
