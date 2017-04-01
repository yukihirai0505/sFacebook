package com.yukihirai0505.sFacebook.model

/**
  * project constant
  *
  * author Yuki Hirai on 2016/11/08.
  */
object Constants {
  private val BASE_URL: String = "https://www.facebook.com"
  private val OAUTH_URL: String = s"$BASE_URL/dialog/oauth"
  val AUTHORIZE_URL: String = s"$OAUTH_URL?client_id=%s&redirect_uri=%s"


  private val BASE_GRAPH_URL: String = "https://graph.facebook.com"

  val ACCESS_TOKEN_ENDPOINT: String = s"$BASE_GRAPH_URL/oauth/access_token"

  val API_URL: String = s"$BASE_GRAPH_URL"

}
