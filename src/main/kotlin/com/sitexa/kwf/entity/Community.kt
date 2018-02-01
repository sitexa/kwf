package com.sitexa.kwf.entity

import java.util.*

/**
 * Created by open on 5/27/16.
 */

data class Community(val communityId: String, val communityName: String,
                     val createDate: Date, val creator: String,
                     val country: Site, val province: Site, val city: Site,
                     val county: Site, val town: Site,
                     val indtroduction: String, val address: String,
                     val communityType: String, val approved: Boolean)