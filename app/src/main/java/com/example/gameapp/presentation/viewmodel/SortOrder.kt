package com.example.gameapp.presentation.viewmodel

enum class SortOrder(
    val apiValue: String?,
    val title: String
) {
    DEFAULT(null, "Default"),

    RATING_ASC("rating", "Rating: ascending"),
    RATING_DESC("-rating", "Rating: descending"),

    RELEASED_ASC("released", "Date: ascendind"),
    RELEASED_DESC("-released", "Date: descending"),

    NAME_ASC("name", "Name: ascending"),
    NAME_DESC("-name", "Name: descending")
}