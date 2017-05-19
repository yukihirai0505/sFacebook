package com.yukihirai0505.sFacebook.responses.comments

case class Comments(data: Seq[CommentData])


import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object Comments {
  implicit val CommentsFormat = JsonNaming.snakecase(Json.format[Comments])
}