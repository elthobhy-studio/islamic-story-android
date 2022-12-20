package com.elthobhy.islamicstory.core.utils

import com.elthobhy.islamicstory.core.data.local.entity.ListEntity
import com.elthobhy.islamicstory.core.data.remote.response.ListResponseItem
import com.elthobhy.islamicstory.core.domain.model.ListDomain

object DataMapper {

    fun mapResponseToEntity(input: List<ListResponseItem>): List<ListEntity>{
        val mapTo = ArrayList<ListEntity>()
        input.map {
            val listTo = it.keyId?.let { it1 ->
                ListEntity(
                    name = it.name,
                    umur = it.umur,
                    profile = it.profile,
                    display = it.display,
                    detail = it.detail,
                    umat = it.umat,
                    keyId = it1,
                    recentActivity = it.recentActivity,
                )
            }
            if (listTo != null) {
                mapTo.add(listTo)
            }
        }
        return mapTo
    }
    fun mapEntityToDomain(input: List<ListEntity>): List<ListDomain>{
        val mapTo = ArrayList<ListDomain>()
        input.map {
            val listTo = ListDomain(
                    name = it.name,
                    umur = it.umur,
                    detail = it.detail,
                    umat = it.umat,
                    profile = it.profile,
                    display = it.display,
                    keyId = it.keyId,
                    recentActivity = it.recentActivity,
                )
            mapTo.add(listTo)
        }
        return mapTo
    }
    fun mapDomainEntity(input: List<ListDomain>): List<ListEntity>{
        val mapTo = ArrayList<ListEntity>()
        input.map {
            val listTo = it.keyId?.let { it1 ->
                ListEntity(
                    name = it.name,
                    umur = it.umur,
                    detail = it.detail,
                    umat = it.umat,
                    profile = it.profile,
                    display = it.display,
                    keyId = it1,
                    recentActivity = it.recentActivity,
                )
            }
            if (listTo != null) {
                mapTo.add(listTo)
            }
        }
        return mapTo
    }
    fun mapDomainToEntity(input: ListDomain): ListEntity? {
        return input.keyId?.let {
            ListEntity(
                name = input.name,
                keyId = it,
                display = input.display,
                profile = input.profile,
                detail = input.detail,
                umat = input.umat,
                umur = input.umur,
                recentActivity = input.recentActivity
            )
        }
    }
}