package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.GitHubAPI
import codecheck.github.events.GitHubEvent

class LogAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = true

  def process(api: GitHubAPI, msg: GitHubEvent): Unit = {
    Logger.info(msg.toString)
  }
}