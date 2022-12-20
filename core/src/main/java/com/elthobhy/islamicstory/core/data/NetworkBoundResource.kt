package com.elthobhy.islamicstory.core.data

import com.elthobhy.islamicstory.core.data.remote.vo.ApiResponse
import com.elthobhy.islamicstory.core.data.remote.vo.StatusResponseNetwork
import com.elthobhy.islamicstory.core.utils.vo.Resource
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.loading())
        val dbSource = loadFromDb().first()
        val new = createCall().first()
        if (shouldFetch(dbSource, new.body)) {
            emit(Resource.loading(dbSource))
            val apiResponse = createCall().first()
            when (apiResponse.statusNetwork) {
                StatusResponseNetwork.SUCCESS -> {
                    apiResponse.body?.let { saveCallResult(it) }
                    emitAll(loadFromDb().map { Resource.success(it) })
                }
                StatusResponseNetwork.EMPTY -> {
                    emitAll(loadFromDb().map { Resource.success(it) })
                }
                StatusResponseNetwork.ERROR -> {
                    onFetchFailed()
                    emit(Resource.error(apiResponse.message + "\n| OFFLINE MODE |" + "\n| Check Your Internet Connection |"))
                    emitAll(loadFromDb().map { Resource.success(it) })
                }
            }
        } else {
            emitAll(loadFromDb().map { Resource.success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract suspend fun loadFromDb(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?, dataNew: RequestType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}