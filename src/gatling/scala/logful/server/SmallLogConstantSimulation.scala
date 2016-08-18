package logful.server

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._

import scala.concurrent.duration.FiniteDuration

class SmallLogConstantSimulation extends Simulation {
  val usersPerSec = 1000
  val time = 60
  val during = new FiniteDuration(time, TimeUnit.SECONDS)

  val second = during.toSeconds

  val c = new LogFileReqConfig((usersPerSec * during.toSeconds).toInt)

  setUp(c.scn.inject(constantUsersPerSec(usersPerSec).during(during)).protocols(c.httpProtocol))
}
