package online.nyam.hikka.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import online.nyam.hikka.api.models.Response
import kotlin.reflect.KClass

sealed class RequestState<T> {
    class Loading<T> : RequestState<T>()

    data class Ready<T>(
        val data: T
    ) : RequestState<T>()

    sealed class Failure<T> : RequestState<T>() {
        data class Abort<T>(
            val message: String,
            val code: String
        ) : Failure<T>()

        data class Crash<T>(
            val error: Throwable
        ) : Failure<T>()
    }
}

@Composable
fun <T> rememberRequestState(
    key: Any? = null,
    makeRequest: suspend () -> Response<T>
): RequestState<T> {
    val requestFun by rememberUpdatedState(newValue = makeRequest)

    val requestState =
        produceState<RequestState<T>>(key1 = key, initialValue = RequestState.Loading()) {
            value =
                try {
                    when (val response = requestFun()) {
                        is Response.Success -> RequestState.Ready(response.data)

                        is Response.Error -> RequestState.Failure.Abort(response.abort.message, response.abort.code)

                        is Response.Crash -> RequestState.Failure.Crash(response.error)
                    }
                } catch (e: Throwable) {
                    RequestState.Failure.Crash(e)
                }
        }
    return requestState.value
}

class RequestStateHandler<T> {
    private val handlers =
        mutableMapOf<KClass<out RequestState<*>>, @Composable (RequestState<T>) -> Unit>()

    fun loading(fn: @Composable (RequestState.Loading<T>) -> Unit) {
        handlers[RequestState.Loading::class] = { fn(it as RequestState.Loading<T>) }
    }

    fun ready(fn: @Composable (RequestState.Ready<T>) -> Unit) {
        handlers[RequestState.Ready::class] = { fn(it as RequestState.Ready<T>) }
    }

    fun failure(fn: @Composable (RequestState.Failure<T>) -> Unit) {
        handlers[RequestState.Failure::class] = { fn(it as RequestState.Failure<T>) }
    }

    @Composable
    fun Handle(rs: RequestState<T>) {
        val handler = handlers[rs::class]

        if (handler != null) {
            handler(rs)
        } else if (rs is RequestState.Failure) {
            handlers[RequestState.Failure::class]?.invoke(rs)
        }
    }
}

/**
 *
 * ```
 * WithRequestState({ api.mangaDetails(slug) }, slug) {
 *     loading { Text("Loading...")}
 *     ready { rs -> Text(rs.data.title) }
 *     failure { Text("Something went wrong") }
 * }
 * ```
 */
@Composable
fun <T> WithRequestState(
    makeRequest: suspend () -> Response<T>,
    key: Any? = null,
    initHandler: RequestStateHandler<T>.() -> Unit
) {
    val handler = remember { RequestStateHandler<T>().apply { initHandler() } }

    val state = rememberRequestState(key, makeRequest = makeRequest)

    handler.Handle(state)
}
