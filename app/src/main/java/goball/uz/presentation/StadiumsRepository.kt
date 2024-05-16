package goball.uz.presentation

import goball.uz.helper.Result
import goball.uz.models.TgToken
import goball.uz.models.staium.StadiumListItem
import kotlinx.coroutines.flow.Flow

interface StadiumsRepository {
    suspend fun getStadiumsList():Flow<Result<List<StadiumListItem>>>

    suspend fun loginWithTelegram(code:Int):Flow<Result<TgToken>>
}