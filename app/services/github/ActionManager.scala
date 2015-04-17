package services.github

import codecheck.github.events.GitHubEvent

class ActionManager(actions: Seq[GitHubAction]) {

  def get(msg: GitHubEvent) = actions.filter(_.isMatch(msg))
}

object ActionManager {
  def apply(actions: Seq[GitHubAction]) = new ActionManager(actions)
}

