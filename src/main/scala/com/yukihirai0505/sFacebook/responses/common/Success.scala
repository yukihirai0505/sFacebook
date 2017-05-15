package com.yukihirai0505.sFacebook.responses.common

case class Success(success: Boolean)

import play.api.libs.json.Json

object Success {
  implicit val SuccessFormat = Json.format[Success]
}