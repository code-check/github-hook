package services.github

import codecheck.github.api.GitHubAPI
import codecheck.github.events.GitHubEvent

trait GitHubAction {
  def isMatch(msg: GitHubEvent): Boolean
  def process(api: GitHubAPI, msg: GitHubEvent): Unit
}