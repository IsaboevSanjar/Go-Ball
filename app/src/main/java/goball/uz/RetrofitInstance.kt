package goball.uz

import com.chuckerteam.chucker.api.ChuckerInterceptor
import goball.uz.app.App
import goball.uz.data.Api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(ChuckerInterceptor(App.instance!!))
        .addInterceptor(interceptorToken())
        .build()

    private fun interceptorToken(
    ): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            builder
                .addHeader("Authorization", "Bearer {AppCache.getHelper().token}")
            val response = chain.proceed(builder.build())
            response
        }
    }

    val api: Api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Api.URL_STADIUM)
        .client(client)
        .build()
        .create(Api::class.java)
}