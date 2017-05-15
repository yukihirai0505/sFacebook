package com.yukihirai0505.sFacebook

import com.netaporter.uri.Uri._
import com.yukihirai0505.sFacebook.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sFacebook.http.{Request, Response, Verbs}
import com.yukihirai0505.sFacebook.model.{Constants, Methods, OAuthConstants, QueryParam}
import com.yukihirai0505.sFacebook.responses.me.UserData
import com.yukihirai0505.sFacebook.responses.post.PublishPost
import dispatch._
import play.api.libs.json.Reads

import scala.language.postfixOps


/**
  * author Yuki Hirai on 2016/11/09.
  */
class Facebook(auth: Auth) {

  /**
    * Transform an Authentication type to be used in a URL.
    *
    * @param a Authentication
    * @return String
    */
  protected def authToGETParams(a: Auth): String = a match {
    case AccessToken(token) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
    case SignedAccessToken(token, _) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
  }

  protected def addSecureSigIfNeeded(url: String, postData: Option[Map[String, String]] = None)
  : String = auth match {
    case SignedAccessToken(_, secret) =>
      val uri = parse(url)
      val params = uri.query.params
      val auth: FacebookAuth = new FacebookAuth
      val sig = auth.createSignedParam(
        secret,
        uri.pathRaw,
        concatMapOpt(postData, params.toMap)
      )
      uri.addParam(QueryParam.SIGNATURE, sig).toStringRaw
    case _ => url
  }

  protected def concatMapOpt(postData: Option[Map[String, String]], params: Map[String, Option[String]])
  : Map[String, Option[String]] = postData match {
    case Some(m) => params ++ m.mapValues(Some(_))
    case _ => params
  }

  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, Option[String]]] = None)(implicit r: Reads[T]): Future[Option[T]] = {
    val parameters: Map[String, String] = params match {
      case Some(m) => m.filter(_._2.isDefined).mapValues(_.getOrElse("")).filter(!_._2.isEmpty)
      case None => Map.empty
    }
    val accessTokenUrl = s"${Constants.API_URL}$apiPath?${authToGETParams(auth)}"
    val effectiveUrl: String = verb match {
      case Verbs.GET => addSecureSigIfNeeded(accessTokenUrl)
      case _ => addSecureSigIfNeeded(accessTokenUrl, Some(parameters))
    }
    val request: Req = url(effectiveUrl).setMethod(verb.label)
    val requestWithParams = if (verb.label == Verbs.GET.label) {
      request <<? parameters
    } else {
      request << parameters
    }
    println(requestWithParams.url)
    Request.send[T](requestWithParams)
  }

  // TODO: User Info
  def getMe(): Future[Option[UserData]] = {
    val apiPath: String = Methods.ME
    request[UserData](Verbs.GET, apiPath)
  }

  // TODO: Post Publish
  def publishPost(userId: String, message: Option[String]): Future[Option[PublishPost]] = {
    val apiPath: String = Methods.POST_WITH_ID format userId
    val params = Option(
      Map(
        "message" -> message
      )
    )
    request[PublishPost](Verbs.POST, apiPath, params)
  }

  // TODO: Post Delete
}