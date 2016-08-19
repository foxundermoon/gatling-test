package logful.server

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._

import scala.concurrent.duration.FiniteDuration

class SmallLogEmptyHandleRampUserPerSecSimulation extends Simulation {

  val from = 100
  val to = 3000
  val time = 3*60
  val during = new FiniteDuration(time, TimeUnit.SECONDS)

  val second = during.toSeconds
  val c = new LogFileReqConfig((0.6 * ((from + to) * time)).toInt)

  setUp(c.scn.inject(rampUsersPerSec(from) to to during during).protocols(c httpProtocol))
}
