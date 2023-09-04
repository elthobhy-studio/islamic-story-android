package com.elthobhy.islamicstory.core.data.remote.vo

class ApiResponse<T>(val statusNetwork: StatusResponseNetwork, val body: T?, val message: String?) {
    companion object {
        fun <T> success(body: T?): ApiResponse<T> =
            ApiResponse(StatusResponseNetwork.SUCCESS, body, null)

        fun <T> error(msg: String? = null, body: T? = null): ApiResponse<T> =
            ApiResponse(StatusResponseNetwork.ERROR, body, msg)

        fun <T> loading(msg: String? = null, body: T? = null): ApiResponse<T> =
            ApiResponse(StatusResponseNetwork.EMPTY, body, msg)
    }
}