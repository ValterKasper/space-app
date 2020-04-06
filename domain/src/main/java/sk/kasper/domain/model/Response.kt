package sk.kasper.domain.model

sealed class Response<out T> {
    fun <R> liftMap(op: (T) -> R): Response<R> = when (this) {
        is SuccessResponse -> SuccessResponse(op(this.data))
        is LoadingResponse -> LoadingResponse()
        is ErrorResponse -> ErrorResponse(this.message)
    }
}
data class SuccessResponse<out T>(val data: T) : Response<T>()
// todo object?
class LoadingResponse : Response<Nothing>()
data class ErrorResponse(val message: String?) : Response<Nothing>()
