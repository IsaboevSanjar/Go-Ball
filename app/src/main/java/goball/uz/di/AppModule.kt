package goball.uz.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import goball.uz.app.App
import goball.uz.network.Api
import goball.uz.network.RetrofitInstance
import goball.uz.presentation.StadiumsRepository
import goball.uz.presentation.StadiumsRepositoryImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {




    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Api.BASE_URL)
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api): StadiumsRepository {
        return StadiumsRepositoryImpl(api)
    }


}