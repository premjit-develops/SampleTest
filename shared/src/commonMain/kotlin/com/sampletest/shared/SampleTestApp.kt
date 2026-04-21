package com.sampletest.shared

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.sampletest.core.design.SampleTestTheme
import com.sampletest.shared.navigation.FavoritesList
import com.sampletest.shared.navigation.QuoteDetail
import com.sampletest.shared.navigation.QuoteList
import com.sampletest.shared.navigation.bottomNavItems
import com.sampletest.shared.ui.screens.FavoritesScreen
import com.sampletest.shared.ui.screens.QuoteDetailScreen
import com.sampletest.shared.ui.screens.QuoteListScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun SampleTestApp() {

    SampleTestTheme {

        Surface(modifier = Modifier.fillMaxSize()) {
            val config = remember {
                SavedStateConfiguration {
                    serializersModule = SerializersModule {
                        polymorphic(NavKey::class) {

                            subclass(
                                subclass = QuoteList::class,
                                serializer = QuoteList.serializer()
                            )
                            subclass(
                                subclass = QuoteDetail::class,
                                serializer = QuoteDetail.serializer()
                            )
                            subclass(
                                subclass = FavoritesList::class,
                                serializer = FavoritesList.serializer()
                            )

                        }
                    }
                }
            }

            val backStack = rememberNavBackStack(config, QuoteList)

            Scaffold(
                modifier = Modifier.systemBarsPadding(),
                bottomBar = {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            val selected = backStack.contains(item.navKey)

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                selected = selected,
                                onClick = {
                                    if (!selected) {
                                        backStack.removeLastOrNull()
                                        backStack.add(item.navKey)
                                    }
                                }
                            )

                        }
                    }
                }
            ) { padding ->

                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.padding(padding),
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<QuoteList> {

                            QuoteListScreen(
                                onQuoteClick = {
                                    backStack.add(QuoteDetail(it))
                                }
                            )
                        }
                        entry<QuoteDetail> { navKey ->
                            QuoteDetailScreen(quoteId = navKey.quoteId)
                        }
                        entry<FavoritesList> {
                            FavoritesScreen(onQuoteClick = {
                                backStack.add(QuoteDetail(it))
                            })
                        }
                    }
                )
            }
        }

    }

}

