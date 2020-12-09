package ipvc.estg.room.entities

//classe que guarda os valores a preencher na BD
data class Problema(

    val id: Int,
    val descricao: String,
    val utilizador_id: Int,
    val tipo: String,
    val latitude : String,
    val longitude : String,
    val utilizador : String
)
