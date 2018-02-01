package com.sitexa.kwf.entity

/**
 * Created by open on 5/27/16.
 */
data class Site(val siteId: String, val siteName: String, val parentId: String,
                val siteType: String, val address: String, val latitude: Double, val longitude: Double)

