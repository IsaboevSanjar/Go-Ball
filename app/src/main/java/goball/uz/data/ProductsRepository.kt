package goball.uz.data

import goball.uz.data.models.Product
import goball.uz.data.models.StadiumListItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProductsList():Flow<Result<List<Product>>>
    suspend fun getStadiumsList():Flow<Result<List<StadiumListItem>>>
}