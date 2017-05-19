package com.yukihirai0505.sFacebook

import java.io.File

import play.api.libs.json.Reads

import com.ning.http.client.multipart.{FilePart, StringPart}
import com.yukihirai0505.sFacebook.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sFacebook.http.{Request, Verbs}
import com.yukihirai0505.sFacebook.model.Constants.ME
import com.yukihirai0505.sFacebook.model.{Constants, Methods, OAuthConstants}
import com.yukihirai0505.sFacebook.responses.comments.Comments
import com.yukihirai0505.sFacebook.responses.common.Success
import com.yukihirai0505.sFacebook.responses.photos.PublishPhotos
import com.yukihirai0505.sFacebook.responses.post.{PostData, PublishPost}
import com.yukihirai0505.sFacebook.responses.user.UserData
import dispatch._

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

  /***
    * Make facebook api request
    * @param verb
    * @param apiPath
    * @param params
    * @param imageFile
    * @param r
    * @tparam T
    * @return
    */
  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, Option[String]]] = None, imageFile: Option[File] = None)(implicit r: Reads[T]): Future[Option[T]] = {
    val parameters: Map[String, String] = params match {
      case Some(m) => m.filter(_._2.isDefined).mapValues(_.getOrElse("")).filter(!_._2.isEmpty)
      case None => Map.empty
    }
    val effectiveUrl = s"${Constants.API_URL}$apiPath?${authToGETParams(auth)}"
    val request: Req = url(effectiveUrl).setMethod(verb.label)
    val requestWithParams =
      if (verb == Verbs.GET) request <<? parameters
      else if (imageFile.isDefined) {
        def addBodyParts(params: Map[String, String], req: Req): Req = {
          if (params.isEmpty) req
          else addBodyParts(params.tail, req.addBodyPart(new StringPart(params.head._1, params.head._2)))
        }

        addBodyParts(parameters, request.addBodyPart(new FilePart("source", imageFile.get)))
      } else request << parameters
    println(requestWithParams.url)
    Request.send[T](requestWithParams)
  }

  /***
    * Get facebook User info
    * @param userId
    * @return
    */
  def getUser(userId: String = ME): Future[Option[UserData]] = {
    val apiPath: String = s"/$userId"
    request[UserData](Verbs.GET, apiPath)
  }

  /***
    * Get facebook post info
    * @param postId
    * @return
    */
  def getPost(postId: String): Future[Option[PostData]] = {
    val apiPath: String = s"/$postId"
    request[PostData](Verbs.GET, apiPath)
  }

  /***
    * Create new post
    * @param userId
    * @param message
    * @return
    */
  def publishPost(userId: String = ME, message: Option[String]): Future[Option[PublishPost]] = {
    val apiPath: String = Methods.FEED format userId
    val params = Option(
      Map(
        "message" -> message
      )
    )
    request[PublishPost](Verbs.POST, apiPath, params)
  }

  /***
    * Delete post
    * @param postId
    * @return
    */
  def deletePost(postId: String): Future[Option[Success]] = {
    val apiPath: String = s"/$postId"
    request[Success](Verbs.DELETE, apiPath)
  }

  /***
    * Create new post with image file
    * @param userId
    * @param caption
    * @param imageFile
    * @return
    */
  def publishPhotos(userId: String = ME, caption: String = "", imageFile: File): Future[Option[PublishPhotos]] = {
    val apiPath: String = Methods.PHOTOS format userId
    val params = Option(
      Map(
        "caption" -> Some(caption)
      )
    )
    request[PublishPhotos](Verbs.POST, apiPath, params, Some(imageFile))
  }

  /***
    * Get facebook comment info
    * @param objectId
    * @return
    */
  def getComments(objectId: String): Future[Option[Comments]] = {
    val apiPath: String = Methods.COMMENTS format objectId
    request[Comments](Verbs.GET, apiPath)
  }
}