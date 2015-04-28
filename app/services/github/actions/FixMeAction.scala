package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent
import codecheck.github.events.IssueCommentEvent
import codecheck.github.models.IssueInput

class FixMeAction extends GitHubAction {
  private def hasFixMe(text: String): Boolean = {
    text.toLowerCase.split("\n").map(_.split("[\t\r\n\\., ]")).filter { words =>
      val fix = words.indexOf("fix")
      val me = words.indexOf("me")
      fix >= 0 && me == fix + 1
    }.length > 0
  }
  private def findAssignee(text: String): Option[String] = {
    text.split("\n").map(_.split("[\t\r\n\\., ]")).flatMap { words =>
      val user = words.find(_.startsWith("@")).map(_.substring(1))
      val fix = words.indexWhere(_.toLowerCase == "fix")
      val me = words.indexWhere(_.toLowerCase == "me")

      if (fix >= 0 && me == fix + 1) user else None
    }.headOption
  }
  def isMatch(msg: GitHubEvent): Boolean = msg match {
    case x: IssueCommentEvent =>
      hasFixMe(x.comment.body)
    case _ => false
  }

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    val (number, opener, labels, text) = msg match {
      case x: IssueCommentEvent => (
        x.issue.number, 
        x.issue.user.name, 
        x.issue.labels,
        x.comment.body
      )
    }
    val newLabels = "Fix me!" :: labels
      .filter(l => l.name != "Review me!" && l.name != "Ship it!")
      .map(_.name)
    api.editIssue(number, IssueInput(
      labels = newLabels,
      assignee = Some(findAssignee(text).getOrElse(opener))
    ))
  }
}