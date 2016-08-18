package logful.server

import io.gatling.charts.stats.LogFileReader
import io.gatling.core.Predef._


class SmallLogSimulation extends Simulation {
  val users = 40000
  val c = new LogFileReqConfig(users)
  setUp(c.scn.inject(atOnceUsers(c.users)).protocols(c.httpProtocol))
}
