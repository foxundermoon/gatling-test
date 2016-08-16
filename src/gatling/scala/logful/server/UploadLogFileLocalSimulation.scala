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

class UploadLogFileLocalSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://192.168.10.56:9610")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.authorizationHeader("bearer 864a39eb-6acb-4816-84c4-66344a46e156")
		.contentTypeHeader("multipart/form-data; boundary=------------------------494caab28d95fd06")

	val mapper = new ObjectMapper()

	val users = 10000

    def genHeader : Map[String, String] = {
    	var fid = UUID.randomUUID.toString.replace("-", "").toLowerCase

    	var map = new java.util.HashMap[String, Object]()
	    map.put("uid", "08a0e07727c744cfb1685e5ca9d44890")
	    map.put("bid", "com.getui.logful.sample")
	    map.put("sum", "220a3b3399689c37d7badb79b981c81c")
	    map.put("aid", "57ac4fe045cea34392ac4fbc")
	    map.put("ver", "0.4.2")
	    map.put("plt", new Integer(1))
	    map.put("fid", fid)

	    var writer = new StringWriter
	    mapper.writeValue(writer, map)
	    var extra = writer.toString()

	    var xExtra = Base64.getEncoder.encodeToString(extra.getBytes(StandardCharsets.UTF_8))

	    return Map("Charsert" -> "UTF-8", "X-EXTRA" -> xExtra)
    }

    val headerFeeder = new Array[Map[String, String]](users)
    for ( i <- 0 to (headerFeeder.length - 1)) {
    	headerFeeder(i) = genHeader
    }

	val scn = scenario("UploadLogFileLocalSimulation")
	    .feed(headerFeeder)
		.exec(http("request_0")
		.post("/v1/log/file/upload")
		.headers(Map("Charsert" -> "${Charsert}", "X-EXTRA" -> "${X-EXTRA}"))
		.body( RawFileBody("UploadLogFileLocalSimulation_0000_request.txt"))
		.check(status.is(202))
		)


	setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}
