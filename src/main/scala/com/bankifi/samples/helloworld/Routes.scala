package com.bankifi.samples.helloworld

import cats.effect.IO
import cats.implicits._

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Routes {

  def probingRoutes(probing: Probing): HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "health" =>
        for {
          status <- probing.health
          resp <- status match {
            case Probing.HealthStatus.Healthy => Ok()
            case Probing.HealthStatus.Unhealthy(reason) => InternalServerError(reason)
          }
        } yield resp

      case GET -> Root / "ready" =>
        probing.ready.flatMap { isReady =>
          if (isReady) Ok()
          else InternalServerError()
        }
    }
  }

  def helloWorldRoutes(H: HelloWorld): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }
}