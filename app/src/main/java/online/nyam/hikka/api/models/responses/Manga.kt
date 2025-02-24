@file:OptIn(ExperimentalSerializationApi::class)

package online.nyam.hikka.api.models.responses

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import online.nyam.hikka.api.models.Genre

@Serializable data class MangaShort(
    @property:JsonNames("title_original") val titleOriginal: String?,
    @property:JsonNames("title_en") val titleEn: String?,
    @property:JsonNames("title_ua") val titleUk: String?,
    val image: String,
    val slug: String,
    val year: Int?
) {
    val title: String = (titleUk ?: titleEn ?: titleOriginal)!!
}

@Serializable data class Manga(
    @property:JsonNames("title_original") val titleOriginal: String?,
    @property:JsonNames("title_en") val titleEn: String?,
    @property:JsonNames("title_ua") val titleUk: String?,
    @property:JsonNames("synopsis_en") val synopsisEn: String?,
    @property:JsonNames("synopsis_ua") val synopsisUk: String?,
    val image: String,
    val slug: String,
    val year: Int?,
    val genres: List<Genre>
) {
    val title: String = titleUk ?: titleEn ?: titleOriginal ?: ""
    val synopsis: String = synopsisUk ?: synopsisEn ?: ""
}
