package services.github

import codecheck.github.api.RepositoryAPI
import codecheck.github.events.GitHubEvent

trait GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean
  def process(api: RepositoryAPI, msg: GitHubEvent): Unit
}