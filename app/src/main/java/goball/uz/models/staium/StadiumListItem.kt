package goball.uz.models.staium

data class StadiumListItem(
    val capacity: Int,
    val city: String,
    val country: String,
    val created_at: String,
    val description: Any,
    val id: Int,
    val image: String,
    val lat: String,
    val long: String,
    val name: String,
    val owner_id: Int,
    val price: Int,
    val rating: Double,
    val status: Any,
    val time_end: String,
    val time_start: String,
    val type: Any,
    val updated_at: String
)