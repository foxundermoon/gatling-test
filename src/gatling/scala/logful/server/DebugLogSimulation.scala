package logful.server

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import logful.server.config.{DebugLogFileReqConfig, VerboseLogFileReqConfig}

import scala.concurrent.duration.FiniteDuration

/**
  * Created by fox on 8/31/16.
  */
class DebugLogSimulation extends Simulation {
  val from = 100
  val to = 3000
  val time = 1 * 60

  val during = new FiniteDuration(time, TimeUnit.SECONDS)
  val second = during.toSeconds
  val vd = new DebugLogFileReqConfig((0.6 * ((from + to) * time)).toInt)

  setUp(
    vd.scn.inject(rampUsersPerSec(from) to to during during).protocols(vd httpProtocol)
  )

}
