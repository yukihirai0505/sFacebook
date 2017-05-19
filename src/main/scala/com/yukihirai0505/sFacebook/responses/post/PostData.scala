package com.yukihirai0505.sFacebook.responses.post

import com.github.tototoshi.play.json.JsonNaming

case class PostData(id: String, message: String, createdTime: String)

import play.api.libs.json.Json

object PostData {
  implicit val PostDataFormat = JsonNaming.snakecase(Json.format[PostData])
}