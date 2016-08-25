package logful.server

import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by fox on 8/18/16.
  */
class LogFileReqConfig(val users:Int) {
  val httpProtocol = http
//    .baseURL("http://192.168.10.56:9610")
    .baseURL("http://127.0.0.1:9600")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .authorizationHeader("bearer 5c9e834e-a233-4c0a-a166-ea8a7dac8e24")
    .contentTypeHeader("multipart/form-data; boundary=------------------------f7f5c1d4698a67b8")

  val mapper = new ObjectMapper()

  def genHeader: Map[String, String] = {
    val fid = UUID.randomUUID.toString.replace("-", "").toLowerCase

    val map = new java.util.HashMap[String, Object]()
    map.put("uid", "9941dda1623c4170b8fab36e0c447596")
    map.put("bid", "com.getui.logful.sample")
    map.put("sum", "1730ba0514a8e8c4d247ec145a209da2")
    map.put("aid", "57b56c5045ce1a6089f81d84")
    map.put("ver", "0.4.2")
    map.put("plt", new Integer(1))
    map.put("fid", fid)

    val writer = new StringWriter
    mapper.writeValue(writer, map)
    val extra = writer.toString()

    val xExtra = Base64.getEncoder.encodeToString(extra.getBytes(StandardCharsets.UTF_8))

    return Map("Charsert" -> "UTF-8", "X-EXTRA" -> xExtra)
  }

  val headerFeeder = new Array[Map[String, String]](users)
  for (i <- 0 to (headerFeeder.length - 1)) {
    headerFeeder(i) = genHeader
  }

  val scn = scenario("SmallLogSimulation")
    .feed(headerFeeder)
    .exec(http("request_0")
      .post("/v1/log/file/upload")
      .headers(Map("Charsert" -> "${Charsert}", "X-EXTRA" -> "${X-EXTRA}"))
      .body(RawFileBody("RecordedSimulation_0000_request.txt"))
      .check(status.is(202))
    )
}
