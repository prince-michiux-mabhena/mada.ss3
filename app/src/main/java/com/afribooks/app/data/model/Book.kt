package com.afribooks.app.data.model

import java.io.Serializable

data class Book(
    val id: Int,
    val title: String,
    val courseName: String,
    val courseCode: String,
    val price: Double,
    val condition: BookCondition,
    val seller: String,
    val sellerEmail: String,
    val description: String,
    val coverColorHex: String = "#C0572B",
    var isSaved: Boolean = false
) : Serializable

enum class BookCondition(val displayName: String) {
    LIKE_NEW("Like New"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor");

    companion object {
        fun fromString(value: String): BookCondition =
            values().find { it.displayName.equals(value, ignoreCase = true) } ?: GOOD
    }
}
