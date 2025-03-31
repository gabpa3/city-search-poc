package com.gabcode.citysearchpoc.data.service

import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response

internal suspend fun <T> makeRequest(service: suspend () -> Response<T>): Result<T> = runCatching {
    val apiCall = service.invoke()
    if (apiCall.isSuccessful) {
        val responseBody = apiCall.body()
        responseBody ?: error("Response body is null")
    } else {
        val error = apiCall.errorBody()
        val errorParsed = error?.let { parseErrorResponse(it) } ?: error("Response error body is null")
        error(errorParsed)
    }
}

private fun parseErrorResponse(error: ResponseBody?): String {
    val value = error?.let {
        String(
            it.bytes(),
            Charsets.UTF_8
        )
    }.orEmpty()

    return JsonParser.parseString(value).asJsonObject.asString
}
