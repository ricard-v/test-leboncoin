package com.mackosoft.lebonalbum.di

import android.content.Context
import com.mackosoft.lebonalbum.services.communication.di.ApiModule
import com.mackosoft.lebonalbum.services.communication.di.RetrofitModule
import com.mackosoft.lebonalbum.services.database.di.DatabaseModule
import com.mackosoft.lebonalbum.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    RetrofitModule::class,
    ApiModule::class,
    DatabaseModule::class
])
interface AppComponent {

    fun inject(mainViewModel: MainViewModel)

    @Component.Builder
    interface Builder {
        fun providesContext(contextModule: ContextModule) : Builder
        fun providesRetrofitModule(retrofitModule: RetrofitModule) : Builder
        fun providesApiModule(apiModule: ApiModule) : Builder
        fun providesDatabaseModule(databaseModule: DatabaseModule) : Builder
        fun build() : AppComponent
    }

}