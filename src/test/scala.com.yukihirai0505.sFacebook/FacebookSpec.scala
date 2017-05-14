package com.yukihirai0505.sFacebook

import com.netaporter.uri.Uri._
import com.yukihirai0505.sFacebook.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sFacebook.http.{Request, Response, Verbs}
import com.yukihirai0505.sFacebook.model.{Constants, OAuthConstants, QueryParam}
import dispatch._
import play.api.libs.json.Reads

import scala.language.postfixOps

/***
  * Yuky
  */
class FacebookSpec extends FlatSpec with Matchers {
}