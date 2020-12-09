package ipvc.estg.room.API_Login

import android.text.Editable
import ipvc.estg.room.Output_Login
import ipvc.estg.room.Output_Problema
import ipvc.estg.room.entities.Problema
import retrofit2.Call
import retrofit2.http.*

interface PostLogin {

    @FormUrlEncoded
    @POST("/myslim/api/login/entra")
    fun postTest(@Field("utilizador") utilizador: String, @Field("palavrapasse") palavrapasse: String): Call<Output_Login>

    @FormUrlEncoded
    @POST("myslim/api/login/criar")
    fun postcriar(@Field("utilizador") utilizador: String, @Field("palavrapasse") palavrapasse: String): Call<Output_Login>

    @FormUrlEncoded
    @POST("myslim/api/problema/criar")
    fun criarProblema(@Field("utilizador") utilizador: String, @Field("tipo") tipo: String, @Field("descricao") descricao: Editable, @Field("latitude") latitude: String, @Field("longitude") longitude: String): Call<Output_Problema>

    @GET("/myslim/api/marcadores")
    fun getProblemas(): Call<List<Problema>>

    @GET("/myslim/api/select/{id}")
    fun getProblema(@Path("id") id: Int): Call<List<Problema>>

    @FormUrlEncoded
    @POST("myslim/api/problema/editar")
    fun editarProblema(@Field("id") id: Int?, @Field("tipo") tipo: String, @Field("descricao") descricao: String, @Field("latitude") latitude: String, @Field("longitude") longitude: String): Call<Output_Problema>

    @GET("myslim/api/problema/remover/{id}")
    fun removerProblema(@Path("id") id: Int?): Call<Output_Login>

}