package goball.uz.presentation

import goball.uz.helper.Result
import goball.uz.models.PasscodeRequest
import goball.uz.models.TgToken
import goball.uz.models.staium.StadiumListItem
import goball.uz.network.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StadiumsRepositoryImpl @Inject constructor(private val api: Api) : StadiumsRepository {

    override suspend fun getStadiumsList(): Flow<Result<List<StadiumListItem>>> {
        return flow {
            val stadiumsFromApi = try {
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

            emit(Result.Success(stadiumsFromApi))
        }
    }

    override suspend fun loginWithTelegram(code: Int): Flow<Result<TgToken>> {
        return flow {
            val login=try {
                api.loginTelegram(PasscodeRequest(code))
            }catch (e: IOException) {
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
            emit(Result.Success(login))
        }
    }
}