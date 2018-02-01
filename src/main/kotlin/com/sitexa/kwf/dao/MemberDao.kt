package com.sitexa.kwf.dao

import com.sitexa.kwf.entity.Member
import java.util.*


/**
 * Created by open on 5/27/16.
 */

interface MemberDao {
    fun getMemberList(): List<Member>
    fun getMemberList(pgsize: Int, pgno: Int): List<Member>
    fun getMemberById(id: String): Member?
    fun getMemberByPrinciple(name: String): Member?
    fun getMemberBySNS(name:String):Member?
    fun saveMember(member: Member): String?
    fun updateMember(id: String, qqno: String?, weixinno: String?, weibono: String?, email: String?, mobile: String?, address: String?): Int
    fun deleteMember(id: String)
    fun setPassword(name: String, oldPassword: String, newPassword:String):Boolean
    fun validateByPrincipal(name: String, pwd: String): Boolean
    fun bindQq(id: String, qqno: String)
    fun bindWeixin(id: String, weixinno: String)
    fun bindWeibo(id: String, weibono: String)
    fun updateMemberSNS(id: String, qqno: String?, weixinno: String?, weibono: String?): Int
}