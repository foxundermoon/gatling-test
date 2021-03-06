package logful.server

import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UploadLogFileSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://192.168.10.56:9610")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.authorizationHeader("bearer 864a39eb-6acb-4816-84c4-66344a46e156")
		.contentTypeHeader("multipart/form-data; boundary=----------------------507d18cf8b72b879")

	val mapper = new ObjectMapper()

	val users = 10

    def genHeader : Map[String, String] = {
    	val fid = UUID.randomUUID.toString.replace("-", "").toLowerCase

    	val map = new java.util.HashMap[String, Object]()
	    map.put("uid", "08a0e07727c744cfb1685e5ca9d44890")
	    map.put("bid", "com.getui.logful.sample")
	    map.put("sum", "d59e7bd21724afffb4af97ea6b3e46cb")
	    map.put("aid", "57ac4fe045cea34392ac4fbc")
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
    for ( i <- 0 to (headerFeeder.length - 1)) {
    	headerFeeder(i) = genHeader
    }

    val uri1 = "http://demo.logful.aoapp.com:9600/v1/log/file/upload"

	val scn = scenario("UploadLogFileSimulation")
	    .feed(headerFeeder)
		.exec(http("uploadLogFile")
		.post("/v1/log/file/upload")
		.headers(Map("Charsert" -> "${Charsert}", "X-EXTRA" -> "${X-EXTRA}"))
		.body(RawFileBody("RecordedSimulation_0000_request.txt"))
		.check(status.is(202)))
	setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}