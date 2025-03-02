package online.nyam.hikka.activities.main

import online.nyam.hikka.activities.main.viewmodels.HomeScreenViewModel
import online.nyam.hikka.activities.main.viewmodels.MangaScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainActivityKoinModule =
    module {
        viewModelOf(::HomeScreenViewModel)
        viewModelOf(::MangaScreenViewModel)
    }
