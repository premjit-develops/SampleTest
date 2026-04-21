package com.sampletest.shared.di

import com.sampletest.core.common.di.DispatcherModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.startKoin


@Module(
    includes = [
        DispatcherModule::class,
        SharedPlatformModule::class,
        SupabaseModule::class,
    ]
)
@Configuration
@ComponentScan("com.sampletest.shared")
class SharedModule

@KoinApplication
object KoinApp


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin<KoinApp> {
        includes(config)
    }
}