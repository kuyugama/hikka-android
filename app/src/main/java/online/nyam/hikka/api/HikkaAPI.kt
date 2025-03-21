package online.nyam.hikka.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.buildUrl
import io.ktor.http.contentType
import io.ktor.http.fullPath
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import online.nyam.hikka.api.models.Paginated
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.requests.MangaCatalog
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.models.responses.MangaShort

class HikkaAPI(
    private val baseUrl: Url
) {
    private val client =
        HttpClient(Android) {
            install(HttpCache)
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

    private operator fun Url.div(other: String) =
        buildUrl {
            protocolOrNull = this@div.protocolOrNull
            host = this@div.host
            port = this@div.port

            path(this@div.fullPath, other.trimStart('/'))
        }

    private inline fun <T> catchIOException(block: () -> Response<T>): Response<T> =
        try {
            block()
        } catch (e: IOException) {
            Response.Crash(e)
        }

    suspend fun mangaCatalog(
        query: String? = null,
        sort: List<String> = listOf("start_date:desc"),
        page: Int = 1,
        size: Int = 15
    ): Response<Paginated<MangaShort>> =
        catchIOException {
            val response =
                client.post(baseUrl / "/manga") {
                    parameter("page", page)
                    parameter("size", size)

                    contentType(ContentType.Application.Json)
                    setBody(MangaCatalog(query = query, sort = sort))
                }

            if (!response.status.isSuccess()) {
                return Response.Error(response.body())
            }

            return Response.Success(response.body())
        }

    suspend fun mangaDetails(slug: String): Response<Manga> =
        catchIOException {
            val response =
                client.get(baseUrl / "manga/$slug")

            if (!response.status.isSuccess()) {
                return Response.Error(response.body())
            }

            return Response.Success(response.body())
        }
}
