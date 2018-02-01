package com.sitexa.kwf

import com.sitexa.kwf.handler.*
import org.slf4j.LoggerFactory
import org.wasabifx.wasabi.app.AppConfiguration
import org.wasabifx.wasabi.app.AppServer
import org.wasabifx.wasabi.interceptors.serveStaticFilesFromFolder
import org.wasabifx.wasabi.routing.RouteHandler

/**
 * Created by open on 5/25/16.
 * First Kotlin wasabi.
 */


fun main(args: Array<String>) {
    val appConfig = AppConfiguration(1234, "My Wasabi api")
    val server = AppServer(appConfig)
    server.serveStaticFilesFromFolder("web")

    server.get("/", indexHandler)
    server.get("/about", aboutHandler)

    server.get("/member/:id", getMember)
    server.get("/member", findMemberGET)
    server.post("/findmember", findMemberPOST)
    server.get("/members", queryMember)
    server.post("/member", createMember)
    server.put("/member/:id", updateMember)
    server.delete("/member/:id", deleteMember)
    server.put("/member/")
    server.put("/member/setpassword/", setPassword)
    server.put("/member/findpassword/", findPassword)

    server.start()
}

private val log = LoggerFactory.getLogger("AppMain")

val indexHandler: RouteHandler.() -> Unit = {
    var str = ""
    str += request.uri+"\n"
    request.rawHeaders.forEach {
        str += it.toString() +"\n"
    }
    log.info(str)
    response.send(str)
}

val aboutHandler: RouteHandler.() -> Unit = {
    response.send("about my wasabi app")
}