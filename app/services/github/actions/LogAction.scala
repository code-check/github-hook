package services.github.actions

import play.api.Logger
import services.github.GitHubAction
import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent

class LogAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = true

  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {
    Logger.info(msg.toString)
  }
}