package com.yukihirai0505.sFacebook.responses.post

case class PublishPost(id: String)

import play.api.libs.json.Json

object PublishPost {
  implicit val PublishPostFormat = Json.format[PublishPost]
}