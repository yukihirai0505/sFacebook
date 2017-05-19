package com.yukihirai0505.sFacebook.responses.me.photos

case class PublishPhotos(id: String, postId: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object PublishPhotos {
  implicit val PublishPhotosFormat = JsonNaming.snakecase(Json.format[PublishPhotos])
}