package com.mackosoft.lebonalbum.di

import com.mackosoft.lebonalbum.services.communication.di.ApiModule
import com.mackosoft.lebonalbum.services.communication.di.RetrofitModule
import com.mackosoft.lebonalbum.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RetrofitModule::class,
    ApiModule::class
])
interface ApiComponent {

    fun inject(viewModel: MainViewModel)

    @Component.Builder
    interface Builder {
        fun providesRetrofitModule(retrofitModule: RetrofitModule) : Builder
        fun providesApiModule(apiModule: ApiModule) : Builder
        fun build() : ApiComponent
    }
}