package logful.server

import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SmallLogSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://192.168.10.56:9610")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.authorizationHeader("bearer 864a39eb-6acb-4816-84c4-66344a46e156")
		.contentTypeHeader("multipart/form-data; boundary=------------------------9eaca678277506bc")

	val mapper = new ObjectMapper()

	val users = 100000
//	val users = 1

    def genHeader : Map[String, String] = {
			val fid = UUID.randomUUID.toString.replace("-", "").toLowerCase

			val map = new java.util.HashMap[String, Object]()
	    map.put("uid", "08a0e07727c744cfb1685e5ca9d44890")
	    map.put("bid", "com.getui.logful.sample")
	    map.put("sum", "220a3b3399689c37d7badb79b981c81c")
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

	val scn = scenario("UploadLogFileLocalSimulation")
	    .feed(headerFeeder)
		.exec(http("request_0")
		.post("/v1/log/file/upload")
		.headers(Map("Charsert" -> "${Charsert}", "X-EXTRA" -> "${X-EXTRA}"))
		.body( RawFileBody("SmallLogSimulation_0000_request.txt"))
		.check(status.is(202))
		)


	setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))
}
