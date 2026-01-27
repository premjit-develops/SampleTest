package com.sampletest.shared.di

import org.koin.core.annotation.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.ksp.generated.startKoin

@KoinApplication
object KoinApp

fun initKoin(config: KoinAppDeclaration? = null) {
    KoinApp.startKoin {
        includes(config)
    }
}