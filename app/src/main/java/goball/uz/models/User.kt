package goball.uz.models

data class User(
    val created_at: String,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: Any,
    val password: Any,
    val phone_number: String,
    val tg_id: Int,
    val updated_at: Any
)