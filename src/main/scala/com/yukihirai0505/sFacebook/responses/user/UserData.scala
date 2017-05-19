package com.yukihirai0505.sFacebook.responses.user

case class UserData(name: String, id: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object UserData {
  implicit val UserDataFormat = JsonNaming.snakecase(Json.format[UserData])
}