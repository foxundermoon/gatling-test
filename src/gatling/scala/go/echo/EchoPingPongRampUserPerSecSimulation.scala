package go.echo

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.FiniteDuration;

/**
  * Created by fox on 8/15/16.
  */
class EchoPingPongRampUserPerSecSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://192.168.10.66:9700")
    .inferHtmlResources()
    .acceptHeader("*/*")
  val scn = scenario("ping")
    .exec(http("ping")
      .get("/ping")
      .check(status.is(200)))
  setUp(scn.inject(rampUsersPerSec(500) to 10000 during(FiniteDuration(5, TimeUnit.MINUTES))).protocols(httpProtocol))
}
