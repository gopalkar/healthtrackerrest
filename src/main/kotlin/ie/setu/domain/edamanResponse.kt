package ie.setu.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class edamanNutrient (
    @SerialName("ENERC_KCAL") val ENERC_KCAL: Double?,
    @SerialName("PROCNT") val PROCNT: Double?,
    @SerialName("FAT") val FAT: Double?,
    @SerialName("CHOCDF") val CHOCDF: Double?,
    @SerialName("FIBTG") val FIBTG: Double?
)

@Serializable
data class food (
    @SerialName("foodId") val foodId: String?,
    @SerialName("label") val label: String?,
    @SerialName("knownAs") val knownAs: String?,
    @SerialName("nutrients") val nutrients: edamanNutrient?,
    @SerialName("brand") val brand: String?,
    @SerialName("category") val category: String?,
    @SerialName("categoryLabel") val categoryLabel: String?,
    @SerialName("foodContentsLabel") val foodContentsLabel: String?,
    @SerialName("servingSizes") val servingSizes: List<servingSize>?,
    @SerialName("servingsPerContainer") val servingsPerContainer: Double?,
    @SerialName("image") val image: String?
)
@Serializable
data class foodList (
    @SerialName("food") val food: food)

@Serializable
data class edamanResponse (
    @SerialName("text") val text: String,
    @SerialName("parsed") val parsed: List<foodList>,
    @SerialName("hints") val hints: List<hints>,
    @SerialName("_links") val _links : Links ?
)

@Serializable
data class Links (
    @SerialName("next") val next: next)

@Serializable
data class next (
    @SerialName("title") val title: String,
    @SerialName("href") val href: String
    )
@Serializable
data class hints (
    @SerialName("food") val food: food,
    @SerialName("measures") val measures: List<measures>?)
@Serializable
data class measures (
    @SerialName("uri") val uri: String?,
    @SerialName("label") val label: String?,
    @SerialName("weight") val weight: Double?,
    @SerialName("qualified") val qualified: List<qualified>?
)

@Serializable
data class qualified (
    @SerialName("qualifiers") val qualifiers: List<qualifier>?,
    @SerialName("weight") val weight: Double?)

@Serializable
data class qualifier (
    @SerialName("uri") val uri: String?,
    @SerialName("label") val label: String?)

@Serializable
data class servingSize (
    @SerialName("uri") val uri: String?,
    @SerialName("label") val label: String?,
    @SerialName("quantity") val quantity: Double?
)