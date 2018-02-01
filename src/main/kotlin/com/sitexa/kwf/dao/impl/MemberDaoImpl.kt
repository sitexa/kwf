package com.sitexa.kwf.dao.impl

import com.sitexa.kwf.dao.MemberDao
import com.sitexa.kwf.dao.Sql2oDao
import com.sitexa.kwf.entity.Member
import org.slf4j.LoggerFactory
import org.sql2o.Connection
import sun.misc.BASE64Encoder
import java.security.MessageDigest

/**
 * Created by open on 5/27/16.
 *
 */

val log = LoggerFactory.getLogger("MemberDaoImpl")

class MemberDaoImpl : Sql2oDao(), MemberDao {
    override fun updateMember(id: String, qqno: String?, weixinno: String?, weibono: String?, email: String?, mobile: String?, address: String?): Int {
        if (id.isEmpty()) return 0
        var sql = ""

        if (!qqno.isNullOrEmpty()) {
            sql += "update t_member set qqno = '%s' ".format(qqno)
            if (!weixinno.isNullOrEmpty()) sql += ", weixonno='%s' ".format(weixinno)
            if (!weibono.isNullOrEmpty()) sql += ", weibono='%s' ".format(weibono)
            if (!email.isNullOrEmpty()) sql += ", email='%s' ".format(email)
            if (!mobile.isNullOrEmpty()) sql += ", mobile='%s' ".format(mobile)
            if (!address.isNullOrEmpty()) sql += ", address='%s' ".format(address)
        } else if (!weixinno.isNullOrEmpty()) {
            sql += "update t_member set weixonno='%s' ".format(weixinno)
            if (!weibono.isNullOrEmpty()) sql += ", weibono='%s' ".format(weibono)
            if (!email.isNullOrEmpty()) sql += ", email='%s' ".format(email)
            if (!mobile.isNullOrEmpty()) sql += ", mobile='%s' ".format(mobile)
            if (!address.isNullOrEmpty()) sql += ", address='%s' ".format(address)
        } else if (!weibono.isNullOrEmpty()) {
            sql += "update t_member set weibono='%s' ".format(weibono)
            if (!email.isNullOrEmpty()) sql += ", email='%s' ".format(email)
            if (!mobile.isNullOrEmpty()) sql += ", mobile='%s' ".format(mobile)
            if (!address.isNullOrEmpty()) sql += ", address='%s' ".format(address)
        } else if (!email.isNullOrEmpty()) {
            sql += "update t_member set email='%s' ".format(email)
            if (!mobile.isNullOrEmpty()) sql += ", mobile='%s' ".format(mobile)
            if (!address.isNullOrEmpty()) sql += ", address='%s' ".format(address)
        } else if (!mobile.isNullOrEmpty()) {
            sql += "update t_member set mobile='%s' ".format(mobile)
            if (!address.isNullOrEmpty()) sql += ", address='%s' ".format(address)
        } else if (!address.isNullOrEmpty()) {
            sql += "update t_member set address='%s' ".format(address)
        } else {
            return 0
        }

        sql += " where memberId = '%s' ".format(id)
        try {
            val conn = sql2o.open()
            val result = conn.createQuery(sql).executeUpdate().result
            conn.close()
            return result
        } catch(e: Exception) {
            return 0
        }
    }

    override fun bindQq(id: String, qqno: String) {
        if (id.isEmpty() || qqno.isEmpty()) return
        val sql = "update t_member set qqno = :qqno where memberId = :memberId "
        try {
            val conn = sql2o.open()
            conn.createQuery(sql).addParameter("qqno", qqno).addParameter("memberId", id).executeUpdate()
            conn.close()
        } catch(e: Exception) {
            log.info(e.message)
        }
    }

    override fun bindWeixin(id: String, weixinno: String) {
        if (id.isEmpty() || weixinno.isEmpty()) return
        val sql = "update t_member set weixinno = :weixinno where memberId = :memberId "
        try {
            val conn = sql2o.open()
            conn.createQuery(sql).addParameter("weixinno", weixinno).addParameter("memberId", id).executeUpdate()
            conn.close()
        } catch(e: Exception) {
            log.info(e.message)
        }
    }

    override fun bindWeibo(id: String, weibono: String) {
        if (id.isEmpty() || weibono.isEmpty()) return
        val sql = "update t_member set weibono = :weibono where memberId = :memberId "
        try {
            val conn = sql2o.open()
            conn.createQuery(sql).addParameter("weibono", weibono).addParameter("memberId", id).executeUpdate()
            conn.close()
        } catch(e: Exception) {
            log.info(e.message)
        }
    }

    override fun setPassword(name: String, oldPassword: String, newPassword: String): Boolean {
        if (name.isEmpty()) return false
        val pass = md5encrypt(oldPassword) ?: return false
        val pass2 = md5encrypt(newPassword) ?: return false

        val member = getMemberByPrinciple(name) ?: return false
        if (!pass.equals(member.password)) return false

        val sql = "update t_member set password = :password where memberId = :memberId "
        try {
            val conn = sql2o.open()
            val result = conn.createQuery(sql).addParameter("password", pass2).addParameter("memberId", member.memberId).executeUpdate().result
            conn.close()
            return result == 1
        } catch(e: Exception) {
            log.info(e.message)
            return false
        }
    }

    override fun validateByPrincipal(name: String, pwd: String): Boolean {
        if (name.isEmpty()) return false

        val sql = " select password from t_member where memberName = :name or email = :name or mobile = :name or qqno = :name or weixinno= :name or weibono = :name "
        try {
            val conn = sql2o.open()
            val result = conn.createQuery(sql).addParameter("name", name).executeScalar() as String?
            conn.close()
            val pass = md5encrypt(pwd)
            return pass.equals(result)
        } catch(e: Exception) {
            log.info(e.message)
            return false
        }
    }

    override fun getMemberList(): List<Member> {
        try {
            val conn = sql2o.open()
            val members = conn.createQuery("select * from t_member").executeAndFetch(Member::class.java)
            conn.close()
            return members
        } catch(e: Exception) {
            log.info(e.message)
            return emptyList()
        }
    }

    override fun getMemberList(pgsize: Int, pgno: Int): List<Member> {
        try {
            val conn = sql2o.open()
            val sql = "select * from t_member order by registerDate desc limit :m,:n"
            val result = conn.createQuery(sql).addParameter("m", (pgno - 1) * pgsize).addParameter("n", pgsize).executeAndFetch(Member::class.java)
            conn.close()
            return result
        } catch(e: Exception) {
            log.info(e.message)
            return emptyList()
        }
    }

    override fun getMemberById(id: String): Member? {
        try {
            val conn = sql2o.open()
            val members = conn.createQuery("select * from t_member where memberId = :memberId ").addParameter("memberId", id).executeAndFetch(Member::class.java)
            conn.close()
            if (members.size == 0) {
                return null
            } else if (members.size == 1) {
                return members.get(0)
            } else {
                return null
            }
        } catch(e: Exception) {
            log.info(e.message)
            return null
        }
    }

    override fun getMemberByPrinciple(name: String): Member? {
        log.info("MemberDaoImpl.getMemberByPrinciple:" + name)
        if (name.isNullOrEmpty()) return null
        try {
            val sql = " select * from t_member where memberName = :name or email = :name or mobile = :name "
            log.info("sql:" + sql)
            val conn = sql2o.open()
            val result = conn.createQuery(sql).addParameter("name", name).executeAndFetchFirst(Member::class.java)
            conn.close()
            log.info("result:" + result)
            return result
        } catch(e: Exception) {
            log.info("dao exception:" + e.message)
            return null
        }
    }

    override fun getMemberBySNS(name: String): Member? {
        if (name.isEmpty()) return null
        try {
            val sql = " select * from t_member where qqno = :name or weixinno= :name or weibono = :name "
            val conn = sql2o.open()
            val result = conn.createQuery(sql).addParameter("name", name).executeAndFetchFirst(Member::class.java)
            conn.close()
            return result
        } catch(e: Exception) {
            log.info(e.message)
            return null
        }
    }

    override fun saveMember(member: Member): String? {
        val sql = "insert into t_member(memberName,gender,birthDate,mobile,email,qqno,weixinno,weibono,address,registerDate,communityId) " +
                " values(:memberName,:gender,:birthDate,:mobile,:email,:qqno,:weixinno,:weibono,:address,:registerDate,:communityId) "
        try {
            val conn = sql2o.open()
            val memberId = conn.createQuery(sql, true)
                    .addParameter("memberName", member.memberName).addParameter("gender", member.gender).addParameter("birthDate", member.birthDate)
                    .addParameter("mobile", member.mobile).addParameter("email", member.email).addParameter("qqno", member.qqno)
                    .addParameter("weixinno", member.weixinno).addParameter("weibono", member.weibono).addParameter("address", member.address)
                    .addParameter("registerDate", member.registerDate).addParameter("communityId", member.communityId).executeUpdate().getKey()
            val result = memberId!!.toString()
            conn.close()
            return result
        } catch(e: Exception) {
            log.info(e.message)
            return null
        }
    }

    override fun deleteMember(id: String) {
        throw UnsupportedOperationException()
    }

    override fun updateMemberSNS(id: String, qqno: String?, weixinno: String?, weibono: String?): Int {

        var sql: String = ""
        if (!qqno.isNullOrEmpty()) {
            sql += "update t_member set qqno = '%s'".format(qqno)
            if (!weixinno.isNullOrEmpty()) {
                sql += ", weixinno='%s'".format(weixinno)
            }
            if (!weibono.isNullOrEmpty()) {
                sql += ",weibono='%s'".format(weibono)
            }
        } else if (!weixinno.isNullOrEmpty()) {
            sql += "update t_member set weixinno = '%s'".format(weixinno)
            if (!weibono.isNullOrEmpty()) {
                sql += ",weibono='%s'".format(weibono)
            }
        } else if (!weibono.isNullOrEmpty()) {
            sql += "update t_member set weibono='%s'".format(weibono)
        }

        sql += " where memberId = '%s' ".format(id)

        try {
            val conn = sql2o.open()
            val result = conn.createQuery(sql).executeUpdate().result
            conn.close()
            return result
        } catch(e: Exception) {
            log.info(e.message)
            return 0
        }
    }

}

fun md5encrypt(text: String): String? {
    if (text.isEmpty()) return null
    return BASE64Encoder().encode(MessageDigest.getInstance("MD5").digest(text.toByteArray()))
}

fun main(args: Array<String>) {
    //val dao = MemberDaoImpl()

    /*
    val member = Member(memberName = "xnpeng5", email = "xnpeng5@sina.com", weixinno = "xnpeng5", weibono = "xnpeng5",
            qqno = "460465", mobile = "18673107435",
            gender = "1", birthDate = Date().fromString("1988-01-01"),
            address = "104 lighting way", registerDate = Date(), communityId = "1003",
            memberId = null, isAdministrator = false, auditDate = null, password = null)
    dao.saveMember(member)
    */

    //val member = dao.getMemberById("1")
    //log.info(member.toString())

    //val members = dao.getMemberList()
    //members.filter { it.memberName.startsWith("xnp") }.forEach { log.info(it.toString()) }

    //xnpeng == WZXuEyrxRWq4I/Ha7kZNbg==
    //pengxn == skI4pjXc1tx2EE9tKIun7w==

    //dao.setPassword("1", "")

    val ss = md5encrypt("xnpeng")
    log.info(ss)

}
