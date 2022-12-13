package com.aljebrastudio.islamicstory.core.utils

import com.aljebrastudio.islamicstory.core.data.local.entity.ListEntity
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain

object DataMapper {
    fun mapDomainToEntity(input: List<ListDomain>): List<ListEntity>{
        val mapTo = ArrayList<ListEntity>()
        input.map {
            val list = it.keyId?.let { it1 ->
                ListEntity(
                    name = it.name,
                    detail = it.detail,
                    umat = it.umat,
                    umur = it.umur,
                    recentActivity = it.recentActivity,
                    keyId = it1,
                )
            }
            if (list != null) {
                mapTo.add(list)
            }
        }
        return mapTo
    }
    fun entityToDomain(input: ListEntity): ListDomain{
        return ListDomain(
            name = input.name,
            umur = input.umur,
            detail = input.detail,
            umat = input.umat,
            keyId = input.keyId,
            recentActivity = input.recentActivity,
        )
    }
    fun listData(): List<ListDomain>{
        val data = ArrayList<ListDomain>()
        for(i in DataListObject.namaNabiRasul.indices){
            val list = ListDomain(
                name = DataListObject.namaNabiRasul[i],
                detail = DataListObject.detailNabiRasul[i],
                umat = DataListObject.Umat[i],
                umur = DataListObject.Umur[i],
                recentActivity = DataListObject.recentActivity[i],
                keyId = DataListObject.key_id[i],
            )
            data.add(list)
        }
        return data
    }
}