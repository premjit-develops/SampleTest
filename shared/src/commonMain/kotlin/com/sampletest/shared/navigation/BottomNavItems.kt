package com.sampletest.shared.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val navKey: NavKey
)

val bottomNavItems: List<BottomNavItem> = listOf(
    BottomNavItem(
        label = "Quotes",
        selectedIcon = Icons.Filled.FormatQuote,
        unselectedIcon = Icons.Outlined.FormatQuote,
        navKey = QuoteList
    ),
    BottomNavItem(
        label = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        navKey = FavoritesList
    )
)