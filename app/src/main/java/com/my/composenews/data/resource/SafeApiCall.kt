package com.my.composenews.data.resource

import com.my.composenews.data.model.response.ErrorDTO
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlinx.serialization.json.Json
import retrofit2.Response

inline fun <reified T> safeApiCall(
    json: Json = Json { ignoreUnknownKeys = true },
    apiCall: () -> Response<T>
): NetworkResponse<T> {
    try {
        val response = apiCall()
        // [200..300]
        if (response.isSuccessful) {
            val body = response.body()
            return NetworkResponse.Success(data = body!!)
        }
        // [400..500]
        val errorBody = response.errorBody()
        return if (errorBody != null) {
            val errorResponse = try {
                json.decodeFromString<ErrorDTO>(errorBody.string())
            } catch (e: Exception) {
                null
            }
            if (errorResponse != null) {
                NetworkResponse.Failed(
                    message = errorResponse.message,
                    type = NetworkErrorType.Dynamic
                )
            } else {
                NetworkResponse.Failed(
                    message = response.message(),
                    type = NetworkErrorType.Dynamic
                )
            }
        } else {
            NetworkResponse.Failed(
                message = response.message(),
                type = NetworkErrorType.Dynamic
            )
        }
    } catch (e: Exception) {
        return when (e) {
            is SocketTimeoutException -> {
                NetworkResponse.Failed(
                    message = e.localizedMessage ?: "SocketTimeoutException",
                    type = NetworkErrorType.NoInternet
                )
            }

            is ConnectException -> {
                NetworkResponse.Failed(
                    message = e.localizedMessage ?: "ConnectException",
                    type = NetworkErrorType.NoInternet
                )
            }

            else -> {
                NetworkResponse.Failed(
                    message = "SafeApiCall exception",
                    type = NetworkErrorType.Dynamic
                )
            }
        }
    }
}
