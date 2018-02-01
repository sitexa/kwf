package com.sitexa.kwf.entity

import java.util.*

/**
 * Created by open on 5/27/16.
 */
data class Member(var memberId: String? = null, var memberName: String,
                  var gender: String? = null, var birthDate: Date?,
                  var mobile: String, var email: String,
                  var qqno: String? = null, var weixinno: String? = null, var weibono: String? = null,
                  var password: String? = null, var address: String? = null,
                  var registerDate: Date = Date(), var auditDate: Date? = null,
                  var approved: Boolean = false, var isAdministrator: Boolean = false,
                  var communityId: String? = null, var imgurl:String?=null)