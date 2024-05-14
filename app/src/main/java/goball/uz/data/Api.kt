package goball.uz.data

import goball.uz.data.models.StadiumList
import retrofit2.http.GET

interface Api {

    @GET("stadium/all")
    suspend fun getAllStadiums():StadiumList

    companion object {
        val BASE_URL="https://5c2e-213-230-82-66.ngrok-free.app/"

    }
}