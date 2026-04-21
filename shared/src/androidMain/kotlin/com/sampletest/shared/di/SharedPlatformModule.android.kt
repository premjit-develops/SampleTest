package com.sampletest.shared.di

import android.content.Context
import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
actual class SharedPlatformModule {

    @Single
    fun cacheFactory(context: Context): NormalizedCacheFactory = SqlNormalizedCacheFactory(
        name = "apollo.db",
        context = context
    )
}
