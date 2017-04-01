# sFacebook

A Scala library for the Facebook API.
An asynchronous non-blocking Scala Facebook API Wrapper,
implemented using play-json.

## Prerequisites

Scala 2.11.+ is supported.

- Go to https://developers.facebook.com/, login with your Facebook account
  and register your application to get a client id and a client secret.
- Once the app has been created, register callback.

## Setup

### sbt

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

```scala
resolvers += Resolver.sonatypeRepo("releases")
```

Also, you need to include the library as your dependency:

```scala
libraryDependencies += "com.yukihirai0505" % "sFacebook_2.11" % "0.0.1"
```

http://mvnrepository.com/artifact/com.yukihirai0505/sFacebook_2.11/0.0.1

## Usage

### Examples

```scala
import com.yukihirai0505.sFacebook.http.Response
import com.yukihirai0505.sFacebook.model.Scope
import com.yukihirai0505.sFacebook.auth.Auth
import com.yukihirai0505.sFacebook.auth.AccessToken
import com.yukihirai0505.sFacebook.{Authentication, Facebook}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

val clientId = "client-id"
val clientSecret = "client-secret"
val callbackUrl = "callback-URI"
val authentication: Authentication = new Authentication
val scopes: Seq[Scope] = Seq(Scope.PUBLIC_PROFILE)

// Server-Side login
// Step 1: Get a URL to call. This URL will return the CODE to use in step 2
val authUrl = authentication.authURL(clientId, callbackUrl, scopes)

// Step 2: Use the code to get an AccessToken
val accessTokenFuture = authentication.requestToken(clientId, clientSecret, callbackUrl, "the-code-from-step-1")
val accessToken = accessTokenFuture onComplete {
  case Success(Response(Some(token: AccessToken), _, _)) => token
  case Failure(t) => println("An error has occured: " + t.getMessage)
}

// Making an authenticated call
val auth: Auth = AccessToken("an-access-token")
// If you want to use signed access token
// val auth: Auth = SignedAccessToken("an-access-token", clientSecret)
val Facebook: Facebook = new Facebook(auth)
// The library is asynchronous by default and returns a promise.
val future = Facebook.getRecentMediaFeed()
import scala.language.postfixOps
future onComplete {
  case Success(Response(body, statusCode, headers)) =>
    println(body.getOrElse())
  case Failure(t) => println("An error has occured: " + t.getMessage)
}
```

Please look at this file to see all available methods:

https://github.com/yukihirai0505/sFacebook/blob/master/src/main/scala/com/yukihirai0505/instsagram/FacebookClient.scala

## References

inspired by following source code

- https://github.com/sachin-handiekar/jFacebook
- https://github.com/Rydgel/scalagram
