package online.nyam.hikka.di

import io.ktor.http.parseUrl
import online.nyam.hikka.api.HikkaAPI
import org.koin.dsl.module

val apiModule =
    module {
        single { HikkaAPI(parseUrl("https://api.hikka.io")!!) }
    }
