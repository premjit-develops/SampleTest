package com.sampletest.shared.model


import androidx.compose.runtime.Immutable


@Immutable
data class QuoteDetail(
    val id: Int,
    val text: String,
    val meaning: String,
    val bookSection: List<BookSection>,
    val isFavorite: Boolean
)

@Immutable
data class Quote(
    val id: Int,
    val text: String,
    val meaning: String
)


@Immutable
data class Book(
    val id: Int,
    val name: String,
    val author: String?
)


@Immutable
data class BookSection(
    val id: Int,
    val pathCode: String,
    val book: Book
)


@Immutable
data class FavoriteQuote(
    val id: String,
    val quoteId: Int,
    val text: String,
    val bookName: String
)