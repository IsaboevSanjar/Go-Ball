package goball.uz.network

import goball.uz.models.staium.StadiumList
import retrofit2.http.GET

interface Api {

    @GET("stadium/all")
    suspend fun getAllStadiums(): StadiumList

    companion object {
        val BASE_URL="https://3ebb-84-54-75-249.ngrok-free.app/"

    }
}