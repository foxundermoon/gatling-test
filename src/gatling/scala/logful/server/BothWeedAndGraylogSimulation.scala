package logful.server

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import logful.server.config.{DebugLogFileReqConfig, LogFileReqConfig, VerboseLogFileReqConfig}

import scala.concurrent.duration.FiniteDuration

/**
  * Created by fox on 8/31/16.
  */
class BothWeedAndGraylogSimulation extends Simulation {
  val from = 100
  val to = 2000
  val time = 5 * 60

  val during = new FiniteDuration(time, TimeUnit.SECONDS)
  val second = during.toSeconds
  val vc = new VerboseLogFileReqConfig((0.6 * ((from + to) * time)).toInt)
  val vd = new DebugLogFileReqConfig((0.6 * ((from + to) * time)).toInt)

  setUp(vc.scn.inject(rampUsersPerSec(from) to to during during).protocols(vc httpProtocol) ::
    (vd.scn.inject(rampUsersPerSec(from) to to during during).protocols(vd httpProtocol) :: Nil)
  )

}
