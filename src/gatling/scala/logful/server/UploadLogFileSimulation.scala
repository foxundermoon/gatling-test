package logful.server

import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UploadLogFileSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://demo.logful.aoapp.com:9600")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.authorizationHeader("bearer 3cadeb15-5282-4c37-82b5-ef0cd788f8ba")
		.contentTypeHeader("multipart/form-data; boundary=------------------------09e492e4de4d9073")

	val mapper = new ObjectMapper()

	val users = 10

    def genHeader : Map[String, String] = {
    	var fid = UUID.randomUUID.toString.replace("-", "").toLowerCase

    	var map = new java.util.HashMap[String, Object]()
	    map.put("uid", "08a0e07727c744cfb1685e5ca9d44890")
	    map.put("bid", "com.getui.logful.sample")
	    map.put("sum", "e9df3527801ef4ce081c301220e27fa7")
	    map.put("aid", "574b96640cf2e86e93af6656")
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

    val uri1 = "http://demo.logful.aoapp.com:9600/v1/log/file/upload"

	val scn = scenario("UploadLogFileSimulation")
	    .feed(headerFeeder)
		.exec(http("request_0")
		.post("/v1/log/file/upload")
		.headers(Map("Charsert" -> "${Charsert}", "X-EXTRA" -> "${X-EXTRA}"))
		.body(RawFileBody("UploadLogFileSimulation_0000_request.txt"))
		.check(status.is(202)))
	setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}