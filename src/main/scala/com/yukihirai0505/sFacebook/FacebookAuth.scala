package com.yukihirai0505.sFacebook

import java.net.URLDecoder

import play.api.libs.json.Json

import com.yukihirai0505.sFacebook.auth.AccessToken
import com.yukihirai0505.sFacebook.model.{Constants, OAuthConstants, Scope}
import com.yukihirai0505.sFacebook.responses.auth._
import dispatch.Defaults._
import dispatch._

class FacebookAuth {

  /**
   * Scope string which will be append to the URL.
   *
   * @param scopes         Scopes
   * @return               String
   */
  def setScopes(scopes: Seq[Scope]): String = {
    val scopeLabels: List[String] = scopes.map(s => s.label).toList
    if (scopes.nonEmpty) scopeLabels.mkString("scope=", "+", "")
    else ""
  }

  /**
   * Create the URL to call when retrieving an access token.
   *
   * @param clientId       Client identifier. (You need to register this on facebook app)
   * @param redirectURI    URI which the response is sent to.
   * @param scopes         Require scope.
   */
  def authURL(clientId: String, redirectURI: String, scopes: Seq[Scope] = Seq()): String = {
    ( Constants.AUTHORIZE_URL format (clientId, redirectURI) ) + s"&${setScopes(scopes)}"
  }

  /**
   * Post request to exchange a authentication code against an access token.
   * Note that an authentication code is valid one time only.
   *
   * @param clientId     Client identifier. (You need to register this on facebook)
   * @param clientSecret Client secret. (You need to register this on Facebook)
   * @param redirectURI  URI which the response is sent to. (You need to register this on Facebook)
   * @param code         Authentication code. You can retrieve it via codeURL.
   * @return             Future of Response[Authentication]
   */
  def requestToken(clientId: String, clientSecret: String, redirectURI: String, code: String): Future[Option[AccessToken]] = {
    val params = Map(
      OAuthConstants.CLIENT_ID -> clientId,
      OAuthConstants.CLIENT_SECRET -> clientSecret,
      OAuthConstants.REDIRECT_URI -> redirectURI,
      OAuthConstants.CODE -> URLDecoder.decode(code, "utf8")
    )
    val request = url(Constants.ACCESS_TOKEN_ENDPOINT) << params
    println(request.url)
    Http(request).map { resp =>
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(response.toString)
      Json.parse(response).asOpt[Oauth] match {
        case Some(o: Oauth) => Some(AccessToken(o.accessToken))
        case _ => None
      }
    }
  }

}
