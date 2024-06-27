package goball.uz.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import goball.uz.app.App
import goball.uz.cache.AppCache
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
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api): StadiumsRepository {
        return StadiumsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    internal fun retrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    internal fun client(
        interceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor(App.instance!!))
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun interceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            builder
                .addHeader("Authorization", "Bearer ${AppCache.getHelper().token}")
            val response = chain.proceed(builder.build())
            response
        }
    }

    @Provides
    @Singleton
    internal fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


}