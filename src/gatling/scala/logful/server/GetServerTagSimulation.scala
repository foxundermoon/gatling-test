package logful.server

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by fox on 8/12/16.
  */
class GetServerTagSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://192.168.10.66:9601")
    .inferHtmlResources()
    .acceptHeader("*/*")
  val users = 20000
  val scn = scenario("GetServerTagSimulation")
    .exec(http("getServerTag")
      .get("/api/end/servertag")
      .check(status.is(200)))


  setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}
