package sk.kasper.domain.model

sealed class Response<out T> {
    fun <R> liftMap(op: (T) -> R): Response<R> = when (this) {
        is SuccessResponse -> SuccessResponse(op(this.data))
        is ErrorResponse -> ErrorResponse(this.message)
    }
}
data class SuccessResponse<out T>(val data: T) : Response<T>()
data class ErrorResponse(val message: String? = null) : Response<Nothing>()
