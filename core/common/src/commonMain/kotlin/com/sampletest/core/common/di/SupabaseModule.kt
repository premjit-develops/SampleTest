package com.sampletest.core.common.di


import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.graphql.GraphQL
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
@Configuration
class SupabaseModule {

    @Single
    fun supabaseClient() = createSupabaseClient(
        supabaseUrl = "just_test",
        supabaseKey = "just_test",
    ) {

        install(GraphQL) {
            apolloConfiguration {
                normalizedCache(SqlNormalizedCacheFactory("apollo.db"))
            }
        }

        install(Auth)

        install(ComposeAuth) {
            googleNativeLogin(serverClientId = "just_test")
        }

        install(Functions)


    }

}