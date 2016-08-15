package go.echo

import io.gatling.core.Predef._;
import io.gatling.http.Predef._;

/**
  * Created by fox on 8/15/16.
  */
class EchoPingPongSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://192.168.10.66:9700")
    .inferHtmlResources()
    .acceptHeader("*/*")
  val users = 20000
  val scn = scenario("ping")
    .exec(http("ping")
      .get("/ping")
      .check(status.is(200)))
  setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}
