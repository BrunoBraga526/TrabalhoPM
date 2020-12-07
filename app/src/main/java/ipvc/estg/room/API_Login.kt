package ipvc.estg.room.API_Login

import android.text.Editable
import ipvc.estg.room.Output_Login
import ipvc.estg.room.Output_Problema
import retrofit2.Call
import retrofit2.http.*

interface PostLogin {

    @FormUrlEncoded
    @POST("myslim/api/login/post")
    fun postTest(@Field("utilizador") username: String, @Field("palavrapasse") password: String?): Call<Output_Login>

    @FormUrlEncoded
    @POST("myslim/api/login/create")
    fun postcreate(@Field("username") username: String, @Field("password") password: String): Call<Output_Login>
    @FormUrlEncoded
    @POST("myslim/api/problema/create")
    fun create(@Field("username") username: String, @Field("tipo") tipo: String, @Field("texto") texto: Editable, @Field("local") local: String, @Field("foto") foto: String): Call<Output_Problema>
    @GET("myslim/api/tipo")
    fun tipo(): Call<List<Output_Login>>

}