package goball.uz.data

import goball.uz.data.models.StadiumListItem
import kotlinx.coroutines.flow.Flow

interface StadiumsRepository {
    suspend fun getStadiumsList():Flow<Result<List<StadiumListItem>>>
}