package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.GitHubAPI
import codecheck.github.events.GitHubEvent

class LGTMAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = msg.match {
    case x: IssueCommentEvent =>
      x.comment.body.toUpperCase.indexOf("LGTM") != -1
    case _ => false
  }

  def process(api: GitHubAPI, msg: GitHubEvent): Unit = {
    Logger.info(msg.toString)
  }
}