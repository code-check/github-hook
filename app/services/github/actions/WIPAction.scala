package services.github.actions

import codecheck.github.events.GitHubEvent
import codecheck.github.models.IssueInput

class WIPAction extends GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean = {false}
  def process(api: RepositoryAPI, msg: GitHubEvent): Unit = {}
}
