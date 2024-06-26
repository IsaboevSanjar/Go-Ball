package goball.uz.models

data class UserData(
    val first_name: String,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_name: Any,
    val phone_number: String,
    val roles: Any,
    val username: String
)