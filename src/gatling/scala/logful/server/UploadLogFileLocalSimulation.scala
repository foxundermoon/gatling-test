package logful.server

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import java.util.Base64
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.io.StringWriter
import com.fasterxml.jackson.databind.ObjectMapper
import scala.collection.mutable.HashMap
import LogFileReqConfig._

class UploadLogFileLocalSimulation extends Simulation {
  setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}
