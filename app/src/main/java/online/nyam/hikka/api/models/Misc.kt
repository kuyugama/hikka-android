@file:OptIn(ExperimentalSerializationApi::class)

package online.nyam.hikka.api.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable data class Genre(
    @property:JsonNames("name_en") val nameEn: String?,
    @property:JsonNames("name_ua") val nameUk: String?,
    val slug: String,
    val type: String
) {
    val name = nameUk ?: nameEn ?: slug
}

@Serializable data class PaginationInfo(
    val total: Int,
    val page: Int,
    @property:JsonNames("pages") val totalPages: Int
)

@Serializable data class Paginated<T>(
    @property:JsonNames("pagination") val info: PaginationInfo,
    @property:JsonNames("list") val items: List<@Serializable T>
)

@Serializable data class Abort(
    val code: String,
    val message: String
)

sealed class Response<T> {
    data class Error<T>(
        val abort: Abort
    ) : Response<T>()

    data class Success<T>(
        val data: T
    ) : Response<T>()
}
