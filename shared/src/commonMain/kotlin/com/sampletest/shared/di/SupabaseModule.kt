package com.sampletest.shared.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.api.CacheKey
import com.apollographql.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import com.sampletest.shared.BuildKonfig
import com.sampletest.shared.cache.Cache.cache
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.graphql.GraphQL
import io.github.jan.supabase.graphql.graphql
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class SupabaseModule {

    @Single
    fun supabaseClient(sqlCacheFactory: NormalizedCacheFactory) = createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_API_KEY,
    ) {

        val chainedCacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
            .chain(sqlCacheFactory)

        install(GraphQL) {
            apolloConfiguration {
                cache(
                    normalizedCacheFactory = chainedCacheFactory,
                    keyScope = CacheKey.Scope.SERVICE
                )
            }
        }

//        install(Auth)


    }


    @Single
    fun providesApolloClient(client: SupabaseClient): ApolloClient = client.graphql.apolloClient


//    @Single
//    fun providesAuth(client: SupabaseClient): Auth = client.auth
//
//
//    @Single
//    fun providesComposeAuth(client: SupabaseClient): ComposeAuth = client.composeAuth


}