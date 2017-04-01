package com.yukihirai0505.responses.auth

case class Oauth(accessToken: String, expires: Long)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Oauth {
  implicit val OauthFormat = JsonNaming.snakecase(Json.format[Oauth])
}