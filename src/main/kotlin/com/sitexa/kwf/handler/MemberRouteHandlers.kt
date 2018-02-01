package com.sitexa.kwf.handler

import com.sitexa.kwf.dao.MemberDao
import com.sitexa.kwf.dao.impl.MemberDaoImpl
import com.sitexa.kwf.entity.Member
import org.slf4j.LoggerFactory
import org.wasabifx.wasabi.routing.routeHandler
import java.util.*

/**
 * Created by open on 5/27/16.
 * routeHandlers for member
 */

private val log = LoggerFactory.getLogger("MemberRouterHandlers")
private val map = HashMap<String, Any>()


val createMember = routeHandler {
    try {
        val memberName = request.bodyParams["memberName"]!! as String
        val communityId = request.bodyParams["communityId"]!! as String?
        val mobile = request.bodyParams["mobile"]!! as String
        val email = request.bodyParams["email"]!! as String
        val sgender = request.bodyParams["gender"]!! as String
        val gender = if (sgender == "1") "1" else "0"
        val sdate = request.bodyParams["birthdate"]!! as Date

        val member = Member(
                memberName = memberName,
                gender = gender,
                birthDate = sdate,
                mobile = mobile,
                email = email,
                communityId = communityId
        )

        val dao = MemberDaoImpl()
        val memberId = dao.saveMember(member)
        member.memberId = memberId

        map.put("results", member)
        map.put("error", false)
        response.send(map, "application/json")
    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

val queryMember = routeHandler {
    try {
        val pgsize = request.queryParams["pgsize"]!!
        val pgno = request.queryParams["pgno"]!!

        val size = pgsize.toInt()
        val no = pgno.toInt()
        val dao: MemberDao = MemberDaoImpl()
        val members = dao.getMemberList(size, no)

        map.put("results", members)
        map.put("error", false)
        response.send(map, "application/json")
    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

//通过get发送的中文字符串，编码发生了变化，查不出正确结果；对比下面通过post发送的中文字符串。
val findMemberGET = routeHandler {
    try {
        val name = request.queryParams["name"]!!
        val dao = MemberDaoImpl()
        val member = dao.getMemberByPrinciple(name)!!
        map.put("error", false)
        map.put("results", member)
        response.send(map, "application/json")
    } catch (e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

//通过post发送的中文字符串，编码正确，能查出正确结果；对比上面通过get发送的中文字符串。
val findMemberPOST = routeHandler {
    try {
        val name = request.bodyParams["name"]!! as String
        val dao = MemberDaoImpl()
        val member = dao.getMemberByPrinciple(name)!!
        map.put("error", false)
        map.put("results", member)
        response.send(map, "application/json")
    } catch (e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

val getMember = routeHandler {
    try {
        val id = request.routeParams["id"]!!
        val dao: MemberDao = MemberDaoImpl()
        val member = dao.getMemberById(id)
        map.put("results", member!!)
        map.put("error", false)
        response.send(map, "application/json")
    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

val updateMember = routeHandler {
    try {
        val id = request.routeParams["id"]!!
        val qqno = request.bodyParams["qqno"]!! as String?
        val weixinno = request.bodyParams["weixinno"]!! as String?
        val weibono = request.bodyParams["weibono"]!! as String?
        val email = request.bodyParams["email"]!! as String?
        val address = request.bodyParams["address"]!! as String?
        val mobile = request.bodyParams["mobile"]!! as String?

        val dao = MemberDaoImpl()
        val result = dao.updateMember(id, qqno, weixinno, weibono, email, mobile, address)

        map.put("results", result)
        map.put("error", false)
        response.send(map, "application/json")
    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
    }
}

val deleteMember = routeHandler {
    val id = request.routeParams["id"]!!
}

val setPassword = routeHandler {
    log.info("setPassword")
    try {
        val name = request.bodyParams["name"]!! as String?
        val oldPassword = request.bodyParams["oldpassword"]!! as String?
        val newPassword = request.bodyParams["newpassword"]!! as String?

        val dao = MemberDaoImpl()
        val result = dao.setPassword(name!!, oldPassword!!, newPassword!!)

        map.put("results", result)
        map.put("error", false)
        response.send(map, "application/json")

    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
        log.info(e.message)
    }
}

val findPassword = routeHandler {
    log.info("findPassword")
    try {
        val name = request.bodyParams["name"]!! as String?
        val mobile = request.bodyParams["mobile"]!! as String?
        val email = request.bodyParams["email"]!! as String?

        val result = findPwd(name, mobile, email)
        map.put("results", result)
        map.put("error", false)
        response.send(map, "application/json")
    } catch(e: Exception) {
        map.put("error", true)
        map.put("results", e.toString())
        response.send(map, "application/json")
        log.info(e.message)
    }
}

fun findPwd(name: String?, mobile: String?, email: String?): String {
    var result = ""
    val member: Member?
    var sms = ""
    var eml = ""
    val dao = MemberDaoImpl()

    if (name != null) {
        member = dao.getMemberByPrinciple(name)
        sms = sendSMS(member!!.mobile)
        eml = sendEmail(member.email)
        result += sms + eml
    } else if (mobile != null) {
        member = dao.getMemberByPrinciple(mobile)
        sms = sendSMS(member!!.mobile)
        eml = sendEmail(member.email)
        result += sms + eml
    } else if (email != null) {
        member = dao.getMemberByPrinciple(email)
        sms = sendSMS(member!!.mobile)
        eml = sendEmail(member.email)
        result += sms + eml
    }
    result += sms + eml

    if (result.isEmpty()) {
        return "0"
    } else {
        return "A message is sent to your mobile or email box, please follow the link to reset your password."
    }
}

fun sendSMS(mobile: String): String {

    return "1"
}

fun sendEmail(email: String): String {

    return "1"
}

