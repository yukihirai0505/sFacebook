package com.yukihirai0505.sFacebook.model

/**
  * author Yuki Hirai on 2016/11/11.
  */
sealed abstract class Scope(val label: String)
object Scope {
  case object PUBLIC_PROFILE extends Scope("public_profile")
  case object PUBLISH_ACTIONS extends Scope("publish_actions")
  case object USER_POSTS extends Scope("user_posts")
}