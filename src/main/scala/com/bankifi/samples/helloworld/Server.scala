package com.bankifi.samples.helloworld

import cats.effect.{ConcurrentEffect, IO, Timer}
import cats.implicits._

import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

object Server {

  def apply(port: Int, host: String)(implicit effect: ConcurrentEffect[IO], timer: Timer[IO]): IO[Nothing] = {
    val helloWorldAlg = HelloWorld()
    val probingAlg = Probing()

    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract a segments not checked
    // in the underlying routes.
    val httpApp = (
      Routes.helloWorldRoutes(helloWorldAlg) <+>
      Routes.probingRoutes(probingAlg)
    ).orNotFound


    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    BlazeServerBuilder[IO]
      .bindHttp(port, host)
      .withHttpApp(finalHttpApp)
      .serve
  }.compile.drain *> IO.never
}