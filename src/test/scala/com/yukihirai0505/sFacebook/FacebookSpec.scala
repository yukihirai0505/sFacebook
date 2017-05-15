package com.yukihirai0505.sFacebook

import com.yukihirai0505.sFacebook.auth.AccessToken
import com.yukihirai0505.sFacebook.model.Scope
import com.yukihirai0505.sFacebook.responses.me.UserData
import helpers.WebHelper
import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.language.postfixOps
import scala.util.Try

/***
  * Yuky
  */
class FacebookSpec extends FlatSpec with Matchers with WebHelper {

  private def anInstanceOf[T](implicit tag: reflect.ClassTag[T]) = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] {
      def apply(left: AnyRef) =
        BePropertyMatchResult(left.getClass.isAssignableFrom(clazz), "an instance of " + clazz.getName)
    }
  }

  private def readTestDataFromConfig(): Map[String, String] = {
    Try {
      val testDataFile = Source.fromURL(getClass.getResource("/testData.txt")).mkString
      val testData = testDataFile.split("\n").toSeq
      Map(testData map { a =>
        val data = a.split("=")
        data(0) -> data(1)
      }: _*)
    }.getOrElse(throw new Exception(
      "Please provide a valid access_token by making a token.txt in resources.\n" +
        "See testData.txt.default for detail."
    ))
  }

  val auth = new FacebookAuth
  val testData: Map[String, String] = readTestDataFromConfig()
  val clientId: String = testData.getOrElse("clientId", "")
  val clientSecret: String = testData.getOrElse("clientSecret", "")
  val redirectUri: String = testData.getOrElse("redirectUri", "")
  val facebookId: String = testData.getOrElse("facebookId", "")
  val facebookPassword: String = testData.getOrElse("facebookPassword", "")
  val scopes = Seq(
    Scope.PUBLIC_PROFILE
  )
  var authUrl = ""
  var code = ""
  var accessToken = ""
  var userId = ""

  "Facebook Auth url" should "return a valid authorization url" in {
    authUrl = auth.authURL(clientId, redirectUri, scopes)
    println(s"authUrl: $authUrl")
    assert(authUrl.nonEmpty)
  }

  "Request Auth url" should "return a valid code" in {
    val emailIdKey = "email"
    val passwordKey = "pass"
    val loginBtn = "loginbutton"

    open(authUrl)

    waitId(emailIdKey)
    findElementById(emailIdKey).sendKeys(facebookId)
    findElementByName(passwordKey).sendKeys(facebookPassword)
    findElementById(loginBtn).click()
    waitUrlContains("code")
    code = "code=[^&]+".r.findFirstIn(currentUrl).getOrElse("").replace("code=", "")
    println(s"code: $code")

    assert(code.nonEmpty)
  }

  "Request AccessToken" should "return accessToken" in {
    val request = Await.result(auth.requestToken(clientId, clientSecret, redirectUri, code), Duration.Inf)
    request.foreach { v =>
      accessToken = v.token
      println(s"accessToken: $accessToken")
    }
    request should be(anInstanceOf[Some[AccessToken]])
  }

  "getMe" should "return UserData" in {
    val request = Await.result(new Facebook(AccessToken(accessToken)).getMe(), Duration.Inf)
    userId = request.fold("")(x => x.id)
    request should be(anInstanceOf[Some[UserData]])
  }
}