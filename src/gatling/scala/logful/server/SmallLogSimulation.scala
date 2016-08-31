package logful.server

import io.gatling.charts.stats.LogFileReader
import io.gatling.core.Predef._
import logful.server.config.LogFileReqConfig


class SmallLogSimulation extends Simulation {
//  val users = 40000
//  val users = 10000
  val users = 1
  val c = new LogFileReqConfig(users)
  setUp(c.scn.inject(atOnceUsers(c.users)).protocols(c.httpProtocol))
}
