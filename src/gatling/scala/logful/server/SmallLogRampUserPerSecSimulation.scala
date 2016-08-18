package logful.server

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._

import scala.concurrent.duration.FiniteDuration

class SmallLogRampUserPerSecSimulation extends Simulation {

  val from = 100
  val to = 1500
  val time = 60
  val during = new FiniteDuration(time, TimeUnit.SECONDS)

  val second = during.toSeconds
  val c = new LogFileReqConfig((0.6 * ((from + to) * time)).toInt)

  setUp(c.scn.inject(rampUsersPerSec(10) to 2000 during during).protocols(c httpProtocol))
}
