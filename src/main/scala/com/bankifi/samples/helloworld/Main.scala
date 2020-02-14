package com.bankifi.samples.helloworld

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    Server(8080, "0.0.0.0").as(ExitCode.Success)
}