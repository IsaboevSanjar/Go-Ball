package goball.uz.presentation

import goball.uz.helper.Result
import goball.uz.models.staium.StadiumListItem
import goball.uz.network.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class StadiumsRepositoryImpl(private val api: Api) : StadiumsRepository {

    override suspend fun getStadiumsList(): Flow<Result<List<StadiumListItem>>> {
        return flow {
            val productsFromApi = try {
                api.getAllStadiums()

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Error loading products"))
                return@flow
            }

            emit(Result.Success(productsFromApi))
        }
    }
}