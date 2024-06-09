package goball.uz.network

import goball.uz.models.PasscodeRequest
import goball.uz.models.TgToken
import goball.uz.models.staium.StadiumList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("stadium/all")
    suspend fun getAllStadiums(): StadiumList

    @POST("user/tg-login")
    suspend fun loginTelegram(
        @Body passcode: PasscodeRequest
    ): TgToken

    companion object {
        val BASE_URL = "https://661b-82-215-121-218.ngrok-free.app/"

    }
}