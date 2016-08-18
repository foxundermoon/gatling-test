package logful.server

import io.gatling.charts.stats.LogFileReader
import io.gatling.core.Predef._


class SmallLogSimulation extends Simulation {
  val c = new  LogFileReqConfig(1000)
  setUp(c.scn.inject(atOnceUsers(c.users)).protocols(c.httpProtocol))
}
