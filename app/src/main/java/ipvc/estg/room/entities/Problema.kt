package ipvc.estg.room.entities

data class Problema(

    val id: Int,
    val descricao: String,
    val utilizador_id: Int,
    val tipo: String,
    val latitude : String,
    val longitude : String,
    val utilizador : String
)
