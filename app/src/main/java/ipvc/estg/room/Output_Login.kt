package ipvc.estg.room

data class Output_Login (
        val sucesso: Boolean, // isSuccessfull
        val utilizador: String, //campo utilizador
        val resposta:String //resposta da API
)