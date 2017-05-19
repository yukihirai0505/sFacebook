package com.yukihirai0505.sFacebook.responses.comments

import com.github.tototoshi.play.json.JsonNaming
import com.yukihirai0505.sFacebook.responses.user.UserData

case class CommentData(id: String, message: String, createdTime: String, from: UserData)

import play.api.libs.json.Json

object CommentData {
  implicit val CommentDataFormat = JsonNaming.snakecase(Json.format[CommentData])
}