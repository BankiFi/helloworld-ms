package com.bankifi.samples.helloworld

import cats.effect.IO

import io.circe.generic.JsonCodec

import scala.util.Random

trait Probing {
  import Probing._

  def health: IO[HealthStatus]

  def ready: IO[Boolean]
}
object Probing {
  sealed trait HealthStatus
  object HealthStatus {
    case object Healthy extends HealthStatus
    case class Unhealthy(reason: String) extends HealthStatus
  }

  final val UnhealthyReasons = List(
    "Database is down",
    "Third party is unresponsive",
    "Something broke internally"
  )

  def apply(): Probing = new Probing {
    private val unhealthyFreq = UnhealthyReasons.indices.toList ++ List.fill(10)(UnhealthyReasons.size)
    private val readyFreq = List.fill(5)(true) ++ List.fill(2)(false)

    override def health: IO[HealthStatus] = {
      def decideStatus(idx: Int) = {
        if (idx >= UnhealthyReasons.size) {
          IO.pure(HealthStatus.Healthy)
        } else {
          IO.pure(HealthStatus.Unhealthy(UnhealthyReasons(idx)))
        }
      }

      for {
        unhealthyReasonIdx <- IO(unhealthyFreq(Random.nextInt(unhealthyFreq.size)))
        status             <- decideStatus(unhealthyReasonIdx)
      } yield status
    }

    override def ready: IO[Boolean] = IO {
      readyFreq(Random.nextInt(readyFreq.size))
    }
  }
}