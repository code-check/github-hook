package models

import play.api.Play
import play.api.libs.ws.ning.NingWSPlugin
import com.ning.http.client.AsyncHttpClient

object AsyncHttpClientHolder {
  implicit def instance: AsyncHttpClient =
    play.api.Play.current.plugin[NingWSPlugin].map(
      _.api.client.underlying[AsyncHttpClient]
    ).getOrElse(throw new Exception("You must import WS plugin!"))
}