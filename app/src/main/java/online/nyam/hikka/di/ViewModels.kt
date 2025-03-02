package online.nyam.hikka.di

import online.nyam.hikka.ui.viewmodels.HomeScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModels =
    module {
        viewModel { HomeScreenViewModel(get()) }
    }
