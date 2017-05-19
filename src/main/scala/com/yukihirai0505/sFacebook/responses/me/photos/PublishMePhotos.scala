package com.yukihirai0505.sFacebook.responses.me.photos

case class PublishMePhotos(id: String, postId: String)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object PublishMePhotos {
  implicit val PublishMePhotosFormat = JsonNaming.snakecase(Json.format[PublishMePhotos])
}