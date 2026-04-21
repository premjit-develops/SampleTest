package com.sampletest.shared.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object QuoteList : NavKey

@Serializable
data class QuoteDetail(val quoteId: Int) : NavKey

@Serializable
data object FavoritesList : NavKey