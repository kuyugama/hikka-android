package online.nyam.hikka.api.models.requests

import kotlinx.serialization.Serializable

@Serializable data class MangaCatalog(
    val query: String? = null,
    val sort: List<String> = listOf("created:desc")
)
