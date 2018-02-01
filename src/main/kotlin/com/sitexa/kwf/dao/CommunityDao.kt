package com.sitexa.kwf.dao

import com.sitexa.kwf.entity.Community

/**
 * Created by open on 5/27/16.
 */

interface CommunityDao {
    fun getCommunityById(id: String): Community
    fun getCommunityBySite(siteId: String): List<Community>
    fun getCommunityByProvince(province: String): List<Community>
    fun getCommunityByCity(city: String): List<Community>
    fun getCommunityByCounty(county: String): List<Community>
    fun getCommunityByTown(town: String): List<Community>
    fun saveCommunity(community: Community): Community
    fun updateCommunity(community: Community)
    fun deleteCommunity(community: Community)
}