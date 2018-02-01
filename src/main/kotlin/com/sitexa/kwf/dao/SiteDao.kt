package com.sitexa.kwf.dao

import com.sitexa.kwf.entity.Site

/**
 * Created by open on 5/27/16.
 */
interface SiteDao {
    fun getSites():List<Site>
    fun getSiteById(siteId:String):Site
    fun getSubSite(siteId:String):List<Site>
    fun getSiteByType(typeId:String):List<Site>
}