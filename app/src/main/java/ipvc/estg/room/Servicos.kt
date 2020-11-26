package ipvc.estg.room

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Servicos {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://apppmbraga.000webhostapp.com/") //URL da API -> BD
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildServico(service: Class<T>): T{
        return retrofit.create(service)
    }
}