package sk.kasper.domain.utils

import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse

suspend fun <T> wrapToResponse(block: suspend () -> T): Response<T> {
    return try {
        SuccessResponse(block())
    } catch (e: Throwable) {
        ErrorResponse(e.message)
    }
}

suspend fun <T> wrapNullableToResponse(block: suspend () -> T?): Response<T> {
    return try {
        val data = block()
        data?.let {
            return SuccessResponse(data)
        } ?: ErrorResponse("Not exist")
    } catch (e: Throwable) {
        ErrorResponse(e.message)
    }
}