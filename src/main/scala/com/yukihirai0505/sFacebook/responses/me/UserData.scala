package com.yukihirai0505.sFacebook.responses.me

case class UserData(name: String, id: String)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object UserData {
  implicit val UserDataFormat = JsonNaming.snakecase(Json.format[UserData])
}