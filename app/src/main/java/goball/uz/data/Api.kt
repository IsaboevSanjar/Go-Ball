package goball.uz.data

import goball.uz.data.models.Products
import goball.uz.data.models.StadiumList
import retrofit2.http.GET

interface Api {

    @GET("products")
    suspend fun getProductsList(): Products

    @GET("stadium/all")
    suspend fun getAllStadiums():StadiumList

    companion object {
        val BASE_URL="https://dummyjson.com/"
        val URL_STADIUM="https://5c2e-213-230-82-66.ngrok-free.app/"

    }
}